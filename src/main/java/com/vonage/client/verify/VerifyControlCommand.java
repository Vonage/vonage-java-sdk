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
package com.vonage.client.verify;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing the control command. The possible commands are {@code cancel} to request cancellation of
 * the verification process, or {@code trigger_next_event} to advance to the next verification event (if any).
 * Cancellation is only possible 30 seconds after the start of the verification request and
 * before the second event (either TTS or SMS) has taken place.
 */
public enum VerifyControlCommand {
    CANCEL,
    TRIGGER_NEXT_EVENT;

    @JsonValue
    @Override
    public String toString() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static VerifyControlCommand fromString(String name) {
        return VerifyControlCommand.valueOf(name.toUpperCase());
    }
}
