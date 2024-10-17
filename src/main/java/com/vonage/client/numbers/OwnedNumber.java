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

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;
import java.util.UUID;

/**
 * Represents a number that is being rented by your account.
 */
public class OwnedNumber extends JsonableNumber {
    private URI moHttpUrl;
    private UpdateNumberRequest.CallbackType voiceCallbackType;
    private String voiceCallbackValue;
    private UUID appId, messagesCallbackValue;

    /**
     * Constructor, not for public use.
     *
     * @deprecated This will be made private in a future release.
     */
    @Deprecated
    public OwnedNumber() {
    }

    /**
     * URL of the webhook endpoint that handles inbound messages.
     *
     * @return The inbound message webhook URL as a string, or {@code null} if unspecified.
     */
    @JsonProperty("moHttpUrl")
    public String getMoHttpUrl() {
        return moHttpUrl != null ? moHttpUrl.toString() : null;
    }

    /**
     * Voice webhook type. In a future release, this will be an enum.
     *
     * @return The voice webhook callback type as a string, or {@code null} if unknown.
     */
    @JsonProperty("voiceCallbackType")
    public String getVoiceCallbackType() {
        return voiceCallbackType != null ? voiceCallbackType.toString() : null;
    }

    /**
     * SIP URI, telephone number or Application ID.
     *
     * @return The voice webhook value as a string, or {@code null} if unknown.
     */
    @JsonProperty("voiceCallbackValue")
    public String getVoiceCallbackValue() {
        return voiceCallbackValue;
    }

    /**
     * Application ID for inbound message handling, if applicable.
     *
     * @return The application ID that will handle inbound messages to this number, or {@code null} if not applicable.
     *
     * @since 8.10.0
     */
    @JsonProperty("messagesCallbackValue")
    public UUID getMessagesCallbackValue() {
        return messagesCallbackValue;
    }

    /**
     * ID of the application linked to this number, if applicable.
     *
     * @return The application UUID that this number is linked to, or {@code null} if not linked to any application.
     * @since 8.12.0
     */
    @JsonProperty("app_id")
    public UUID getAppId() {
        return appId;
    }

    @Deprecated
    public void setMoHttpUrl(String moHttpUrl) {
        if (moHttpUrl != null) {
            this.moHttpUrl = URI.create(moHttpUrl);
        }
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
