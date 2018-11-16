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

import com.nexmo.client.voice.ncco.TalkAction;
import org.junit.Test;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbstractAnswerServletTest {
    private AbstractAnswerServlet servlet = new AbstractAnswerServlet() {
        @Override
        protected NccoResponse handleRequest(HttpServletRequest request) {
            return new NccoResponseBuilder().appendNcco(new TalkAction.Builder("Hello").build()).getValue();
        }
    };

    private HttpServletResponse mockResponse() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        ServletOutputStream outputStream = new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {
                // We don't care about this.
            }

            @Override
            public void write(int b) throws IOException {
                out.write(b);
            }

            @Override
            public String toString() {
                return out.toString();
            }
        };

        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getOutputStream()).thenReturn(outputStream);

        return response;
    }

    @Test
    public void doGet() throws Exception {
        HttpServletResponse response = mockResponse();
        servlet.doGet(null, response);
        assertEquals(
                "[{\"text\":\"Hello\",\"action\":\"talk\"}]",
                response.getOutputStream().toString());
    }

    @Test
    public void doPost() throws Exception {
        HttpServletResponse response = mockResponse();
        servlet.doPost(null, response);
        assertEquals(
                "[{\"text\":\"Hello\",\"action\":\"talk\"}]",
                response.getOutputStream().toString());
    }

    @Test
    public void handleRequest() throws Exception {
        assertEquals(
                "[{\"text\":\"Hello\",\"action\":\"talk\"}]",
                servlet.handleRequest(null).toJson());
    }

}