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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * Represents properties of a live streaming broadcast.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Broadcast extends StreamComposition {
	private String multiBroadcastTag;
	@JsonProperty("updatedAt") private Long updatedAt;
	@JsonProperty("maxDuration") private Integer maxDuration;
	private Integer maxBitrate;
	private BroadcastStatus status;
	private BroadcastUrls broadcastUrls;
	private Settings settings;
	private Outputs outputs;

	protected Broadcast() {
	}

	protected Broadcast(Builder builder) {
		super(builder);
		outputs = new Outputs();
		outputs.rtmp = builder.rtmps.isEmpty() ? null : builder.rtmps;
		if ((outputs.hls = builder.hls) == null && outputs.rtmp == null) {
			throw new IllegalStateException("At least one output stream (RTMP or HLS) must be specified.");
		}
		if ((maxDuration = builder.maxDuration) != null && (maxDuration < 60 || maxDuration > 36000)) {
			throw new IllegalArgumentException("maxDuration must be between 60 seconds and 10 hours.");
		}
		maxBitrate = builder.maxBitrate;
		multiBroadcastTag = builder.multiBroadcastTag;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	static class Settings {
		@JsonProperty("hls") private Hls hls;
	}

	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	static class Outputs {
		@JsonProperty("rtmp") private List<Rtmp> rtmp;
		@JsonProperty("hls") private Hls hls;
	}

	@JsonProperty("outputs")
	Outputs getOutputs() {
		return outputs;
	}

	@JsonProperty("settings")
	Settings getSettings() {
		return settings;
	}

	/**
	 * @return The unique tag for simultaneous broadcasts (if one was set).
	 */
	@JsonProperty("multiBroadcastTag")
	public String getMultiBroadcastTag() {
		return multiBroadcastTag;
	}

	/**
	 * @return For this start method, this timestamp matches the createdAt timestamp.
	 */
	@JsonProperty("updatedAt")
	public Long getUpdatedAtRaw() {
		return updatedAt;
	}

	/**
	 * @return The updatedAt time, or {@code null} if unset / not applicable.
	 */
	@JsonIgnore
	public Instant getUpdatedAt() {
		return updatedAt != null ? Instant.ofEpochMilli(updatedAt) : null;
	}

	/**
	 * @return The maximum duration for the broadcast (if one was set), in seconds.
	 */
	@JsonProperty("maxDuration")
	public Integer getMaxDurationRaw() {
		return maxDuration;
	}

	/**
	 * @return The maximum duration for the broadcast (precision in seconds), or {@code null} if unset.
	 */
	@JsonIgnore
	public Duration getMaxDuration() {
		return maxDuration != null ? Duration.ofSeconds(maxDuration) : null;
	}

	/**
	 * @return The unique tag for simultaneous broadcasts (if one was set).
	 */
	@JsonProperty("maxBitrate")
	public Integer getMaxBitrate() {
		return maxBitrate;
	}

	/**
	 * @return Current status of the broadcast as an enum.
	 */
	@JsonProperty("status")
	public BroadcastStatus getStatus() {
		return status;
	}

	/**
	 * @return Details about the HLS and RTMP broadcasts.
	 */
	@JsonProperty("broadcastUrls")
	public BroadcastUrls getBroadcastUrls() {
		return broadcastUrls;
	}

	/**
	 * @return Further details on the HLS broadcast stream, or {@code null} if not applicable.
	 */
	@JsonIgnore
	public Hls getHlsSettings() {
		return settings != null ? settings.hls : null;
	}

	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static Broadcast fromJson(String json) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, Broadcast.class);
		}
		catch (IOException ex) {
			throw new VonageUnexpectedException("Failed to produce Broadcast from json.", ex);
		}
	}

	/**
	 * Instantiates a Builder, used to construct this object.
	 * Note that you must specify at least one RTMP stream or HLS properties.
	 *
	 * @param sessionId ID of the Vonage Video session to broadcast.
	 *
	 * @return A new {@linkplain Builder}.
	 */
	public static Builder builder(String sessionId) {
		return new Builder(sessionId);
	}

	/**
	 * Used to construct a Broadcast object.
	 */
	public static class Builder extends StreamComposition.Builder {
		private final List<Rtmp> rtmps = new ArrayList<>();
		private Hls hls;
		private Integer maxDuration, maxBitrate;
		private String multiBroadcastTag;

		Builder(String sessionId) {
			this.sessionId = sessionId;
		}

		/**
		 * (OPTIONAL)
		 * Whether streams included in the broadcast are selected automatically ("auto", the default) or manually
		 * ("manual"). When streams are selected automatically ("auto"), all streams in the session can be included
		 * in the broadcast. When streams are selected manually ("manual"), you specify which streams to include
		 * based on calls to {@link VideoClient#addBroadcastStream(String, String, Boolean, Boolean)}.
		 * For both automatic and manual modes, the broadcast composer includes streams based on
		 * <a href=https://tokbox.com/developer/guides/archive-broadcast-layout/#stream-prioritization-rules>
		 * stream prioritization rules</a>.
		 *
		 * @param streamMode The streaming mode as an enum.
		 *
		 * @return This builder.
		 */
		public Builder streamMode(StreamMode streamMode) {
			this.streamMode = streamMode;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * The resolution of the broadcast: either "640x480" (SD landscape, the default), "1280x720" (HD landscape),
		 * "1920x1080" (FHD landscape), "480x640" (SD portrait), "720x1280" (HD portrait), or "1080x1920"
		 * (FHD portrait). You may want to use a portrait aspect ratio for broadcasts that include video streams
		 * from mobile devices (which often use the portrait aspect ratio).
		 *
		 * @param resolution The video resolution as an enum.
		 *
		 * @return This builder.
		 */
		public Builder resolution(Resolution resolution) {
			this.resolution = resolution;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * Specify this to assign the initial layout type for the broadcast.
		 * If you do not specify an initial layout type, the broadcast stream uses the Best Fit layout type.
		 *
		 * @param layout The broadcast's initial layout properties.
		 *
		 * @return This builder.
		 */
		public Builder layout(StreamCompositionLayout layout) {
			this.layout = layout;
			return this;
		}

		/**
		 * (OPTIONAL, but REQUIRED if HLS is unspecified)
		 * You can specify up to five target RTMP streams (or just one).
		 * For each RTMP stream, specify serverUrl (the RTMP server URL), streamName
		 * (the stream name, such as the YouTube Live stream name or the Facebook stream key), and
		 * (optionally) id (a unique ID for the stream). If you specify an ID, it will be included in the REST call
		 * response and the REST method for getting information about a live streaming broadcast. Vonage streams
		 * the session to each RTMP URL you specify. Note that Vonage live streaming supports RTMP and RTMPS.
		 *
		 * @param rtmp The RTMP stream to include in the broadcast.
		 *
		 * @return This builder.
		 */
		public Builder addRtmpStream(Rtmp rtmp) {
			rtmps.add(Objects.requireNonNull(rtmp, "Rtmp cannot be null."));
			return this;
		}

		/**
		 * (OPTIONAL, but REQUIRED if HLS is unspecified)
		 * You can specify up to five target RTMP streams (or just one).
		 * For each RTMP stream, specify serverUrl (the RTMP server URL), streamName
		 * (the stream name, such as the YouTube Live stream name or the Facebook stream key), and
		 * (optionally) id (a unique ID for the stream). If you specify an ID, it will be included in the REST call
		 * response and the REST method for getting information about a live streaming broadcast. Vonage streams
		 * the session to each RTMP URL you specify. Note that Vonage live streaming supports RTMP and RTMPS.
		 *
		 * @param rtmps The RTMP streams to include in the broadcast.
		 *
		 * @return This builder.
		 * @see #addRtmpStream(Rtmp)
		 */
		public Builder rtmpStreams(Collection<Rtmp> rtmps) {
			for (Rtmp rtmp : Objects.requireNonNull(rtmps, "Rtmps cannot be null.")) {
				addRtmpStream(rtmp);
			}
			return this;
		}

		/**
		 * (OPTIONAL, but REQUIRED if no RTMP URLs are set)
		 * Sets the HTTP Live Streaming (HLS) output of the broadcast.
		 *
		 * @param hls The HLS broadcast properties.
		 *
		 * @return This builder.
		 */
		public Builder hls(Hls hls) {
			this.hls = hls;
			return this;
		}

		/**
		 * (OPTIONAL) The maximum bitrate for the broadcast, in bits per second.
		 *
		 * @param maxBitrate The maximum bitrate as an integer.
		 *
		 * @return This builder.
		 */
		public Builder maxBitrate(int maxBitrate) {
			this.maxBitrate = maxBitrate;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * The maximum duration for the broadcast, in seconds. The broadcast will automatically stop when the
		 * maximum duration is reached. You can set the maximum duration to a value from 60 (60 seconds) to 36000
		 * (10 hours). The default maximum duration is 4 hours (14400 seconds).
		 *
		 * @param maxDuration The maximum duration as an integer.
		 *
		 * @return This builder.
		 */
		public Builder maxDuration(int maxDuration) {
			this.maxDuration = maxDuration;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * Set this to support multiple broadcasts for the same session simultaneously.
		 * Set this to a unique string for each simultaneous broadcast of an ongoing session.
		 *
		 * @param multiBroadcastTag The tag for this broadcast.
		 *
		 * @return This builder.
		 */
		public Builder multiBroadcastTag(String multiBroadcastTag) {
			this.multiBroadcastTag = multiBroadcastTag;
			return this;
		}

		/**
		 * Builds the {@linkplain Broadcast} object with this builder's settings.
		 *
		 * @return A new {@link Broadcast} instance.
		 */
		public Broadcast build() {
			return new Broadcast(this);
		}
	}
}
