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
package com.vonage.client.insight;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BasicInsightResponse {
    private InsightStatus status;
    private String statusMessage;
    private String errorText;

    private String requestId;
    private String internationalFormatNumber;
    private String nationalFormatNumber;
    private String countryCode;
    private String countryCodeIso3;
    private String countryName;
    private String countryPrefix;

    public static BasicInsightResponse fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, BasicInsightResponse.class);
        } catch (IOException jpe) {
            throw new VonageUnexpectedException("Failed to produce BasicInsightResponse from json.", jpe);
        }
    }

    public InsightStatus getStatus() {
        return status;
    }

    @JsonProperty("status_message")
    public String getStatusMessage() {
        return statusMessage;
    }

    @JsonProperty("error_text")
    public String getErrorText() {
        return errorText;
    }

    @JsonProperty("request_id")
    public String getRequestId() {
        return requestId;
    }

    @JsonProperty("international_format_number")
    public String getInternationalFormatNumber() {
        return internationalFormatNumber;
    }

    @JsonProperty("national_format_number")
    public String getNationalFormatNumber() {
        return nationalFormatNumber;
    }

    @JsonProperty("country_code")
    public String getCountryCode() {
        return countryCode;
    }

    @JsonProperty("country_code_iso3")
    public String getCountryCodeIso3() {
        return countryCodeIso3;
    }

    @JsonProperty("country_name")
    public String getCountryName() {
        return countryName;
    }

    @JsonProperty("country_prefix")
    public String getCountryPrefix() {
        return countryPrefix;
    }
}
