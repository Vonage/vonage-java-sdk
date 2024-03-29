/*
 *   Copyright 2024 Vonage
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
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.Executable;
import static org.mockito.Mockito.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class ClientTest<T> {
    protected final String applicationId = UUID.randomUUID().toString();
    protected final String apiKey = "a1b2c3d4";
    protected final String apiSecret = "1234567890abcdef";
    protected final String testReason = "Test reason";
    protected HttpWrapper wrapper;
    protected T client;

    protected ClientTest() {
        wrapper = new HttpWrapper(
                new TokenAuthMethod(apiKey, apiSecret),
                new JWTAuthMethod(applicationId, new byte[0])
        );
    }

    protected HttpClient stubHttpClient(int statusCode) throws Exception {
        return stubHttpClient(statusCode, "");
    }

    protected HttpClient stubHttpClient(int statusCode, String content, String... additionalReturns) throws Exception {
        HttpClient result = mock(HttpClient.class);

        HttpResponse response = mock(HttpResponse.class);
        StatusLine sl = mock(StatusLine.class);
        HttpEntity entity = mock(HttpEntity.class);

        when(result.execute(any(HttpUriRequest.class))).thenReturn(response);
        Function<String, InputStream> transformation = c -> new ByteArrayInputStream(c.getBytes(StandardCharsets.UTF_8));
        InputStream[] contentsEncoded = Arrays.stream(additionalReturns).map(transformation).toArray(InputStream[]::new);
        when(entity.getContent()).thenReturn(transformation.apply(content), contentsEncoded);
        when(sl.getStatusCode()).thenReturn(statusCode);
        when(sl.getReasonPhrase()).thenReturn(testReason);
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

    protected void stubResponseAndAssertThrows(Executable invocation,
                                               Class<? extends Exception> exceptionClass) throws Exception {
        stubResponseAndAssertThrows(200, invocation, exceptionClass);
    }

    protected void stubResponseAndAssertThrows(int statusCode, Executable invocation,
                                               Class<? extends Exception> exceptionClass) throws Exception {
        stubResponse(statusCode);
        assertThrows(exceptionClass, invocation);
    }

    protected void stubResponseAndAssertThrows(String response, Executable invocation,
                                               Class<? extends Exception> exceptionClass) throws Exception {
        stubResponse(200, response);
        assertThrows(exceptionClass, invocation);
    }

    protected void stubResponseAndAssertThrows(int statusCode, String response, Executable invocation,
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

    protected <E extends VonageApiResponseException> E assert401ApiResponseException(
            Class<E> exClass, Executable invocation) throws Exception {
        String responseJson = "{\n" +
                "   \"type\": \"https://developer.nexmo.com/api-errors#unauthorized\",\n" +
                "   \"title\": \"Invalid credentials supplied\",\n" +
                "   \"detail\": \"You did not provide correct credentials\",\n" +
                "   \"instance\": \"798b8f199c45014ab7b08bfe9cc1c12c\"\n" +
                "}";
        return assertApiResponseException(401, responseJson, exClass, invocation);
    }

    @SuppressWarnings("unchecked")
    protected <E extends VonageApiResponseException> E assertApiResponseException(
            int statusCode, String response, Class<E> exClass, Executable invocation) throws Exception {
        E expectedResponse = (E) exClass.getDeclaredMethod("fromJson", String.class).invoke(exClass, response);
        String expectedJson = expectedResponse.toJson();
        wrapper.setHttpClient(stubHttpClient(statusCode, expectedJson));
        java.lang.reflect.Method setStatusCode = exClass.getDeclaredMethod("setStatusCode", int.class);
        setStatusCode.setAccessible(true);
        setStatusCode.invoke(expectedResponse, statusCode);
        String failPrefix = "Expected "+exClass.getSimpleName()+", but got ";

        try {
            invocation.execute();
            fail(failPrefix + "nothing.");
        }
        catch (Throwable ex) {
            assertEquals(exClass, ex.getClass(), failPrefix + ex.getClass());
            if (expectedResponse.getTitle() == null) {
                expectedResponse.title = testReason;
            }
            assertEquals(expectedResponse, ex);
            String actualJson = ((E) ex).toJson().replace("\"title\":\""+testReason+"\",", "");
            assertEquals(expectedJson, actualJson);
        }
        return expectedResponse;
    }
}
