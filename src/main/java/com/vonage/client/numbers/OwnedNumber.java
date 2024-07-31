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

import com.vonage.client.JsonableBaseObject;
import java.net.URI;

public class OwnedNumber extends JsonableBaseObject {
    private URI moHttpUrl;
    private Type type;
    private UpdateNumberRequest.CallbackType voiceCallbackType;
    private String country, msisdn, voiceCallbackValue;
    private Feature[] features;

    /**
     * Constructor, not for public use.
     *
     * @deprecated This will be made private in a future release.
     */
    @Deprecated
    public OwnedNumber() {
    }

    /**
     * Two character country code in ISO 3166-1 alpha-2 format.
     *
     * @return The number's country code.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Phone number in E.164 format.
     *
     * @return The MSISDN as a string.
     */
    public String getMsisdn() {
        return msisdn;
    }

    /**
     * URL of the webhook endpoint that handles inbound messages.
     *
     * @return The inbound message webhook URL as a string, or {@code null} if unspecified.
     */
    public String getMoHttpUrl() {
        return moHttpUrl != null ? moHttpUrl.toString() : null;
    }

    /**
     * Type of number as a string. In a future release, this will be an enum.
     *
     * @return The type of number as a string.
     */
    public String getType() {
        return type != null ? type.toString() : null;
    }

    /**
     * Capabilities of the number as an array of strings. In a future release, these will be enums.
     *
     * @return The number's capabilities as a string array.
     */
    public String[] getFeatures() {
        return Feature.getToString(features);
    }

    /**
     * Voice webhook type. In a future release, this will be an enum.
     *
     * @return The voice webhook callback type as a string, or {@code null} if unknown.
     */
    public String getVoiceCallbackType() {
        return voiceCallbackType != null ? voiceCallbackType.toString() : null;
    }

    /**
     * SIP URI, telephone number or Application ID.
     *
     * @return The voice webhook value as a string, or {@code null} if unknown.
     */
    public String getVoiceCallbackValue() {
        return voiceCallbackValue;
    }

    @Deprecated
    public void setCountry(String country) {
        this.country = country;
    }

    @Deprecated
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    @Deprecated
    public void setMoHttpUrl(String moHttpUrl) {
        if (moHttpUrl != null) {
            this.moHttpUrl = URI.create(moHttpUrl);
        }
    }

    @Deprecated
    public void setType(String type) {
        this.type = Type.fromString(type);
    }

    @Deprecated
    public void setFeatures(String[] features) {
        this.features = Feature.setFromString(features);
    }

    @Deprecated
    public void setVoiceCallbackType(String voiceCallbackType) {
        this.voiceCallbackType = UpdateNumberRequest.CallbackType.fromString(voiceCallbackType);
    }

    @Deprecated
    public void setVoiceCallbackValue(String voiceCallbackValue) {
        this.voiceCallbackValue = voiceCallbackValue;
    }
}
