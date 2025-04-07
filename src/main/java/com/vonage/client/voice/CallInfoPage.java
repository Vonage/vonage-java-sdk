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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.common.HalPageResponse;
import java.util.Iterator;
import java.util.List;

/**
 * Response from {@link VoiceClient#listCalls(CallsFilter)}.
 * This will be refactored to be based on {@link com.vonage.client.common.HalPageResponse} in the next major release.
 */
public class CallInfoPage extends HalPageResponse implements Iterable<CallInfo> {
    private Integer count, recordIndex;
    @JsonProperty("_embedded") private Embedded embedded;

    static class Embedded extends JsonableBaseObject {
        @JsonProperty("calls") private List<CallInfo> callInfos;
    }

    /**
     * Constructor used reflectively by Jackson for instantiation.
     */
    CallInfoPage() {}

    /**
     * Total number of results.
     *
     * @return The total count as an integer.
     */
    @JsonProperty("count")
    public Integer getCount() {
        return count;
    }

    /**
     * Index of the first record in the result set.
     *
     * @return The record index as an integer.
     */
    @JsonProperty("record_index")
    public Integer getRecordIndex() {
        return recordIndex;
    }

    /**
     * Gets the call details.
     *
     * @return The list of CallInfos from the embedded object.
     *
     * @since 9.0.0
     */
    @JsonIgnore
    public List<CallInfo> getCallInfos() {
        return embedded.callInfos;
    }
    
    @Override
    public Iterator<CallInfo> iterator() {
        return getCallInfos().iterator();
    }
}