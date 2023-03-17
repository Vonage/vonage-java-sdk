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
 * Defines properties for sending a verification code to a user via SMS.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class SmsVerificationRequest extends RegularVerificationRequest {

	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	static final class Workflow extends VerificationRequest.Workflow {
		String appHash;

		Workflow(Channel channel, String to, String appHash) {
			super(channel, to);
			if ((this.appHash = appHash) != null && appHash.length() != 11) {
				throw new IllegalArgumentException("Android app hash must be 11 characters.");
			}
		}

		@JsonProperty("app_hash")
		public String getAppHash() {
			return appHash;
		}
	}

	SmsVerificationRequest(Builder builder) {
		super(builder);
		workflow.set(0, new Workflow(builder.channel, builder.to, builder.appHash));
	}

	@JsonIgnore
	public String getAppHash() {
		if (workflow == null || !((workflow.get(0)) instanceof Workflow)) return null;
		return ((Workflow) workflow.get(0)).appHash;
	}

	/**
	 * Entry point for constructing an instance of this class.
	 *
	 * @return A new Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends RegularVerificationRequest.Builder<SmsVerificationRequest, Builder> {
		String appHash;

		Builder() {
			super(Channel.SMS);
		}

		/**
		 * (OPTIONAL)
		 * Android Application Hash Key for automatic code detection on a user's device.
		 *
		 * @param appHash The Android application hash key (11 characters in length).
		 *
		 * @return This builder.
		 */
		public Builder appHash(String appHash) {
			this.appHash = appHash;
			return this;
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
		public SmsVerificationRequest build() {
			return new SmsVerificationRequest(this);
		}
	}
}
