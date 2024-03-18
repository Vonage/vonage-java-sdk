/*
 *   Copyright 2024 Vonage
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

import java.util.Map;

/**
 * Represents an Ephemeral event.
 */
public final class EphemeralEvent extends Event<Map<String, Object>> {

    private EphemeralEvent() {}

    private EphemeralEvent(Builder builder) {
        super(builder);
    }

    public static final class Builder extends Event.Builder<Map<String, Object>, EphemeralEvent, Builder> {
        Builder() {
            super(EventType.EPHEMERAL);
        }

        @Override
        public EphemeralEvent build() {
            return new EphemeralEvent(this);
        }
    }
}
