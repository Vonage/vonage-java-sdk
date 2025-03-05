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
package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represents an {@link EventType#AUDIO_DTMF} event.
 *
 * @since 8.19.0
 */
public final class AudioDtmfEvent extends AbstractChannelEvent<AudioDtmfEventBody> {

    AudioDtmfEvent() {}

    private AudioDtmfEvent(Builder builder) {
        super(builder);
        body = new AudioDtmfEventBody(builder);
    }

    /**
     * DTMF digits for the event.
     *
     * @return The DTMF digits as a string.
     */
    @JsonIgnore
    public String getDigits() {
        return body != null ? body.digits : null;
    }

    /**
     * Sequence number for the event.
     *
     * @return The DTMF sequence as an integer, or {@code null} if unspecified / unknown.
     */
    @JsonIgnore
    public Integer getDtmfSeq() {
        return body != null ? body.dtmfSeq : null;
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @return A new Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for setting the Audio DTMF event parameters.
     */
    public static final class Builder extends AbstractChannelEvent.Builder<AudioDtmfEvent, AudioDtmfEventBody, Builder> {
        String digits;
        Integer dtmfSeq;

        Builder() {
            super(EventType.AUDIO_DTMF);
        }

        /**
         * Set the digits for the event.
         *
         * @param digits The digits for the event.
         *
         * @return This builder.
         */
        public Builder digits(String digits) {
            this.digits = digits;
            return this;
        }

        /**
         * Set the DTMF sequence for the event.
         *
         * @param dtmfSeq The DTMF sequence for the event.
         *
         * @return This builder.
         */
        public Builder dtmfSeq(int dtmfSeq) {
            this.dtmfSeq = dtmfSeq;
            return this;
        }

        @Override
        public AudioDtmfEvent build() {
            return new AudioDtmfEvent(this);
        }
    }
}
