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

import com.vonage.client.voice.AdvancedMachineDetection;
import com.vonage.client.voice.MachineDetection;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Collection;
import java.util.Map;

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
                .from("15554449876").eventType(EventType.SYNCHRONOUS)
                .timeOut(3).limit(2).eventUrl("https://example.com")
                .machineDetection(MachineDetection.CONTINUE)
                .ringbackTone("http://example.com/ringbackTone.wav")
                .eventMethod(EventMethod.POST).build();

        String expectedJson = "[{\"endpoint\":[{\"number\":\"15554441234\",\"type\":\"phone\"}]," +
                "\"from\":\"15554449876\",\"eventType\":\"synchronous\",\"limit\":2,\"timeout\":3," +
                "\"machineDetection\":\"continue\",\"eventUrl\":[\"https://example.com\"],\"eventMethod\":" +
                "\"POST\",\"ringbackTone\":\"http://example.com/ringbackTone.wav\",\"action\":\"connect\"}]";
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

        String expectedJson = "[{\"endpoint\":[{\"uri\":\"wss://example.com\",\"content-type\":\"content-type\"," +
                "\"type\":\"websocket\"}],\"from\":\"15554449876\",\"eventType\":\"synchronous\"," +
                "\"limit\":2,\"timeout\":3,\"machineDetection\":\"continue\"," +
                "\"eventUrl\":[\"https://example.com\"],\"eventMethod\":\"POST\",\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testAllFieldsWithSipEndpoint() {
        String uri = "sip:test@sip.example.com", user2User = "someRandomValue;encoding=L0L";
        SipEndpoint endpoint = SipEndpoint.builder(uri)
                .userToUserHeader(user2User).headers(Map.of()).build();

        ConnectAction connect = ConnectAction.builder(endpoint)
                .from("15554449876")
                .eventType(EventType.SYNCHRONOUS)
                .timeOut(3).limit(2)
                .machineDetection(MachineDetection.CONTINUE)
                .eventUrl("https://example.com")
                .eventMethod(EventMethod.POST)
                .build();

        String expectedJson = "[{\"endpoint\":[{\"uri\":\""+uri+"\",\"headers\":{},\"standardHeaders\":" +
                "{\"User-to-User\":\""+user2User+"\"},\"type\":\"sip\"}]," +
                "\"from\":\"15554449876\",\"eventType\":\"synchronous\",\"limit\":2,\"timeout\":3," +
                "\"machineDetection\":\"continue\",\"eventUrl\":[\"https://example.com\"]," +
                "\"eventMethod\":\"POST\",\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testAllFieldsWithVbcEndpoint() {
        VbcEndpoint endpoint = VbcEndpoint.builder("9870").build();
        ConnectAction connect = ConnectAction.builder(endpoint)
                .from("15554449876").eventType(EventType.SYNCHRONOUS)
                .advancedMachineDetection(AdvancedMachineDetection.builder().build())
                .eventUrl("https://example.com").eventMethod(EventMethod.POST)
                .timeOut(61).limit(3602).build();

        String expectedJson = "[{\"endpoint\":[{\"extension\":\"9870\",\"type\":\"vbc\"}]," +
                "\"from\":\"15554449876\",\"eventType\":\"synchronous\",\"limit\":3602,\"timeout\":61," +
                "\"advancedMachineDetection\":{},\"eventUrl\":[\"https://example.com\"],\"eventMethod\":\"POST\"," +
                "\"action\":\"connect\"}]";
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

        String expectedJson = "[{\"endpoint\":[{\"uri\":\"wss://example.com\"," +
                "\"content-type\":\"content-type\",\"type\":\"websocket\"}],\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testDefaultWithWebSip() {
        SipEndpoint endpoint = SipEndpoint.builder("sip:test@sip.example.com").build();
        ConnectAction connect = ConnectAction.builder(endpoint).build();

        String expectedJson = "[{\"endpoint\":[{\"uri\":\"sip:test@sip.example.com\",\"type\":\"sip\"}]," +
                "\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testEndpointField() {
        SipEndpoint initialEndpoint = SipEndpoint.builder("sip:test@sip.example.com").build();
        PhoneEndpoint newEndpoint = PhoneEndpoint.builder("15554441234").build();

        ConnectAction connect = ConnectAction.builder(initialEndpoint).endpoint(newEndpoint).build();

        String expectedJson = "[{\"endpoint\":[{\"number\":\"15554441234\",\"type\":\"phone\"}]," +
                "\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testFrom() {
        ConnectAction connect = ConnectAction.builder(PhoneEndpoint.builder("15554441234").build())
                .from("15554449876").build();

        String expectedJson = "[{\"endpoint\":[{\"number\":\"15554441234\",\"type\":\"phone\"}]," +
                "\"from\":\"15554449876\",\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testEventType() {
        ConnectAction connect = ConnectAction.builder(PhoneEndpoint.builder("15554441234").build())
                .eventType(EventType.SYNCHRONOUS)
                .build();

        String expectedJson = "[{\"endpoint\":[{\"number\":\"15554441234\",\"type\":\"phone\"}]," +
                "\"eventType\":\"synchronous\",\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testTimeOut() {
        ConnectAction connect = ConnectAction.builder(PhoneEndpoint.builder("15554441234").build()).timeOut(5).build();

        String expectedJson = "[{\"endpoint\":[{\"number\":\"15554441234\",\"type\":\"phone\"}]," +
                "\"timeout\":5,\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testLimit() {
        ConnectAction connect = ConnectAction.builder(PhoneEndpoint.builder("15554441234").build())
                .limit(5).build();

        String expectedJson = "[{\"endpoint\":[{\"number\":\"15554441234\",\"type\":\"phone\"}]," +
                "\"limit\":5,\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testMachineDetection() {
        ConnectAction.Builder builder = ConnectAction.builder(PhoneEndpoint.builder("15554441234").build());
        ConnectAction connectContinue = builder.machineDetection(MachineDetection.CONTINUE).build();
        ConnectAction connectHangup = builder.machineDetection(MachineDetection.HANGUP).build();

        String expectedJson = "[{\"endpoint\":[{\"number\":\"15554441234\",\"type\":\"phone\"}]," +
                "\"machineDetection\":\"continue\",\"action\":\"connect\"},{\"endpoint\":[{\"number\":" +
                "\"15554441234\",\"type\":\"phone\"}],\"machineDetection\":\"hangup\",\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connectContinue, connectHangup).toJson());
    }

    @Test
    public void testAdvancedMachineDetection() {
        PhoneEndpoint phone = PhoneEndpoint.builder("15554321234").build();
        AdvancedMachineDetection amd = AdvancedMachineDetection.builder()
                .mode(AdvancedMachineDetection.Mode.DETECT_BEEP)
                .behavior(MachineDetection.CONTINUE).build();
        ConnectAction ca = ConnectAction.builder(phone).advancedMachineDetection(amd).build();
        String expectedJson = "[{\"endpoint\":[{\"number\":\"15554321234\",\"type\":\"phone\"}]," +
                "\"advancedMachineDetection\":{\"behavior\":\"continue\",\"mode\":\"detect_beep\"}," +
                "\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(ca).toJson());
    }

    @Test
    public void testEventUrl() {
        ConnectAction connect = ConnectAction.builder(PhoneEndpoint.builder("15554441234").build())
                .eventUrl("https://example.org").build();

        String expectedJson = "[{\"endpoint\":[{\"number\":\"15554441234\",\"type\":\"phone\"}]," +
                "\"eventUrl\":[\"https://example.org\"],\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testEventMethod() {
        ConnectAction connect = ConnectAction.builder(PhoneEndpoint.builder("15554441234").build())
                .eventMethod(EventMethod.POST).build();

        String expectedJson = "[{\"endpoint\":[{\"number\":\"15554441234\",\"type\":\"phone\"}]," +
                "\"eventMethod\":\"POST\",\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testRandomFromNumber() {
        var builder = ConnectAction.builder(VbcEndpoint.builder("789").build());
        ConnectAction connect = builder.randomFromNumber(true).build();
        String expectedJson = "[{\"endpoint\":[{\"extension\":\"789\",\"type\":\"vbc\"}]," +
                "\"randomFromNumber\":true,\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());

        assertThrows(IllegalStateException.class, () -> builder.from("447900000001").build());
        assertThrows(IllegalStateException.class, () -> builder.randomFromNumber(false).build());
        connect = builder.from(null).build();
        expectedJson = expectedJson.replace("true", "false");
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testEndpointRequired() {
        assertThrows(IllegalStateException.class, () -> ConnectAction.builder().build());
    }

    @Test
    public void testLimitBoundaries() {
        var builder = ConnectAction.builder(AppEndpoint.builder("Me").build());
        int max = 7200;
        assertEquals(max, builder.limit(max).build().getLimit());
        assertThrows(IllegalArgumentException.class, () -> builder.limit(max + 1).build());
        assertEquals(1, builder.limit(1).build().getLimit());
        assertThrows(IllegalArgumentException.class, () -> builder.limit(0).build());
    }

    @Test
    public void testTimeOutBoundaries() {
        var builder = ConnectAction.builder(AppEndpoint.builder("Me").build());
        int min = 3, max = 7200;
        assertEquals(max, builder.timeOut(max).build().getTimeOut());
        assertThrows(IllegalArgumentException.class, () -> builder.timeOut(max + 1).build());
        assertEquals(min, builder.timeOut(min).build().getTimeOut());
        assertThrows(IllegalArgumentException.class, () -> builder.timeOut(min - 1).build());
    }

    @Test
    public void testNullEndpoint() {
        assertThrows(IllegalStateException.class, () -> ConnectAction.builder((Collection<Endpoint>) null).build());
    }
}
