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
package com.vonage.client;

import com.vonage.client.auth.TokenAuthMethod;
import com.vonage.client.logging.LoggingUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpUriRequest;
import static org.junit.Assert.assertThrows;
import org.junit.function.ThrowingRunnable;
import static org.mockito.Mockito.*;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

public abstract class ClientTest<T> {
    protected HttpWrapper wrapper;
    protected T client;

    protected ClientTest() {
        wrapper = new HttpWrapper(new TokenAuthMethod("not-an-api-key", "secret"));
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

    protected void stubResponseAndRun(Runnable invocation) throws Exception {
        stubResponseAndRun(200, invocation);
    }

    protected void stubResponseAndRun(int statusCode, Runnable invocation) throws Exception {
        stubResponse(statusCode);
        invocation.run();
    }

    protected <R> R stubResponseWithResult(int statusCode, String response, Supplier<? extends R> invocation) throws Exception {
        stubResponse(statusCode, response);
        return invocation.get();
    }

    protected <R> R stubResponseWithResult(String response, Supplier<? extends R> invocation) throws Exception {
        stubResponse(response);
        return invocation.get();
    }

    protected void stubResponseAndAssertThrows(int statusCode, ThrowingRunnable invocation,
                                               Class<? extends Exception> exceptionClass) throws Exception {
        stubResponse(statusCode);
        assertThrows(exceptionClass, invocation);
    }

    protected void stubResponseAndAssertThrows(String response, ThrowingRunnable invocation,
                                               Class<? extends Exception> exceptionClass) throws Exception {
        stubResponse(response);
        assertThrows(exceptionClass, invocation);
    }

    protected void stubResponseAndAssertThrows(int statusCode, String response, ThrowingRunnable invocation,
                                               Class<? extends Exception> exceptionClass) throws Exception {
        stubResponse(statusCode, response);
        assertThrows(exceptionClass, invocation);
    }

    protected void stubResponseAndAssertThrowsHttpResponseException(int statusCode, String response,
                                                                    ThrowingRunnable invocation) throws Exception {
        stubResponseAndAssertThrows(statusCode, response, invocation, HttpResponseException.class);
    }

    protected void stubResponseAndAssertThrowsIAX(int statusCode, ThrowingRunnable invocation) throws Exception {
        stubResponseAndAssertThrows(statusCode, invocation, IllegalArgumentException.class);
    }

    protected void stubResponseAndAssertThrowsIAX(ThrowingRunnable invocation) throws Exception {
        stubResponseAndAssertThrowsIAX(200, invocation);
    }

    protected void stubResponseAndAssertThrowsIAX(String response, ThrowingRunnable invocation) throws Exception {
        stubResponseAndAssertThrows(response, invocation, IllegalArgumentException.class);
    }

    protected void stubResponseAndAssertThrowsNPE(ThrowingRunnable invocation) throws Exception {
        stubResponseAndAssertThrows(200, invocation, NullPointerException.class);
    }

    protected void stubResponseAndAssertThrowsBadRequestException(int statusCode, String response,
                                                                  ThrowingRunnable invocation) throws Exception {
        stubResponseAndAssertThrows(statusCode, response, invocation, VonageBadRequestException.class);
    }

    protected void stubResponseAndAssertThrowsResponseParseException(int statusCode, String response,
                                                                  ThrowingRunnable invocation) throws Exception {
        stubResponseAndAssertThrows(statusCode, response, invocation, VonageResponseParseException.class);
    }
}
