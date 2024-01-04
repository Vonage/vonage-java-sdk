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
package com.vonage.client.meetings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InitialJoinOptions {
	private MicrophoneState microphoneState;

	protected InitialJoinOptions() {
	}

	InitialJoinOptions(Builder builder) {
		microphoneState = builder.microphoneState;
	}

	/**
	 * The default microphone option for users in the pre-join screen of this room.
	 *
	 * @return The microphone state, as an enum
	 */
	@JsonProperty("microphone_state")
	public MicrophoneState getMicrophoneState() {
		return microphoneState;
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
