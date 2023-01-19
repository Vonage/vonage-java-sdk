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
package com.vonage.client.meetings;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class InitialJoinOptions {
	private final MicrophoneState microphoneState;

	InitialJoinOptions(Builder builder) {
		microphoneState = builder.microphoneState;
	}

	/**
	 * Set the default microphone option for users in the pre-join screen of this room.
	 */
	@JsonProperty("microphone_state")
	public MicrophoneState getMicrophoneState() {
		return microphoneState;
	}
	
	/**
	 * Generates a JSON payload from this request.
	 *
	 * @return JSON representation of this InitialJoinOptions object.
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
	 * Entry point for constructing an instance of this class.
	 * 
	 * @return A new Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private MicrophoneState microphoneState;
	
		Builder() {}
	
		/**
		 *
		 * @param microphoneState Set the default microphone option for users in the pre-join screen of this room.
		 *
		 * @return This builder.
		 */
		public Builder microphoneState(MicrophoneState microphoneState) {
			this.microphoneState = microphoneState;
			return this;
		}

	
		/**
		 * Builds the {@linkplain InitialJoinOptions}.
		 *
		 * @return An instance of InitialJoinOptions, populated with all fields from this builder.
		 */
		public InitialJoinOptions build() {
			return new InitialJoinOptions(this);
		}
	}
}