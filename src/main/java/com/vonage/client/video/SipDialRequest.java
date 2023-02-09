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
package com.vonage.client.video;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents an outbound SIP dial request's properties.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SipDialRequest {
	private final String sessionId, token;
	@JsonProperty("sip") private final Sip sip = new Sip();

	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	static class Sip {
		@JsonProperty("uri") String uri;
		@JsonProperty("from") String from;
		@JsonProperty("headers") Map<String, String> headers;
		@JsonProperty("auth") Auth auth;
		@JsonProperty("secure") Boolean secure;
		@JsonProperty("video") Boolean video;
		@JsonProperty("observeForceMute") Boolean observeForceMute;

		@JsonInclude(value = JsonInclude.Include.NON_NULL)
		static class Auth {
			@JsonProperty("username") String username;
			@JsonProperty("password") String password;
		}
	}

	private SipDialRequest(Builder builder) {
		sessionId = Objects.requireNonNull(builder.sessionId, "Session ID is required.");
		token = Objects.requireNonNull(builder.token, "Token is required.");
		sip.uri = Objects.requireNonNull(builder.uri, "SIP URI is required.");
		sip.from = builder.from;
		sip.secure = builder.secure;
		sip.video = builder.video;
		sip.observeForceMute = builder.observeForceMute;
		if (!builder.headers.isEmpty()) {
			sip.headers = builder.headers;
		}
		if (builder.username != null) {
			sip.auth = new Sip.Auth();
			sip.auth.username = builder.username;
			if (builder.password != null) {
				sip.auth.password = builder.password;
			}
		}
		else if (builder.password != null) {
			throw new IllegalStateException("SIP Auth username is required if password is provided.");
		}
	}

	@JsonProperty("sessionId")
	public String getSessionId() {
		return sessionId;
	}

	@JsonProperty("token")
	public String getToken() {
		return token;
	}

	@JsonIgnore
	public String getUri() {
		return sip.uri;
	}

	@JsonIgnore
	public String getFrom() {
		return sip.from;
	}

	@JsonIgnore
	public Map<String, String> getHeaders() {
		return sip.headers;
	}

	@JsonIgnore
	public String getUsername() {
		return sip.auth != null ? sip.auth.username : null;
	}

	@JsonIgnore
	public String getPassword() {
		return sip.auth != null ? sip.auth.password : null;
	}

	@JsonIgnore
	public Boolean getSecure() {
		return sip.secure;
	}

	@JsonIgnore
	public Boolean getVideo() {
		return sip.video;
	}

	@JsonIgnore
	public Boolean getObserveForceMute() {
		return sip.observeForceMute;
	}

	/**
	 * Generates a JSON payload from this request.
	 *
	 * @return JSON representation of this SipDialRequest object.
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
	 * Instantiates a Builder, used to construct this object.
	 *
	 * @return A new {@linkplain SipDialRequest.Builder}.
	 */
	public static SipDialRequest.Builder builder() {
		return new SipDialRequest.Builder();
	}

	/**
	 * Used to create an SipDialRequest object.
	 *
	 * @see SipDialRequest
	 */
	public static class Builder {
		private final Map<String, String> headers = new HashMap<>();
		private String sessionId, token, from, uri, username, password;
		private Boolean secure, video, observeForceMute;

		/**
		 * (REQUIRED)
		 * Video session ID for the SIP call to join.
		 *
		 * @param sessionId The session ID as a string.
		 * @return This builder.
		 */
		public Builder sessionId(String sessionId) {
			this.sessionId = sessionId;
			return this;
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
		public Builder token(String token) {
			this.token = token;
			return this;
		}

		/**
		 * (REQUIRED)
		 * The SIP URI to be used as destination of the SIP call initiated from Vonage to your SIP platform.
		 *
		 * @param uri The URI as an object.
		 *
		 * @param secure Whether the negotiation between Vonage and the SIP endpoint will be done securely.
		 * Note that this will only apply to the negotiation itself, and not to the transmission of audio.
		 * Setting this to true will append {@code transport=tls} to the URI.
		 * If you also want the audio transmission to be encrypted, set the {@link #secure(boolean)}
		 * property of this builder to true.
		 *
		 * @return This builder.
		 */
		public Builder uri(URI uri, boolean secure) {
			this.uri = uri + (secure ? ";transport=tls" : "");
			return this;
		}

		/**
		 * (OPTIONAL)
		 * The number or string that will be sent to the final SIP number as the caller. It must be a string in
		 * the form of {@code from@example.com}, where from can be a string or a number. If from is set to a number
		 * (for example, "14155550101@example.com"), it will show up as the incoming number on PSTN phones. If from
		 * is undefined or set to a string (for example, "joe@example.com"), +00000000 will show up as the incoming
		 * number on PSTN phones.
		 *
		 * @param from The caller number or email as a string.
		 *
		 * @return This builder.
		 */
		public Builder from(String from) {
			this.from = from;
			return this;
		}

		/**
		 * (OPTIONAL, but REQUIRED if {@link #password(String)} is provided)
		 * The username to be used in the SIP INVITE request for HTTP digest authentication,
		 * if it is required by your SIP platform.
		 *
		 * @param username The username as a string.
		 *
		 * @return This builder.
		 * @see #password(String)
		 */
		public Builder username(String username) {
			this.username = username;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * The password corresponding to the username for to be used in the SIP INVITE request.
		 *
		 * @param password The password as a string.
		 *
		 * @return This builder.
		 * @see #username(String)
		 */
		public Builder password(String password) {
			this.password = password;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * Define a custom header to be added to the SIP INVITE request initiated from Vonage to your SIP platform.
		 *
		 * @param key The header key.
		 * @param value The header value.
		 *
		 * @return This builder.
		 * @see #addHeaders(Map)
		 */
		public Builder addHeader(String key, String value) {
			headers.put(key, value);
			return this;
		}

		/**
		 * (OPTIONAL)
		 * Define custom headers (i.e. those starting with "X-") to be added to the SIP INVITE request
		 * initiated from Vonage to your SIP platform.
		 *
		 * @param headers Custom header key-value pairs as strings.
		 *
		 * @return This builder.
		 * @see #addHeader(String, String)
		 */
		public Builder addHeaders(Map<String, String> headers) {
			this.headers.putAll(headers);
			return this;
		}

		/**
		 * (OPTIONAL)
		 * Flag that indicates whether the media must be transmitted encrypted (true) or not (false, the default).
		 *
		 * @param secure Whether media should be transmitted securely.
		 *
		 * @return This builder.
		 */
		public Builder secure(boolean secure) {
			this.secure = secure;
			return this;
		}

		/**
		 * (OPTIONAL)
		 *  Flag that indicates whether the SIP call will include video (true) or not (false, the default).
		 *  With video included, the SIP client's video is included in the video stream that is sent to the
		 *  Vonage video session. The SIP client will receive a single composed video of the published streams
		 *  in the Vonage video session.
		 *
		 * @param video Whether to include video in the SIP call.
		 *
		 * @return This builder.
		 */
		public Builder video(boolean video) {
			this.video = video;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * flag that indicates whether the SIP end point observes force mute moderation (true) or not (false, the
		 * default). Also, with observeForceMute set to true, the caller can press "*6" to unmute and mute the
		 * published audio. For the "*6" mute toggle to work, the SIP caller must negotiate RFC2833 DTMFs
		 * (RFC2833/RFC4733 digits). The mute toggle is not supported with SIP INFO or in-band DTMFs. A message
		 * (in English) is played to the caller when the caller mutes and unmutes, or when the SIP client is muted
		 * through a force mute action.
		 *
		 * @param observeForceMute Whether to observe forceMute moderation.
		 *
		 * @return This builder.
		 */
		public Builder observeForceMute(boolean observeForceMute) {
			this.observeForceMute = observeForceMute;
			return this;
		}

		/**
		 * Builds the {@linkplain SipDialRequest} object with this builder's settings.
		 *
		 * @return A new {@link SipDialRequest} instance.
		 */
		public SipDialRequest build() {
			return new SipDialRequest(this);
		}
	}
}
