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

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ConversationActionTest {

    @Test
    public void testBuilderMultipleInstances() {
        ConversationAction.Builder builder = ConversationAction.builder("test-conversation");
        assertNotSame(builder.build(), builder.build());
    }

    @Test
    public void testAllFields() {
        ConversationAction action = ConversationAction.builder("test-conversation")
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
        ConversationAction conversation = ConversationAction.builder("test").build();
        assertEquals("conversation", conversation.getAction());
    }

    @Test
    public void testDefault() {
        ConversationAction conversationAction = ConversationAction.builder("test-name").build();

        assertEquals("[{\"name\":\"test-name\",\"action\":\"conversation\"}]", new Ncco(conversationAction).toJson());
    }

    @Test
    public void testName() {
        ConversationAction conversationAction = ConversationAction.builder("test-name").name("rename").build();

        assertEquals("[{\"name\":\"rename\",\"action\":\"conversation\"}]", new Ncco(conversationAction).toJson());
    }

    @Test
    public void testMusicOnHoldUrl() {
        ConversationAction conversation = ConversationAction.builder("Test")
                .musicOnHoldUrl("https://example.org")
                .build();

        String expectedJson = "[{\"name\":\"Test\",\"musicOnHoldUrl\":[\"https://example.org\"],\"action\":\"conversation\"}]";
        assertEquals(expectedJson, new Ncco(conversation).toJson());
    }

    @Test
    public void testStartOnEnter() {
        ConversationAction conversation = ConversationAction.builder("Test").startOnEnter(true).build();

        String expectedJson = "[{\"name\":\"Test\",\"startOnEnter\":true,\"action\":\"conversation\"}]";
        assertEquals(expectedJson, new Ncco(conversation).toJson());
    }

    @Test
    public void testEndOnExit() {
        ConversationAction conversation = ConversationAction.builder("Test").endOnExit(true).build();

        String expectedJson = "[{\"name\":\"Test\",\"endOnExit\":true,\"action\":\"conversation\"}]";
        assertEquals(expectedJson, new Ncco(conversation).toJson());
    }

    @Test
    public void testRecord() {
        ConversationAction conversation = ConversationAction.builder("Test").record(true).build();

        String expectedJson = "[{\"name\":\"Test\",\"record\":true,\"action\":\"conversation\"}]";
        assertEquals(expectedJson, new Ncco(conversation).toJson());
    }

    @Test
    public void testEventUrl() {
        ConversationAction conversation = ConversationAction.builder("Test")
                .eventUrl("https://example.org")
                .build();

        String expectedJson = "[{\"name\":\"Test\",\"eventUrl\":[\"https://example.org\"],\"action\":\"conversation\"}]";
        assertEquals(expectedJson, new Ncco(conversation).toJson());
    }

    @Test
    public void testEventMethod() {
        ConversationAction conversation = ConversationAction.builder("Test").eventMethod(EventMethod.POST).build();

        String expectedJson = "[{\"name\":\"Test\",\"eventMethod\":\"POST\",\"action\":\"conversation\"}]";
        assertEquals(expectedJson, new Ncco(conversation).toJson());
    }
}
