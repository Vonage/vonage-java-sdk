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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.common.HttpMethod;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class CustomClientTest extends AbstractClientTest<CustomClient> {
    private static final String URL = "https://api.example.com/endpoint";
    private static final Map<String, ?> PAYLOAD_MAP = Map.of(
            "Key 1", "value1",
            "K2", 2,
            "Key3", true,
            "4th_Key", Map.of(
                    "Nested Key 1", "nestedValue1",
                    "Nested Key 2", List.of("nestedLevel2Val1", 3.1459),
                    "NK3", false,
                    "Nested Key 4", Map.of(
                            "Deeply Nested Key 1", "deeplyNestedValue1",
                            "Deeply Nested Key 2", Map.of("singleton", List.of()),
                            "Deeply Nested Key 3", 0.000000001
                    )
            ),
            "k5", List.of(Map.of("k5l1", List.of(7, 8, 9)))
    );
    private static final String PAYLOAD_STR = new OrderedMap(PAYLOAD_MAP.entrySet().toArray(Map.Entry[]::new)).toJson();
    private static final TestResponse TEST_JSONABLE = Jsonable.fromJson(PAYLOAD_STR);

    static class TestResponse extends JsonableBaseObject {
        private String key1;
        private Integer k2;
        private Boolean key3;
        private Map<String, Object> nestedKey4;

        @JsonProperty("Key 1")
        public String getKey1() {
            return key1;
        }

        @JsonProperty("K2")
        public Integer getK2() {
            return k2;
        }

        @JsonProperty("Key3")
        public Boolean isKey3() {
            return key3;
        }

        @JsonProperty("4th_Key")
        public Map<String, ?> getNestedKey4() {
            return nestedKey4;
        }
    }

    public CustomClientTest() {
        client = new CustomClient(wrapper);
    }

    @Test
    public void testDelete() throws Exception {
        stubResponse(204);
        client.delete(URL);
        assertNull(client.delete(URL, (Object) null));
        assertThrows(NullPointerException.class, () -> client.delete(URL, (Object[]) null));
        stubResponse(204, PAYLOAD_STR);
        TestResponse responseBody = client.delete(URL);
        assertEquals(TEST_JSONABLE, responseBody);
        stubResponse(401);
        assertThrows(VonageApiResponseException.class, () -> client.delete(URL));
    }

    @Test
    public void testGet() throws Exception {
        stubResponse(PAYLOAD_STR);
        TestResponse responseBody = client.get(URL);
        assertEquals(TEST_JSONABLE, responseBody);
        stubResponse(PAYLOAD_STR);
        Map<String, ?> responseBodyMap = client.get(URL);
        assertEquals(PAYLOAD_MAP, responseBodyMap);
        stubResponse(404);
        assertThrows(VonageApiResponseException.class, () -> client.get(URL));
    }

    @Test
    public void testPost() throws Exception {
        stubResponse(PAYLOAD_STR);
        TestResponse responseBody = client.post(URL, (Jsonable) new OrderedMap());
        assertEquals(TEST_JSONABLE, responseBody);
        stubResponse(PAYLOAD_STR);
        Map<String, ?> responseBodyMap = client.post(URL, (Map<String, ?>) new OrderedMap());
        assertEquals(PAYLOAD_MAP, responseBodyMap);
        stubResponse(201);
        client.post(URL, TEST_JSONABLE);
        assertNull(client.post(URL, TEST_JSONABLE, (Object) null));
        assertThrows(NullPointerException.class, () -> client.post(URL, TEST_JSONABLE, (Object[]) null));
        stubResponse(PAYLOAD_STR);
        responseBody = client.post(URL, (Jsonable) null);
        assertEquals(TEST_JSONABLE, responseBody);
        stubResponse(400);
        assertThrows(VonageApiResponseException.class, () -> client.post(URL, TEST_JSONABLE));
    }

    @Test
    public void testPut() throws Exception {
        stubResponse(PAYLOAD_STR);
        TestResponse responseBody = client.put(URL, (Map<String, ?>) new OrderedMap());
        assertEquals(TEST_JSONABLE, responseBody);
        stubResponse(PAYLOAD_STR);
        Map<String, ?> responseBodyMap = client.put(URL, (Jsonable) new OrderedMap());
        assertEquals(PAYLOAD_MAP, responseBodyMap);
        stubResponse(202);
        client.put(URL, TEST_JSONABLE);
        assertNull(client.put(URL, TEST_JSONABLE, (Object) null));
        assertThrows(NullPointerException.class, () -> client.put(URL, TEST_JSONABLE, (Object[]) null));
        stubResponse(409);
        assertThrows(VonageApiResponseException.class, () -> client.put(URL, TEST_JSONABLE));
    }

    @Test
    public void testPatch() throws Exception {
        stubResponse(PAYLOAD_STR);
        TestResponse responseBody = client.patch(URL, (Jsonable) new OrderedMap());
        assertEquals(TEST_JSONABLE, responseBody);
        stubResponse(PAYLOAD_STR);
        Map<String, ?> responseBodyMap = client.patch(URL, (Map<String, ?>) new OrderedMap());
        assertEquals(PAYLOAD_MAP, responseBodyMap);
        stubResponse(204);
        client.patch(URL, TEST_JSONABLE);
        assertNull(client.patch(URL, TEST_JSONABLE, (Object) null));
        assertThrows(NullPointerException.class, () -> client.patch(URL, TEST_JSONABLE, (Object[]) null));
        stubResponse(406);
        assertThrows(VonageApiResponseException.class, () -> client.patch(URL, TEST_JSONABLE));
    }

    @Test
    public void testVarResponse() throws Exception {
        stubResponse(PAYLOAD_STR);
        var var = client.makeRequest(HttpMethod.GET, URL, TEST_JSONABLE);
        assertNull(var);
    }

    @Test
    public void testUnassignedResponse() throws Exception {
        stubResponse(PAYLOAD_STR);
        client.makeRequest(HttpMethod.GET, URL, TEST_JSONABLE);
    }

    @Test
    public void testUnknownObjectResponse() throws Exception {
        stubResponse(PAYLOAD_STR);
        class MyClass {}
        MyClass object = client.makeRequest(HttpMethod.GET, URL, TEST_JSONABLE);
        assertNull(object);
    }

    @Test
    public void testByteArrayResponse() throws Exception {
        stubResponse(PAYLOAD_STR);
        byte[] binary = client.makeRequest(HttpMethod.GET, URL, TEST_JSONABLE);
        assertNotNull(binary);
        assertEquals(PAYLOAD_STR, new String(binary));
    }

    @Test
    public void testCollectionResponse() throws Exception {
        stubResponse("[ " + PAYLOAD_STR + ", {}]");
        List<Map<String, ?>> list = client.makeRequest(HttpMethod.GET, URL, TEST_JSONABLE);
        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals(PAYLOAD_MAP, list.getFirst());
        assertEquals(Map.of(), list.get(1));
    }

    @Test
    public void testStringResponse() throws Exception {
        stubResponse(PAYLOAD_STR);
        String str = client.makeRequest(HttpMethod.GET, URL, TEST_JSONABLE);
        assertEquals(PAYLOAD_STR, str);
    }
}
