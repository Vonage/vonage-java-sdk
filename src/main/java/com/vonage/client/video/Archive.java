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
    private Integer duration, partnerId;
    private Boolean hasVideo, hasAudio;
    private String id, name, url, reason, sessionId, password, multiArchiveTag;
    private ArchiveStatus status;
    private OutputMode outputMode;
    private StreamMode streamMode;
    private Resolution resolution;
    private List<ArchiveStream> streams;

    protected Archive() {
    }

    /**
     * The time at which the archive was created, in milliseconds since the Unix epoch.
     */
    public Long getCreatedAt() {
        return createdAt;
    }

    /**
     * The duration of the archive, in seconds.
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * The archive ID.
     */
    public String getId() {
        return id;
    }

    /**
     * The name of the archive.
     */
    public String getName() {
        return name;
    }

    /**
     * The resolution of the archive.
     */
    public Resolution getResolution() {
        return resolution;
    }

    /**
     * The OpenTok API key associated with the archive.
     */
    public Integer getPartnerId() {
        return partnerId;
    }

    /**
     * For archives with the status Status.STOPPED or Status.FAILED, this string describes the
     * reason the archive stopped (such as "maximum duration exceeded") or failed.
     */
    public String getReason() {
        return reason;
    }

    /**
     * The session ID of the OpenTok session associated with this archive.
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * The size of the MP4 file. For archives that have not been generated, this value is set to 0.
     */
    public Long getSize() {
        return size;
    }

    /**
     * The status of the archive, as defined by the {@link ArchiveStatus} enum.
     */
    public ArchiveStatus getStatus() {
        return status;
    }

    /**
     * The download URL of the available MP4 file. This is only set for an archive with the status
     * set to Status.AVAILABLE; for other archives, (including archives with the status of
     * Status.UPLOADED) this method returns null. The download URL is obfuscated, and the file
     * is only available from the URL for 10 minutes. To generate a new URL, call the
     * {@link VideoClient#listArchives()} or {@link VideoClient#getArchive(String)}
     * method.
     */
    public String getUrl() {
        return url;
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
     * The output mode to be generated for this archive: <code>composed</code> or <code>individual</code>.
     */
    public OutputMode getOutputMode() {
        return outputMode;
    }

    /**
     * The stream mode to used for selecting streams to be included in this archive:
     * <code>StreamMode.AUTO</code> or <code>StreamMode.MANUAL</code>.
     */
    public StreamMode getStreamMode() {
        return streamMode;
    }
    
    /**
     * The encrypted password if an archive storage was configured to use an encryption key.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the multiArchiveTag if set for the Archive.
     */
    public String getMultiArchiveTag() {
        return multiArchiveTag;
    }

    /**
     * Returns the streams associated with this archive.
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
