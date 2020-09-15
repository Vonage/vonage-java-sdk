/*
 *   Copyright 2020 Vonage
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ArrayIterator;
import com.vonage.client.VonageUnexpectedException;

import java.io.IOException;
import java.util.Iterator;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CallInfoPage implements Iterable<CallInfo> {
    private int count;
    private int pageSize;
    private int recordIndex;

    private PageLinks links;
    private EmbeddedCalls embedded;

    public int getCount() {
        return count;
    }

    @JsonProperty("page_size")
    public int getPageSize() {
        return pageSize;
    }

    @JsonProperty("record_index")
    public int getRecordIndex() {
        return recordIndex;
    }

    @JsonProperty("_links")
    public PageLinks getLinks() {
        return links;
    }

    @JsonProperty("_embedded")
    public EmbeddedCalls getEmbedded() {
        return embedded;
    }

    @Override
    public Iterator<CallInfo> iterator() {
        return new ArrayIterator<>(embedded.getCallInfos());
    }

    public static CallInfoPage fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper.readValue(json, CallInfoPage.class);
        } catch (IOException jpe) {
            throw new VonageUnexpectedException("Failed to produce json from Call object.", jpe);
        }
    }
}