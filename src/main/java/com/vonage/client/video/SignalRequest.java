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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.util.Objects;

public class SignalRequest extends JsonableBaseObject {
	private final String type, data;
	@JsonIgnore String sessionId;
	@JsonIgnore String connectionId;

	SignalRequest(Builder builder) {
		this.type = Objects.requireNonNull(builder.type, "Type is required.");
		this.data = Objects.requireNonNull(builder.data, "Data is required.");
	}

	/**
	 * @return The signal type.
	 */
	@JsonProperty("type")
	public String getType() {
		return type;
	}

	/**
	 * @return The signal data.
	 */
	@JsonProperty("data")
	public String getData() {
		return data;
	}

	/**
	 * Entry point for constructing an instance of SignalRequest.
	 *
	 * @return A new Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private String type, data;
	
		Builder() {}
	
		/**
		 * (REQUIRED)
		 * Type of data that is being sent to the client. This cannot exceed 128 bytes.
		 *
		 * @param type The type as a string.
		 * @return This builder.
		 */
		public Builder type(String type) {
			this.type = type;
			return this;
		}

		/**
		 * (REQUIRED)
		 * Payload that is being sent to the client. This cannot exceed 8 kilobytes.
		 *
		 * @param data The data as a string.
		 * @return This builder.
		 */
		public Builder data(String data) {
			this.data = data;
			return this;
		}
	
		/**
		 * Builds the {@linkplain SignalRequest}.
		 *
		 * @return An instance of SignalRequest, populated with all fields from this builder.
		 */
		public SignalRequest build() {
			return new SignalRequest(this);
		}
	}
}
