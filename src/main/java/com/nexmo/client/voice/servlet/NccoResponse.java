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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.NexmoUnexpectedException;
import com.nexmo.client.voice.ncco.Action;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates a set of NCCO objects for driving the Nexmo Voice API.
 * <p>
 * This would usually be returned by {@link AbstractAnswerServlet#handleRequest(HttpServletRequest)}, which serializes
 * it correctly for the Voice API. {@link NccoResponseBuilder} provides a fluent interface for constructing instances
 * of this class.
 */
public class NccoResponse {
    // This object has been purposefully designed to be relatively opaque, as
    // the internal structure of this response may become more complex, and so
    // we don't want users to become dependent on it being a simple list of
    // Ncco objects.

    private List<Action> actionList;

    public NccoResponse() {
        actionList = new ArrayList<>();
    }

    public void appendNcco(Action action) {
        this.actionList.add(action);
    }

    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(actionList);
        } catch (JsonProcessingException e) {
            throw new NexmoUnexpectedException("Failed to serialize NccoResponse object.", e);
        }
    }
}