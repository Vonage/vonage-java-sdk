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
package com.vonage.client.voice;

import com.fasterxml.jackson.annotation.*;
import com.vonage.client.JsonableBaseObject;

/**
 * Configure the behavior of Vonage's advanced machine detection. See
 * <a href=https://developer.vonage.com/en/voice/voice-api/concepts/advanced-machine-detection>
 * the documentation</a> for details.
 *
 * @since 7.4.0
 */
public class AdvancedMachineDetection extends JsonableBaseObject {

	/**
	 * Represents the beep detection mode.
	 */
	public enum Mode {
		/**
		 * Detect machine and send a status human / machine webhook.
		 */
		DETECT,

		/**
		 * Detect machine and send back a status human / machine webhook, but also when machine is detected, attempt
		 * to detect voice mail beep and send back another status machine webhook with {@code sub_state: beep_start}.
		 */
		DETECT_BEEP,

		/**
		 * Asynchronously start processing NCCO actions during the detection phase.
		 *
		 * @since 8.2.0
		 */
		DEFAULT;

		@JsonCreator
		public static Mode fromString(String value) {
			try {
				return Mode.valueOf(value.toUpperCase());
			}
			catch (NullPointerException | IllegalArgumentException ex) {
				return null;
			}
		}

		@JsonValue
		@Override
		public String toString() {
			return name().toLowerCase();
		}
	}

	private MachineDetection behavior;
	private Mode mode;
	private Integer beepTimeout;

	AdvancedMachineDetection() {}

	AdvancedMachineDetection(Builder builder) {
		behavior = builder.behavior;
		mode = builder.mode;
		if ((beepTimeout = builder.beepTimeout) != null && (beepTimeout < 45 || beepTimeout > 120)) {
			throw new IllegalArgumentException("Beep timeout must be between 45 and 120 seconds.");
		}
	}

	/**
	 * Defines how the system responds when a machine is detected.
	 * When {@link MachineDetection#HANGUP} is used, the call will be terminated if a machine is detected.
	 * When {@link MachineDetection#CONTINUE} is used, the call will continue even if a machine is detected.
	 *
	 * @return The machine detection behaviour as an enum.
	 */
	@JsonProperty("behavior")
	public MachineDetection getBehavior() {
		return behavior;
	}

	/**
	 * Detect if machine answered and sends a human or machine status in the webhook payload. When set to
	 * {@link Mode#DETECT_BEEP}, the system also attempts to detect voice mail beep and sends an additional
	 * parameter {@code sub_state} in the webhook with the value {@code beep_start}.
	 *
	 * @return The machine detection mode.
	 */
	@JsonProperty("mode")
	public Mode getMode() {
		return mode;
	}

	/**
	 * Maximum time in seconds Vonage should wait for a machine beep to be detected. A machine event with
	 * {@code sub_state} set to {@code beep_timeout} will be sent if the timeout is exceeded.
	 *
	 * @return The maximum wait time in seconds for machine detection, or {@code null} if unset.
	 */
	@JsonProperty("beep_timeout")
	public Integer getBeepTimeout() {
		return beepTimeout;
	}

	/**
	 * Entry point for constructing an instance of this class.
	 *
	 * @return A new builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder for specifying the Advanced Machine Detection properties.
	 */
	public static class Builder {
		private MachineDetection behavior;
		private Mode mode;
		private Integer beepTimeout;

		Builder() {}

		/**
		 * Define how the system responds when a machine is detected.
		 * When {@link MachineDetection#HANGUP} is used, the call will be terminated if a machine is detected.
		 * When {@link MachineDetection#CONTINUE} is used, the call will continue even if a machine is detected.
		 *
		 * @param behavior The machine detection behaviour as an enum.
		 *
		 * @return This builder.
		 */
		public Builder behavior(MachineDetection behavior) {
			this.behavior = behavior;
			return this;
		}

		/**
		 * Detect if machine answered and sends a human or machine status in the webhook payload. When set to
		 * {@link Mode#DETECT_BEEP}, the system also attempts to detect voice mail beep and sends an additional
		 * parameter {@code sub_state} in the webhook with the value {@code beep_start}.
		 *
		 * @param mode The machine detection mode enum.
		 *
		 * @return This builder.
		 */
		public Builder mode(Mode mode) {
			this.mode = mode;
			return this;
		}

		/**
		 * Maximum time in seconds Vonage should wait for a machine beep to be detected. A machine event with
		 * {@code sub_state} set to {@code beep_timeout} will be sent if the timeout is exceeded.
		 *
		 * @param beepTimeout The beep timeout in seconds as an integer.
		 *
		 * @return This builder.
		 */
		public Builder beepTimeout(int beepTimeout) {
			this.beepTimeout = beepTimeout;
			return this;
		}

		/**
		 * Constructs the AdvancedMachineDetection object.
		 *
		 * @return A new AdvancedMachineDetection instance with this builder's properties.
		 */
		public AdvancedMachineDetection build() {
			return new AdvancedMachineDetection(this);
		}
	}
}
