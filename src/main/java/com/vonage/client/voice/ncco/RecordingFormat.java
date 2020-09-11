/*
 *   Copyright 2020 Vonage
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

import java.util.HashMap;
import java.util.Map;

public enum RecordingFormat {
    MP3, WAV, OGG, UNKNOWN;

    private static final Map<String, RecordingFormat> RECORDING_FORMAT_INDEX = new HashMap<>();

    static {
        for (RecordingFormat recordingFormat : RecordingFormat.values()) {
            RECORDING_FORMAT_INDEX.put(recordingFormat.name(), recordingFormat);
        }
    }

    @JsonValue
    @Override
    public String toString() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static RecordingFormat fromString(String name) {
        RecordingFormat foundRecordingFormat = RECORDING_FORMAT_INDEX.get(name.toUpperCase());
        return (foundRecordingFormat != null) ? foundRecordingFormat : UNKNOWN;
    }
}