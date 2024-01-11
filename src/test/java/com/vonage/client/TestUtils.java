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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TestUtils {

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

    @SafeVarargs
    public static <T extends JsonableBaseObject> void testJsonableBaseObject(String json, T... typeHack) {
        testJsonableBaseObject(Jsonable.fromJson(json, typeHack));
    }

    public static <T extends JsonableBaseObject> void testJsonableBaseObject(T parsed) {
        assertNotNull(parsed);
        String toJson = parsed.toJson();
        Class<? extends JsonableBaseObject> clazz = parsed.getClass();
        JsonableBaseObject reparsed = Jsonable.fromJson(toJson, clazz);
        assertEquals(parsed, reparsed);
        assertEquals(parsed.hashCode(), reparsed.hashCode());
        String expectedToString = clazz.getSimpleName()+' '+toJson;
        assertEquals(expectedToString, parsed.toString());
        assertEquals(expectedToString, reparsed.toString());
    }
}
