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
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
abstract class RegularVerificationRequest extends VerificationRequest {
	protected Locale locale;
	protected Integer channelTimeout, codeLength;
	protected String clientRef;

	protected RegularVerificationRequest(Builder<?, ?> builder) {
		super(builder);
		locale = builder.locale;
		clientRef = builder.clientRef;
		if ((channelTimeout = builder.timeout) != null && (channelTimeout < 60 || channelTimeout > 900)) {
			throw new IllegalArgumentException("Delivery wait timeout must be between 60 and 900 seconds.");
		}
		if ((codeLength = builder.codeLength) != null && (codeLength < 4 || codeLength > 10)) {
			throw new IllegalArgumentException("Code length must be between 4 and 10.");
		}
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

	@SuppressWarnings("unchecked")
	protected abstract static class Builder<V extends RegularVerificationRequest, B extends Builder<V, B>> extends VerificationRequest.Builder<V, B> {
		protected Locale locale;
		protected String clientRef;
		protected Integer timeout, codeLength;

		protected Builder(Channel channel) {
			super(channel);
		}

		/**
		 * (REQUIRED)
		 * Length of the code to send to the user, must be between 4 and 10 (inclusive).
		 *
		 * @param codeLength The verification code length.
		 *
		 * @return This builder.
		 */
		protected B codeLength(int codeLength) {
			this.codeLength = codeLength;
			return (B) this;
		}

		/**
		 * (OPTIONAL)
		 * Specifies the wait time in seconds between attempts to delivery the verification code.
		 * Must be between 60 and 900. Default is 300.
		 *
		 * @param timeout The delivery timeout in seconds.
		 *
		 * @return This builder.
		 */
		protected B timeout(int timeout) {
			this.timeout = timeout;
			return (B) this;
		}

		/**
		 * (OPTIONAL)
		 * Languages that are available to use.
		 *
		 * @param locale The language locale as an enum.
		 *
		 * @return This builder.
		 */
		protected B locale(Locale locale) {
			this.locale = locale;
			return (B) this;
		}

		/**
		 * (OPTIONAL)
		 * If this reference is set when the request is sent, it will be included in the callbacks.
		 *
		 * @param clientRef The callback reference for this request.
		 *
		 * @return This builder.
		 */
		protected B clientRef(String clientRef) {
			this.clientRef = clientRef;
			return (B) this;
		}
	}
}
