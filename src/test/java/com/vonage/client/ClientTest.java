/*
 *   Copyright 2023 Vonage
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
package com.vonage.client;

import com.vonage.client.auth.JWTAuthMethod;
import com.vonage.client.auth.TokenAuthMethod;
import com.vonage.client.logging.LoggingUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertThrows;
import org.junit.function.ThrowingRunnable;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.function.Supplier;

public abstract class ClientTest<T> {
    protected String applicationId = UUID.randomUUID().toString();
    protected HttpWrapper wrapper;
    protected T client;

    protected ClientTest() {
        wrapper = new HttpWrapper(
                new TokenAuthMethod("not-an-api-key", "secret"),
                new JWTAuthMethod(applicationId, new byte[0])
        );
    }

    protected HttpClient stubHttpClient(int statusCode) throws Exception {
        return stubHttpClient(statusCode, "");
    }

    protected HttpClient stubHttpClient(int statusCode, String content) throws Exception {
        HttpClient result = mock(HttpClient.class);

        HttpResponse response = mock(HttpResponse.class);
        StatusLine sl = mock(StatusLine.class);
        HttpEntity entity = mock(HttpEntity.class);

        when(result.execute(any(HttpUriRequest.class))).thenReturn(response);
        when(LoggingUtils.logResponse(any(HttpResponse.class))).thenReturn("response logged");
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
        when(sl.getStatusCode()).thenReturn(statusCode);
        when(sl.getReasonPhrase()).thenReturn("Test reason");
        when(response.getStatusLine()).thenReturn(sl);
        when(response.getEntity()).thenReturn(entity);

        return result;
    }

    protected void stubResponse(int code, String response) throws Exception {
        wrapper.setHttpClient(stubHttpClient(code, response));
    }

    protected void stubResponse(String response) throws Exception {
        stubResponse(200, response);
    }

    protected void stubResponse(int code) throws Exception {
        wrapper.setHttpClient(stubHttpClient(code));
    }

    protected void stubResponseAndAssertThrows(ThrowingRunnable invocation,
                                               Class<? extends Exception> exceptionClass) throws Exception {
        stubResponseAndAssertThrows(200, invocation, exceptionClass);
    }

    protected void stubResponseAndAssertThrows(int statusCode, ThrowingRunnable invocation,
                                               Class<? extends Exception> exceptionClass) throws Exception {
        stubResponse(statusCode);
        assertThrows(exceptionClass, invocation);
    }

    protected void stubResponseAndAssertThrows(String response, ThrowingRunnable invocation,
                                               Class<? extends Exception> exceptionClass) throws Exception {
        stubResponse(200, response);
        assertThrows(exceptionClass, invocation);
    }

    protected void stubResponseAndAssertThrows(int statusCode, String response, ThrowingRunnable invocation,
                                               Class<? extends Exception> exceptionClass) throws Exception {
        stubResponse(statusCode, response);
        assertThrows(exceptionClass, invocation);
    }

    protected void stubResponseAndRun(String responseJson, Runnable invocation) throws Exception {
        stubResponse(200, responseJson);
        invocation.run();
    }

    protected void stubResponseAndRun(int statusCode, Runnable invocation) throws Exception {
        stubResponse(statusCode);
        invocation.run();
    }

    protected <R> R stubResponseAndGet(String response, Supplier<? extends R> invocation) throws Exception {
        return stubResponseAndGet(200, response, invocation);
    }

    protected <R> R stubResponseAndGet(int statusCode, String response, Supplier<? extends R> invocation) throws Exception {
        stubResponse(statusCode, response);
        return invocation.get();
    }
}
