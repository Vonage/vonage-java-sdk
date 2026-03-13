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

import com.vonage.client.TestUtils;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.*;

public class TransferActionTest {

    private static final String CONVERSATION_ID = "CON-f972836a-550f-45fa-956c-12a2ab5b7d22";
    private static final String UUID_1 = "9c132730-8c22-4760-a4dc-40502f05b444";
    private static final String UUID_2 = "416dfcfc-d86a-41c2-92ad-9a4fe1266898";

    @Test
    public void testBuilderMultipleInstances() {
        TransferAction.Builder builder = TransferAction.builder(CONVERSATION_ID);
        assertNotSame(builder.build(), builder.build());
    }

    @Test
    public void testGetAction() {
        TransferAction action = TransferAction.builder(CONVERSATION_ID).build();
        assertEquals("transfer", action.getAction());
    }

    @Test
    public void testMinimalAction() {
        TransferAction action = TransferAction.builder(CONVERSATION_ID).build();
        String expectedJson = "[{\"conversationId\":\"" + CONVERSATION_ID + "\",\"action\":\"transfer\"}]";
        assertEquals(expectedJson, new Ncco(action).toJson());
        TestUtils.testJsonableBaseObject(action);
    }

    @Test
    public void testAllFields() {
        TransferAction action = TransferAction.builder(CONVERSATION_ID)
                .mute(true)
                .addCanSpeak(UUID_1)
                .addCanHear(UUID_2)
                .build();

        String expectedJson = "[{\"conversationId\":\"" + CONVERSATION_ID + "\"," +
                "\"mute\":true,\"canSpeak\":[\"" + UUID_1 + "\"]," +
                "\"canHear\":[\"" + UUID_2 + "\"],\"action\":\"transfer\"}]";
        assertEquals(expectedJson, new Ncco(action).toJson());
        TestUtils.testJsonableBaseObject(action);
    }

    @Test
    public void testConversationIdRequired() {
        assertThrows(NullPointerException.class, () -> TransferAction.builder(null).build());
    }

    @Test
    public void testConversationIdCannotBeEmpty() {
        assertThrows(IllegalArgumentException.class, () -> TransferAction.builder("").build());
        assertThrows(IllegalArgumentException.class, () -> TransferAction.builder("   ").build());
    }

    @Test
    public void testConversationIdCanBeChanged() {
        String newId = "CON-different-id";
        TransferAction action = TransferAction.builder(CONVERSATION_ID)
                .conversationId(newId)
                .build();
        assertEquals(newId, action.getConversationId());
        assertTrue(action.toJson().contains("\"conversationId\":\"" + newId + "\""));
    }

    @Test
    public void testMute() {
        TransferAction action = TransferAction.builder(CONVERSATION_ID).mute(true).build();
        assertEquals(true, action.getMute());
        String expectedJson = "[{\"conversationId\":\"" + CONVERSATION_ID + "\",\"mute\":true,\"action\":\"transfer\"}]";
        assertEquals(expectedJson, new Ncco(action).toJson());

        action = TransferAction.builder(CONVERSATION_ID).mute(false).build();
        assertEquals(false, action.getMute());

        action = TransferAction.builder(CONVERSATION_ID).build();
        assertNull(action.getMute());
    }

    @Test
    public void testCanSpeak() {
        String uuid1 = UUID.randomUUID().toString().replace("-", "");
        String uuid2 = UUID.randomUUID().toString();

        TransferAction.Builder builder = TransferAction.builder(CONVERSATION_ID)
                .addCanSpeak(uuid1)
                .addCanSpeak(uuid2);

        TransferAction action = builder.build();
        var canSpeak = action.getCanSpeak();
        assertNotNull(canSpeak);
        assertEquals(2, canSpeak.size());
        Iterator<String> iter = canSpeak.iterator();
        assertEquals(uuid1, iter.next());
        assertEquals(uuid2, iter.next());
        assertTrue(action.toJson().contains("\"canSpeak\":[\"" + uuid1 + "\",\"" + uuid2 + "\"]"));

        action = builder.canSpeak(Collections.emptyList()).build();
        canSpeak = action.getCanSpeak();
        assertNotNull(canSpeak);
        assertTrue(canSpeak.isEmpty());
        assertTrue(action.toJson().contains("\"canSpeak\":[]"));

        action = builder.canSpeak(null).build();
        canSpeak = action.getCanSpeak();
        assertNull(canSpeak);
        assertFalse(action.toJson().contains("canSpeak"));

        action = builder
                .addCanSpeak(uuid1)
                .canSpeak(new ArrayList<>())
                .addCanSpeak(uuid2)
                .addCanSpeak(uuid2)
                .build();
        canSpeak = action.getCanSpeak();
        assertNotNull(canSpeak);
        assertEquals(1, canSpeak.size());
        assertTrue(action.toJson().contains("\"canSpeak\":[\"" + uuid2 + "\"]"));
    }

    @Test
    public void testCanHear() {
        String uuid1 = UUID.randomUUID().toString();
        String uuid2 = UUID.randomUUID().toString().replace("-", "");

        TransferAction.Builder builder = TransferAction.builder(CONVERSATION_ID)
                .addCanHear(uuid1)
                .addCanHear(uuid2);

        TransferAction action = builder.build();
        var canHear = action.getCanHear();
        assertNotNull(canHear);
        assertEquals(2, canHear.size());
        Iterator<String> iter = canHear.iterator();
        assertEquals(uuid1, iter.next());
        assertEquals(uuid2, iter.next());
        assertTrue(action.toJson().contains("\"canHear\":[\"" + uuid1 + "\",\"" + uuid2 + "\"]"));

        action = builder.canHear(Collections.emptyList()).build();
        canHear = action.getCanHear();
        assertNotNull(canHear);
        assertTrue(canHear.isEmpty());
        assertTrue(action.toJson().contains("\"canHear\":[]"));

        action = builder.canHear(null).build();
        canHear = action.getCanHear();
        assertNull(canHear);
        assertFalse(action.toJson().contains("canHear"));

        action = builder
                .addCanHear(uuid1)
                .canHear(new ArrayList<>())
                .addCanHear(uuid2)
                .addCanHear(uuid2)
                .build();
        canHear = action.getCanHear();
        assertNotNull(canHear);
        assertEquals(1, canHear.size());
        assertTrue(action.toJson().contains("\"canHear\":[\"" + uuid2 + "\"]"));
    }

    @Test
    public void testMultipleCanSpeakAdditions() {
        String uuid1 = "uuid-1";
        String uuid2 = "uuid-2";
        String uuid3 = "uuid-3";

        TransferAction action = TransferAction.builder(CONVERSATION_ID)
                .addCanSpeak(uuid1, uuid2, uuid3)
                .build();

        var canSpeak = action.getCanSpeak();
        assertNotNull(canSpeak);
        assertEquals(3, canSpeak.size());
        assertTrue(canSpeak.contains(uuid1));
        assertTrue(canSpeak.contains(uuid2));
        assertTrue(canSpeak.contains(uuid3));
    }

    @Test
    public void testMultipleCanHearAdditions() {
        String uuid1 = "uuid-1";
        String uuid2 = "uuid-2";
        String uuid3 = "uuid-3";

        TransferAction action = TransferAction.builder(CONVERSATION_ID)
                .addCanHear(uuid1, uuid2, uuid3)
                .build();

        var canHear = action.getCanHear();
        assertNotNull(canHear);
        assertEquals(3, canHear.size());
        assertTrue(canHear.contains(uuid1));
        assertTrue(canHear.contains(uuid2));
        assertTrue(canHear.contains(uuid3));
    }

    @Test
    public void testDuplicatesAreRemoved() {
        String uuid = "duplicate-uuid";

        TransferAction action = TransferAction.builder(CONVERSATION_ID)
                .addCanSpeak(uuid, uuid, uuid)
                .addCanHear(uuid, uuid)
                .build();

        assertEquals(1, action.getCanSpeak().size());
        assertEquals(1, action.getCanHear().size());
    }

    @Test
    public void testJsonRoundTrip() {
        TransferAction original = TransferAction.builder(CONVERSATION_ID)
                .mute(true)
                .addCanSpeak(UUID_1)
                .addCanHear(UUID_2)
                .build();

        String json = new Ncco(original).toJson();
        assertNotNull(json);
        assertTrue(json.contains("\"action\":\"transfer\""));
        assertTrue(json.contains("\"conversationId\":\"" + CONVERSATION_ID + "\""));
    }
}
