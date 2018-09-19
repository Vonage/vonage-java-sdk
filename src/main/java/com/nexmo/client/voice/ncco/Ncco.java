/*
 * Copyright (c) 2011-2018 Nexmo Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nexmo.client.voice.ncco;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nexmo.client.NexmoUnexpectedException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Nexmo Call Control Object for controlling the flow of a Voice API call.
 */
public class Ncco {
    private Collection<Action> actions;
    private ObjectWriter writer;

    public Ncco() {
        this(new ObjectMapper().writer(), Collections.<Action>emptyList());
    }

    public Ncco(Collection<Action> actions) {
        this(new ObjectMapper().writer(), actions);
    }

    public Ncco(ObjectWriter writer) {
        this(writer, Collections.<Action>emptyList());
    }

    public Ncco(ObjectWriter writer, Collection<Action> actions) {
        this.writer = writer;
        this.actions = actions;
    }

    public Ncco(ObjectWriter writer, Action... action) {
        this(writer, Arrays.asList(action));
    }

    public Ncco(Action... action) {
        this(Arrays.asList(action));
    }

    public Collection<Action> getActions() {
        return this.actions;
    }

    public String toJson() {
        try {
            return this.writer.writeValueAsString(this.actions);
        } catch (JsonProcessingException e) {
            throw new NexmoUnexpectedException("Unable to convert NCCO Object to JSON.");
        }
    }
}
