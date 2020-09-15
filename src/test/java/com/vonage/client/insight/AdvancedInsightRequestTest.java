/*
 *   Copyright 2020 Vonage
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
package com.vonage.client.insight;

import org.junit.Test;

import static org.junit.Assert.*;

public class AdvancedInsightRequestTest {
    @Test
    public void testWithNumber() {
        AdvancedInsightRequest request = AdvancedInsightRequest.withNumber("12345");
        assertEquals(request.getNumber(), "12345");
        assertNull(request.getCountry());
    }

    @Test
    public void testWithNumberAndCountry() {
        AdvancedInsightRequest request = AdvancedInsightRequest.withNumberAndCountry("12345", "GB");
        assertEquals(request.getNumber(), "12345");
        assertEquals(request.getCountry(), "GB");
    }

    @Test
    public void testAsync() {
        AdvancedInsightRequest request = AdvancedInsightRequest.builder("12345")
                .async(true)
                .callback("https://example.com")
                .build();

        assertTrue(request.isAsync());
        assertEquals("https://example.com", request.getCallback());
    }

    @Test(expected = IllegalStateException.class)
    public void testAsyncWithoutCallbackThrowsIllegalStateException() {
        AdvancedInsightRequest request = AdvancedInsightRequest.builder("12345")
                .async(true)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testAsyncWithBlankCallbackThrowsIllegalStateException() {
        AdvancedInsightRequest request = AdvancedInsightRequest.builder("12345")
                .async(true)
                .callback("")
                .build();
    }

    @Test
    public void testBuildWithAllFields() throws Exception {
        AdvancedInsightRequest request = AdvancedInsightRequest.builder("12345")
                .country("GB")
                .ipAddress("123.123.123.123")
                .cnam(true)
                .async(true)
                .callback("https://example.com")
                .build();
        assertEquals(request.getNumber(), "12345");
        assertEquals(request.getCountry(), "GB");
        assertEquals(request.getIpAddress(), "123.123.123.123");
        assertTrue(request.getCnam());

        request = AdvancedInsightRequest.builder("12345")
                .number("98765")
                .country("GB")
                .ipAddress("123.123.123.123")
                .cnam(true)
                .async(true)
                .callback("https://example.com")
                .build();
        assertEquals(request.getNumber(), "98765");
        assertEquals(request.getCountry(), "GB");
        assertEquals(request.getIpAddress(), "123.123.123.123");
        assertTrue(request.getCnam());
        assertTrue(request.isAsync());
        assertEquals("https://example.com", request.getCallback());
    }
}
