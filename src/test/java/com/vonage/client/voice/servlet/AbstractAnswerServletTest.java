/*
 *   Copyright 2022 Vonage
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

import com.vonage.client.voice.ncco.TalkAction;
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
    private final AbstractAnswerServlet servlet = new AbstractAnswerServlet() {
        @Override
        protected NccoResponse handleRequest(HttpServletRequest request) {
            return new NccoResponseBuilder().appendNcco(TalkAction.builder("Hello").build()).getValue();
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