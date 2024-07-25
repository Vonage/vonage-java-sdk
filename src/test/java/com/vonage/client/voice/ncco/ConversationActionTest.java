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
package com.vonage.client.voice.ncco;

import com.vonage.client.TestUtils;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.*;

public class ConversationActionTest {

    @Test
    public void testBuilderMultipleInstances() {
        ConversationAction.Builder builder = ConversationAction.builder("test-conversation");
        assertNotSame(builder.build(), builder.build());
    }

    @Test
    public void testAllFields() {
        ConversationAction action = ConversationAction.builder("test-conversation")
                .name("different-name").musicOnHoldUrl("http://example.com/music")
                .startOnEnter(true).endOnExit(true).record(true).mute(true)
                .eventUrl("https://example.com/event").eventMethod(EventMethod.GET)
                .addCanSpeak("6a4d6af0-55a6-4667-be90-8614e4c8e83c")
                .addCanHear("416dfcfc-d86a-41c2-92ad-9a4fe1266898")
                .transcription(TranscriptionSettings.builder().build()).build();

        String expectedJson = "[{\"name\":\"different-name\",\"startOnEnter\":true," +
                "\"endOnExit\":true,\"record\":true,\"mute\":true,\"eventMethod\":\"GET\"," +
                "\"musicOnHoldUrl\":[\"http://example.com/music\"],\"eventUrl\":[\"https://example.com/event\"]," +
                "\"canSpeak\":[\"6a4d6af0-55a6-4667-be90-8614e4c8e83c\"],\"canHear\":[" +
                "\"416dfcfc-d86a-41c2-92ad-9a4fe1266898\"],\"transcription\":{},\"action\":\"conversation\"}]";
        assertEquals(expectedJson, new Ncco(action).toJson());
        TestUtils.testJsonableBaseObject(action);
    }

    @Test
    public void testGetAction() {
        ConversationAction conversation = ConversationAction.builder("test").build();
        assertEquals("conversation", conversation.getAction());
    }

    @Test
    public void testName() {
        ConversationAction conversationAction = ConversationAction.builder("test-name").name("rename").build();
        assertEquals("[{\"name\":\"rename\",\"action\":\"conversation\"}]", new Ncco(conversationAction).toJson());
    }

    private ConversationAction.Builder newBuilder() {
        return ConversationAction.builder("Test");
    }

    @Test
    public void testMusicOnHoldUrl() {
        ConversationAction conversation = newBuilder().musicOnHoldUrl("https://example.org").build();

        String expectedJson = "[{\"name\":\"Test\",\"musicOnHoldUrl\":[\"https://example.org\"],\"action\":\"conversation\"}]";
        assertEquals(expectedJson, new Ncco(conversation).toJson());
    }

    @Test
    public void testStartOnEnter() {
        ConversationAction conversation = newBuilder().startOnEnter(true).build();

        String expectedJson = "[{\"name\":\"Test\",\"startOnEnter\":true,\"action\":\"conversation\"}]";
        assertEquals(expectedJson, new Ncco(conversation).toJson());
    }

    @Test
    public void testEndOnExit() {
        ConversationAction conversation = newBuilder().endOnExit(true).build();

        String expectedJson = "[{\"name\":\"Test\",\"endOnExit\":true,\"action\":\"conversation\"}]";
        assertEquals(expectedJson, new Ncco(conversation).toJson());
    }

    @Test
    public void testRecord() {
        ConversationAction conversation = newBuilder().record(true).build();

        String expectedJson = "[{\"name\":\"Test\",\"record\":true,\"action\":\"conversation\"}]";
        assertEquals(expectedJson, new Ncco(conversation).toJson());
    }

    @Test
    public void testEventUrl() {
        ConversationAction conversation = newBuilder().eventUrl("https://example.org").build();

        String expectedJson = "[{\"name\":\"Test\",\"eventUrl\":[\"https://example.org\"],\"action\":\"conversation\"}]";
        assertEquals(expectedJson, new Ncco(conversation).toJson());
    }

    @Test
    public void testEventMethod() {
        ConversationAction conversation = newBuilder().eventMethod(EventMethod.POST).build();

        String expectedJson = "[{\"name\":\"Test\",\"eventMethod\":\"POST\",\"action\":\"conversation\"}]";
        assertEquals(expectedJson, new Ncco(conversation).toJson());
    }

    @Test
    public void testCanSpeak() {
        String uuid1 = UUID.randomUUID().toString().replace("-", ""), uuid2 = UUID.randomUUID().toString();

        ConversationAction.Builder builder = newBuilder().addCanSpeak(uuid1).addCanSpeak(uuid2);

        ConversationAction conversation = builder.build();
        var canSpeak = conversation.getCanSpeak();
        assertNotNull(canSpeak);
        assertEquals(2, canSpeak.size());
        Iterator<String> iter = canSpeak.iterator();
        assertEquals(uuid1, iter.next());
        assertEquals(uuid2, iter.next());
        assertTrue(conversation.toJson().contains("\"canSpeak\":[\""+uuid1+"\",\""+uuid2+"\"]"));

        conversation = builder.canSpeak(Collections.emptyList()).build();
        canSpeak = conversation.getCanSpeak();
        assertNotNull(canSpeak);
        assertTrue(canSpeak.isEmpty());
        assertTrue(conversation.toJson().contains("\"canSpeak\":[]"));

        conversation = builder.canSpeak(null).build();
        canSpeak = conversation.getCanSpeak();
        assertNull(canSpeak);
        assertFalse(conversation.toJson().contains("canSpeak"));

        conversation = builder
                .addCanSpeak(uuid1)
                .canSpeak(new ArrayList<>())
                .addCanSpeak(uuid2).addCanSpeak(uuid2)
                .build();
        canSpeak = conversation.getCanSpeak();
        assertNotNull(canSpeak);
        assertEquals(1, canSpeak.size());
        assertTrue(conversation.toJson().contains("\"canSpeak\":[\""+uuid2+"\"]"));
    }

    @Test
    public void testCanHear() {
        String uuid1 = UUID.randomUUID().toString(), uuid2 = UUID.randomUUID().toString().replace("-", "");

        ConversationAction.Builder builder = newBuilder().addCanHear(uuid1).addCanHear(uuid2);

        ConversationAction conversation = builder.build();
        var canHear = conversation.getCanHear();
        assertNotNull(canHear);
        assertEquals(2, canHear.size());
        Iterator<String> iter = canHear.iterator();
        assertEquals(uuid1, iter.next());
        assertEquals(uuid2, iter.next());
        assertTrue(conversation.toJson().contains("\"canHear\":[\""+uuid1+"\",\""+uuid2+"\"]"));

        conversation = builder.canHear(Collections.emptyList()).build();
        canHear = conversation.getCanHear();
        assertNotNull(canHear);
        assertTrue(canHear.isEmpty());
        assertTrue(conversation.toJson().contains("\"canHear\":[]"));

        conversation = builder.canHear(null).build();
        canHear = conversation.getCanHear();
        assertNull(canHear);
        assertFalse(conversation.toJson().contains("canHear"));

        conversation = builder
                .addCanHear(uuid1)
                .canHear(new ArrayList<>())
                .addCanHear(uuid2).addCanHear(uuid2)
                .build();
        canHear = conversation.getCanHear();
        assertNotNull(canHear);
        assertEquals(1, canHear.size());
        assertTrue(conversation.toJson().contains("\"canHear\":[\""+uuid2+"\"]"));
    }

    @Test
    public void testTranscriptionSettings() {
        TranscriptionSettings transcription = TranscriptionSettings.builder()
                .eventMethod(EventMethod.GET)
                .eventUrl("https://example.com/events")
                .language(SpeechSettings.Language.PERSIAN)
                .sentimentAnalysis(true).build();

        ConversationAction.Builder builder = newBuilder().transcription(transcription);
        assertThrows(IllegalStateException.class, builder::build);
        assertThrows(IllegalStateException.class, () -> builder.record(false).build());

        ConversationAction conversation = builder.record(true).build();
        TestUtils.testJsonableBaseObject(conversation);

        String expectedJsonFragment = "\"record\":true," +
                "\"transcription\":{\"language\":\"fa-IR\"," +
                "\"eventUrl\":[\"https://example.com/events\"]," +
                "\"eventMethod\":\"GET\",\"sentimentAnalysis\":true}";
        assertTrue(conversation.toJson().contains(expectedJsonFragment));
    }
}
