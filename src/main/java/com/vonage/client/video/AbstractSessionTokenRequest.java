/*
 *   Copyright 2024 Vonage
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
package com.vonage.client.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.util.Objects;

/**
 * Base class for requests that require a sessionId and token.
 *
 * @since 8.5.0
 */
abstract class AbstractSessionTokenRequest extends JsonableBaseObject {
	String sessionId, token;

	AbstractSessionTokenRequest() {}

	AbstractSessionTokenRequest(Builder<?, ?> builder) {
		sessionId = Objects.requireNonNull(builder.sessionId, "Session ID is required.");
		token = Objects.requireNonNull(builder.token, "Token is required.");
	}

	/**
	 *
	 * @return
	 */
	@JsonProperty("sessionId")
	public String getSessionId() {
		return sessionId;
	}

	/**
	 *
	 * @return
	 */
	@JsonProperty("token")
	public String getToken() {
		return token;
	}

	@SuppressWarnings("unchecked")
	protected abstract static class Builder<R extends AbstractSessionTokenRequest, B extends Builder<R, B>> {
		private String sessionId, token;

		/**
		 * (REQUIRED)
		 * Video session ID for the SIP call to join.
		 *
		 * @param sessionId The session ID as a string.
		 * @return This builder.
		 */
		public B sessionId(String sessionId) {
			this.sessionId = sessionId;
			return (B) this;
		}

		/**
		 * (REQUIRED)
		 * The video token to be used for the participant being called.
		 * You can add token data to identify that the participant is on a SIP endpoint or for other identifying data,
		 * such as phone numbers. The video client libraries include properties for inspecting the connection data
		 * for a client connected to a session. See the Token Creation developer guide for more info.
		 *
		 * @param token The token as a string.
		 *
		 * @return This builder.
		 */
		public B token(String token) {
			this.token = token;
			return (B) this;
		}

		/**
		 * Builds the {@linkplain AbstractSessionTokenRequest} object with this builder's settings.
		 *
		 * @return A new {@link AbstractSessionTokenRequest} instance.
		 */
		public abstract R build();
	}
}
