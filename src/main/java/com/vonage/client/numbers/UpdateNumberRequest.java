/*
 *   Copyright 2025 Vonage
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

import com.fasterxml.jackson.annotation.JsonCreator;
import java.net.URI;
import java.util.*;

/**
 * Defines properties for updating an existing owned number.
 */
public class UpdateNumberRequest extends BaseNumberRequest {
    private final UUID applicationId;
    private CallbackType voiceCallbackType;
    private URI moHttpUrl, voiceStatusCallback;
    private String moSmppSysType, voiceCallbackValue,  messagesCallbackValue;

    private UpdateNumberRequest(Builder builder) {
        super(builder.country, builder.msisdn);
        applicationId = builder.appId;
        moHttpUrl = builder.moHttpUrl;
        voiceStatusCallback = builder.voiceStatusCallback;
        moSmppSysType = builder.moSmppSysType;
        voiceCallbackType = builder.voiceCallbackType;
        voiceCallbackValue = builder.voiceCallbackValue;
    }

    /**
     * Deprecated constructor.
     *
     * @param msisdn The inbound virtual number to update.
     * @param country The two character country code in ISO 3166-1 alpha-2 format.
     * @deprecated Use {@link #builder(String, String)}. This will be removed in the next major release.
     */
    @Deprecated
    public UpdateNumberRequest(String msisdn, String country) {
        this(builder(msisdn, country));
    }

    /**
     * ID of the application that will handle inbound traffic to this number.
     *
     * @return The application ID, or {@code null} if unspecified.
     */
    public UUID getApplicationId() {
        return applicationId;
    }

    /**
     * A URL-encoded URI to the webhook endpoint that handles inbound messages. Your webhook endpoint must
     * be active before you make this request. Vonage makes a GET request to the endpoint and checks that
     * it returns a 200 OK response. Set this parameter's value to an empty string to remove the webhook.
     *
     * @return The inbound message webhook URL as a string, or {@code null} if unspecified.
     */
    public String getMoHttpUrl() {
        return moHttpUrl != null ? moHttpUrl.toString() : null;
    }

    /**
     * The associated system type for your SMPP client.
     *
     * @return The SMPP system type as a string, or {@code null} if unspecified.
     */
    public String getMoSmppSysType() {
        return moSmppSysType;
    }

    /**
     * Specifies whether inbound voice calls on your number are forwarded to a SIP or a telephone number.
     * If set, sip or tel are prioritized over the Voice capability in your Application.
     *
     * @return The voice callback type as an enum, or {@code null} if unspecified.
     */
    public CallbackType getVoiceCallbackType() {
        return voiceCallbackType;
    }

    /**
     * The SIP URI or telephone number.
     *
     * @return The voice callback value as a string, or {@code null} if unspecified.
     */
    public String getVoiceCallbackValue() {
        return voiceCallbackValue;
    }

    /**
     * The webhook URI for Vonage to send a request to when a call ends.
     *
     * @return The voice status callback URL as a string, or {@code null} if unspecified.
     */
    public String getVoiceStatusCallback() {
        return voiceStatusCallback != null ? voiceStatusCallback.toString() : null;
    }

    @Deprecated
    public void setMoHttpUrl(String moHttpUrl) {
        this.moHttpUrl = URI.create(moHttpUrl);
    }

    @Deprecated
    public void setMoSmppSysType(String moSmppSysType) {
        this.moSmppSysType = moSmppSysType;
    }

    @Deprecated
    public void setVoiceCallbackType(CallbackType voiceCallbackType) {
        this.voiceCallbackType = voiceCallbackType;
    }

    @Deprecated
    public void setVoiceCallbackValue(String voiceCallbackValue) {
        this.voiceCallbackValue = voiceCallbackValue;
    }

    @Deprecated
    public void setVoiceStatusCallback(String voiceStatusCallback) {
        this.voiceStatusCallback = URI.create(voiceStatusCallback);
    }

    @Deprecated
    public String getMessagesCallbackValue() {
        return messagesCallbackValue;
    }

    @Deprecated
    public void setMessagesCallbackValue(String messagesCallbackValue) {
        this.messagesCallbackValue = messagesCallbackValue;
    }

    @Override
    public Map<String, String> makeParams() {
        Map<String, String> params = super.makeParams();
        if (applicationId != null) {
            params.put("app_id", applicationId.toString());
        }
        if (moHttpUrl != null) {
            params.put("moHttpUrl", moHttpUrl.toString());
        }
        if (moSmppSysType != null) {
            params.put("moSmppSysType", moSmppSysType);
        }
        if (voiceCallbackType != null) {
            params.put("voiceCallbackType", voiceCallbackType.toString());
        }
        if (voiceCallbackValue != null) {
            params.put("voiceCallbackValue", voiceCallbackValue);
        }
        if (voiceStatusCallback != null) {
            params.put("voiceStatusCallback", voiceStatusCallback.toString());
        }
        if (messagesCallbackValue != null) {
            params.put("messagesCallbackValue", messagesCallbackValue);
            params.put("messagesCallbackType", CallbackType.APP.paramValue());
        }
        return params;
    }

    /**
     * Represents the callback type for voice.
     */
    public enum CallbackType {
        SIP,

        TEL,

        @Deprecated
        VXML,

        APP;


        /**
         * Serialized enum.
         *
         * @return The string value.
         * @deprecated Use {@link #toString()}.
         */
        @Deprecated
        public String paramValue() {
            return toString();
        }

        @Override
        public String toString() {
            return name().toLowerCase();
        }

        /**
         * Creates the enum from its string representation.
         *
         * @param type The serialized callback type as a string.
         *
         * @return Enum representation of the callback type, or {@code null} if {@code type} is null.
         * @since 8.10.0
         */
        @JsonCreator
        public static CallbackType fromString(String type) {
            if (type == null) return null;
            return valueOf(type.toUpperCase());
        }
    }

    /**
     * Entrypoint for constructing an instance of this class.
     *
     * @param country The two character country code in ISO 3166-1 alpha-2 format.
     * @param msisdn The inbound virtual number to update.
     *
     * @return A new Builder.
     *
     * @since 8.10.0
     */
    public static Builder builder(String msisdn, String country) {
        return new Builder(msisdn, country);
    }

    /**
     * Builder for specifying the properties of the number to update.
     *
     * @since 8.10.0
     */
    public static class Builder {
        private final String country, msisdn;
        private String moSmppSysType, voiceCallbackValue;
        private CallbackType voiceCallbackType;
        private UUID appId;
        private URI moHttpUrl, voiceStatusCallback;

        private Builder(String msisdn, String country) {
            this.country = country;
            this.msisdn = msisdn;
        }

        /**
         * Sets the associated system type for your SMPP client.
         *
         * @param moSmppSysType The system type as a string.
         * @return This builder.
         */
        public Builder moSmppSysType(String moSmppSysType) {
            this.moSmppSysType = moSmppSysType;
            return this;
        }

        /**
         * Sets the voice callback type and value (either SIP or PSTN).
         *
         * @param type Specify whether inbound voice calls on your number are forwarded to a SIP or
         *             a telephone number. If set, {@code sip} or {@code tel} are prioritized over
         *             the Voice capability in your Application.
         *
         * @param voiceCallbackValue The SIP URI or telephone number.
         *
         * @return This builder.
         */
        public Builder voiceCallback(CallbackType type, String voiceCallbackValue) {
            this.voiceCallbackType = Objects.requireNonNull(type, "Voice callback type is required.)");
            this.voiceCallbackValue = Objects.requireNonNull(voiceCallbackValue, "Voice callback value is required.");
            return this;
        }

        /**
         * Sets the application that will handle inbound traffic to this number.
         *
         * @param appId The application ID as a string.
         * @return This builder.
         */
        public Builder applicationId(String appId) {
            return applicationId(UUID.fromString(appId));
        }

        /**
         * Sets the application that will handle inbound traffic to this number.
         *
         * @param appId The application ID.
         * @return This builder.
         */
        public Builder applicationId(UUID appId) {
            this.appId = appId;
            return this;
        }

        /**
         * Sets the URL-encoded URI to the webhook endpoint that handles inbound messages. Your webhook endpoint must
         * be active before you make this request. Vonage makes a GET request to the endpoint and checks that it
         * returns a 200 OK response. Set this parameter's value to an empty string to remove the webhook.
         *
         * @param moHttpUrl The inbound message webhook URL as a string.
         * @return This builder.
         */
        public Builder moHttpUrl(String moHttpUrl) {
            return moHttpUrl(URI.create(moHttpUrl));
        }

        /**
         * Sets the URL-encoded URI to the webhook endpoint that handles inbound messages. Your webhook endpoint must
         * be active before you make this request. Vonage makes a GET request to the endpoint and checks that it
         * returns a 200 OK response. Set this parameter's value to an empty string to remove the webhook.
         *
         * @param moHttpUrl The inbound message webhook URL.
         * @return This builder.
         */
        public Builder moHttpUrl(URI moHttpUrl) {
            this.moHttpUrl = moHttpUrl;
            return this;
        }

        /**
         * Sets the webhook URI for Vonage to send a request to when a call ends.
         *
         * @param voiceStatusCallback The voice status webhook URL as a string.
         * @return This builder.
         */
        public Builder voiceStatusCallback(String voiceStatusCallback) {
            return voiceStatusCallback(URI.create(voiceStatusCallback));
        }

        /**
         * Sets the webhook URI for Vonage to send a request to when a call ends.
         *
         * @param voiceStatusCallback The voice status webhook URL.
         * @return This builder.
         */
        public Builder voiceStatusCallback(URI voiceStatusCallback) {
            this.voiceStatusCallback = voiceStatusCallback;
            return this;
        }

        /**
         * Builds the UpdateNumberRequest.
         *
         * @return A new UpdateNumberRequest with this builder's properties.
         */
        public UpdateNumberRequest build() {
            return new UpdateNumberRequest(this);
        }
    }
}
