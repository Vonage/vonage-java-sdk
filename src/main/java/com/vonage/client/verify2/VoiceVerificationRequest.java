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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Defines properties for sending a verification code to a user over a voice call.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class VoiceVerificationRequest extends RegularVerificationRequest {

	VoiceVerificationRequest(Builder builder) {
		super(builder);
	}


	/**
	 * Entry point for constructing an instance of this class.
	 *
	 * @return A new Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends RegularVerificationRequest.Builder<VoiceVerificationRequest, Builder> {
		Builder() {
			super(Channel.VOICE);
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
		public VoiceVerificationRequest build() {
			return new VoiceVerificationRequest(this);
		}
	}
}
