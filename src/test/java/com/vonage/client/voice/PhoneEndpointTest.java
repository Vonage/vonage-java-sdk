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
package com.vonage.client.voice;

import com.vonage.client.TestUtils;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;


public class PhoneEndpointTest {

    @Test
    public void testConstructor() {
        PhoneEndpoint e = new PhoneEndpoint("number", "dtmf");
        TestUtils.testJsonableBaseObject(e);
        assertEquals("number", e.getNumber());
        assertEquals("dtmf", e.getDtmfAnswer());
    }

    @Test
    public void testComparison() {

        PhoneEndpoint e1 = new PhoneEndpoint("number");
        PhoneEndpoint e2 = new PhoneEndpoint("number");
        assertEquals(e1, e2, "Endpoints with the same values should be equal");
        assertEquals(e1.hashCode(), e2.hashCode(), "Endpoints with the same values should have the same hash");

        e1 = new PhoneEndpoint("number");
        e2 = new PhoneEndpoint("number1");
        assertNotEquals(e1, e2, "Endpoints with different numbers should not be equal");
        assertNotEquals(e1.hashCode(), e2.hashCode(), "Endpoints with different numbers should have different hashes");

        e1 = new PhoneEndpoint("number", "dtmf");
        e2 = new PhoneEndpoint("number", "dtmf1");
        assertNotEquals(e1, e2, "Endpoints with different dtmfAnswers should not be equal");

        e1 = new PhoneEndpoint("number", "dtmf");
        e2 = null;
        assertNotEquals(e2, e1, "An instance is not equal to null");

        e1 = new PhoneEndpoint("number", "dtmf");
        assertNotEquals(new Object(), e1, "An instance is not equal to a different type.");
    }
}