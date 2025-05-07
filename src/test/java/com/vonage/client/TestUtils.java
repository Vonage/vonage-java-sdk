/*
 *   Copyright 2025 Vonage
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.auth.*;
import com.vonage.client.auth.hashutils.HashType;
import org.apache.http.*;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

public class TestUtils {
    public static final UUID APPLICATION_ID = UUID.randomUUID();
    public static final String
            APPLICATION_ID_STR = APPLICATION_ID.toString(),
            API_KEY = "a1b2c3d4",
            API_SECRET = "1234567890abcdef",
            SIGNATURE_SECRET = "kTCRawcijyNTfQ1sNqVrz3ZDyRQRZXoL8IhaYTrMxKg153UcHT",
            TEST_BASE_URI = "http://localhost:8081",
            TEST_REASON = "Test reason",
            TEST_REDIRECT_URI = "https://www.example.org/login/challenge?key="+API_KEY;

    public static final HttpConfig TEST_HTTP_CONFIG = HttpConfig.builder().baseUri(TEST_BASE_URI).build();

    public byte[] loadKey(String path) throws IOException {
        int size = 1024;
        byte[] buf = new byte[size];

        ByteArrayOutputStream bos = new ByteArrayOutputStream(2048);
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                throw new IOException("Could not find resource at: " + getClass().getResource(path));
            }
            for (int len; (len = is.read(buf, 0, size)) != -1; bos.write(buf, 0, len));
        }
        return bos.toByteArray();
    }

    public static Map<String, String> makeParameterMap(List<NameValuePair> params) {
        Map<String, String> result = new HashMap<>();
        for (NameValuePair param : params) {
            result.put(param.getName(), param.getValue());
        }
        return result;
    }

    /**
     * Used in cases where the same url parameter is repeated, returns a map where the values are Lists of Strings,
     * rather than just an individual String
     */
    public static Map<String, List<String>> makeFullParameterMap(List<NameValuePair> params) {
        Map<String, List<String>> result = new HashMap<>();
        for (NameValuePair param : params) {
            List<String> values = result.computeIfAbsent(param.getName(), k -> new ArrayList<>(1));
            values.add(param.getValue());
        }
        return result;
    }

    public static HttpResponse makeJsonHttpResponse(int statusCode, String json) {
        HttpResponse stubResponse = new BasicHttpResponse(
                new BasicStatusLine(new ProtocolVersion("1.1", 1, 1), statusCode, "OK")
        );
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(jsonStream);
        stubResponse.setEntity(entity);
        return stubResponse;
    }

    public static void test429(AbstractMethod<?, ?> methodUnderTest) throws Exception {
        try {
            methodUnderTest.parseResponse(TestUtils.makeJsonHttpResponse(429, "Don't know what this is"));
            fail("A 429 response should raise a HttpResponseException");
        } catch (HttpResponseException e) {
            // This is expected
        }
    }

    public static Map<String, String> decodeTokenBody(String jwt) {
        String[] parts = jwt.split("\\.");
        if (parts.length < 2 || parts.length > 3) {
            throw new IllegalArgumentException("Invalid JWT: "+jwt);
        }
        String claims = new String(Base64.getDecoder().decode(parts[1]));
        try {
            return new ObjectMapper().readValue(claims, new TypeReference<LinkedHashMap<String, String>>(){});
        }
        catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Could not decode "+claims, ex);
        }
    }

    public static <T extends JsonableBaseObject> void testJsonableBaseObject(T parsed) {
        testJsonableBaseObject(parsed, false);
    }

    public static <T extends JsonableBaseObject> void testJsonableBaseObject(T parsed, boolean customToString) {
        assertNotNull(parsed);
        String toJson = parsed.toJson(), toString = parsed.toString();
        Class<? extends JsonableBaseObject> clazz = parsed.getClass();
        JsonableBaseObject reparsed = Jsonable.fromJson(toJson, clazz);
        assertEquals(parsed, reparsed);
        assertEquals(parsed.hashCode(), reparsed.hashCode());
        assertEquals(toString, reparsed.toString());
        if (!customToString) {
            assertEquals(toString, clazz.getSimpleName()+' '+toJson);
        }
    }

    public static String mapToJson(Map<String, ?> expected) {
        try {
            return Jsonable.createDefaultObjectMapper().writeValueAsString(expected);
        }
        catch (JsonProcessingException ex) {
            fail(ex);
            return null;
        }
    }

    public static HttpWrapper httpWrapperWithAllAuthMethods() {
        return new HttpWrapper(new NoAuthMethod(),
                new ApiKeyHeaderAuthMethod(API_KEY, API_SECRET),
                new ApiKeyQueryParamsAuthMethod(API_KEY, API_SECRET),
                new SignatureAuthMethod(API_KEY, SIGNATURE_SECRET, HashType.HMAC_SHA256),
                new JWTAuthMethod(APPLICATION_ID_STR, new byte[0])
        );
    }


    static CloseableHttpClient stubHttpClient(int statusCode) throws Exception {
        return stubHttpClient(statusCode, "");
    }

    static CloseableHttpClient stubHttpClient(int statusCode, String content, String... additionalReturns) throws Exception {
        CloseableHttpClient result = mock(CloseableHttpClient.class);

        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        StatusLine sl = mock(StatusLine.class);
        HttpEntity entity = mock(HttpEntity.class);

        when(result.execute(any(HttpUriRequest.class))).thenReturn(response);
        Function<String, InputStream> transformation = c -> new ByteArrayInputStream(c.getBytes(StandardCharsets.UTF_8));
        InputStream[] contentsEncoded = Arrays.stream(additionalReturns).map(transformation).toArray(InputStream[]::new);
        when(entity.getContent()).thenReturn(transformation.apply(content), contentsEncoded);

        if (additionalReturns.length > 0) {
            final int success = 200;
            Integer[] statusCodeReturns = new Integer[additionalReturns.length];
            for (int i = 0; i < statusCodeReturns.length - 1; statusCodeReturns[i++] = success);
            statusCodeReturns[statusCodeReturns.length - 1] = statusCode;
            when(sl.getStatusCode()).thenReturn(success, statusCodeReturns);
        }
        else {
            when(sl.getStatusCode()).thenReturn(statusCode);
        }

        when(sl.getReasonPhrase()).thenReturn(TestUtils.TEST_REASON);
        when(response.getStatusLine()).thenReturn(sl);
        when(response.getEntity()).thenReturn(entity);
        Header locationHeader = mock(Header.class);
        when(response.getFirstHeader("Location")).thenReturn(locationHeader);
        when(locationHeader.getValue()).thenReturn(TestUtils.TEST_REDIRECT_URI);

        return result;
    }

    public static void stubResponse(HttpWrapper wrapper, int code, String response, String... additionalResponses) throws Exception {
        wrapper.setHttpClient(stubHttpClient(code, response, additionalResponses));
    }

    public void stubResponse(HttpWrapper wrapper, int statusCode) throws Exception {
        wrapper.setHttpClient(stubHttpClient(statusCode, ""));
    }
}
