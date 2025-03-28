/*
 *   Copyright 2025 Vonage
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

/**
 * Represents HTTP Live Streaming (HLS) options for a {@link Broadcast}.
 */
public final class Hls extends JsonableBaseObject {
	private Boolean dvr, lowLatency;

	Hls() {}

	Hls(Builder builder) {
		// Non-short-circuiting for setter
		if (((dvr = builder.dvr) != null && dvr) & ((lowLatency = builder.lowLatency) != null && lowLatency)) {
			throw new IllegalArgumentException("Cannot set both DVR and Low Latency on HLS.");
		}
	}

	/**
	 * Whether DVR functionality — rewinding, pausing, and resuming — is enabled in players that support it.
	 *
	 * @return {@code true} if DVR functionality is enabled, or {@code null} if unknown / unset.
	 */
	@JsonProperty("dvr")
	public Boolean getDvr() {
		return dvr;
	}

	/**
	 * Whether low-latency mode is enabled for the HLS stream. Some HLS players do not support low-latency mode.
	 *
	 * @return {@code true} if low latency mode is enabled, or {@code null} if unknown / unset.
	 */
	@JsonProperty("lowLatency")
	public Boolean getLowLatency() {
		return lowLatency;
	}

	/**
	 * Entrypoint for constructing an instance of this class.
	 *
	 * @return A new Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Used to create the Hls object.
	 */
	public static final class Builder {
		private Boolean dvr, lowLatency;

		Builder() {}

		/**
		 * Whether to enable DVR functionality — rewinding, pausing, and resuming — in players that support it
		 * (true), or not (false, the default). With DVR enabled, the HLS URL will include a {@code ?DVR} query
		 * string appended to the end.
		 *
		 * @param dvr DVR toggle.
		 *
		 * @return This builder.
		 */
		public Builder dvr(boolean dvr) {
			this.dvr = dvr;
			return this;
		}

		/**
		 * Whether to enable low-latency mode for the HLS stream. Some HLS players do not support low-latency mode.
		 * This feature is incompatible with DVR mode HLS broadcasts.
		 *
		 * @param lowLatency Low latency toggle.
		 *
		 * @return This builder.
		 */
		public Builder lowLatency(boolean lowLatency) {
			this.lowLatency = lowLatency;
			return this;
		}

		/**
		 * Builds the HLS object with the selected settings.
		 *
		 * @return A new HLS instance.
		 */
		public Hls build() {
			return new Hls(this);
		}
	}
}
