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

/**
 * Defines properties for sending a verification code to a user via e-mail.
 * <p>
 * Our email solution supports domain registration, if you plan to scale email verification to high volumes with
 * Verify v2, please contact Sales in order to get your account configured properly.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class EmailVerificationRequest extends RegularVerificationRequest {

	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	static final class Workflow extends VerificationRequest.Workflow {
		@JsonProperty("from") String from;

		Workflow(Channel channel, String to, String from) {
			super(channel, to);
			if ((this.from = from) == null || from.trim().isEmpty()) {
				throw new IllegalArgumentException("Sender e-mail address is required.");
			}
		}
	}

	EmailVerificationRequest(Builder builder) {
		super(builder);
		workflow.set(0, new Workflow(builder.channel, builder.to, builder.from));
	}


	@JsonIgnore
	String getSender() {
		if (workflow == null || !((workflow.get(0)) instanceof Workflow)) return null;
		return ((Workflow) workflow.get(0)).from;
	}

	/**
	 * Entry point for constructing an instance of this class.
	 *
	 * @return A new Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends RegularVerificationRequest.Builder<EmailVerificationRequest, Builder> {
		String from;

		Builder() {
			super(Channel.EMAIL);
		}

		/**
		 * (REQUIRED)
		 * The e-mail address to send the verification request from.
		 *
		 * @param from The sender e-mail address.
		 *
		 * @return This builder.
		 */
		public Builder from(String from) {
			this.from = from;
			return this;
		}

		/**
		 * (REQUIRED)
		 * The email address to send the verification request to.
		 *
		 * @param to The recipient e-mail address.
		 *
		 * @return This builder.
		 */
		@Override
		public Builder to(String to) {
			return super.to(to);
		}

		@Override
		protected Builder codeLength(int codeLength) {
			return super.codeLength(codeLength);
		}

		@Override
		protected Builder timeout(int timeout) {
			return super.timeout(timeout);
		}

		@Override
		protected Builder locale(Locale locale) {
			return super.locale(locale);
		}

		@Override
		protected Builder clientRef(String clientRef) {
			return super.clientRef(clientRef);
		}

		@Override
		public EmailVerificationRequest build() {
			return new EmailVerificationRequest(this);
		}
	}
}
