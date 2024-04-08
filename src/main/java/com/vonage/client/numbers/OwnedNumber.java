/*
 *   Copyright 2024 Vonage
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
package com.vonage.client.numbers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.vonage.client.JsonableBaseObject;

public class OwnedNumber extends JsonableBaseObject {
    private String country, msisdn, moHttpUrl, type, voiceCallbackType, voiceCallbackValue;
    private String[] features;

    public String getCountry() {
        return country;
    }

    @Deprecated
    public void setCountry(String country) {
        this.country = country;
    }

    public String getMsisdn() {
        return msisdn;
    }

    @Deprecated
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getMoHttpUrl() {
        return moHttpUrl;
    }

    @Deprecated
    public void setMoHttpUrl(String moHttpUrl) {
        this.moHttpUrl = moHttpUrl;
    }

    public String getType() {
        return type;
    }

    @Deprecated
    public void setType(String type) {
        this.type = type;
    }

    public String[] getFeatures() {
        return features;
    }

    @Deprecated
    public void setFeatures(String[] features) {
        this.features = features;
    }

    public String getVoiceCallbackType() {
        return voiceCallbackType;
    }

    @Deprecated
    public void setVoiceCallbackType(String voiceCallbackType) {
        this.voiceCallbackType = voiceCallbackType;
    }

    public String getVoiceCallbackValue() {
        return voiceCallbackValue;
    }

    @Deprecated
    public void setVoiceCallbackValue(String voiceCallbackValue) {
        this.voiceCallbackValue = voiceCallbackValue;
    }
}
