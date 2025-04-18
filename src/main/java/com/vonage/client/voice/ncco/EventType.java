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

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The type of event to use for {@link ConnectAction}.
 */
public enum EventType {

    /**
     * Enables eventUrl to return an NCCO that overrides the current NCCO when a call moves to specific states.
     */
    SYNCHRONOUS;

    @JsonValue
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
