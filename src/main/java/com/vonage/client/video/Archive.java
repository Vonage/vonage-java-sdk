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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import java.io.IOException;

/**
* Represents an archive of a video session.
*/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Archive extends StreamComposition {
    private Long size;
    private Integer duration;
    private String name, url, reason, multiArchiveTag;
    private ArchiveStatus status;
    private OutputMode outputMode;

    protected Archive() {
    }

    /**
     * The duration of the recording, in seconds.
     *
     * @return The duration as an integer.
     */
    public Integer getDuration() {
        return duration;
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
     * For archives with {@link ArchiveStatus#STOPPED} or {@link ArchiveStatus#FAILED}, this string
     * describes the reason the archive stopped (such as "maximum duration exceeded") or failed.
     *
     * @return The reason text.
     */
    public String getReason() {
        return reason;
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
     * The output mode to be generated for this archive: {@code composed} or {@code individual}.
     *
     * @return The {@linkplain OutputMode}.
     */
    public OutputMode getOutputMode() {
        return outputMode;
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
}
