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

import com.vonage.client.users.channels.Websocket;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class WebSocketEndpointTest {

    @Test
    public void testBasicConstructor() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("custom-header", "value");
        
        WebSocketEndpoint endpoint = new WebSocketEndpoint(
            "wss://example.com",
            "audio/l16;rate=16000",
            headers
        );
        
        assertEquals(URI.create("wss://example.com"), endpoint.getUri());
        assertEquals(Websocket.ContentType.AUDIO_L16_16K, endpoint.getContentType());
        assertEquals(headers, endpoint.getHeadersMap());
        assertNull(endpoint.getAuthorization());
    }

    @Test
    public void testConstructorWithURI() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("custom-header", "value");
        
        WebSocketEndpoint endpoint = new WebSocketEndpoint(
            URI.create("wss://example.com"),
            Websocket.ContentType.AUDIO_L16_8K,
            headers
        );
        
        assertEquals(URI.create("wss://example.com"), endpoint.getUri());
        assertEquals(Websocket.ContentType.AUDIO_L16_8K, endpoint.getContentType());
        assertEquals(headers, endpoint.getHeadersMap());
        assertNull(endpoint.getAuthorization());
    }

    @Test
    public void testConstructorWithAuthorizationVonage() {
        Map<String, Object> headers = new HashMap<>();
        Authorization auth = Authorization.vonage();
        
        WebSocketEndpoint endpoint = new WebSocketEndpoint(
            URI.create("wss://example.com"),
            Websocket.ContentType.AUDIO_L16_16K,
            headers,
            auth
        );
        
        assertEquals(URI.create("wss://example.com"), endpoint.getUri());
        assertNotNull(endpoint.getAuthorization());
        assertEquals(Authorization.Type.VONAGE, endpoint.getAuthorization().getType());
    }

    @Test
    public void testConstructorWithAuthorizationCustom() {
        Map<String, Object> headers = new HashMap<>();
        Authorization auth = Authorization.custom("Bearer token123");
        
        WebSocketEndpoint endpoint = new WebSocketEndpoint(
            URI.create("wss://example.com"),
            Websocket.ContentType.AUDIO_L16_16K,
            headers,
            auth
        );
        
        assertEquals(URI.create("wss://example.com"), endpoint.getUri());
        assertNotNull(endpoint.getAuthorization());
        assertEquals(Authorization.Type.CUSTOM, endpoint.getAuthorization().getType());
        assertEquals("Bearer token123", endpoint.getAuthorization().getValue());
    }

    @Test
    public void testStringConstructorWithAuthorizationVonage() {
        Map<String, Object> headers = new HashMap<>();
        Authorization auth = Authorization.vonage();
        
        WebSocketEndpoint endpoint = new WebSocketEndpoint(
            "wss://example.com",
            "audio/l16;rate=16000",
            headers,
            auth
        );
        
        assertEquals(URI.create("wss://example.com"), endpoint.getUri());
        assertEquals(Websocket.ContentType.AUDIO_L16_16K, endpoint.getContentType());
        assertNotNull(endpoint.getAuthorization());
        assertEquals(Authorization.Type.VONAGE, endpoint.getAuthorization().getType());
    }

    @Test
    public void testStringConstructorWithAuthorizationCustom() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("custom-header", "value");
        Authorization auth = Authorization.custom("Bearer abc123");
        
        WebSocketEndpoint endpoint = new WebSocketEndpoint(
            "wss://your-server.example.com",
            "audio/l16;rate=8000",
            headers,
            auth
        );
        
        assertEquals(URI.create("wss://your-server.example.com"), endpoint.getUri());
        assertEquals(Websocket.ContentType.AUDIO_L16_8K, endpoint.getContentType());
        assertEquals("value", endpoint.getHeadersMap().get("custom-header"));
        assertNotNull(endpoint.getAuthorization());
        assertEquals(Authorization.Type.CUSTOM, endpoint.getAuthorization().getType());
        assertEquals("Bearer abc123", endpoint.getAuthorization().getValue());
    }

    @Test
    public void testSerializationWithVonageAuthorization() {
        Authorization auth = Authorization.vonage();
        WebSocketEndpoint endpoint = new WebSocketEndpoint(
            URI.create("wss://example.com"),
            Websocket.ContentType.AUDIO_L16_16K,
            null,
            auth
        );
        
        String json = endpoint.toJson();
        assertTrue(json.contains("\"type\":\"websocket\""));
        assertTrue(json.contains("\"uri\":\"wss://example.com\""));
        assertTrue(json.contains("\"authorization\":{\"type\":\"vonage\"}"));
    }

    @Test
    public void testSerializationWithCustomAuthorization() {
        Authorization auth = Authorization.custom("Bearer abc123");
        Map<String, Object> headers = new HashMap<>();
        headers.put("custom-header", "value");
        
        WebSocketEndpoint endpoint = new WebSocketEndpoint(
            URI.create("wss://your-server.example.com"),
            Websocket.ContentType.AUDIO_L16_16K,
            headers,
            auth
        );
        
        String json = endpoint.toJson();
        assertTrue(json.contains("\"type\":\"websocket\""));
        assertTrue(json.contains("\"uri\":\"wss://your-server.example.com\""));
        assertTrue(json.contains("\"content-type\":\"audio/l16;rate=16000\""));
        assertTrue(json.contains("\"authorization\":{\"type\":\"custom\",\"value\":\"Bearer abc123\"}"));
        assertTrue(json.contains("\"custom-header\":\"value\""));
    }

    @Test
    public void testSerializationWithoutAuthorization() {
        WebSocketEndpoint endpoint = new WebSocketEndpoint(
            URI.create("wss://example.com"),
            Websocket.ContentType.AUDIO_L16_16K,
            null
        );
        
        String json = endpoint.toJson();
        assertTrue(json.contains("\"type\":\"websocket\""));
        assertTrue(json.contains("\"uri\":\"wss://example.com\""));
        assertFalse(json.contains("\"authorization\""));
    }

    @Test
    public void testDeserializationWithVonageAuthorization() {
        String json = "{\"type\":\"websocket\",\"uri\":\"wss://example.com\"," +
                      "\"content-type\":\"audio/l16;rate=16000\"," +
                      "\"authorization\":{\"type\":\"vonage\"}}";
        
        WebSocketEndpoint endpoint = com.vonage.client.Jsonable.fromJson(json, WebSocketEndpoint.class);
        
        assertEquals(URI.create("wss://example.com"), endpoint.getUri());
        assertEquals(Websocket.ContentType.AUDIO_L16_16K, endpoint.getContentType());
        assertNotNull(endpoint.getAuthorization());
        assertEquals(Authorization.Type.VONAGE, endpoint.getAuthorization().getType());
    }

    @Test
    public void testDeserializationWithCustomAuthorization() {
        String json = "{\"type\":\"websocket\",\"uri\":\"wss://example.com\"," +
                      "\"content-type\":\"audio/l16;rate=16000\"," +
                      "\"headers\":{\"custom-header\":\"value\"}," +
                      "\"authorization\":{\"type\":\"custom\",\"value\":\"Bearer token123\"}}";
        
        WebSocketEndpoint endpoint = com.vonage.client.Jsonable.fromJson(json, WebSocketEndpoint.class);
        
        assertEquals(URI.create("wss://example.com"), endpoint.getUri());
        assertEquals(Websocket.ContentType.AUDIO_L16_16K, endpoint.getContentType());
        assertNotNull(endpoint.getAuthorization());
        assertEquals(Authorization.Type.CUSTOM, endpoint.getAuthorization().getType());
        assertEquals("Bearer token123", endpoint.getAuthorization().getValue());
        assertEquals("value", endpoint.getHeadersMap().get("custom-header"));
    }

    @Test
    public void testDeserializationWithoutAuthorization() {
        String json = "{\"type\":\"websocket\",\"uri\":\"wss://example.com\"," +
                      "\"content-type\":\"audio/l16;rate=16000\"}";
        
        WebSocketEndpoint endpoint = com.vonage.client.Jsonable.fromJson(json, WebSocketEndpoint.class);
        
        assertEquals(URI.create("wss://example.com"), endpoint.getUri());
        assertEquals(Websocket.ContentType.AUDIO_L16_16K, endpoint.getContentType());
        assertNull(endpoint.getAuthorization());
    }

    @Test
    public void testEndpointType() {
        WebSocketEndpoint endpoint = new WebSocketEndpoint(
            URI.create("wss://example.com"),
            Websocket.ContentType.AUDIO_L16_16K,
            null
        );
        
        assertEquals(EndpointType.WEBSOCKET, endpoint.getType());
    }
}
