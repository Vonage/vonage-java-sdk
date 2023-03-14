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
 * Defines properties for sending a verification code to a user over WhatsApp Interactive.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WhatsappInteractiveVerificationRequest extends RegularVerificationRequest {

	WhatsappInteractiveVerificationRequest(Builder builder) {
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

	public static final class Builder extends RegularVerificationRequest.Builder<WhatsappInteractiveVerificationRequest, Builder> {
		Builder() {
			super(Channel.WHATSAPP_INTERACTIVE);
		}

		@Override
		public WhatsappInteractiveVerificationRequest build() {
			return new WhatsappInteractiveVerificationRequest(this);
		}
	}
}
