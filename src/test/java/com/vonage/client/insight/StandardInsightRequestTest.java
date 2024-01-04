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
package com.vonage.client.insight;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StandardInsightRequestTest {

    @Test
    public void testWithNumber() {
        StandardInsightRequest request = StandardInsightRequest.withNumber("12345");
        assertEquals("12345", request.getNumber());
        assertNull(request.getCountry());
    }

    @Test
    public void testWithNumberAndCountry() {
        StandardInsightRequest request = StandardInsightRequest.withNumberAndCountry("12345", "GB");
        assertEquals("12345", request.getNumber());
        assertEquals("GB", request.getCountry());
    }

    @Test
    public void testBuildWithoutNumberThrowsException() {
        assertThrows(IllegalStateException.class, () -> StandardInsightRequest.builder().build());
    }

    @Test
    public void testBuildWithNumberAfterConstruction() {
        StandardInsightRequest request = StandardInsightRequest.builder()
                .number("12345").build();
        assertEquals("12345", request.getNumber());
    }

    @Test
    public void testMutateNumberAfterConstruction() {
        String number = "12345";
        StandardInsightRequest.Builder builder = StandardInsightRequest.builder(number);
        assertEquals(number, builder.build().getNumber());
        assertEquals(number = "6789", builder.number(number).build().getNumber());
    }

    @Test
    public void testBuildWithAllFields() {
        StandardInsightRequest request = StandardInsightRequest.builder("12345").country("GB").cnam(true).build();
        assertEquals("12345", request.getNumber());
        assertEquals("GB", request.getCountry());
        assertTrue(request.getCnam());

        request = StandardInsightRequest.builder("12345").number("98765").country("GB").cnam(true).build();
        assertEquals("98765", request.getNumber());
        assertEquals("GB", request.getCountry());
        assertTrue(request.getCnam());
    }
}
