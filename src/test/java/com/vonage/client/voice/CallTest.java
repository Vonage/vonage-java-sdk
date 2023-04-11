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

import com.vonage.client.VonageUnexpectedException;
import com.vonage.client.voice.ncco.*;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class CallTest {

    @Test
    public void testToJson() throws Exception {
        Call call = new Call("4477999000", "44111222333", "https://callback.example.com/");
        assertEquals(
                "{\"to\":[{\"number\":\"4477999000\",\"type\":\"phone\"}]," +
                        "\"from\":{\"number\":\"44111222333\",\"type\":\"phone\"}," +
                        "\"answer_method\":\"GET\",\"answer_url\":[\"https://callback.example.com/\"]}",
                call.toJson()
        );
    }

    @Test
    public void testToJsonRandomNumber() throws Exception {
        Call call = new Call();
        call.setTo(new Endpoint[]{new PhoneEndpoint("4477999000")});
        call.setFromRandomNumber(true);
        call.setAnswerMethod("GET");
        call.setAnswerUrl("https://callback.example.com/");

        assertEquals("{\"to\":[{\"number\":\"4477999000\",\"type\":\"phone\"}],"
                + "\"answer_method\":\"GET\",\"answer_url\":[\"https://callback.example.com/\"],\"from_random_number\":true}",
                call.toJson());
    }

    @Test
    public void testToJsonMachineDetection() throws Exception {
        Call call = new Call("4477999000", "44111222333", "https://callback.example.com/");
        call.setMachineDetection(MachineDetection.CONTINUE);
        assertEquals(
                "{\"to\":[{\"number\":\"4477999000\",\"type\":\"phone\"}],"
                        + "\"from\":{\"number\":\"44111222333\",\"type\":\"phone\"},"
                        + "\"answer_method\":\"GET\",\"answer_url\":[\"https://callback.example.com/\"],\"machine_detection\":\"continue\"}",
                call.toJson()
        );
    }

    @Test
    public void testSetters() throws Exception {
        PhoneEndpoint from = new PhoneEndpoint("44-AAA-FROM");
        PhoneEndpoint to = new PhoneEndpoint("44-BBB-TO");
        Call call = new Call("", "", "https://callback.example.com/");
        call.setAnswerMethod("BREW");
        call.setAnswerUrl("https://answer.example.com/");
        call.setEventMethod("RUN");
        call.setEventUrl("https://events.example.com/");
        call.setFrom(from);
        call.setLengthTimer(101);
        call.setMachineDetection(MachineDetection.CONTINUE);
        call.setRingingTimer(300);
        call.setTo(new PhoneEndpoint[]{to});

        assertEquals("BREW", call.getAnswerMethod());
        assertEquals("https://answer.example.com/", call.getAnswerUrl()[0]);
        assertEquals("RUN", call.getEventMethod());
        assertEquals("https://events.example.com/", call.getEventUrl()[0]);
        assertEquals(from, call.getFrom());
        assertEquals(101, call.getLengthTimer().intValue());
        assertEquals(MachineDetection.CONTINUE, call.getMachineDetection());
        assertEquals(300, call.getRingingTimer().intValue());
        assertEquals(to, call.getTo()[0]);
    }

    @Test
    public void testFromJsonWithEveryActionAndEndpoint() {
        String toFromJsonStart = "{\"to\":[" +
                "{\"type\":\"phone\",\"number\":\"441632960960\"}," +
                "{\"type\":\"sip\",\"uri\":\"sip:sip@example.com\"}," +
                "{\"type\":\"vbc\",\"extension\":\"123\"}," +
                "{\"type\": \"websocket\",\n" +
                "        \"uri\": \"ws://example.com/socket\",\n" +
                "        \"content-type\": \"audio/l16;rate=16000\",\n" +
                "        \"headers\": {\n" +
                "            \"name\": \"J Doe\",\n" +
                "            \"age\": 40,\n" +
                "            \"address\": {\n" +
                "                \"line_1\": \"Apartment 14\",\n" +
                "                \"line_2\": \"123 Example Street\",\n" +
                "                \"city\": \"New York City\"\n" +
                "            },\n" +
                "            \"system_roles\": [183493, 1038492, 22],\n" +
                "            \"enable_auditing\": false\n" +
                "         }" +
                "}],\"from\":{\"type\":\"app\",\"user\":\"nexmo\"}";

        Map<String, Object> headers = new LinkedHashMap<>(8);
        headers.put("name", "J Doe");
        headers.put("age", 40);
        Map<String, String> address = new LinkedHashMap<>(4);
        address.put("line_1", "Apartment 14");
        address.put("line_2", "123 Example Street");
        address.put("city", "New York City");
        headers.put("address", address);
        headers.put("system_roles", Arrays.asList(183493, 1038492, 22));
        headers.put("enable_auditing", false);
        com.vonage.client.voice.Endpoint[] endpoints = {
                new PhoneEndpoint("441632960960"),
                new SipEndpoint("sip:sip@example.com"),
                new VbcEndpoint("123"),
                new WebSocketEndpoint(
                        "ws://example.com/socket",
                        "audio/l16;rate=16000",
                        headers
                )
        };
        com.vonage.client.voice.Endpoint fromEndpoint = new com.vonage.client.voice.AppEndpoint("nexmo");
        Call expectedCall = new Call(endpoints, fromEndpoint, "http://example.com/answer");
        String jsonString = toFromJsonStart + ",\"answer_url\":\"http://example.com/answer\"}";
        Call fromJson = Call.fromJson(jsonString);
        assertEquals(expectedCall.toJson(), fromJson.toJson());

        jsonString = toFromJsonStart + ",\n   \"ncco\":[\n" +
                "      {\n" +
                "         \"action\":\"record\",\n" +
                "         \"eventUrl\":[\n" +
                "            \"http://voice1.yellowfin.npe:9087/callback/verify/{userId}\"\n" +
                "         ],\n" +
                "         \"endOnKey\":\"#\",\n" +
                "         \"timeOut\":5,\n" +
                "         \"beepStart\":true\n" +
                "      },\n" +
                "      {\n" +
                "         \"action\":\"stream\",\n" +
                "         \"streamUrl\":[\n" +
                "            \n" +
                "         ],\n" +
                "         \"bargeIn\":true\n" +
                "      },\n" +
                "      {\n" +
                "         \"action\":\"notify\",\n" +
                "         \"payload\":{\n" +
                "            \"k1\":\"v1\"\n" +
                "         }\n" +
                "      },\n" +
                "      {\n" +
                "         \"action\":\"input\",\n" +
                "         \"dtmf\":{\n" +
                "            \"timeOut\":30,\n" +
                "            \"maxDigits\":12,\n" +
                "            \"submitOnHash\":false\n" +
                "         }\n" +
                "      },\n" +
                "      {\n" +
                "         \"action\":\"connect\",\n" +
                "         \"eventType\":\"synchronous\",\n" +
                "         \"machineDetection\":\"hangup\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"action\":\"conversation\",\n" +
                "         \"name\":\"Conference call\",\n" +
                "         \"startOnEnter\":\"false\",\n" +
                "         \"endOnExit\":true\n" +
                "      },\n" +
                "      {\n" +
                "         \"action\":\"talk\",\n" +
                "         \"text\":\"Thank you, good bye\",\n" +
                "         \"language\":\"en-US\",\n" +
                "         \"style\":10\n" +
                "      },\n" +
                "      {\n" +
                "         \"action\":\"pay\",\n" +
                "         \"currency\":\"eur\",\n" +
                "         \"amount\":13.37\n" +
                "      }\n" +
                "   ]\n" +
                "}";

        fromJson = Call.fromJson(jsonString);
        assertEquals(4, fromJson.getTo().length);
        assertEquals("phone", fromJson.getTo()[0].getType());
        assertEquals("441632960960", ((PhoneEndpoint) fromJson.getTo()[0]).getNumber());
        assertEquals("sip", fromJson.getTo()[1].getType());
        assertEquals("sip:sip@example.com", ((SipEndpoint) fromJson.getTo()[1]).getUri());
        assertEquals("vbc", fromJson.getTo()[2].getType());
        assertEquals("123", ((VbcEndpoint) fromJson.getTo()[2]).getExtension());
        assertEquals("websocket", fromJson.getTo()[3].getType());
        assertEquals("ws://example.com/socket", ((WebSocketEndpoint) fromJson.getTo()[3]).getUri());
        assertEquals("audio/l16;rate=16000", ((WebSocketEndpoint) fromJson.getTo()[3]).getContentType());
        assertEquals("app", fromJson.getFrom().getType());
        assertEquals("nexmo", ((AppEndpoint) fromJson.getFrom()).getUser());
        Collection<? extends Action> ncco = fromJson.getNcco();
        assertEquals(8, ncco.size());
        Iterator<? extends Action> actionsIter = ncco.iterator();

        RecordAction record = (RecordAction) actionsIter.next();
        assertEquals(1, record.getEventUrl().size());
        assertEquals(Character.valueOf('#'), record.getEndOnKey());
        assertEquals(Integer.valueOf(5), record.getTimeOut());
        assertTrue(record.getBeepStart());

        StreamAction stream = (StreamAction) actionsIter.next();
        assertTrue(stream.getStreamUrl().isEmpty());
        assertTrue(stream.getBargeIn());

        NotifyAction notify = (NotifyAction) actionsIter.next();
        assertEquals(1, notify.getPayload().size());

        InputAction input = (InputAction) actionsIter.next();
        DtmfSettings dtmf = input.getDtmf();
        assertEquals(Integer.valueOf(30), dtmf.getTimeOut());
        assertEquals(Integer.valueOf(12), dtmf.getMaxDigits());
        assertFalse(dtmf.isSubmitOnHash());

        ConnectAction connect = (ConnectAction) actionsIter.next();
        assertEquals(EventType.SYNCHRONOUS, connect.getEventType());
        assertEquals(MachineDetection.HANGUP, connect.getMachineDetection());

        ConversationAction conversation = (ConversationAction) actionsIter.next();
        assertEquals("Conference call", conversation.getName());
        assertFalse(conversation.getStartOnEnter());
        assertTrue(conversation.getEndOnExit());

        TalkAction talk = (TalkAction) actionsIter.next();
        assertEquals("Thank you, good bye", talk.getText());
        assertEquals(TextToSpeechLanguage.AMERICAN_ENGLISH, talk.getLanguage());
        assertEquals(Integer.valueOf(10), talk.getStyle());

        PayAction pay = (PayAction) actionsIter.next();
        assertEquals("eur", pay.getCurrency());
        assertEquals(13.37, pay.getAmount(), 0.001);
    }

    @Test
    public void testMalformedJson() throws Exception {
        try {
            Call.fromJson("{\n" + "    \"unknownProperty\": \"unknown\"\n" + "}");
            fail("Expected a VonageUnexpectedException to be thrown");
        } catch (VonageUnexpectedException e) {
            assertEquals("Failed to produce json from Call object.", e.getMessage());
        }
    }

    @Test
    public void testNullAnswerMethodIfNoAnswerUrlDefined() {
        Call call = new Call();
        assertNull(call.getAnswerMethod());
    }

    @Test
    public void testGetAnswerUrlReturnsNullIfNotDefined() {
        Call call = new Call();
        assertNull(call.getAnswerUrl());
    }

    @Test
    public void testNccoParameterWithEmptyNcco() {
        Call call = new Call("15551234567", "25551234567", Collections.emptyList());
        assertEquals(
                "{\"to\":[{\"number\":\"15551234567\",\"type\":\"phone\"}],\"from\":{\"number\":\"25551234567\",\"type\":\"phone\"},\"ncco\":[]}",
                call.toJson()
        );
    }

    @Test
    public void testNccoParameterWithSingleActionNcco() {
        Call call = new Call("15551234567", "25551234567", Collections.singletonList(TalkAction.builder("Hello World").build()));
        assertEquals(
                "{\"to\":[{\"number\":\"15551234567\",\"type\":\"phone\"}],\"from\":{\"number\":\"25551234567\",\"type\":\"phone\"},\"ncco\":[{\"text\":\"Hello World\",\"action\":\"talk\"}]}",
                call.toJson()
        );
    }

    @Test
    public void testNccoParameterWithMultiActionNcco() {
        Call call = new Call("15551234567", "25551234567", Arrays.asList(
                TalkAction.builder("Hello World").build(),
                RecordAction.builder().build(),
                InputAction.builder().build(),
                TalkAction.builder("Goodbye").build()
        ));
        assertEquals(
                "{\"to\":[{\"number\":\"15551234567\",\"type\":\"phone\"}],\"from\":{\"number\":\"25551234567\",\"type\":\"phone\"},\"ncco\":[{\"text\":\"Hello World\",\"action\":\"talk\"},{\"action\":\"record\"},{\"action\":\"input\"},{\"text\":\"Goodbye\",\"action\":\"talk\"}]}",
                call.toJson()
        );
    }

}
