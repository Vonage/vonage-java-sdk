/*
 * Copyright (c) 2011-2017 Nexmo Inc
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
package com.nexmo.client.voice.servlet;

import com.nexmo.client.voice.ncco.Action;

/**
 * Provides a fluent interface for constructing instances of {@link NccoResponse}.
 * <p>
 * Currently, an NccoResponse consists of a flat series of Ncco objects which are implemented sequentially (except for
 * NCCOs where bargeIn is set to true, when more than one NCCO can be executed in parallel).
 */
public class NccoResponseBuilder {
    private NccoResponse value;

    public NccoResponseBuilder() {
        this.value = new NccoResponse();
    }

    public NccoResponseBuilder appendNcco(Action action) {
        this.value.appendNcco(action);
        return this;
    }

    public NccoResponse getValue() {
        return this.value;
    }
}