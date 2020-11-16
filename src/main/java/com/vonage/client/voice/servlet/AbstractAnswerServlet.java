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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Useful abstract HttpServlet for implementing NCCO callbacks.
 * <p>
 *     Implement {@link #handleRequest(HttpServletRequest)} to return an {@link NccoResponse} and this servlet will
 *     ensure that the response is serialized correctly for the Vonage Voice API.
 * </p>
 */
public abstract class AbstractAnswerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        serializeNccoResponse(resp, handleRequest(req));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        serializeNccoResponse(resp, handleRequest(req));
    }

    private void serializeNccoResponse(HttpServletResponse httpResponse, NccoResponse nccoResponse) throws IOException {
        byte[] json = nccoResponse.toJson().getBytes("UTF-8");
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json");
        httpResponse.setContentLength(json.length);
        httpResponse.getOutputStream().write(json);
    }

    /**
     * Handle a request for NCCO instructions from the Vonage Voice API.
     * <p>
     * Implementations should return an NccoResponse object (most easily constructed using {@link NccoResponseBuilder}.
     *
     * @param request the HttpServletRequest parsed from the request made by the Vonage Voice API
     * @return An NccoResponse containing Ncco instructions for the Vonage Voice API
     */
    protected abstract NccoResponse handleRequest(HttpServletRequest request);
}
