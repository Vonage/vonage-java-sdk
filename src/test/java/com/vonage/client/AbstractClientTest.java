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

import com.vonage.client.auth.ApiKeyHeaderAuthMethod;
import com.vonage.client.auth.ApiKeyQueryParamsAuthMethod;
import com.vonage.client.auth.JWTAuthMethod;
import com.vonage.client.auth.SignatureAuthMethod;
import com.vonage.client.auth.hashutils.HashUtil;
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
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractClientTest<T> {
    protected HttpWrapper wrapper;
    protected T client;

    protected AbstractClientTest() {
        wrapper = new HttpWrapper(
                new ApiKeyHeaderAuthMethod(TestUtils.API_KEY, TestUtils.API_SECRET),
                new ApiKeyQueryParamsAuthMethod(TestUtils.API_KEY, TestUtils.API_SECRET),
                new SignatureAuthMethod(TestUtils.API_KEY, TestUtils.SIGNATURE_SECRET, HashUtil.HashType.HMAC_SHA256),
                new JWTAuthMethod(TestUtils.APPLICATION_ID, new byte[0])
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
        when(sl.getReasonPhrase()).thenReturn(TestUtils.TEST_REASON);
        when(response.getStatusLine()).thenReturn(sl);
        when(response.getEntity()).thenReturn(entity);

        return result;
    }

    protected void stubNetworkResponse(String mainResponse) throws Exception {
        stubResponse(200,
                "{\"auth_req_id\": \"0dadaeb4-7c79-4d39-b4b0-5a6cc08bf537\"}",
                "{\"access_token\": \"youMayProceed\"}",
                mainResponse
        );
    }

    protected void stubResponse(int code, String response, String... additionalResponses) throws Exception {
        wrapper.setHttpClient(stubHttpClient(code, response, additionalResponses));
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
        stubResponseAndRun(200, responseJson, invocation);
    }

    protected void stubResponseAndRun(int statusCode, String responseJson, Runnable invocation) throws Exception {
        stubResponse(statusCode, responseJson);
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
        stubResponse(statusCode, expectedJson);
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
            if (expectedResponse.title == null) {
                expectedResponse.title = TestUtils.TEST_REASON;
            }
            assertEquals(expectedResponse, ex);
            String actualJson = ((E) ex).toJson().replace("\"title\":\""+ TestUtils.TEST_REASON +"\",", "");
            assertEquals(expectedJson, actualJson);
        }
        return expectedResponse;
    }
}
