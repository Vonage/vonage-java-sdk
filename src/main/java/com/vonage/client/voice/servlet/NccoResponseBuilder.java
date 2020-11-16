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
package com.vonage.client.voice.servlet;

import com.vonage.client.voice.ncco.Action;

/**
 * Provides a fluent interface for constructing instances of {@link NccoResponse}.
 * <p>
 * Currently, an NccoResponse consists of a flat series of Ncco objects which are implemented sequentially (except for
 * NCCOs where bargeIn is set to true, when more than one NCCO can be executed in parallel).
 */
public class NccoResponseBuilder {
    private NccoResponse value;

    public NccoResponseBuilder() {
        value = new NccoResponse();
    }

    public NccoResponseBuilder appendNcco(Action action) {
        value.appendNcco(action);
        return this;
    }

    public NccoResponse getValue() {
        return value;
    }
}