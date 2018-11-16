/*
 * Copyright (c) 2011-2017 Nexmo Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nexmo.client.insight;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.NexmoUnexpectedException;

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
            throw new NexmoUnexpectedException("Failed to produce BasicInsightResponse from json.", jpe);
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
