/*
 * Copyright (c) 2011-2017 Nexmo Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nexmo.client.voice.ncco;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConversationNccoTest {
    @Test
    public void testJson() throws Exception {
        ConversationNcco ncco = new ConversationNcco("conversation-name");
        assertEquals("{\"name\":\"conversation-name\",\"action\":\"conversation\"}", ncco.toJson());
    }

    @Test
    public void testToJson() throws Exception {
        ConversationNcco ncco = new ConversationNcco("chat-with-matt");
        ncco.setName("overwrite-name");
        ncco.setEventMethod("GET");
        ncco.setEventUrl("https://api.example.com/event");
        ncco.setEndOnExit(true);
        ncco.setMusicOnHoldUrl("https://api.example.com/on-hold-music");
        ncco.setRecord(true);
        ncco.setStartOnEnter(false);

        ConversationNcco ncco2 = new ObjectMapper().readValue(ncco.toJson(), ConversationNcco.class);
        assertEquals("overwrite-name", ncco.getName());
        assertEquals("GET", ncco2.getEventMethod());
        assertArrayEquals(new String[]{"https://api.example.com/event"}, ncco2.getEventUrl());
        assertEquals(true, ncco2.getEndOnExit());
        assertEquals("https://api.example.com/on-hold-music", ncco2.getMusicOnHoldUrl());
        assertEquals(true, ncco2.getRecord());
        assertEquals(false, ncco2.getStartOnEnter());
    }

    @Test
    public void testWithOnlyNameParameter() throws Exception {
        ConversationNcco ncco = new ConversationNcco("Test");

        String expectedJson = "{\"name\":\"Test\",\"action\":\"conversation\"}";
        assertEquals(expectedJson, ncco.toJson());
    }

    @Test
    public void testMusicOnHoldUrl() throws Exception {
        ConversationNcco ncco = new ConversationNcco("Test");
        ncco.setMusicOnHoldUrl("https://example.org");

        String expectedJson = "{\"name\":\"Test\",\"musicOnHoldUrl\":[\"https://example.org\"],\"action\":\"conversation\"}";
        assertEquals(expectedJson, ncco.toJson());
    }

    @Test
    public void testStartOnEnter() throws Exception {
        ConversationNcco ncco = new ConversationNcco("Test");
        ncco.setStartOnEnter(true);

        String expectedJson = "{\"name\":\"Test\",\"startOnEnter\":true,\"action\":\"conversation\"}";
        assertEquals(expectedJson, ncco.toJson());
    }

    @Test
    public void testEndOnExit() throws Exception {
        ConversationNcco ncco = new ConversationNcco("Test");
        ncco.setEndOnExit(true);

        String expectedJson = "{\"name\":\"Test\",\"endOnExit\":true,\"action\":\"conversation\"}";
        assertEquals(expectedJson, ncco.toJson());
    }

    @Test
    public void testRecord() throws Exception {
        ConversationNcco ncco = new ConversationNcco("Test");
        ncco.setRecord(true);

        String expectedJson = "{\"name\":\"Test\",\"record\":true,\"action\":\"conversation\"}";
        assertEquals(expectedJson, ncco.toJson());
    }

    @Test
    public void testEventUrl() throws Exception {
        ConversationNcco ncco = new ConversationNcco("Test");
        ncco.setEventUrl("https://exmaple.org");

        String expectedJson = "{\"name\":\"Test\",\"action\":\"conversation\",\"eventUrl\":[\"https://exmaple.org\"]}";
        assertEquals(expectedJson, ncco.toJson());
    }

    @Test
    public void testEventMethod() throws Exception {
        ConversationNcco ncco = new ConversationNcco("Test");
        ncco.setEventMethod("Test");

        String expectedJson = "{\"name\":\"Test\",\"eventMethod\":\"Test\",\"action\":\"conversation\"}";
        assertEquals(expectedJson, ncco.toJson());
    }
}