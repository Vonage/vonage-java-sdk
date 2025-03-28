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
package com.vonage.client.insight;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.net.URI;

public class AdvancedAsyncInsightRequestTest {

    @Test
    public void testCallback() {
        AdvancedInsightAsyncRequest request = AdvancedInsightAsyncRequest.builder()
                .number("447700900000").callback("https://example.com").build();

        assertEquals(URI.create("https://example.com"), request.getCallback());
    }

    @Test
    public void testInvalidCallback() {
        assertThrows(IllegalArgumentException.class, () ->
                AdvancedInsightAsyncRequest.builder().number("12345").callback(" ").build()
        );
        assertThrows(IllegalArgumentException.class, () ->
                AdvancedInsightAsyncRequest.builder().number("12345").build()
        );
        assertThrows(IllegalArgumentException.class, () ->
                AdvancedInsightAsyncRequest.builder().number("12345").callback("''~#-+=&6^$£!").build()
        );
    }

    @Test
    public void testBuildWithoutNumberThrowsException() {
        assertThrows(IllegalStateException.class, () -> AdvancedInsightAsyncRequest.builder().build());
    }

    @Test
    public void testBuildWithNumberAfterConstruction() {
        AdvancedInsightAsyncRequest request = AdvancedInsightAsyncRequest.builder()
                .callback("ftp://myserver.tld/cb").number("12345").build();
        assertEquals("12345", request.getNumber());
    }

    @Test
    public void testMutateNumberAfterConstruction() {
        String number = "12345";
        AdvancedInsightAsyncRequest.Builder builder = AdvancedInsightAsyncRequest.builder()
                .number(number).callback("foo");
        assertEquals(number, builder.build().getNumber());
        assertEquals(number = "6789", builder.number(number).build().getNumber());
    }

    @Test
    public void testBuildWithAllFields() {
        AdvancedInsightAsyncRequest request = AdvancedInsightAsyncRequest.builder()
                .country("GB").cnam(true).number("12345")
                .callback("https://example.com/cb").build();

        assertEquals("12345", request.getNumber());
        assertEquals("GB", request.getCountry());
        assertTrue(request.getCnam());
        assertEquals(URI.create("https://example.com/cb"), request.getCallback());
    }
}
