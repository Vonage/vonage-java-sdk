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

/**
 * Represents a Custom event.
 */
public final class CustomEvent extends GenericEvent {

    private CustomEvent() {}

    private CustomEvent(Builder builder) {
        super(builder);
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @param typeName The suffix to append to the event type name (excluding the {@code custom:} prefix).
     *
     * @return A new Builder.
     * @since 8.20.0 Added {@code typeName} parameter.
     */
    public static Builder builder(String typeName) {
        return new Builder(EventType.CUSTOM + ":" + typeName);
    }

    public static final class Builder extends GenericEvent.Builder<CustomEvent, Builder> {
        Builder(String type) {
            super(type);
        }

        @Override
        public CustomEvent build() {
            return new CustomEvent(this);
        }
    }
}
