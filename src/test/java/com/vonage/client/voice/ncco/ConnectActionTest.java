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

import com.vonage.client.voice.MachineDetection;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class ConnectActionTest {
    @Test
    public void testBuilderMultipleInstances() {
        ConnectAction.Builder builder = ConnectAction.builder(PhoneEndpoint.builder("15554441234").build());
        assertNotSame(builder.build(), builder.build());
    }

    @Test
    public void testAllFieldsWithPhoneEndpoint() {
        PhoneEndpoint endpoint = PhoneEndpoint.builder("15554441234").build();
        ConnectAction connect = ConnectAction.builder(endpoint)
                .from("15554449876")
                .eventType(EventType.SYNCHRONOUS)
                .timeOut(3)
                .limit(2)
                .machineDetection(MachineDetection.CONTINUE)
                .eventUrl("https://example.com")
                .eventMethod(EventMethod.POST)
                .build();

        String expectedJson = "[{\"endpoint\":[{\"number\":\"15554441234\",\"type\":\"phone\"}],\"from\":\"15554449876\",\"eventType\":\"synchronous\",\"limit\":2,\"machineDetection\":\"continue\",\"eventUrl\":[\"https://example.com\"],\"eventMethod\":\"POST\",\"action\":\"connect\",\"timeout\":3}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testAllFieldsWithWebSocketEndpoint() {
        WebSocketEndpoint endpoint = WebSocketEndpoint.builder("wss://example.com", "content-type").build();
        ConnectAction connect = ConnectAction.builder(endpoint)
                .from("15554449876")
                .eventType(EventType.SYNCHRONOUS)
                .timeOut(3)
                .limit(2)
                .machineDetection(MachineDetection.CONTINUE)
                .eventUrl("https://example.com")
                .eventMethod(EventMethod.POST)
                .build();

        String expectedJson = "[{\"endpoint\":[{\"uri\":\"wss://example.com\",\"type\":\"websocket\",\"content-type\":\"content-type\"}],\"from\":\"15554449876\",\"eventType\":\"synchronous\",\"limit\":2,\"machineDetection\":\"continue\",\"eventUrl\":[\"https://example.com\"],\"eventMethod\":\"POST\",\"action\":\"connect\",\"timeout\":3}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testAllFieldsWithSipEndpoint() {
        SipEndpoint endpoint = SipEndpoint.builder("sip:test@sip.example.com").build();
        ConnectAction connect = ConnectAction.builder(endpoint)
                .from("15554449876")
                .eventType(EventType.SYNCHRONOUS)
                .timeOut(3)
                .limit(2)
                .machineDetection(MachineDetection.CONTINUE)
                .eventUrl("https://example.com")
                .eventMethod(EventMethod.POST)
                .build();

        String expectedJson = "[{\"endpoint\":[{\"uri\":\"sip:test@sip.example.com\",\"type\":\"sip\"}],\"from\":\"15554449876\",\"eventType\":\"synchronous\",\"limit\":2,\"machineDetection\":\"continue\",\"eventUrl\":[\"https://example.com\"],\"eventMethod\":\"POST\",\"action\":\"connect\",\"timeout\":3}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testGetAction() {
        ConnectAction connect = ConnectAction.builder(PhoneEndpoint.builder("15554441234").build()).build();
        assertEquals("connect", connect.getAction());
    }

    @Test
    public void testDefaultWithPhone() {
        PhoneEndpoint endpoint = PhoneEndpoint.builder("15554441234").build();
        ConnectAction connect = ConnectAction.builder(endpoint).build();

        String expectedJson = "[{\"endpoint\":[{\"number\":\"15554441234\",\"type\":\"phone\"}],\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testDefaultWithWebSocket() {
        WebSocketEndpoint endpoint = WebSocketEndpoint.builder("wss://example.com", "content-type").build();
        ConnectAction connect = ConnectAction.builder(endpoint).build();

        String expectedJson = "[{\"endpoint\":[{\"uri\":\"wss://example.com\",\"type\":\"websocket\",\"content-type\":\"content-type\"}],\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testDefaultWithWebSip() {
        SipEndpoint endpoint = SipEndpoint.builder("sip:test@sip.example.com").build();
        ConnectAction connect = ConnectAction.builder(endpoint).build();

        String expectedJson = "[{\"endpoint\":[{\"uri\":\"sip:test@sip.example.com\",\"type\":\"sip\"}],\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testEndpointField() {
        SipEndpoint initialEndpoint = SipEndpoint.builder("sip:test@sip.example.com").build();
        PhoneEndpoint newEndpoint = PhoneEndpoint.builder("15554441234").build();

        ConnectAction connect = ConnectAction.builder(initialEndpoint).endpoint(newEndpoint).build();

        String expectedJson = "[{\"endpoint\":[{\"number\":\"15554441234\",\"type\":\"phone\"}],\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testFrom() {
        ConnectAction connect = ConnectAction.builder(PhoneEndpoint.builder("15554441234").build())
                .from("15554449876")
                .build();

        String expectedJson = "[{\"endpoint\":[{\"number\":\"15554441234\",\"type\":\"phone\"}],\"from\":\"15554449876\",\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testEventType() {
        ConnectAction connect = ConnectAction.builder(PhoneEndpoint.builder("15554441234").build())
                .eventType(EventType.SYNCHRONOUS)
                .build();

        String expectedJson = "[{\"endpoint\":[{\"number\":\"15554441234\",\"type\":\"phone\"}],\"eventType\":\"synchronous\",\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testTimeOut() {
        ConnectAction connect = ConnectAction.builder(PhoneEndpoint.builder("15554441234").build()).timeOut(5).build();

        String expectedJson = "[{\"endpoint\":[{\"number\":\"15554441234\",\"type\":\"phone\"}],\"action\":\"connect\",\"timeout\":5}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testLimit() {
        ConnectAction connect = ConnectAction.builder(PhoneEndpoint.builder("15554441234").build()).limit(5).build();

        String expectedJson = "[{\"endpoint\":[{\"number\":\"15554441234\",\"type\":\"phone\"}],\"limit\":5,\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testMachineDetection() {
        ConnectAction.Builder builder = ConnectAction.builder(PhoneEndpoint.builder("15554441234").build());
        ConnectAction connectContinue = builder.machineDetection(MachineDetection.CONTINUE).build();
        ConnectAction connectHangup = builder.machineDetection(MachineDetection.CONTINUE).build();

        String expectedJson = "[{\"endpoint\":[{\"number\":\"15554441234\",\"type\":\"phone\"}],\"machineDetection\":\"continue\",\"action\":\"connect\"},{\"endpoint\":[{\"number\":\"15554441234\",\"type\":\"phone\"}],\"machineDetection\":\"continue\",\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connectContinue, connectHangup).toJson());
    }

    @Test
    public void testEventUrl() {
        ConnectAction connect = ConnectAction.builder(PhoneEndpoint.builder("15554441234").build())
                .eventUrl("https://example.org")
                .build();

        String expectedJson = "[{\"endpoint\":[{\"number\":\"15554441234\",\"type\":\"phone\"}],\"eventUrl\":[\"https://example.org\"],\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testEventMethod() {
        ConnectAction connect = ConnectAction.builder(PhoneEndpoint.builder("15554441234").build())
                .eventMethod(EventMethod.POST)
                .build();

        String expectedJson = "[{\"endpoint\":[{\"number\":\"15554441234\",\"type\":\"phone\"}],\"eventMethod\":\"POST\",\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }
}
