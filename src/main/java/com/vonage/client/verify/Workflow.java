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
package com.vonage.client.verify;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeration representing different verification workflows.
 * <p>
 * See: <a href="https://developer.nexmo.com/verify/guides/workflows-and-events">
 * https://developer.nexmo.com/verify/guides/workflows-and-events</a> for more details.
 *
 * @since 9.0.0 Moved to its own class / file from {@linkplain VerifyRequest} and {@linkplain Psd2Request}.
 */
public enum Workflow {
    /**
     * The default workflow.
     */
    SMS_TTS_TTS(1),
    SMS_SMS_TTS(2),
    TTS_TTS(3),
    SMS_SMS(4),
    SMS_TTS(5),
    SMS(6),
    TTS(7);

    private final int id;

    Workflow(int id) {
        this.id = id;
    }

    /**
     * Gets the workflow ID.
     *
     * @return The workflow ID as an integer.
     */
    @JsonValue
    public int getId() {
        return id;
    }
}
