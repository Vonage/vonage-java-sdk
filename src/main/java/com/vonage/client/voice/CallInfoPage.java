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
package com.vonage.client.voice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.util.ArrayIterator;
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;
import java.util.Iterator;

/**
 * Response from {@link VoiceClient#listCalls(CallsFilter)}.
 * This will be refactored to be based on {@link com.vonage.client.common.HalPageResponse} in the next major release.
 */
public class CallInfoPage extends JsonableBaseObject implements Iterable<CallInfo> {
    private int count, pageSize, recordIndex;
    private PageLinks links;
    private EmbeddedCalls embedded;

    @Deprecated
    public CallInfoPage() {}

    /**
     * Total number of results.
     *
     * @return The total count as an integer.
     */
    @JsonProperty("count")
    public int getCount() {
        return count;
    }

    /**
     * Number of results per page.
     *
     * @return The page size as an integer.
     */
    @JsonProperty("page_size")
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Index of the first record in the result set.
     *
     * @return The record index as an integer.
     */
    @JsonProperty("record_index")
    public int getRecordIndex() {
        return recordIndex;
    }

    /**
     * Links to the first, last, next, and previous pages.
     *
     * @return The {@code _links} section of the response.
     */
    @JsonProperty("_links")
    public PageLinks getLinks() {
        return links;
    }

    /**
     * Main response body containing call information objects.
     *
     * @return The {@code _embedded} section of the response.
     */
    @JsonProperty("_embedded")
    public EmbeddedCalls getEmbedded() {
        return embedded;
    }
    
    @Override
    public Iterator<CallInfo> iterator() {
        return new ArrayIterator<>(embedded.getCallInfos());
    }

    /**
     * Creates an instance of this class from a JSON payload.
     *
     * @param json The JSON string to parse.
     *
     * @return An instance of this class with the fields populated, if present.
     *
     * @deprecated This will be removed in a future release.
     */
    @Deprecated
    public static CallInfoPage fromJson(String json) {
        return Jsonable.fromJson(json);
    }
}