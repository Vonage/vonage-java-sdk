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

public class GenericEvent extends EventWithBody<Map<String, ?>> {

    GenericEvent() {}

    GenericEvent(Builder<?, ?> builder) {
        super(builder);
        body = builder.body;
    }

    /**
     * Custom event data as key-value pairs.
     *
     * @return The event's main body as a Map.
     */
    public Map<String, ?> getBody() {
        return body;
    }

    @SuppressWarnings("unchecked")
    public static abstract class Builder<E extends GenericEvent,
            B extends Builder<? extends E, ? extends  B>>
            extends EventWithBody.Builder<GenericEvent, Builder<E, B>> {

        private Map<String, ?> body;

        Builder(EventType type) {
            super(type);
        }

        /**
         * Custom data send in the body.
         *
         * @param body The custom data as key-value pairs.
         *
         * @return This builder.
         */
        public B body(Map<String, ?> body) {
            this.body = body;
            return (B) this;
        }
    }
}
