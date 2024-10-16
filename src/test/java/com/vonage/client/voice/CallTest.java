/*
 *   Copyright 2024 Vonage
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
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.common.HttpMethod;
import com.vonage.client.voice.ncco.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
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
        Call call = Call.builder().to(new PhoneEndpoint("4477999000"))
                .fromRandomNumber(true).answerMethod(HttpMethod.GET)
                .answerUrl("https://callback.example.com/").build();

        assertEquals("{\"to\":[{\"number\":\"4477999000\",\"type\":\"phone\"}],"
                + "\"answer_method\":\"GET\",\"answer_url\":[\"https://callback.example.com/\"],\"random_from_number\":true}",
                call.toJson());
    }

    @Test
    public void testToJsonMachineDetection() throws Exception {
        Call call = Call.builder()
                .to(new PhoneEndpoint("4477999000"))
                .from(new PhoneEndpoint("44111222333"))
                .answerUrl("https://callback.example.com/")
                .machineDetection(MachineDetection.CONTINUE).build();

        assertEquals(
                "{\"to\":[{\"number\":\"4477999000\",\"type\":\"phone\"}],"
                        + "\"from\":{\"number\":\"44111222333\",\"type\":\"phone\"},"
                        + "\"answer_method\":\"GET\",\"answer_url\":[\"https://callback.example.com/\"],\"machine_detection\":\"continue\"}",
                call.toJson()
        );
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
                "            \"timeOut\":8,\n" +
                "            \"maxDigits\":12,\n" +
                "            \"submitOnHash\":false\n" +
                "         }\n" +
                "      },\n" +
                "      {\n" +
                "         \"action\":\"connect\",\n" +
                "         \"eventType\":\"synchronous\",\n" +
                "         \"machineDetection\":\"hangup\"\n," +
                "         \"advancedMachineDetection\":{\n" +
                "             \"behavior\": \"continue\",\n" +
                "             \"mode\": \"detect_beep\",\n" +
                "             \"beep_timeout\": 51" +
                "         }\n" +
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
                "      }\n" +
                "   ]\n" +
                "}";

        fromJson = Call.fromJson(jsonString);
        TestUtils.testJsonableBaseObject(fromJson);

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
        assertEquals(7, ncco.size());
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
        assertEquals(Integer.valueOf(8), dtmf.getTimeOut());
        assertEquals(Integer.valueOf(12), dtmf.getMaxDigits());
        assertFalse(dtmf.isSubmitOnHash());

        ConnectAction connect = (ConnectAction) actionsIter.next();
        assertEquals(EventType.SYNCHRONOUS, connect.getEventType());
        assertEquals(MachineDetection.HANGUP, connect.getMachineDetection());
        AdvancedMachineDetection amd = connect.getAdvancedMachineDetection();
        assertNotNull(amd);
        assertEquals(MachineDetection.CONTINUE, amd.getBehavior());
        assertEquals(AdvancedMachineDetection.Mode.DETECT_BEEP, amd.getMode());
        assertEquals(51, amd.getBeepTimeout().intValue());

        ConversationAction conversation = (ConversationAction) actionsIter.next();
        assertEquals("Conference call", conversation.getName());
        assertFalse(conversation.getStartOnEnter());
        assertTrue(conversation.getEndOnExit());

        TalkAction talk = (TalkAction) actionsIter.next();
        assertEquals("Thank you, good bye", talk.getText());
        assertEquals(TextToSpeechLanguage.AMERICAN_ENGLISH, talk.getLanguage());
        assertEquals(Integer.valueOf(10), talk.getStyle());
    }

    @Test
    public void testMalformedJson() throws Exception {
        try {
            Call.fromJson("{ bad Jason: \"unknown\"\n" + "}");
            fail("Expected a VonageUnexpectedException to be thrown");
        } catch (VonageResponseParseException e) {
            assertEquals("Failed to produce Call from JSON.", e.getMessage());
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
                InputAction.builder().dtmf().build(),
                TalkAction.builder("Goodbye").build()
        ));
        assertEquals(
                "{\"to\":[{\"number\":\"15551234567\",\"type\":\"phone\"}],\"from\":{\"number\":\"25551234567\",\"type\":\"phone\"},\"ncco\":[{\"text\":\"Hello World\",\"action\":\"talk\"},{\"action\":\"record\"},{\"type\":[\"dtmf\"],\"action\":\"input\"},{\"text\":\"Goodbye\",\"action\":\"talk\"}]}",
                call.toJson()
        );
    }

    @Test
    public void testInvalidMachineDetectionCombination() {
        assertThrows(IllegalStateException.class, () -> Call.builder().from("447900000001")
                .to(new VbcEndpoint("456"))
                .machineDetection(MachineDetection.HANGUP)
                .advancedMachineDetection(AdvancedMachineDetection.builder().build())
                .build()
        );
    }

    @Test
    public void testConstructAllParams() {
        var sipCustomHeaders = Map.of("ab", List.of("C", 2, 'd'));
        var sipUri = "sip://example.com";
        var sipUser2User = "unvalidated_content";

        Call call = Call.builder()
                .from("447900000001")
                .to(
                    new AppEndpoint("nexmo"),
                    new SipEndpoint(sipUri, sipCustomHeaders),
                    new VbcEndpoint("7890"),
                    new WebSocketEndpoint("wss://example.org", "audio/l16", Collections.emptyMap()),
                    new SipEndpoint(sipUri, sipUser2User),
                    new SipEndpoint(sipUri, sipCustomHeaders, sipUser2User)
                )
                .ncco(
                    TalkAction.builder("Hello").build(),
                    RecordAction.builder().build(),
                    ConnectAction.builder(com.vonage.client.voice.ncco.VbcEndpoint.builder("123").build()).build()
                )
                .answerMethod(HttpMethod.POST).eventMethod(HttpMethod.POST)
                .eventUrl("https://example.com/voice/event")
                .answerUrl("https://example.com/voice/answer")
                .fromRandomNumber(false).machineDetection(MachineDetection.HANGUP)
                .lengthTimer(30).ringingTimer(55).build();

        assertEquals(30, call.getLengthTimer().intValue());
        assertEquals(55, call.getRingingTimer().intValue());
        assertNotNull(call.getAnswerUrl());
        assertNotNull(call.getEventUrl());
        assertEquals("POST", call.getAnswerMethod());
        assertEquals("POST", call.getEventMethod());
        assertFalse(call.getFromRandomNumber());
        assertEquals(MachineDetection.HANGUP, call.getMachineDetection());
        assertEquals("phone", call.getFrom().getType());
        Endpoint[] to = call.getTo();
        assertEquals(6, to.length);
        assertEquals("app", to[0].getType());
        assertEquals("sip", to[1].getType());
        assertEquals("vbc", to[2].getType());
        assertEquals("websocket", to[3].getType());
        assertEquals(sipUri, ((SipEndpoint) to[4]).getUri());
        assertEquals(sipUser2User, ((SipEndpoint) to[4])
                .getStandardHeaders().get(SipHeader.fromString("User-to-User"))
        );
        assertNull(((SipEndpoint) to[4]).getHeaders());
        assertEquals(sipUri, ((SipEndpoint) to[5]).getUri());
        assertEquals(1, ((SipEndpoint) to[5]).getStandardHeaders().size());
        assertEquals(sipCustomHeaders, ((SipEndpoint) to[5]).getHeaders());
        assertNotNull(to[0].toLog());
        assertNotNull(to[1].toLog());
        assertNotNull(to[2].toLog());
        assertNotNull(to[3].toLog());
        Collection<? extends Action> ncco = call.getNcco();
        assertNotNull(ncco);
        assertEquals(3, ncco.size());
        Iterator<? extends Action> nccoIter = ncco.iterator();
        assertEquals("talk", nccoIter.next().getAction());
        assertEquals("record", nccoIter.next().getAction());
        assertEquals("connect", nccoIter.next().getAction());
    }

    @Test
    public void testConstructRequiredParams() {
        Call call = Call.builder().to(new VbcEndpoint("123")).build();
        assertTrue(call.getFromRandomNumber());
        assertNull(call.getEventUrl());
        assertNull(call.getAnswerUrl());
        assertNull(call.getNcco());
        assertNull(call.getEventMethod());
        assertNull(call.getAnswerMethod());
        assertNull(call.getLengthTimer());
        assertNull(call.getRingingTimer());
        assertNull(call.getMachineDetection());
        assertNull(call.getAdvancedMachineDetection());
        assertNull(call.getFrom());
        assertEquals("123", call.getTo()[0].toLog());
        assertThrows(IllegalStateException.class, () -> Call.builder().build());
        assertThrows(IllegalStateException.class, () -> Call.builder().to().build());
        assertThrows(IllegalStateException.class, () -> Call.builder().to((Endpoint) null).build());
    }

    @Test
    public void testConstructDefaultAnswerMethod() {
        Call call = Call.builder().to(new VbcEndpoint("123"))
                .answerUrl("http://example.com/answer").build();
        assertEquals("GET", call.getAnswerMethod());
    }

    @Test
    public void testConstructFromRandomNumber() {
        VbcEndpoint vbc = new VbcEndpoint("789");
        assertThrows(IllegalStateException.class, () ->
            Call.builder().to(vbc).from("447900000001").fromRandomNumber(true).build()
        );
        assertTrue(Call.builder().to(vbc).fromRandomNumber(true).build().getFromRandomNumber());
    }

    @Test
    public void testConstructInvalidMethod() {
        VbcEndpoint vbc = new VbcEndpoint("789");
        assertThrows(IllegalArgumentException.class, () ->
                Call.builder().to(vbc).answerMethod(HttpMethod.PATCH).build()
        );
        assertThrows(IllegalArgumentException.class, () ->
                Call.builder().to(vbc).eventMethod(HttpMethod.PATCH).build()
        );
    }

    @Test
    public void testRingingTimerBounds() {
        Call.Builder builder = Call.builder().to(new VbcEndpoint("789"));
        int min = 1, max = 120;
        assertEquals(min, builder.ringingTimer(min).build().getRingingTimer().intValue());
        assertEquals(max, builder.ringingTimer(max).build().getRingingTimer().intValue());
        assertThrows(IllegalArgumentException.class, () -> builder.ringingTimer(max+1).build());
        assertThrows(IllegalArgumentException.class, () -> builder.ringingTimer(0).build());
    }

    @Test
    public void testLengthTimerBounds() {
        Call.Builder builder = Call.builder().to(new VbcEndpoint("789"));
        int min = 1, max = 7200;
        assertEquals(min, builder.lengthTimer(min).build().getLengthTimer().intValue());
        assertEquals(max, builder.lengthTimer(max).build().getLengthTimer().intValue());
        assertThrows(IllegalArgumentException.class, () -> builder.lengthTimer(max+1).build());
        assertThrows(IllegalArgumentException.class, () -> builder.lengthTimer(0).build());
    }

    @Test
    public void testRandomNumberIsTrueWhenFromIsNotSet() {
        var call = Call.builder().to(new AppEndpoint("user123")).build();
        assertTrue(call.getFromRandomNumber());
        assertNull(call.getFrom());
        call = Call.builder().to(call.getTo()).from(new AppEndpoint("admin")).build();
        assertNotNull(call.getFrom());
        assertNull(call.getFromRandomNumber());
    }
}
