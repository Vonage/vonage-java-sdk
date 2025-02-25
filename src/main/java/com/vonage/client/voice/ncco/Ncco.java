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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.vonage.client.Jsonable;
import com.vonage.client.VonageUnexpectedException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Vonage Call Control Object for controlling the flow of a Voice API call.
 */
public class Ncco implements Jsonable {
    @JsonValue
    private Collection<? extends Action> actions;
    private ObjectWriter writer;

    public Ncco() {
        this(new ObjectMapper().writer(), Collections.emptyList());
    }

    public Ncco(Collection<Action> actions) {
        this(Jsonable.createDefaultObjectMapper().writer(), actions);
    }

    @Deprecated
    public Ncco(ObjectWriter writer) {
        this(writer, Collections.emptyList());
    }

    @Deprecated
    public Ncco(ObjectWriter writer, Collection<? extends Action> actions) {
        this.writer = writer;
        this.actions = actions;
    }

    @Deprecated
    public Ncco(ObjectWriter writer, Action... action) {
        this(writer, Arrays.asList(action));
    }

    public Ncco(Action... action) {
        this(Arrays.asList(action));
    }

    /**
     * Gets the NCCO actions as an ordered collection.
     *
     * @return The call actions to take in execution order.
     */
    public Collection<? extends Action> getActions() {
        return this.actions;
    }

    @Override
    public String toJson() {
        try {
            return this.writer.writeValueAsString(this.actions);
        } catch (JsonProcessingException e) {
            throw new VonageUnexpectedException("Unable to convert NCCO Object to JSON.");
        }
    }
}
