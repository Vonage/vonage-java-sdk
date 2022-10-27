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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;

/**
 * Defines the properties of a new archive. Use this class to override default values.
 *
 * @see VideoClient#createArchive
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CreateArchiveRequest {
    private final String sessionId, name, multiArchiveTag;
    private final Boolean hasAudio, hasVideo;
    private final Resolution resolution;
    private final OutputMode outputMode;
    private final StreamMode streamMode;
    private final ArchiveLayout layout;

    private CreateArchiveRequest(Builder builder) {
        if ((sessionId = builder.sessionId) == null || sessionId.isEmpty()) {
            throw new IllegalArgumentException("Session ID is required");
        }
        name = builder.name;
        multiArchiveTag = builder.multiArchiveTag;
        hasAudio = builder.hasAudio;
        hasVideo = builder.hasVideo;
        resolution = builder.resolution;
        outputMode = builder.outputMode;
        streamMode = builder.streamMode;
        layout = builder.layout;
    }

    /**
     * Returns the ID of the Vonage Video session this archive will be associated with.
     */
    public String getSessionId() {
       return sessionId;
    }

    /**
     * Returns the name of the archive, which you can use to identify the archive.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the resolution of the archive.
     */
    public Resolution getResolution() {
        return resolution;
    }

    /**
     * Returns the multiArchiveTag, if present.
     */
    public String getMultiArchiveTag() {
        return multiArchiveTag;
    }

    /**
     * Whether the archive has a video track (<code>true</code>) or not (<code>false</code>).
     */
    @JsonProperty("hasVideo")
    public Boolean hasVideo() {
        return hasVideo;
    }

    /**
     * Whether the archive has an audio track (<code>true</code>) or not (<code>false</code>).
     */
    @JsonProperty("hasAudio")
    public Boolean hasAudio() {
        return hasAudio;
    }

    /**
     * The output mode of the archive.
     */
    public OutputMode getOutputMode() {
        return outputMode;
    }

    /**
     * The stream mode of the archive.
     */
    public StreamMode getStreamMode() {
        return streamMode;
    }

    /**
     * Returns the custom layout of the archive (composed archives only).
     */
    public ArchiveLayout getLayout() {
        return layout;
    }

    /**
     * Generates a JSON payload from this request.
     *
     * @return JSON representation of this CreateArchiveRequest object.
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
     * @param sessionId The ID of the Vonage Video session you are working with.
     *
     * @return A new {@linkplain CreateArchiveRequest.Builder}.
     */
    public static Builder builder(String sessionId) {
        return new Builder(sessionId);
    }

    /**
     * Used to create an ArchiveProperties object.
     *
     * @see CreateArchiveRequest
     */
    public static class Builder {
        private final String sessionId;
        private String name, multiArchiveTag;
        private Boolean hasAudio, hasVideo;
        private Resolution resolution;
        private OutputMode outputMode;
        private StreamMode streamMode;
        private ArchiveLayout layout;

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
         * Call this method to include an audio track (<code>true</code>, the default)
         * or not <code>false</code>).
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
         * Call this method to include an video track (<code>true</code>, the default)
         * or not <code>false</code>).
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
         * When streams are selected automatically (<code>StreamMode.AUTO</code>, the default), all
         * streams in the session can be included in the archive. When streams are selected manually
         * (<code>StreamMode.MANUAL</code>), you specify streams to be included based on calls
         * to the {@link VideoClient#addArchiveStream(String, String, Boolean, Boolean)} and
         * {@link VideoClient#removeArchiveStream(String, String)} methods. With
         * <code>StreamMode.MANUAL</code>, you can specify whether a stream's audio, video, or both
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
         * Sets the layout for a composed archive.
         *
         * @param layout An object of type {@link ArchiveLayout} .
         *
         * @return This Builder with the layout setting.
         */
        public Builder layout(ArchiveLayout layout) {
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
         * Builds the ArchiveProperties object.
         *
         * @return The ArchiveProperties object.
         */
        public CreateArchiveRequest build() {
            return new CreateArchiveRequest(this);
        }
    }
}
