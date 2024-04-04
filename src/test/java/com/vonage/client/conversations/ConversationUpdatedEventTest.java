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
package com.vonage.client.conversations;

import com.vonage.client.TestUtils;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.*;

public class ConversationUpdatedEventTest extends AbstractEventTest {

    @Test
    public void testParseConversationUpdatedEvent() {
        var event = parseEvent(EventType.CONVERSATION_UPDATED, ConversationUpdatedEvent.class, STR."""
            {
               "type": "conversation:updated",
               "body": {
                    "id": "CON-\{randomId}",
                    "name": "Test_conv",
                    "timestamp": {
                       "created": "2020-01-01T14:00:00.02Z",
                       "updated": "2020-01-01T14:05:00.00Z",
                       "destroyed": "2020-01-01T14:20:00.36Z"
                    },
                    "display_name": "Conversation DP",
                    "image_url": "https://example.org/pic.png",
                    "state": "ACTIVE"
               }
            }
            """
        );
        BaseConversation conversation = event.getConversation();
        TestUtils.testJsonableBaseObject(conversation);
        assertNotNull(conversation.getName());
        assertNotNull(conversation.getId());
        assertNotNull(conversation.getDisplayName());
        assertNotNull(conversation.getImageUrl());
        //assertNotNull(conversation.getState());
        var timestamp = conversation.getTimestamp();
        assertNotNull(timestamp);
        assertNotNull(timestamp.getCreated());
        assertNotNull(timestamp.getUpdated());
        assertNotNull(timestamp.getDestroyed());
    }
}
