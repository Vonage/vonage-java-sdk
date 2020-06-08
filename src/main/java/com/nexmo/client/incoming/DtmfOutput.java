package com.nexmo.client.incoming;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DtmfOutput {

    private String digits;
    @JsonProperty("timed_out")
    private boolean timedOut;

    public String getDigits() {
        return digits;
    }

    public boolean getTimedOut() {
        return timedOut;
    }
}
