package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

/**
 * Represents the {@code reason} field in the body of a hangup event.
 *
 * @since 8.20.0
 */
public class HangupReason extends JsonableBaseObject {
    private String text, code, sipCode;

    /**
     * Gets the reason text.
     *
     * @return The reason text.
     */
    @JsonProperty("text")
    public String getText() {
        return text;
    }

    /**
     * Gets the reason code.
     *
     * @return The reason code.
     */
    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    /**
     * Gets the SIP code.
     *
     * @return The SIP code.
     */
    @JsonProperty("sip_code")
    public String getSipCode() {
        return sipCode;
    }
}
