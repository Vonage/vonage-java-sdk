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
package com.vonage.client.users.channels;

import com.fasterxml.jackson.annotation.*;
import java.net.URI;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a Websocket channel.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Websocket extends Channel {
	private URI uri;
	private ContentType contentType;
	private Map<String, ?> headers;

	protected Websocket() {}

	public Websocket(String uri) {
		Objects.requireNonNull(uri, "Websocket URI is required");
		if (!(uri.startsWith("ws://") || uri.startsWith("wss://"))) {
			throw new IllegalArgumentException("Invalid websocket URI protocol.");
		}
		this.uri = URI.create(uri);
	}

	public Websocket(String uri, ContentType contentType, Map<String, ?> headers) {
		this(uri);
		this.contentType = contentType;
		this.headers = headers;
	}

	/**
	 * Full URI of the websocket.
	 *
	 * @return The websocket URI, or {@code null} if not set.
	 */
	@JsonProperty("uri")
	public URI getUri() {
		return uri;
	}

	/**
	 * MIME type of the content sent over the websocket.
	 *
	 * @return The media content type as an enum, or {@code null} if unknown.
	 */
	@JsonProperty("content-type")
	public ContentType getContentType() {
		return contentType;
	}

	/**
	 * Custom properties to add to the header.
	 *
	 * @return Header properties as a Map, or {@code null} if not set.
	 */
	@JsonProperty("headers")
	public Map<String, ?> getHeaders() {
		return headers;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Websocket websocket = (Websocket) o;
		return Objects.equals(uri, websocket.uri) &&
				contentType == websocket.contentType &&
				Objects.equals(headers, websocket.headers);
	}

	@Override
	public int hashCode() {
		return Objects.hash(uri, contentType, headers);
	}

	/**
	 * Represents the possible content types for a Websocket.
	 */
	public enum ContentType {
		/**
		 * audio/l16 with bitrate of 8000.
		 */
		AUDIO_L16_8K("audio/l16;rate=8000"),

		/**
		 * audio/l16 with bitrate of 16000.
		 */
		AUDIO_L16_16K("audio/l16;rate=16000");

		private final String value;

		ContentType(String ser) {
			this.value = ser;
		}

		@JsonValue
		@Override
		public String toString() {
			return value;
		}

		@JsonCreator
		public static ContentType fromString(String value) {
			if (value == null) return null;
			switch (value.toLowerCase()) {
				case ("audio/l16;rate=8000"): return AUDIO_L16_8K;
				case ("audio/l16;rate=16000"): return AUDIO_L16_16K;
				default: throw new IllegalArgumentException("Unknown content-type: "+value);
			}
		}
	}
}
