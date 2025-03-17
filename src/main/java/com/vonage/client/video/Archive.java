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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;
import java.time.Duration;

/**
* Represents an archive of a video session.
*/
public class Archive extends StreamComposition {
    private Long size;
    @JsonProperty("duration") private Integer duration;
    private String name, reason, multiArchiveTag;
    private URI url;
    private ArchiveStatus status;
    private OutputMode outputMode;
    private Integer quantizationParameter;

    protected Archive() {
    }

    protected Archive(Builder builder) {
        if ((sessionId = builder.sessionId) == null || sessionId.isEmpty()) {
            throw new IllegalArgumentException("Session ID is required.");
        }
        layout = builder.layout;
        if ((outputMode = builder.outputMode) != OutputMode.COMPOSED && layout != null) {
            throw new IllegalStateException("Layout can only be applied to composed archives.");
        }
        name = builder.name;
        multiArchiveTag = builder.multiArchiveTag;
        hasAudio = builder.hasAudio;
        hasVideo = builder.hasVideo;
        resolution = builder.resolution;
        streamMode = builder.streamMode;
        if ((maxBitrate = builder.maxBitrate) != null && (maxBitrate < 100000 || maxBitrate > 6000000)) {
            throw new IllegalArgumentException("Maximum bitrate must be between 100000 and 6000000.");
        }
        if ((quantizationParameter = builder.quantizationParameter) != null) {
            if (maxBitrate != null) {
                throw new IllegalStateException("Quantization parameter is mutually exclusive with maxBitrate.");
            }
            if (quantizationParameter < 15 || quantizationParameter > 40) {
                throw new IllegalArgumentException("Quantization parameter must be between 15 and 40.");
            }
        }
    }

    /**
     * The duration of the recording, in seconds.
     *
     * @return The duration as an integer.
     */
    @JsonProperty("duration")
    public Integer getDurationSeconds() {
        return duration;
    }

    /**
     * The duration of the recording (precision in seconds).
     *
     * @return The duration.
     */
    @JsonIgnore
    public Duration getDuration() {
        return duration != null ? Duration.ofSeconds(duration) : null;
    }

    /**
     * The name of the archive.
     *
     * @return The archive name.
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * For archives with {@link ArchiveStatus#STOPPED} or {@link ArchiveStatus#FAILED}, this string
     * describes the reason the archive stopped (such as "maximum duration exceeded") or failed.
     *
     * @return The failure reason text, or {@code null} if not applicable.
     */
    @JsonProperty("reason")
    public String getReason() {
        return reason;
    }

    /**
     * The size of the MP4 file. For archives that have not been generated, this value is set to 0.
     *
     * @return The size as a long, or {@code null} if unset.
     */
    @JsonProperty("size")
    public Long getSize() {
        return size;
    }

    /**
     * The status of the archive, as defined by the {@link ArchiveStatus} enum.
     *
     * @return The status.
     */
    @JsonProperty("status")
    public ArchiveStatus getStatus() {
        return status;
    }

    /**
     * The download URL of the available MP4 file. This is only set for an archive with the status
     * set to {@link ArchiveStatus#AVAILABLE}; for other archives (including those with the status of
     * {@link ArchiveStatus#UPLOADED}) this method returns null. The download URL is obfuscated, and the file
     * is only available from the URL for 10 minutes. To generate a new URL, call the
     * {@link VideoClient#listArchives()} or {@link VideoClient#getArchive(String)} method.
     *
     * @return The download URL.
     */
    @JsonProperty("url")
    public URI getUrl() {
        return url;
    }

    /**
     * The output mode to be generated for this archive: {@code composed} or {@code individual}.
     *
     * @return The {@linkplain OutputMode}.
     */
    @JsonProperty("outputMode")
    public OutputMode getOutputMode() {
        return outputMode;
    }

    /**
     * Returns the multiArchiveTag if set for the Archive.
     *
     * @return The multiArchiveTag, or {@code null} if not applicable.
     */
    @JsonProperty("multiArchiveTag")
    public String getMultiArchiveTag() {
        return multiArchiveTag;
    }

    /**
     * Returns the quantization parameter if set for the Archive.
     *
     * @return The quantization parameter, or {@code null} if not applicable.
     * @since 8.20.0
     */
    @JsonProperty("quantizationParameter")
    public Integer getQuantizationParameter() {
        return quantizationParameter;
    }


    /**
     * Instantiates a Builder, used to construct this object.
     *
     * @param sessionId The ID of the Vonage Video session you are working with.
     *
     * @return A new {@linkplain Archive.Builder}.
     */
    public static Builder builder(String sessionId) {
        return new Builder(sessionId);
    }

    /**
     * Used to construct an Archive object.
     *
     * @see Archive
     */
    public static class Builder extends StreamComposition.Builder {
        private String name, multiArchiveTag;
        private OutputMode outputMode;
        private Integer quantizationParameter;

        Builder(String sessionId) {
            this.sessionId = sessionId;
        }

        /**
         * Sets a name for the archive.
         *
         * @param name The name of the archive. You can use this name to identify the archive. It is a property
         * of the Archive object, and it is a property of archive-related events in the SDK.
         *
         * @return This Builder with the name setting.
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the resolution of the archive.
         *
         * @param resolution The resolution of the archive, either "640x480" (SD, the default) or
         * "1280x720" (HD). This property only applies to composed archives. If you set this
         * and set the outputMode property to "individual", the call in the API method results in
         * an error.
         *
         * @return This Builder with the resolution setting.
         */
        public Builder resolution(Resolution resolution) {
            this.resolution = resolution;
            return this;
        }

        /**
         * Call this method to include an audio track ({@code true}, the default)
         * or not {@code false}).
         *
         * @param hasAudio Whether the archive will include an audio track.
         *
         * @return This Builder with the hasAudio setting.
         */
        public Builder hasAudio(boolean hasAudio) {
            this.hasAudio = hasAudio;
            return this;
        }

        /**
         * Call this method to include a video track ({@code true}, the default)
         * or not {@code false}).
         *
         * @param hasVideo Whether the archive will include a video track.
         *
         * @return This Builder with the hasVideo setting.
         */
        public Builder hasVideo(boolean hasVideo) {
            this.hasVideo = hasVideo;
            return this;
        }

        /**
         * Sets the output mode for this archive.
         *
         * @param outputMode Set to a value defined in the {@link OutputMode} enum.
         *
         * @return This Builder with the output mode setting.
         */
        public Builder outputMode(OutputMode outputMode) {
            this.outputMode = outputMode;
            return this;
        }

        /**
         * Sets the stream mode for this archive.
         * <p>
         * When streams are selected automatically ({@code StreamMode.AUTO}, the default), all
         * streams in the session can be included in the archive. When streams are selected manually
         * ({@code StreamMode.MANUAL}), you specify streams to be included based on calls
         * to the {@link VideoClient#addArchiveStream(String, String, Boolean, Boolean)} and
         * {@link VideoClient#removeArchiveStream(String, String)} methods. With
         * {@code StreamMode.MANUAL}, you can specify whether a stream's audio, video, or both
         * are included in the archive. In both automatic and manual modes, the archive composer
         * includes streams based on
         * <a href="https://tokbox.com/developer/guides/archive-broadcast-layout/#stream-prioritization-rules">stream
         * prioritization rules</a>.
         *
         * @param streamMode Set to a value defined in the {@link StreamMode} enum.
         *
         * @return This Builder with the stream mode setting.
         */
        public Builder streamMode(StreamMode streamMode) {
            this.streamMode = streamMode;
            return this;
        }

        /**
         * Sets the layout for a composed archive. If this option is specified,
         * {@linkplain Builder#outputMode(OutputMode)} must be {@linkplain OutputMode#COMPOSED}.
         *
         * @param layout The layout type to use.
         *
         * @return This Builder with the layout setting.
         */
        public Builder layout(StreamCompositionLayout layout) {
            this.layout = layout;
            return this;
        }

        /**
         * Set this to support recording multiple archives for the same session simultaneously.
         * Set this to a unique string for each simultaneous archive of an ongoing session. You must also set this
         * option when manually starting an archive that is automatically archived. If you do
         * not specify a unique multiArchiveTag, you can only record one archive at a time for a given session. See
         * <a href="https://tokbox.com/developer/guides/archiving/#simultaneous-archives">
         * Simultaneous Archives documentation</a>.
         *
         * @param multiArchiveTag A unique archive tag.
         *
         * @return This Builder with the MultiArchiveTag setting.
         */
        public Builder multiArchiveTag(String multiArchiveTag) {
            this.multiArchiveTag = multiArchiveTag;
            return this;
        }

        /**
         * (OPTIONAL) The maximum bitrate for the archive, in bits per second.
         * This must be between 100000 and 6000000.
         *
         * @param maxBitrate The maximum bitrate as an int.
         *
         * @return This builder.
         * @since 8.14.0
         */
        public Builder maxBitrate(int maxBitrate) {
            this.maxBitrate = maxBitrate;
            return this;
        }

        /**
         * (OPTIONAL) Quantization Parameter (QP) is an optional video encoding value allowed for composed archiving,
         * smaller values generate higher quality and larger archives, larger values generate lower quality and smaller
         * archives, QP uses variable bitrate (VBR). This must be between 15 and 40.
         *
         * @param quantizationParameter The quantization parameter as an integer (minimum 15, maximum 40).
         *
         * @return This builder.
         * @since 8.20.0
         */
        public Builder quantizationParameter(int quantizationParameter) {
            this.quantizationParameter = quantizationParameter;
            return this;
        }

        /**
         * Builds the {@linkplain Archive} object with this builder's settings.
         *
         * @return A new {@link Archive} instance.
         */
        public Archive build() {
            return new Archive(this);
        }
    }
}
