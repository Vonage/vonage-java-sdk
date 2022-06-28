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

public class StandardInsightRequestTest {

    @Test
    public void testWithNumber() {
        StandardInsightRequest request = StandardInsightRequest.withNumber("12345");
        assertEquals(request.getNumber(), "12345");
        assertNull(request.getCountry());
    }

    @Test
    public void testWithNumberAndCountry() {
        StandardInsightRequest request = StandardInsightRequest.withNumberAndCountry("12345", "GB");
        assertEquals(request.getNumber(), "12345");
        assertEquals(request.getCountry(), "GB");
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildWithoutNumberThrowsException() {
        StandardInsightRequest.builder().build();
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
        assertEquals(request.getNumber(), "12345");
        assertEquals(request.getCountry(), "GB");
        assertTrue(request.getCnam());

        request = StandardInsightRequest.builder("12345").number("98765").country("GB").cnam(true).build();
        assertEquals(request.getNumber(), "98765");
        assertEquals(request.getCountry(), "GB");
        assertTrue(request.getCnam());
    }
}
