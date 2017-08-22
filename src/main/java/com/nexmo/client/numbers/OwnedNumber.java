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
package com.nexmo.client.numbers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OwnedNumber {
    private String country;
    private String msisdn;
    private String moHttpUrl;
    private String type;
    private String[] features;
    private String voiceCallbackType;
    private String voiceCallbackValue;

    public OwnedNumber() {
    }

    public OwnedNumber(
            @JsonProperty String country,
            @JsonProperty String msisdn,
            @JsonProperty String moHttpUrl,
            @JsonProperty String type,
            @JsonProperty String[] features,
            @JsonProperty String voiceCallbackType,
            @JsonProperty String voiceCallbackValue) {
        this.country = country;
        this.msisdn = msisdn;
        this.moHttpUrl = moHttpUrl;
        this.type = type;
        this.features = features;
        this.voiceCallbackType = voiceCallbackType;
        this.voiceCallbackValue = voiceCallbackValue;
    }

    public String getCountry() {
        return country;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public String getMoHttpUrl() {
        return moHttpUrl;
    }

    public String getType() {
        return type;
    }

    public String[] getFeatures() {
        return features;
    }

    public String getVoiceCallbackType() {
        return voiceCallbackType;
    }

    public String getVoiceCallbackValue() {
        return voiceCallbackValue;
    }
}
