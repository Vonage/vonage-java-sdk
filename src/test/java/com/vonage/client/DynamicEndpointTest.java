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

import com.vonage.client.auth.NoAuthMethod;
import com.vonage.client.common.HttpMethod;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.net.URI;

public class DynamicEndpointTest {

    @Test
    public void testRedirectHandling() throws Exception {
        var wrapper = new HttpWrapper(new NoAuthMethod());
        @SuppressWarnings("unchecked")
        var endpoint = DynamicEndpoint.builder(URI.class)
                .wrapper(wrapper).authMethod(NoAuthMethod.class)
                .pathGetter((de, req) -> TestUtils.TEST_BASE_URI + req)
                .requestMethod(HttpMethod.GET).build();

        String redirectUri = TestUtils.TEST_REDIRECT_URI;
        TestUtils.stubResponse(wrapper, 302, redirectUri);
        assertEquals(URI.create(redirectUri), endpoint.execute("foo"));
    }
}
