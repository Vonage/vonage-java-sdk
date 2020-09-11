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
package com.vonage.client.sms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;

import java.io.IOException;
import java.text.SimpleDateFormat;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchRejectedMessagesResponse {
    private int count;
    private RejectedMessage[] items;

    public int getCount() {
        return count;
    }

    public RejectedMessage[] getItems() {
        return items;
    }

    public static SearchRejectedMessagesResponse fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            return mapper.readValue(json, SearchRejectedMessagesResponse.class);
        } catch (IOException jpe) {
            throw new VonageUnexpectedException("Failed to produce SearchRejectedMessagesResponse from json.", jpe);
        }
    }
}
