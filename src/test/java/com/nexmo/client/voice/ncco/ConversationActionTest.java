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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class ConversationActionTest {

    @Test
    public void testBuilderMultipleInstances() {
        ConversationAction.Builder builder = new ConversationAction.Builder("test-conversation");
        assertNotSame(builder.build(), builder.build());
    }

    @Test
    public void testAllFields() {
        ConversationAction action = new ConversationAction.Builder("test-conversation")
                .name("different-name")
                .musicOnHoldUrl("http://example.com/music")
                .startOnEnter(true)
                .endOnExit(true)
                .record(true)
                .eventUrl("https://example.com/event")
                .eventMethod(EventMethod.GET)
                .build();

        String expectedJson = "[{\"name\":\"different-name\",\"musicOnHoldUrl\":[\"http://example.com/music\"],\"startOnEnter\":true,\"endOnExit\":true,\"record\":true,\"eventUrl\":[\"https://example.com/event\"],\"eventMethod\":\"GET\",\"action\":\"conversation\"}]";
        assertEquals(expectedJson, new Ncco(action).toJson());
    }

    @Test
    public void testGetAction() {
        ConversationAction conversation = new ConversationAction.Builder("test").build();
        assertEquals("conversation", conversation.getAction());
    }

    @Test
    public void testDefault() {
        ConversationAction conversationAction = new ConversationAction.Builder("test-name").build();

        assertEquals("[{\"name\":\"test-name\",\"action\":\"conversation\"}]", new Ncco(conversationAction).toJson());
    }

    @Test
    public void testName() {
        ConversationAction conversationAction = new ConversationAction.Builder("test-name").name("rename").build();

        assertEquals("[{\"name\":\"rename\",\"action\":\"conversation\"}]", new Ncco(conversationAction).toJson());
    }

    @Test
    public void testMusicOnHoldUrl() {
        ConversationAction conversation = new ConversationAction.Builder("Test")
                .musicOnHoldUrl("https://example.org")
                .build();

        String expectedJson = "[{\"name\":\"Test\",\"musicOnHoldUrl\":[\"https://example.org\"],\"action\":\"conversation\"}]";
        assertEquals(expectedJson, new Ncco(conversation).toJson());
    }

    @Test
    public void testStartOnEnter() throws Exception {
        ConversationAction conversation = new ConversationAction.Builder("Test").startOnEnter(true).build();

        String expectedJson = "[{\"name\":\"Test\",\"startOnEnter\":true,\"action\":\"conversation\"}]";
        assertEquals(expectedJson, new Ncco(conversation).toJson());
    }

    @Test
    public void testEndOnExit() throws Exception {
        ConversationAction conversation = new ConversationAction.Builder("Test").endOnExit(true).build();

        String expectedJson = "[{\"name\":\"Test\",\"endOnExit\":true,\"action\":\"conversation\"}]";
        assertEquals(expectedJson, new Ncco(conversation).toJson());
    }

    @Test
    public void testRecord() throws Exception {
        ConversationAction conversation = new ConversationAction.Builder("Test").record(true).build();

        String expectedJson = "[{\"name\":\"Test\",\"record\":true,\"action\":\"conversation\"}]";
        assertEquals(expectedJson, new Ncco(conversation).toJson());
    }

    @Test
    public void testEventUrl() throws Exception {
        ConversationAction conversation = new ConversationAction.Builder("Test")
                .eventUrl("https://example.org")
                .build();

        String expectedJson = "[{\"name\":\"Test\",\"eventUrl\":[\"https://example.org\"],\"action\":\"conversation\"}]";
        assertEquals(expectedJson, new Ncco(conversation).toJson());
    }

    @Test
    public void testEventMethod() throws Exception {
        ConversationAction conversation = new ConversationAction.Builder("Test").eventMethod(EventMethod.POST).build();

        String expectedJson = "[{\"name\":\"Test\",\"eventMethod\":\"POST\",\"action\":\"conversation\"}]";
        assertEquals(expectedJson, new Ncco(conversation).toJson());
    }
}
