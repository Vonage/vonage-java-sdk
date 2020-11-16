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
package com.vonage.client.numbers;

import org.apache.http.client.methods.RequestBuilder;

public class UpdateNumberRequest {
    private final String country;
    private final String msisdn;
    private String moHttpUrl;
    private String moSmppSysType;
    private CallbackType voiceCallbackType;
    private String voiceCallbackValue;
    private String voiceStatusCallback;
    private String messagesCallbackValue;

    public UpdateNumberRequest(String msisdn, String country) {
        this.country = country;
        this.msisdn = msisdn;
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

    public void setMoHttpUrl(String moHttpUrl) {
        this.moHttpUrl = moHttpUrl;
    }

    public String getMoSmppSysType() {
        return moSmppSysType;
    }

    public void setMoSmppSysType(String moSmppSysType) {
        this.moSmppSysType = moSmppSysType;
    }

    public CallbackType getVoiceCallbackType() {
        return voiceCallbackType;
    }

    public void setVoiceCallbackType(CallbackType voiceCallbackType) {
        this.voiceCallbackType = voiceCallbackType;
    }

    public String getVoiceCallbackValue() {
        return voiceCallbackValue;
    }

    public void setVoiceCallbackValue(String voiceCallbackValue) {
        this.voiceCallbackValue = voiceCallbackValue;
    }

    public String getVoiceStatusCallback() {
        return voiceStatusCallback;
    }

    public void setVoiceStatusCallback(String voiceStatusCallback) {
        this.voiceStatusCallback = voiceStatusCallback;
    }

    public String getMessagesCallbackValue() {
        return messagesCallbackValue;
    }

    public void setMessagesCallbackValue(String messagesCallbackValue) {
        this.messagesCallbackValue = messagesCallbackValue;
    }

    public void addParams(RequestBuilder request) {
        request.addParameter("country", country).addParameter("msisdn", msisdn);
        if (moHttpUrl != null) {
            request.addParameter("moHttpUrl", moHttpUrl);
        }
        if (moSmppSysType != null) {
            request.addParameter("moSmppSysType", moSmppSysType);
        }
        if (voiceCallbackType != null) {
            request.addParameter("voiceCallbackType", voiceCallbackType.paramValue());
        }
        if (voiceCallbackValue != null) {
            request.addParameter("voiceCallbackValue", voiceCallbackValue);
        }
        if (voiceStatusCallback != null) {
            request.addParameter("voiceStatusCallback", voiceStatusCallback);
        }
        if (messagesCallbackValue != null) {
            request.addParameter("messagesCallbackValue", messagesCallbackValue);
            request.addParameter("messagesCallbackType", CallbackType.APP.paramValue());
        }
    }

    public enum CallbackType {
        SIP, TEL, VXML, APP;

        public String paramValue() {
            return name().toLowerCase();
        }
    }
}
