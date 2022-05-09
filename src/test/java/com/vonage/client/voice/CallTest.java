/*
 *   Copyright 2020 Vonage
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
import com.vonage.client.voice.ncco.InputAction;
import com.vonage.client.voice.ncco.RecordAction;
import com.vonage.client.voice.ncco.TalkAction;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class CallTest {

    @Test
    public void testToJson() throws Exception {
        Call call = new Call("4477999000", "44111222333", "https://callback.example.com/");
        assertEquals(
                "{\"to\":[{\"type\":\"phone\",\"number\":\"4477999000\"}],\"from\":{\"type\":\"phone\","
                        + "\"number\":\"44111222333\""
                        + "},\"answer_url\":[\"https://callback.example.com/\"],\"answer_method\":\"GET\"}",
                call.toJson()
        );
    }

    @Test
    public void testToJsonRandomNumber() throws Exception{
        Call call = new Call();
        call.setTo(new Endpoint[]{new PhoneEndpoint("4477999000")});
        call.setFromRandomNumber(true);
        call.setAnswerMethod("GET");
        call.setAnswerUrl("https://callback.example.com/");

        assertEquals("{\"to\":[{\"type\":\"phone\",\"number\":\"4477999000\"}],"
                + "\"answer_url\":[\"https://callback.example.com/\"],\"answer_method\":\"GET\",\"from_random_number\":true}",
                call.toJson());
    }

    @Test
    public void testToJsonMachineDetection() throws Exception {
        Call call = new Call("4477999000", "44111222333", "https://callback.example.com/");
        call.setMachineDetection(MachineDetection.CONTINUE);
        assertEquals(
                "{\"to\":[{\"type\":\"phone\",\"number\":\"4477999000\"}],\"from\":{\"type\":\"phone\","
                        + "\"number\":\"44111222333\""
                        + "},\"answer_url\":[\"https://callback.example.com/\"],\"answer_method\":\"GET\",\"machine_detection\":\"continue\"}",
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
    public void testFromJson() {
        String jsonString = "{\"to\":" + "[{\"type\":\"phone\",\"number\":\"441632960960\"}],"
                + "\"from\":{\"type\":\"phone\",\"number\":\"441632960961\"},"
                + "\"answer_url\":\"http://example.com/answer\"}";

        Call newCall = new Call("441632960960", "441632960961", "http://example.com/answer");
        Call fromJson = Call.fromJson(jsonString);
        assertEquals(newCall.toJson(), fromJson.toJson());

        jsonString = "{\n" +
                "                \"to\": [\n" +
                "                    {\n" +
                "                        \"type\": \"phone\",\n" +
                "                        \"number\": \"{phoneNumber}\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"from\": {\n" +
                "                    \"type\": \"phone\",\n" +
                "                    \"number\": \"447441442936\"\n" +
                "                },\n" +
                "                \"ncco\": [\n" +
                "                    {\n" +
                "                        \"action\": \"talk\",\n" +
                "                        \"text\": \"There's need to verify your voice, please say Never forget tomorrow is a new day after the tone \",\n" +
                "                        \"language\": \"en-US\",\n" +
                "                        \"style\": 10\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"action\": \"record\",\n" +
                "                        \"eventUrl\": [\n" +
                "                            \"http://voice1.yellowfin.npe:9087/callback/verify/{userId}\"\n" +
                "                        ],\n" +
                "                        \"endOnKey\": \"#\",\n" +
                "                        \"timeOut\": 5,\n" +
                "                        \"beepStart\": true\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"action\": \"talk\",\n" +
                "                        \"text\": \"Thank you, good bye\",\n" +
                "                        \"language\": \"en-US\",\n" +
                "                        \"style\": 10\n" +
                "                    }\n" +
                "                ]\n" +
                "}";

        fromJson = Call.fromJson(jsonString);
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
                "{\"to\":[{\"type\":\"phone\",\"number\":\"15551234567\"}],\"from\":{\"type\":\"phone\",\"number\":\"25551234567\"},\"ncco\":[]}",
                call.toJson()
        );
    }

    @Test
    public void testNccoParameterWithSingleActionNcco() {
        Call call = new Call("15551234567", "25551234567", Collections.singletonList(TalkAction.builder("Hello World").build()));
        assertEquals(
                "{\"to\":[{\"type\":\"phone\",\"number\":\"15551234567\"}],\"from\":{\"type\":\"phone\",\"number\":\"25551234567\"},\"ncco\":[{\"text\":\"Hello World\",\"action\":\"talk\"}]}",
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
                "{\"to\":[{\"type\":\"phone\",\"number\":\"15551234567\"}],\"from\":{\"type\":\"phone\",\"number\":\"25551234567\"},\"ncco\":[{\"text\":\"Hello World\",\"action\":\"talk\"},{\"action\":\"record\"},{\"action\":\"input\"},{\"text\":\"Goodbye\",\"action\":\"talk\"}]}",
                call.toJson()
        );
    }

}
