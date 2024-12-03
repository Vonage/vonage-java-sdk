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

import static com.vonage.client.TestUtils.API_KEY;
import static com.vonage.client.TestUtils.API_SECRET;
import com.vonage.client.application.ApplicationClient;
import com.vonage.client.auth.ApiKeyHeaderAuthMethod;
import com.vonage.client.auth.AuthCollection;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class HttpWrapperTest {
    private HttpWrapper wrapper;

    @BeforeEach
    public void setUp() {
        wrapper = new HttpWrapper(new AuthCollection());
    }

    @Test
    public void basicTest() {
        assertNotNull(wrapper.getHttpClient());
    }

    @Test
    public void testGetApiKey() {
        assertNull(wrapper.getApiKey());
    }

    @Test
    public void testAuthMethodAccessors() {
        AuthCollection auths = new AuthCollection();
        wrapper.setAuthCollection(auths);
        assertEquals(auths, wrapper.getAuthCollection());
    }

    @Test
    public void testDefaultConstructorSetsDefaultConfigValues() {
        HttpConfig config = wrapper.getHttpConfig();
        assertNotNull(config);
        HttpConfigTest.assertDefaults(config);
    }

    @Test
    public void testDefaultUserAgent() {
        assertNull(wrapper.getHttpConfig().getCustomUserAgent());
        assertEquals(
                "vonage-java-sdk/"+wrapper.getClientVersion()+" java/" + System.getProperty("java.version"),
                wrapper.getUserAgent()
        );
    }

    @Test
    public void testValidCustomUserAgent() {
        String customUa = "my-custom-agent", defaultUa = wrapper.getUserAgent();
        wrapper = new HttpWrapper(HttpConfig.builder().appendUserAgent(customUa).build());
        assertEquals(customUa, wrapper.getHttpConfig().getCustomUserAgent());
        assertEquals(defaultUa + " " + customUa, wrapper.getUserAgent());
    }

    @Test
    public void testProxy() {
        var headerAuth = new ApiKeyHeaderAuthMethod(API_KEY, API_SECRET);
        wrapper = new HttpWrapper(headerAuth);
        assertNull(wrapper.getHttpConfig().getProxy());

        var appClient = new ApplicationClient(wrapper);
        assertThrows(VonageApiResponseException.class, appClient::listApplications);

        wrapper = new HttpWrapper(HttpConfig.builder().proxy("http://localhost:8080").build(), headerAuth);
        var proxyUri = wrapper.getHttpConfig().getProxy();
        assertNotNull(proxyUri);
        assertEquals("localhost", proxyUri.getHost());
        assertEquals(8080, proxyUri.getPort());
        assertEquals("http", proxyUri.getScheme());

        appClient = new ApplicationClient(wrapper);
        assertThrows(VonageMethodFailedException.class, appClient::listApplications);
    }
}
