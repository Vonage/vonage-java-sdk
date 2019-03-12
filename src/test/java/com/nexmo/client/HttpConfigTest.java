/*
 * Copyright (c) 2011-2018 Nexmo Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nexmo.client;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HttpConfigTest {
    private static final String EXPECTED_DEFAULT_API_BASE_URI = "https://api.nexmo.com";
    private static final String EXPECTED_DEFAULT_REST_BASE_URI = "https://rest.nexmo.com";
    private static final String EXPECTED_DEFAULT_SNS_BASE_URI = "https://sns.nexmo.com";

    @Test
    public void testDefaultFactoryMethod() {
        HttpConfig config = HttpConfig.defaultConfig();

        assertEquals(EXPECTED_DEFAULT_API_BASE_URI, config.getApiBaseUri());
        assertEquals(EXPECTED_DEFAULT_REST_BASE_URI, config.getRestBaseUri());
        assertEquals(EXPECTED_DEFAULT_SNS_BASE_URI, config.getSnsBaseUri());
    }

    @Test
    public void testApiBaseUriOnly() {
        HttpConfig config = HttpConfig.builder().apiBaseUri("https://example.com").build();

        assertEquals("https://example.com", config.getApiBaseUri());
        assertEquals(EXPECTED_DEFAULT_REST_BASE_URI, config.getRestBaseUri());
        assertEquals(EXPECTED_DEFAULT_SNS_BASE_URI, config.getSnsBaseUri());
    }

    @Test
    public void testApiRestUriOnly() {
        HttpConfig config = HttpConfig.builder().restBaseUri("https://example.com").build();

        assertEquals(EXPECTED_DEFAULT_API_BASE_URI, config.getApiBaseUri());
        assertEquals("https://example.com", config.getRestBaseUri());
        assertEquals(EXPECTED_DEFAULT_SNS_BASE_URI, config.getSnsBaseUri());
    }

    @Test
    public void testSnsBaseUriOnly() {
        HttpConfig config = HttpConfig.builder().snsBaseUri("https://example.com").build();

        assertEquals(EXPECTED_DEFAULT_API_BASE_URI, config.getApiBaseUri());
        assertEquals(EXPECTED_DEFAULT_REST_BASE_URI, config.getRestBaseUri());
        assertEquals("https://example.com", config.getSnsBaseUri());
    }

    @Test
    public void testAllBaseUri() {
        HttpConfig config = HttpConfig.builder().baseUri("https://example.com").build();

        assertEquals("https://example.com", config.getApiBaseUri());
        assertEquals("https://example.com", config.getRestBaseUri());
        assertEquals("https://example.com", config.getSnsBaseUri());
    }
}
