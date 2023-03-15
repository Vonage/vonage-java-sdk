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
 * Base class for user verification request properties.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class VerificationRequest {
	protected String brand;
	protected List<Workflow> workflow = new ArrayList<>(1);

	protected VerificationRequest(Builder<?, ?> builder) {
		if ((brand = builder.brand) == null || brand.isEmpty()) {
			throw new IllegalArgumentException("Brand name is required.");
		}
		workflow.add(new Workflow(builder.channel, builder.to));
	}

	protected static class Workflow {
		@JsonProperty("channel") protected Channel channel;
		@JsonProperty("to") protected String to;

		protected Workflow(Channel channel, String to) {
			this.channel = Objects.requireNonNull(channel, "Verification channel is required.");
			if ((this.to = to) == null || to.trim().isEmpty()) {
				throw new IllegalArgumentException("Recipient is required.");
			}
		}
	}

	@JsonProperty("brand")
	public String getBrand() {
		return brand;
	}

	@JsonIgnore
	protected String getRecipient() {
		if (workflow == null || workflow.isEmpty()) return null;
		return workflow.get(0).to;
	}

	@JsonProperty("workflow")
	protected List<Workflow> getWorkflow() {
		return workflow;
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

	@SuppressWarnings("unchecked")
	public static abstract class Builder<V extends VerificationRequest, B extends Builder<V, B>> {
		protected final Channel channel;
		protected String brand, to;

		protected Builder(Channel channel) {
			this.channel = channel;
		}

		/**
		 * (REQUIRED)
		 * The phone number to contact, in E.164 format. Don't use a leading + or 00 when entering a
		 * phone number, start with the country code, for example, 447700900000.
		 *
		 * @param to The phone number of the recipient.
		 *
		 * @return This builder.
		 */
		public B to(String to) {
			this.to = to;
			return (B) this;
		}

		/**
		 * (REQUIRED)
		 * The brand that is sending the verification request.
		 *
		 * @param brand The brand name.
		 *
		 * @return This builder.
		 */
		public B brand(String brand) {
			this.brand = brand;
			return (B) this;
		}

		/**
		 * Constructs a VerificationRequest with this builder's properties.
		 *
		 * @return A new VerificationRequest instance.
		 */
		public abstract V build();
	}
}
