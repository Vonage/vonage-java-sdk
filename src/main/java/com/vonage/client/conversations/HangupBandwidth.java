package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

/**
 * Represents the {@code bandwidth} field in the body of a hangup event.
 *
 * @since 8.20.0
 */
public class HangupBandwidth extends JsonableBaseObject {
    private Integer byteIn, byteOut;

    /**
     * Gets the inbound byte count.
     *
     * @return The inbound byte count.
     */
    @JsonProperty("byte_in")
    public Integer getByteIn() {
        return byteIn;
    }

    /**
     * Gets the outbound byte count.
     *
     * @return The outbound byte count.
     */
    @JsonProperty("byte_out")
    public Integer getByteOut() {
        return byteOut;
    }
}
