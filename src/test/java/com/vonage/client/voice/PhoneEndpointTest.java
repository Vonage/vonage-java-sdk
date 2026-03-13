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
        assertNull(e.getShaken());
    }

    @Test
    public void testConstructorWithShaken() {
        String shakenHeader = "eyJhbGciOiJFUzI1NiIsInBwdCI6InNoYWtlbiIsInR5cCI6InBhc3Nwb3J0IiwieDV1IjoiaHR0cHM6Ly9jZXJ0LmV4YW1wbGUuY29tL3Bhc3Nwb3J0LnBlbSJ9.eyJhdHRlc3QiOiJBIiwiZGVzdCI6eyJ0biI6WyIxMjEyNTU1MTIxMiJdfSwiaWF0IjoxNjk0ODcwNDAwLCJvcmlnIjp7InRuIjoiMTQxNTU1NTEyMzQifSwib3JpZ2lkIjoiMTIzZTQ1NjctZTg5Yi0xMmQzLWE0NTYtNDI2NjE0MTc0MDAwIn0.MEUCIQCrfKeMtvn9I6zXjE2VfGEcdjC2sm5M6cPqBvFyV9XkpQIgLxlvLNmC8DJEKexXZqTZ;info=<https://stir-provider.example.net/cert.cer>;alg=ES256;ppt=\"shaken\"";
        PhoneEndpoint e = new PhoneEndpoint("14155551234", "p*123#", shakenHeader);
        TestUtils.testJsonableBaseObject(e);
        assertEquals("14155551234", e.getNumber());
        assertEquals("p*123#", e.getDtmfAnswer());
        assertEquals(shakenHeader, e.getShaken());
    }

    @Test
    public void testSerializationWithShaken() {
        String shakenHeader = "eyJhbGciOiJFUzI1NiIsInBwdCI6InNoYWtlbiIsInR5cCI6InBhc3Nwb3J0IiwieDV1IjoiaHR0cHM6Ly9jZXJ0LmV4YW1wbGUuY29tL3Bhc3Nwb3J0LnBlbSJ9.eyJhdHRlc3QiOiJBIiwiZGVzdCI6eyJ0biI6WyIxMjEyNTU1MTIxMiJdfSwiaWF0IjoxNjk0ODcwNDAwLCJvcmlnIjp7InRuIjoiMTQxNTU1NTEyMzQifSwib3JpZ2lkIjoiMTIzZTQ1NjctZTg5Yi0xMmQzLWE0NTYtNDI2NjE0MTc0MDAwIn0.MEUCIQCrfKeMtvn9I6zXjE2VfGEcdjC2sm5M6cPqBvFyV9XkpQIgLxlvLNmC8DJEKexXZqTZ;info=<https://stir-provider.example.net/cert.cer>;alg=ES256;ppt=\"shaken\"";
        PhoneEndpoint endpoint = new PhoneEndpoint("14155551234", null, shakenHeader);
        String json = endpoint.toJson();
        assertTrue(json.contains("\"shaken\""), "JSON should contain shaken field");
        
        // Test deserialization
        PhoneEndpoint deserialized = com.vonage.client.Jsonable.fromJson(json, PhoneEndpoint.class);
        assertEquals(endpoint.getNumber(), deserialized.getNumber());
        assertEquals(endpoint.getShaken(), deserialized.getShaken());
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

        e1 = new PhoneEndpoint("number", "dtmf", "shaken1");
        e2 = new PhoneEndpoint("number", "dtmf", "shaken2");
        assertNotEquals(e1, e2, "Endpoints with different shaken values should not be equal");

        e1 = new PhoneEndpoint("number", "dtmf");
        e2 = null;
        assertNotEquals(e2, e1, "An instance is not equal to null");

        e1 = new PhoneEndpoint("number", "dtmf");
        assertNotEquals(new Object(), e1, "An instance is not equal to a different type.");
    }
}