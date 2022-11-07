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

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import java.io.IOException;
import java.util.List;

/**
* Represents an archive of a video session.
*/
@JsonIgnoreProperties(ignoreUnknown = true)
public class Archive {
    private Long createdAt, size;
    private Integer duration;
    private Boolean hasVideo, hasAudio;
    private String id, name, url, reason, sessionId, multiArchiveTag;
    private ArchiveStatus status;
    private OutputMode outputMode;
    private StreamMode streamMode;
    private Resolution resolution;
    private List<ArchiveStream> streams;

    protected Archive() {
    }

    /**
     * The time at which the archive was created, in milliseconds since the Unix epoch.
     *
     * @return The created time, as a long.
     */
    public Long getCreatedAt() {
        return createdAt;
    }

    /**
     * The duration of the archive, in seconds.
     *
     * @return The duration.
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * ID of the archive.
     *
     * @return The archive ID.
     */
    public String getId() {
        return id;
    }

    /**
     * The name of the archive.
     *
     * @return The archive name.
     */
    public String getName() {
        return name;
    }

    /**
     * The resolution of the archive as an enum.
     *
     * @return The resolution.
     */
    public Resolution getResolution() {
        return resolution;
    }

    /**
     * For archives with {@link ArchiveStatus#STOPPED} or {@link ArchiveStatus#FAILED}, this string
     * describes the reason the archive stopped (such as "maximum duration exceeded") or failed.
     *
     * @return The reason text.
     */
    public String getReason() {
        return reason;
    }

    /**
     * The ID of the video session associated with this archive.
     *
     * @return The session ID.
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * The size of the MP4 file. For archives that have not been generated, this value is set to 0.
     *
     * @return The size, as a long.
     */
    public Long getSize() {
        return size;
    }

    /**
     * The status of the archive, as defined by the {@link ArchiveStatus} enum.
     *
     * @return The status.
     */
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
     * @return The URL.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Whether the archive has a video track ({@code true}) or not ({@code false}).
     *
     * @return Whether the archive has video.
     */
    @JsonProperty("hasVideo")
    public Boolean hasVideo() {
        return hasVideo;
    }

    /**
     * Whether the archive has an audio track ({@code true}) or not ({@code false}).
     *
     * @return Whether the archive has sound.
     */
    @JsonProperty("hasAudio")
    public Boolean hasAudio() {
        return hasAudio;
    }

    /**
     * The output mode to be generated for this archive: {@code composed} or {@code individual}.
     *
     * @return The {@linkplain OutputMode}.
     */
    public OutputMode getOutputMode() {
        return outputMode;
    }

    /**
     * The stream mode to used for selecting streams to be included in this archive:
     * {@link StreamMode#AUTO} or {@link StreamMode#MANUAL}.
     *
     * @return The {@linkplain StreamMode}.
     */
    public StreamMode getStreamMode() {
        return streamMode;
    }

    /**
     * Returns the multiArchiveTag if set for the Archive.
     *
     * @return The multiArchiveTag, or {@code null} if not applicable.
     */
    public String getMultiArchiveTag() {
        return multiArchiveTag;
    }

    /**
     * Returns the streams associated with this archive.
     *
     * @return The list of ArchiveStream details.
     */
    public List<ArchiveStream> getStreams() {
        return streams;
    }

    /**
     * Creates an instance of this class from a JSON payload.
     *
     * @param json The JSON string to parse.
     * @return An instance of this class with the fields populated, if present.
     */
    public static Archive fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, Archive.class);
        }
        catch (IOException ex) {
            throw new VonageUnexpectedException("Failed to produce Archive from json.", ex);
        }
    }

    @Override
    public String toString() {
        try {
            ObjectMapper om = new ObjectMapper();
            om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            return om.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
