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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.net.URI;
import java.util.Objects;

/**
 * Represents an RTMP stream in a video session.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Rtmp extends JsonableBaseObject {
    private String id, streamName;
    private URI serverUrl;
    private RtmpStatus status;

    protected Rtmp() {
    }

    protected Rtmp(Builder builder) {
        if ((id = builder.id) != null && id.trim().isEmpty()) {
            throw new IllegalArgumentException("RTMP ID cannot be blank.");
        }
        if ((streamName = builder.streamName) == null || streamName.trim().isEmpty()) {
            throw new IllegalArgumentException("RTMP stream name cannot be blank.");
        }
        serverUrl = URI.create(Objects.requireNonNull(builder.serverUrl, "RTMP server URL is required."));
    }

    /**
     * @return A unique identifier for the stream.
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * @return The stream name, such as the YouTube Live stream name or the Facebook stream key.
     */
    @JsonProperty("streamName")
    public String getStreamName() {
        return streamName;
    }

    /**
     * @return The RTMP server URL.
     */
    @JsonProperty("serverUrl")
    public URI getServerUrl() {
        return serverUrl;
    }

    /**
     * The status of the RTMP stream. Poll frequently to check status updates.
     *
     * @return The status as an enum.
     */
    @JsonProperty("status")
    public RtmpStatus getStatus() {
        return status;
    }

    /**
     * Entrypoint for creating an instance of this class.
     *
     * @return A new Builder instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id, streamName, serverUrl;

        Builder() {}

        /**
         * (OPTIONAL)
         * Sets a unique ID for the stream.
         *
         * @param id The unique identifier for this RTMP stream as a string.
         *
         * @return This builder.
         */
        public Builder id(String id) {
            this.id = id;
            return this;
        }

        /**
         * (REQUIRED)
         * Sets the stream name, such as the YouTube Live stream name or the Facebook stream key.
         *
         * @param streamName The name or key for this RTMP stream.
         *
         * @return This builder.
         */
        public Builder streamName(String streamName) {
            this.streamName = streamName;
            return this;
        }

        /**
         * (REQUIRED)
         * The RTMP server URL.
         *
         * @param serverUrl The server URL as a string.
         *
         * @return This builder.
         */
        public Builder serverUrl(String serverUrl) {
            this.serverUrl = serverUrl;
            return this;
        }

        /**
         * Constructs a Rtmp instance with this builder's properties.
         *
         * @return A new Rtmp object.
         */
        public Rtmp build() {
            return new Rtmp(this);
        }
    }
}

