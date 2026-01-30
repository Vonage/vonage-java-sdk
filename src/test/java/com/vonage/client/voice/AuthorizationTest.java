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

import com.vonage.client.VonageResponseParseException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class AuthorizationTest {

    @Test
    public void testVonageAuthorization() {
        Authorization auth = Authorization.vonage();
        assertEquals(Authorization.Type.VONAGE, auth.getType());
        assertNull(auth.getValue());
    }

    @Test
    public void testVonageAuthorizationBuilder() {
        Authorization auth = Authorization.builder(Authorization.Type.VONAGE).build();
        assertEquals(Authorization.Type.VONAGE, auth.getType());
        assertNull(auth.getValue());
    }

    @Test
    public void testVonageAuthorizationIgnoresValue() {
        // For type 'vonage', any provided value should be stored but not validated
        Authorization auth = Authorization.builder(Authorization.Type.VONAGE)
                .value("ignored-value")
                .build();
        assertEquals(Authorization.Type.VONAGE, auth.getType());
        assertEquals("ignored-value", auth.getValue());
    }

    @Test
    public void testCustomAuthorization() {
        String customValue = "Bearer abc123xyz";
        Authorization auth = Authorization.custom(customValue);
        assertEquals(Authorization.Type.CUSTOM, auth.getType());
        assertEquals(customValue, auth.getValue());
    }

    @Test
    public void testCustomAuthorizationBuilder() {
        String customValue = "ApiKey X9Z...";
        Authorization auth = Authorization.builder(Authorization.Type.CUSTOM)
                .value(customValue)
                .build();
        assertEquals(Authorization.Type.CUSTOM, auth.getType());
        assertEquals(customValue, auth.getValue());
    }

    @Test
    public void testCustomAuthorizationRequiresValue() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            Authorization.builder(Authorization.Type.CUSTOM).build();
        });
        assertTrue(ex.getMessage().contains("Authorization value is required"));
    }

    @Test
    public void testCustomAuthorizationNullValue() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            Authorization.custom(null);
        });
        assertTrue(ex.getMessage().contains("Authorization value is required"));
    }

    @Test
    public void testTypeRequired() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            Authorization.builder(null).build();
        });
        assertTrue(ex.getMessage().contains("Authorization type is required"));
    }

    @Test
    public void testValueMaxLength() {
        // Create a string with exactly 8191 characters (max allowed)
        String validValue = "x".repeat(8191);
        Authorization auth = Authorization.custom(validValue);
        assertEquals(validValue, auth.getValue());
    }

    @Test
    public void testValueExceedsMaxLength() {
        // Create a string with 8192 characters (one over the limit)
        String tooLongValue = "x".repeat(8192);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            Authorization.custom(tooLongValue);
        });
        assertTrue(ex.getMessage().contains("must not exceed 8191 characters"));
    }

    @Test
    public void testValueValidCharacters() {
        // Test with all printable ASCII characters (0x20 to 0x7E)
        String validValue = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
        Authorization auth = Authorization.custom(validValue);
        assertEquals(validValue, auth.getValue());
    }

    @Test
    public void testValueInvalidCharacters() {
        // Test with tab character (0x09, not in the valid range)
        String invalidValue = "Bearer\tabc123";
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            Authorization.custom(invalidValue);
        });
        assertTrue(ex.getMessage().contains("printable ASCII characters"));
    }

    @Test
    public void testValueInvalidNewline() {
        // Test with newline character
        String invalidValue = "Bearer abc123\n";
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            Authorization.custom(invalidValue);
        });
        assertTrue(ex.getMessage().contains("printable ASCII characters"));
    }

    @Test
    public void testSerializationVonage() throws VonageResponseParseException {
        Authorization auth = Authorization.vonage();
        String json = auth.toJson();
        assertTrue(json.contains("\"type\":\"vonage\""));
        assertFalse(json.contains("\"value\""));
    }

    @Test
    public void testSerializationCustom() throws VonageResponseParseException {
        Authorization auth = Authorization.custom("Bearer token123");
        String json = auth.toJson();
        assertTrue(json.contains("\"type\":\"custom\""));
        assertTrue(json.contains("\"value\":\"Bearer token123\""));
    }

    @Test
    public void testDeserializationVonage() {
        String json = "{\"type\":\"vonage\"}";
        Authorization auth = com.vonage.client.Jsonable.fromJson(json, Authorization.class);
        assertEquals(Authorization.Type.VONAGE, auth.getType());
        assertNull(auth.getValue());
    }

    @Test
    public void testDeserializationCustom() {
        String json = "{\"type\":\"custom\",\"value\":\"Bearer token123\"}";
        Authorization auth = com.vonage.client.Jsonable.fromJson(json, Authorization.class);
        assertEquals(Authorization.Type.CUSTOM, auth.getType());
        assertEquals("Bearer token123", auth.getValue());
    }

    @Test
    public void testTypeToString() {
        assertEquals("vonage", Authorization.Type.VONAGE.toString());
        assertEquals("custom", Authorization.Type.CUSTOM.toString());
    }
}
