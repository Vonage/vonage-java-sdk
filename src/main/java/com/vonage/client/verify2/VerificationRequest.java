/*
 *   Copyright 2023 Vonage
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.vonage.client.verify2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Defines properties for a verify request to be sent to the user. Such properties include the brand
 * (i.e. the sender that the user sees asking them for verification), language to send the message in,
 * length of the verification code and timeout between delivery channel workflows.
 * <p>
 * A verification request can have multiple "workflows". Each workflow defines a contact
 * method for verification. The order of workflows defines the order in which each contact
 * method will be attempted. Once a contact method has succeeded, the remaining workflows will
 * be aborted. This flexibility exists as a fallback / backup verification. A different communication
 * channel and/or number can be contacted if desired.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VerificationRequest {
	protected final Locale locale;
	protected final Integer channelTimeout, codeLength;
	protected final Boolean fraudCheck;
	protected final String brand, clientRef;
	protected final List<Workflow> workflows;

	VerificationRequest(Builder builder) {
		locale = builder.locale;
		clientRef = builder.clientRef;
		fraudCheck = builder.fraudCheck;
		if ((brand = builder.brand) == null || brand.isEmpty()) {
			throw new IllegalArgumentException("Brand name is required.");
		}
		if ((channelTimeout = builder.timeout) != null && (channelTimeout < 60 || channelTimeout > 900)) {
			throw new IllegalArgumentException("Delivery wait timeout must be between 60 and 900 seconds.");
		}
		if ((codeLength = builder.codeLength) != null && (codeLength < 4 || codeLength > 10)) {
			throw new IllegalArgumentException("Code length must be between 4 and 10.");
		}
		if ((workflows = builder.workflows).isEmpty()) {
			throw new IllegalStateException("At least one workflow must be defined.");
		}
		if (isCodeless() && codeLength != null) {
			throw new IllegalStateException("Code length has no effect for codeless workflows.");
		}
	}

	/**
	 * The brand that is sending the verification request. This is what
	 * the user will see when they receive the notification.
	 *
	 * @return The verification sender name.
	 */
	@JsonProperty("brand")
	public String getBrand() {
		return brand;
	}

	/**
	 * Language for the request in ISO_639-1 format.
	 *
	 * @return The language as an enum, or {@code null} if not set (the default).
	 */
	@JsonProperty("locale")
	public Locale getLocale() {
		return locale;
	}

	/**
	 * Specifies the wait time in seconds between attempts to delivery the verification code.
	 *
	 * @return The delivery timeout, or {@code null} if not set (the default).
	 */
	@JsonProperty("channel_timeout")
	public Integer getChannelTimeout() {
		return channelTimeout;
	}

	/**
	 * Length of the code to send to the user. Does not apply to codeless verification channels.
	 *
	 * @return The verification code length, or {@code null} if unset (the default) or not applicable.
	 */
	@JsonProperty("code_length")
	public Integer getCodeLength() {
		return codeLength;
	}

	/**
	 * If the client_ref is set when the request is sent, it will be included in the callbacks.
	 *
	 * @return The client reference, or {@code null} if not set.
	 */
	@JsonProperty("client_ref")
	public String getClientRef() {
		return clientRef;
	}

	/**
	 * Determines if the network level fraud check is enforced. See
	 * <a href=https://developer.vonage.com/en/verify/verify-v2/guides/v2-anti-fraud>the documentation</a>.
	 *
	 * @return Whether network block is respected, or {@code null} if not set (the default).
	 */
	@JsonProperty("fraud_check")
	public Boolean getFraudCheck() {
		return fraudCheck;
	}

	/**
	 * Workflows are a sequence of actions that Vonage use to reach the user you wish to verify with a PIN code.
	 *
	 * @return The list of workflows (contact methods) to be used in verification, in order of preference.
	 */
	@JsonProperty("workflow")
	protected List<Workflow> getWorkflows() {
		return workflows;
	}

	/**
	 * Determines if the workflows defined in this request do not prompt the user for code entry.
	 *
	 * @return {@code true} if all the defined workflows do not require a code or {@code false}
	 * if at least one of the contact methods involves a code being sent to the user.
	 */
	@JsonIgnore
	public boolean isCodeless() {
		return workflows.stream().allMatch(type ->
				type instanceof WhatsappCodelessWorkflow ||
				type instanceof SilentAuthWorkflow
		);
	}

	/**
	 * Generates a JSON payload from this request.
	 *
	 * @return JSON representation of this VerificationRequest object.
	 */
	public String toJson() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(this);
		}
		catch (JsonProcessingException jpe) {
			throw new VonageUnexpectedException("Failed to produce JSON from "+getClass().getSimpleName()+" object.", jpe);
		}
	}

	/**
	 * Entry point for constructing an instance of this class.
	 *
	 * @return A new Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	public static final class Builder {
		Boolean fraudCheck;
		String brand, clientRef;
		Integer timeout, codeLength;
		Locale locale;
		List<Workflow> workflows = new ArrayList<>(1);

		Builder() {}

		/**
		 * (REQUIRED)
		 * Workflows are a sequence of actions that Vonage use to reach the user you wish to verify with a PIN code.
		 * Each workflow represents a contact method - typically, the channel and number. A verification request must
		 * define at least one workflow. The order in which they are defined is the order in which they will be
		 * attempted, until one is successful. The first one you add will be the preferred contact method. Any
		 * subsequent ones act as a fallback / backup.
		 *
		 * @param workflow The workflow to add to the list.
		 *
		 * @return This builder.
		 */
		public Builder addWorkflow(Workflow workflow) {
			workflows.add(Objects.requireNonNull(workflow, "Workflow cannot be null"));
			return this;
		}

		/**
		 * (REQUIRED if {@linkplain #addWorkflow(Workflow)}) has not been called.
		 * Workflows are a sequence of actions that Vonage use to reach the user you wish to verify with a PIN code.
		 * Each workflow represents a contact method - typically, the channel and number. A verification request must
		 * define at least one workflow. The order in which they are defined is the order in which they will be
		 * attempted, until one is successful. The first one you add will be the preferred contact method. Any
		 * subsequent ones act as a fallback / backup.
		 *
		 * @param workflows The workflows to use.This will replace the existing list.
		 *
		 * @return This builder.
		 */
		public Builder workflows(List<Workflow> workflows) {
			this.workflows.clear();
			this.workflows.addAll(Objects.requireNonNull(workflows, "Workflows must not be null."));
			return this;
		}

		/**
		 * (REQUIRED)
		 * The brand that is sending the verification request.
		 * This is what the user will see when they receive the notification.
		 *
		 * @param brand The brand name.
		 *
		 * @return This builder.
		 */
		public Builder brand(String brand) {
			this.brand = brand;
			return this;
		}

		/**
		 * (REQUIRED)
		 * Length of the code to send to the user, must be between 4 and 10 (inclusive).
		 * This is not used for Silent Auth or Whatsapp Interactive.
		 *
		 * @param codeLength The verification code length.
		 *
		 * @return This builder.
		 */
		public Builder codeLength(int codeLength) {
			this.codeLength = codeLength;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * Specifies the wait time in seconds between attempts to delivery the verification code
		 * between workflows. Must be between 60 and 900. Default is 300.
		 *
		 * @param timeout The delivery timeout in seconds.
		 *
		 * @return This builder.
		 */
		public Builder channelTimeout(int timeout) {
			this.timeout = timeout;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * Languages that are available to use.
		 *
		 * @param locale The language locale as an enum.
		 *
		 * @return This builder.
		 */
		public Builder locale(Locale locale) {
			this.locale = locale;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * If this reference is set when the request is sent, it will be included in the callbacks.
		 *
		 * @param clientRef The callback reference for this request.
		 *
		 * @return This builder.
		 */
		public Builder clientRef(String clientRef) {
			this.clientRef = clientRef;
			return this;
		}

		/**
		 * Set this parameter to {@code false} to force through the request even if it's
		 * blocked by the network's fraud protection. Refer to
		 * <a href=https://developer.vonage.com/en/verify/verify-v2/guides/v2-anti-fraud>
		 * the documentation</a> for details.
		 *
		 * @param fraudCheck Whether to enforce network block. Default is {@code true}.
		 * Set to {@code false} to bypass a network block for this request.
		 *
		 * @return This builder.
		 */
		public Builder fraudCheck(boolean fraudCheck) {
			this.fraudCheck = fraudCheck;
			return this;
		}

		/**
		 * Constructs a VerificationRequest with this builder's properties.
		 *
		 * @return A new VerificationRequest instance.
		 */
		public VerificationRequest build() {
			return new VerificationRequest(this);
		}
	}
}
