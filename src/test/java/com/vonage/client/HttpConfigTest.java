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

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.net.URI;
import java.util.Map;

public class HttpConfigTest {
    static final String
            EXPECTED_DEFAULT_API_BASE_URI = "https://api.nexmo.com",
            EXPECTED_DEFAULT_REST_BASE_URI = "https://rest.nexmo.com",
            EXPECTED_DEFAULT_API_EU_BASE_URI = "https://api-eu.vonage.com",
            EXPECTED_DEFAULT_VIDEO_BASE_URI = "https://video.api.vonage.com",
            EXAMPLE_BASE_URI = "https://example.com";

    static void assertDefaults(HttpConfig config) {
        assertEquals(60000, config.getTimeoutMillis());
        assertEquals(EXPECTED_DEFAULT_API_BASE_URI, config.getApiBaseUri());
        assertEquals(EXPECTED_DEFAULT_REST_BASE_URI, config.getRestBaseUri());
        assertEquals(EXPECTED_DEFAULT_API_EU_BASE_URI, config.getApiEuBaseUri());
        assertEquals(EXPECTED_DEFAULT_VIDEO_BASE_URI, config.getVideoBaseUri());
        assertEquals(URI.create(EXPECTED_DEFAULT_API_EU_BASE_URI), config.getRegionalBaseUri(ApiRegion.API_EU));
        assertNull(config.getProxy());
        assertNotNull(config.getCustomHeaders());
        assertEquals(0, config.getCustomHeaders().size());
    }

    @Test
    public void testDefaultFactoryMethod() {
        assertDefaults(HttpConfig.defaultConfig());
    }

    @Test
    public void testApiBaseUriOnly() {
        HttpConfig config = HttpConfig.builder().apiBaseUri(EXAMPLE_BASE_URI).build();

        assertEquals(EXAMPLE_BASE_URI, config.getApiBaseUri());
        assertEquals(EXPECTED_DEFAULT_REST_BASE_URI, config.getRestBaseUri());
        assertEquals(EXPECTED_DEFAULT_API_EU_BASE_URI, config.getApiEuBaseUri());
        assertEquals(EXPECTED_DEFAULT_VIDEO_BASE_URI, config.getVideoBaseUri());
    }

    @Test
    public void testApiRestUriOnly() {
        HttpConfig config = HttpConfig.builder().restBaseUri(EXAMPLE_BASE_URI).build();

        assertEquals(EXPECTED_DEFAULT_API_BASE_URI, config.getApiBaseUri());
        assertEquals(EXAMPLE_BASE_URI, config.getRestBaseUri());
        assertEquals(EXPECTED_DEFAULT_API_EU_BASE_URI, config.getApiEuBaseUri());
        assertEquals(EXPECTED_DEFAULT_VIDEO_BASE_URI, config.getVideoBaseUri());
    }

    @Test
    public void testApiEuUriOnly() {
        HttpConfig config = HttpConfig.builder().apiEuBaseUri(EXAMPLE_BASE_URI).build();

        assertEquals(EXAMPLE_BASE_URI, config.getApiEuBaseUri());
        assertEquals(EXPECTED_DEFAULT_API_BASE_URI, config.getApiBaseUri());
        assertEquals(EXPECTED_DEFAULT_REST_BASE_URI, config.getRestBaseUri());
        assertEquals(EXPECTED_DEFAULT_VIDEO_BASE_URI, config.getVideoBaseUri());
    }

    @Test
    public void testVideoUriOnly() {
        HttpConfig config = HttpConfig.builder().videoBaseUri(EXAMPLE_BASE_URI).build();

        assertEquals(EXAMPLE_BASE_URI, config.getVideoBaseUri());
        assertEquals(EXPECTED_DEFAULT_API_BASE_URI, config.getApiBaseUri());
        assertEquals(EXPECTED_DEFAULT_REST_BASE_URI, config.getRestBaseUri());
        assertEquals(EXPECTED_DEFAULT_API_EU_BASE_URI, config.getApiEuBaseUri());
    }

    @Test
    public void testAllBaseUri() {
        HttpConfig config = HttpConfig.builder().baseUri(URI.create(EXAMPLE_BASE_URI + '/')).build();

        assertEquals(EXAMPLE_BASE_URI, config.getApiBaseUri());
        assertEquals(EXAMPLE_BASE_URI, config.getRestBaseUri());
        assertEquals(EXAMPLE_BASE_URI, config.getApiEuBaseUri());
        assertEquals(EXAMPLE_BASE_URI, config.getVideoBaseUri());
        assertEquals(
                URI.create(EXAMPLE_BASE_URI.replace("://", "://api-eu.")),
                config.getRegionalBaseUri(ApiRegion.API_EU)
        );
        assertEquals("https://api-ap.example.com", config.getRegionalBaseUri(ApiRegion.API_AP).toString());

        assertThrows(NullPointerException.class, () ->
                HttpConfig.builder().baseUri((String) null).build()
        );
        assertThrows(IllegalArgumentException.class, () ->
                HttpConfig.builder().baseUri("*&^%$£;@not-a_'u(R)L").build()
        );
    }

    @Test
    public void testApiRegionEnum() {
        for (var region : ApiRegion.values()) {
            var toString = region.toString();
            assertEquals(toString, region.name().toLowerCase().replace('_', '-'));
            assertEquals(region, ApiRegion.fromString(toString));
        }
        assertNull(ApiRegion.fromString(null));
    }

    @Test
    public void testCustomHeaders() {
        var customHeaders = Map.of(
                "Correlation-Id", "1234567890",
                "X-My-Header", "MyValue"
        );
        var configBuilder = HttpConfig.builder();
        customHeaders.forEach(configBuilder::addRequestHeader);
        var config = configBuilder.build();
        assertEquals(customHeaders, config.getCustomHeaders());
    }

    @Test
    public void testCustomUserAgentValidation() {
        assertEquals("Abc123", HttpConfig.builder().appendUserAgent(" Abc123\t\n").build().getCustomUserAgent());
        assertThrows(NullPointerException.class, () ->
                HttpConfig.builder().appendUserAgent(null).build()
        );
        assertThrows(IllegalArgumentException.class, () ->
                HttpConfig.builder().appendUserAgent("   \t\n").build()
        );
        assertThrows(IllegalArgumentException.class, () ->
                HttpConfig.builder().appendUserAgent("d".repeat(128)).build()
        );
    }
}
