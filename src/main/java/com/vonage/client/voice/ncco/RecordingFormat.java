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
package com.vonage.client.voice.ncco;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the audio recording file format in {@linkplain RecordAction}.
 */
public enum RecordingFormat {
    MP3, WAV, OGG, UNKNOWN;

    @JsonValue
    @Override
    public String toString() {
        return name().toLowerCase();
    }

    /**
     * Convert a string value into a {@link RecordingFormat} enum.
     *
     * @param name The string value to convert.
     *
     * @return The recording format as an enum, or {@code UNKNOWN} if an invalid value is provided.
     */
    @JsonCreator
    public static RecordingFormat fromString(String name) {
        try {
            return RecordingFormat.valueOf(name.toUpperCase());
        }
        catch (IllegalArgumentException ex) {
            return UNKNOWN;
        }
    }
}