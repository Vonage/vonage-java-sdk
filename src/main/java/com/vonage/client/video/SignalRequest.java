/*
 *   Copyright 2022 Vonage
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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import java.util.Objects;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SignalRequest {
	private final String type, data;

	SignalRequest(Builder builder) {
		this.type = Objects.requireNonNull(builder.type, "Type is required.");
		this.data = Objects.requireNonNull(builder.data, "Data is required.");
	}

	public String getType() {
		return type;
	}

	public String getData() {
		return data;
	}
	
	/**
	 * Generates a JSON payload from this request.
	 *
	 * @return JSON representation of this SignalRequest object.
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
	 * Entry point for constructing an instance of .
	 *
	 * @return A new Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private String type;
		private String data;
	
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
		 * Payload that is being sent to the client. This cannot exceed 8kb.
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
