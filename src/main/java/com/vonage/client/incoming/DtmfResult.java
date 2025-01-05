/*
 * Copyright 2025 Vonage
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vonage.client.incoming;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @deprecated Use {@link com.vonage.client.voice.DtmfResult}.
 */
@Deprecated
public class DtmfResult {
    private String digits;
    private boolean timedOut;

    /**
     * The button sequence pressed by the user.
     *
     * @return The buttons pressed as a String.
     */
    @JsonProperty("digits")
    public String getDigits() {
        return digits;
    }

    /**
     * Whether the DTMF input timed out.
     *
     * @return {@code true} if the DTMF timed out, {@code false} otherwise.
     */
    @JsonProperty("timed_out")
    public boolean isTimedOut() {
        return timedOut;
    }
}
