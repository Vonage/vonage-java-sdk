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

abstract class GenericEvent extends Event<Map<String, Object>> {

    GenericEvent() {}

    GenericEvent(Builder<?, ?> builder) {
        super(builder);
    }

    static abstract class Builder<E extends GenericEvent,
            B extends Builder<? extends E, ? extends  B>>
            extends Event.Builder<Map<String, Object>, GenericEvent, Builder<E, B>> {

        Builder(EventType type) {
            super(type);
        }
    }
}
