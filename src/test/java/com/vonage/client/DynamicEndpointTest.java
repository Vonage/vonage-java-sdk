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

import static com.vonage.client.TestUtils.*;
import com.vonage.client.auth.NoAuthMethod;
import com.vonage.client.common.HttpMethod;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.net.URI;

public class DynamicEndpointTest {
    private static final HttpWrapper WRAPPER = new HttpWrapper(new NoAuthMethod());

    @SuppressWarnings("unchecked")
    static <T, R> DynamicEndpoint<T, R> newEndpoint(R... responseType) {
        return DynamicEndpoint.<T, R> builder(responseType)
                .wrapper(WRAPPER).authMethod(NoAuthMethod.class)
                .pathGetter((de, req) -> TEST_BASE_URI)
                .requestMethod(HttpMethod.GET).acceptHeader("text").build();
    }

    @Test
    public void testRedirectHandling() throws Exception {
        DynamicEndpoint<byte[], URI> uriEndpoint = newEndpoint();
        stubResponse(WRAPPER, 302, TEST_REDIRECT_URI);
        assertEquals(URI.create(TEST_REDIRECT_URI), uriEndpoint.execute(new byte[0]));

        DynamicEndpoint<Object, String> stringEndpoint = newEndpoint();
        stubResponse(WRAPPER, 302, TEST_REDIRECT_URI);
        assertEquals(TEST_REDIRECT_URI, stringEndpoint.execute("bar"));

        DynamicEndpoint<String, Object> objectEndpoint = newEndpoint();
        stubResponse(WRAPPER, 302, TEST_REDIRECT_URI);
        assertThrows(IllegalStateException.class, () -> objectEndpoint.execute("foo"));
    }

    @Test
    public void testUnknownRequestMethod() throws Exception {
        @SuppressWarnings("unchecked")
        var endpoint = DynamicEndpoint.builder(Void.class)
                .wrapper(WRAPPER).authMethod(NoAuthMethod.class)
                .pathGetter((de, req) -> TEST_BASE_URI + req)
                .requestMethod(HttpMethod.UNKNOWN).build();

        assertThrows(IllegalStateException.class, () -> endpoint.execute("rest"));
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
