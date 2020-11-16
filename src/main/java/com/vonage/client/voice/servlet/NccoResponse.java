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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import com.vonage.client.voice.ncco.Action;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates a set of NCCO objects for driving the Vonage Voice API.
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
        actionList.add(action);
    }

    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(actionList);
        } catch (JsonProcessingException e) {
            throw new VonageUnexpectedException("Failed to serialize NccoResponse object.", e);
        }
    }
}