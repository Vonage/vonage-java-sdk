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

import org.apache.http.client.methods.RequestBuilder;

public class UpdateNumberRequest {
    private final String country;
    private final String msisdn;
    private String moHttpUrl;
    private String moSmppSysType;
    private CallbackType voiceCallbackType;
    private String voiceCallbackValue;
    private String voiceStatusCallback;

    public UpdateNumberRequest(String country, String msisdn) {
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

    public void addParams(RequestBuilder request) {
        request.addParameter("country", this.country).addParameter("msisdn", msisdn);
        if (this.moHttpUrl != null) {
            request.addParameter("moHttpUrl", moHttpUrl);
        }
        if (this.moSmppSysType != null) {
            request.addParameter("moSmppSysType", moSmppSysType);
        }
        if (this.voiceCallbackType != null) {
            request.addParameter("voiceCallbackType", voiceCallbackType.paramValue());
        }
        if (this.voiceCallbackValue != null) {
            request.addParameter("voiceCallbackValue", voiceCallbackValue);
        }
        if (this.voiceStatusCallback != null) {
            request.addParameter("voiceStatusCallback", voiceStatusCallback);
        }

    }

    public enum CallbackType {
        SIP, TEL, VXML, APP;

        public String paramValue() {
            return this.name().toString();
        }
    }
}
