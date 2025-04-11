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
import static com.vonage.client.TestUtils.*;
import com.vonage.client.auth.AuthMethod;
import com.vonage.client.auth.NoAuthMethod;
import com.vonage.client.common.HttpMethod;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.net.URI;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DynamicEndpointTest {
    private static final HttpWrapper WRAPPER = new HttpWrapper(new NoAuthMethod());

    static class SampleJsonable extends JsonableBaseObject {
        @JsonProperty("field") String field;
    }

    static class SampleJsonableCollection extends ArrayList<SampleJsonable> { }

    @SuppressWarnings("unchecked")
    static <T, R> DynamicEndpoint<T, R> newEndpoint(R... responseType) {
        var endpoint = DynamicEndpoint.<T, R> builder(responseType)
                .wrapper(WRAPPER).authMethod(NoAuthMethod.class, (Class<? extends AuthMethod>[]) null)
                .pathGetter((de, req) -> TEST_BASE_URI)
                .requestMethod(HttpMethod.GET).acceptHeader("text").build();
        Logger.getLogger(endpoint.getClass().getName()).setLevel(Level.FINE);
        return endpoint;
    }


    @Test
    public void testUntypedEndpoint() {
        assertThrows(IllegalStateException.class, DynamicEndpointTest::newEndpoint);
        assertThrows(IllegalStateException.class, () -> newEndpoint((Object) null));
        assertThrows(NullPointerException.class, () -> newEndpoint((Object[]) null));
    }

    @Test
    public void testRedirectHandling() throws Exception {
        DynamicEndpoint<byte[], URI> uriEndpoint = newEndpoint();
        stubResponse(WRAPPER, 300, TEST_REDIRECT_URI);
        assertEquals(URI.create(TEST_REDIRECT_URI), uriEndpoint.execute(new byte[0]));

        DynamicEndpoint<Object, String> stringEndpoint = newEndpoint();
        stubResponse(WRAPPER, 302, TEST_REDIRECT_URI);
        assertEquals(TEST_REDIRECT_URI, stringEndpoint.execute("bar"));

        DynamicEndpoint<String, DynamicEndpointTest> unsupportedReturnEndpoint = newEndpoint();
        stubResponse(WRAPPER, 302, TEST_REDIRECT_URI);
        assertThrows(IllegalStateException.class, () -> unsupportedReturnEndpoint.execute("foo"));
    }

    @Test
    public void testInformationalResponse() throws Exception {
        DynamicEndpoint<byte[], Void> endpoint = newEndpoint();
        stubResponse(WRAPPER, 100, "Continue");
        assertNull(endpoint.execute(new byte[0]));
    }

    @Test
    public void testBinaryRequest() throws Exception {
        class BinaryRequest implements com.vonage.client.BinaryRequest {
            @Override
            public byte[] toByteArray() {
                return new byte[] { 0, 1, 2, 3, 4, 5 };
            }
        }

        var endpoint = DynamicEndpointTest.<BinaryRequest, String> newEndpoint();
        stubResponse(WRAPPER, 200, "OK");
        assertEquals("OK", endpoint.execute(new BinaryRequest()));
    }

    @Test
    public void testParseJsonableCollection() throws Exception {
        var endpoint = DynamicEndpointTest.<Void, SampleJsonableCollection> newEndpoint();
        stubResponse(WRAPPER, 200, "[{\"field\":\"Test value\"}]");
        var response = endpoint.execute(null);
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Test value", response.getFirst().field);
    }

    @Test
    public void testIterableQueryParamsRequest() throws Exception {
        class IterableQueryParamsRequest implements QueryParamsRequest {
            @Override
            public Map<String, Collection<?>> makeParams() {
                return Map.of(
                        "Foo", Set.of("Bar", "Baz"),
                        "Qux", List.of(1, 2, 3, 5, 4, 6)
                );
            }
        }

        var endpoint = DynamicEndpointTest.<IterableQueryParamsRequest, String> newEndpoint();
        stubResponse(WRAPPER, 200, "OK");
        assertEquals("OK", endpoint.execute(new IterableQueryParamsRequest()));
    }

    @Test
    public void testCustomParsedResponse() throws Exception {
        record CustomData(Object field) { }

        @SuppressWarnings("unchecked")
        class CustomEndpoint extends DynamicEndpoint<Object, CustomData> {
            CustomEndpoint() {
                super(DynamicEndpoint.builder(CustomData.class)
                        .wrapper(WRAPPER).authMethod(NoAuthMethod.class)
                        .pathGetter((de, req) -> TEST_BASE_URI + req)
                        .requestMethod(HttpMethod.PUT)
                );
            }

            @Override
            protected CustomData parseResponseFromString(String response) {
                return new CustomData("Custom response: "+response);
            }
        }

        var responseText = "Some content";
        var customEndpoint = new CustomEndpoint();
        stubResponse(WRAPPER, 203, responseText);
        var expectedResponse = new CustomData("Custom response: "+responseText);
        assertEquals(expectedResponse, customEndpoint.execute("FooBarBaz"));

        stubResponse(WRAPPER, 406, responseText);
        assertEquals(expectedResponse, customEndpoint.execute("FooBarBaz"));
    }
}
