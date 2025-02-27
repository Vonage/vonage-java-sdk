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
package com.vonage.client.voice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.voice.ncco.Ncco;

/**
 * Represents the destination of a transfer action in {@link TransferCallPayload#getDestination()}.
 */
class TransferDestination extends JsonableBaseObject {
    private final Type type;
    private final String[] urls;
    private final Ncco ncco;

    /**
     * Create a new TransferDestination.
     *
     * @param url URL to transfer the call to.
     */
    TransferDestination(String url) {
        this(Type.NCCO, url, null);
    }

    /**
     * Create a new TransferDestination.
     *
     * @param ncco NCCO to transfer the call to.
     */
    TransferDestination(Ncco ncco) {
        this(Type.NCCO, null, ncco);
    }

    /**
     * Create a new TransferDestination.
     *
     * @param type Transfer destination type.
     * @param url URL to transfer the call to.
     * @param ncco NCCO to transfer the call to.
     */
    public TransferDestination(Type type, String url, Ncco ncco) {
        this.type = type;
        this.urls = url != null ? new String[]{url} : null;
        this.ncco = ncco;
    }

    /**
     * Transfer destination type.
     *
     * @return The type as an enum.
     */
    @JsonProperty("type")
    public Type getType() {
        return type;
    }

    /**
     * URL to transfer the call to.
     *
     * @return The URL wrapped in a singleton string array, or {@code null} if using NCCO instead.
     */
    @JsonProperty("url")
    public String[] getUrl() {
        return urls;
    }

    /**
     * NCCO to transfer the call to.
     *
     * @return The NCCO, or {@code null} if using URL instead.
     */
    @JsonProperty("ncco")
    public Ncco getNcco() {
        return ncco;
    }

    /**
     * Represents the destination type.
     */
    public enum Type {
        NCCO;

        @JsonValue
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
