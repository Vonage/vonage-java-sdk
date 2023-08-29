/*
 *   Copyright 2023 Vonage
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

import java.util.Map;

public class UpdateNumberRequest extends BaseNumberRequest {
    private CallbackType voiceCallbackType;
    private String moHttpUrl, moSmppSysType, voiceCallbackValue, voiceStatusCallback,  messagesCallbackValue;

    public UpdateNumberRequest(String msisdn, String country) {
        super(country, msisdn);
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

    @Override
    public Map<String, String> makeParams() {
        Map<String, String> params = super.makeParams();
        if (moHttpUrl != null) {
            params.put("moHttpUrl", moHttpUrl);
        }
        if (moSmppSysType != null) {
            params.put("moSmppSysType", moSmppSysType);
        }
        if (voiceCallbackType != null) {
            params.put("voiceCallbackType", voiceCallbackType.paramValue());
        }
        if (voiceCallbackValue != null) {
            params.put("voiceCallbackValue", voiceCallbackValue);
        }
        if (voiceStatusCallback != null) {
            params.put("voiceStatusCallback", voiceStatusCallback);
        }
        if (messagesCallbackValue != null) {
            params.put("messagesCallbackValue", messagesCallbackValue);
            params.put("messagesCallbackType", CallbackType.APP.paramValue());
        }
        return params;
    }

    public enum CallbackType {
        SIP, TEL, VXML, APP;

        public String paramValue() {
            return name().toLowerCase();
        }
    }
}
