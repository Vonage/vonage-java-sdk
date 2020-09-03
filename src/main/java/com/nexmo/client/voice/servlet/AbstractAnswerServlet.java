/*
 * Copyright (c) 2020 Vonage
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
        serializeNccoResponse(resp, this.handleRequest(req));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        serializeNccoResponse(resp, this.handleRequest(req));
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
