/*
 *   Copyright 2023 Vonage
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;


public class PhoneEndpointTest {

    @Test
    public void testConstructor() throws Exception {
        PhoneEndpoint e = new PhoneEndpoint("number", "dtmf");
        assertEquals("number", e.getNumber());
        assertEquals("dtmf", e.getDtmfAnswer());
    }

    @Test
    public void testSetNumber() throws Exception {
        PhoneEndpoint e = new PhoneEndpoint("number", "dtmf");
        e.setNumber("1234");
        assertEquals("1234", e.getNumber());
    }

    @Test
    public void testSetDtmf() throws Exception {
        PhoneEndpoint e = new PhoneEndpoint("number", "dtmf");
        e.setDtmfAnswer("#123");
        assertEquals("#123", e.getDtmfAnswer());
    }

    @Test
    public void testComparison() throws Exception {

        PhoneEndpoint e1 = new PhoneEndpoint("number");
        PhoneEndpoint e2 = new PhoneEndpoint("number");
        assertEquals("Endpoints with the same values should be equal", e1, e2);
        assertEquals("Endpoints with the same values should have the same hash", e1.hashCode(), e2.hashCode());

        e1 = new PhoneEndpoint("number");
        e2 = new PhoneEndpoint("number1");
        assertNotEquals("Endpoints with different numbers should not be equal", e1, e2);
        assertNotEquals("Endpoints with different numbers should have different hashes", e1.hashCode(), e2.hashCode());

        e1 = new PhoneEndpoint("number", "dtmf");
        e2 = new PhoneEndpoint("number", "dtmf1");
        assertNotEquals("Endpoints with different dtmfAnswers should not be equal", e1, e2);

        e1 = new PhoneEndpoint("number", "dtmf");
        e2 = null;
        assertNotEquals("An instance is not equal to null", e1, e2);

        e1 = new PhoneEndpoint("number", "dtmf");
        assertNotEquals("An instance is not equal to a different type.", e1, new Object());
    }
}