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
package com.vonage.client.verify;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.VonageUnexpectedException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchVerifyResponse {
    private VerifyStatus status;
    private List<VerifyDetails> verificationRequests;
    private String errorText;

    @JsonCreator
    SearchVerifyResponse() {
        status = VerifyStatus.OK;
    }

    SearchVerifyResponse(List<VerifyDetails> verificationRequests) {
        status = VerifyStatus.OK;
        this.verificationRequests = verificationRequests;
    }

    SearchVerifyResponse(VerifyStatus status, String errorText) {
        this.status = status;
        this.errorText = errorText;
    }

    public VerifyStatus getStatus() {
        return this.status;
    }

    @JsonProperty("verification_requests")
    public List<VerifyDetails> getVerificationRequests() {
        return verificationRequests;
    }

    @JsonProperty("error_text")
    public String getErrorText() {
        return errorText;
    }

    public static SearchVerifyResponse fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            SimpleModule module = new SimpleModule();
            module.addDeserializer(SearchVerifyResponse.class, new SearchVerifyResponseDeserializer());
            mapper.registerModule(module);

            return mapper.readValue(json, SearchVerifyResponse.class);
        } catch (JsonMappingException jme) {
            throw new VonageResponseParseException("Failed to produce SearchVerifyResponse from json.", jme);
        } catch (IOException jpe) {
            throw new VonageUnexpectedException("Failed to produce SearchVerifyResponse from json.", jpe);
        }
    }
}
