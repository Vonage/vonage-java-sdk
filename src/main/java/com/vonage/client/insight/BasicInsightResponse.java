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

/**
 * Response object constructed from the JSON payload returned for Basic number insight requests.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BasicInsightResponse {
    private InsightStatus status;
    private String statusMessage;
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

    /**
     * @return The status code of the message, as an enum.
     */
    @JsonProperty("status")
    public InsightStatus getStatus() {
        return status;
    }

    /**
     * @return The status description of your request.
     */
    @JsonProperty("status_message")
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * @return The unique identifier for your request.
     * This is an alphanumeric string of up to 40 characters.
     */
    @JsonProperty("request_id")
    public String getRequestId() {
        return requestId;
    }

    /**
     * @return The number in your request in international format.
     */
    @JsonProperty("international_format_number")
    public String getInternationalFormatNumber() {
        return internationalFormatNumber;
    }

    /**
     * @return The number in your request in the format used by the country the number belongs to.
     */
    @JsonProperty("national_format_number")
    public String getNationalFormatNumber() {
        return nationalFormatNumber;
    }

    /**
     * @return Two character country code for number. This is in ISO 3166-1 alpha-2 format.
     */
    @JsonProperty("country_code")
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * @return Three character country code for number. This is in ISO 3166-1 alpha-3 format.
     */
    @JsonProperty("country_code_iso3")
    public String getCountryCodeIso3() {
        return countryCodeIso3;
    }

    /**
     * @return The full name of the country that number is registered in.
     */
    @JsonProperty("country_name")
    public String getCountryName() {
        return countryName;
    }

    /**
     * @return The numeric prefix for the country that number is registered in.
     */
    @JsonProperty("country_prefix")
    public String getCountryPrefix() {
        return countryPrefix;
    }
}
