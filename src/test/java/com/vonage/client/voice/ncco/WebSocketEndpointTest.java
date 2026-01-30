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
package com.vonage.client.voice.ncco;

import com.vonage.client.users.channels.Websocket;
import com.vonage.client.voice.Authorization;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.Collections;

public class WebSocketEndpointTest {

    @Test
    public void testAllFields() {
        WebSocketEndpoint endpoint = WebSocketEndpoint.builder("wss://example.com", "some-content")
                .uri("wss://example.net")
                .contentType(Websocket.ContentType.AUDIO_L16_8K)
                .headers(Collections.singletonMap("keyOne", Collections.singletonList(1)))
                .build();
        ConnectAction connect = ConnectAction.builder(endpoint).build();

        String expectedJson = "[{\"endpoint\":[{\"uri\":\"wss://example.net\",\"content-type\":\"" +
                "audio/l16;rate=8000\",\"headers\":{\"keyOne\":[1]},\"type\":\"websocket\"}],\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testAuthorizationVonage() {
        WebSocketEndpoint endpoint = WebSocketEndpoint.builder("wss://example.com", "audio/l16;rate=16000")
                .authorization(Authorization.vonage())
                .build();
        ConnectAction connect = ConnectAction.builder(endpoint).build();

        String expectedJson = "[{\"endpoint\":[{\"uri\":\"wss://example.com\",\"content-type\":\"audio/l16;rate=16000\"," +
                "\"authorization\":{\"type\":\"vonage\"},\"type\":\"websocket\"}],\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testAuthorizationCustom() {
        WebSocketEndpoint endpoint = WebSocketEndpoint.builder("wss://example.com", "audio/l16;rate=16000")
                .authorization(Authorization.custom("Bearer eyJhbGciOi..."))
                .build();
        ConnectAction connect = ConnectAction.builder(endpoint).build();

        String expectedJson = "[{\"endpoint\":[{\"uri\":\"wss://example.com\",\"content-type\":\"audio/l16;rate=16000\"," +
                "\"authorization\":{\"type\":\"custom\",\"value\":\"Bearer eyJhbGciOi...\"},\"type\":\"websocket\"}],\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testAllFieldsWithAuthorization() {
        WebSocketEndpoint endpoint = WebSocketEndpoint.builder("wss://example.com", "audio/l16;rate=16000")
                .uri("wss://your-server.example.com")
                .contentType("audio/l16;rate=16000")
                .headers(Collections.singletonMap("custom-header", "value"))
                .authorization(Authorization.custom("Bearer eyJhbGciOi..."))
                .build();
        ConnectAction connect = ConnectAction.builder(endpoint).build();

        String json = new Ncco(connect).toJson();
        assertTrue(json.contains("\"uri\":\"wss://your-server.example.com\""));
        assertTrue(json.contains("\"content-type\":\"audio/l16;rate=16000\""));
        assertTrue(json.contains("\"custom-header\":\"value\""));
        assertTrue(json.contains("\"authorization\":{\"type\":\"custom\",\"value\":\"Bearer eyJhbGciOi...\"}"));
    }

    @Test
    public void testNoAuthorization() {
        WebSocketEndpoint endpoint = WebSocketEndpoint.builder("wss://example.com", "audio/l16;rate=16000")
                .build();
        
        assertNull(endpoint.getAuthorization());
        
        String json = new Ncco(ConnectAction.builder(endpoint).build()).toJson();
        assertFalse(json.contains("\"authorization\""));
    }
}

