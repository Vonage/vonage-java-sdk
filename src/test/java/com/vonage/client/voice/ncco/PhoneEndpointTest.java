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
package com.vonage.client.voice.ncco;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class PhoneEndpointTest {
    @Test
    public void testOnAnswerWithUrlNoRingback() {
        PhoneEndpoint endpoint = PhoneEndpoint.builder("15554441234")
                .number("15554441235")
                .dtmfAnswer("1234")
                .onAnswer("http://example.com")
                .build();
        ConnectAction connect = ConnectAction.builder(endpoint).build();

        String expectedJson = "[{\"endpoint\":[{\"number\":\"15554441235\",\"dtmfAnswer\":\"1234\",\"onAnswer\":{\"url\":\"http://example.com\"},\"type\":\"phone\"}],\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testAllFields() {
        PhoneEndpoint endpoint = PhoneEndpoint.builder("15554441234")
                .number("15554441235")
                .dtmfAnswer("1234")
                .onAnswer("http://example.com", "https://example.com/ringback.mp3")
                .build();
        ConnectAction connect = ConnectAction.builder(endpoint).build();

        String expectedJson = "[{\"endpoint\":[{\"number\":\"15554441235\",\"dtmfAnswer\":\"1234\",\"onAnswer\":{\"url\":\"http://example.com\",\"ringback\":\"https://example.com/ringback.mp3\"},\"type\":\"phone\"}],\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }
}
