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
package com.vonage.client.voice;

import static com.vonage.client.TestUtils.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.net.URI;

public class AnswerWebhookTest {

    @Test
    public void testSerdesAllParams() {
        String regionUrl = "https://api-us-3.vonage.com",
                endpointType = "phone", to = "442079460000",
                conversationId = "CON-55d50a94-7c84-484a-b1b7-f27633133cb4",
                uuid = "f7aebf19bd374d638f3532352f48901b",
                from = "447700900000", json = "{\n" +
                    "  \"to\": \""+to+"\",\n" +
                    "  \"from\": \""+from+"\",\n" +
                    "  \"endpoint_type\": \""+endpointType+"\",\n" +
                    "  \"conversation_uuid\": \""+conversationId+"\",\n" +
                    "  \"region_url\": \""+regionUrl+"\",\n" +
                    "  \"uuid\": \""+uuid+"\"\n}";

        AnswerWebhook parsed = AnswerWebhook.fromJson(json);
        testJsonableBaseObject(parsed);
        assertEquals(EndpointType.PHONE, parsed.getEndpointType());
        assertEquals(to, parsed.getTo());
        assertEquals(from, parsed.getFrom());
        assertEquals(conversationId, parsed.getConversationUuid());
        assertEquals(uuid, parsed.getUuid());
        assertEquals(URI.create(regionUrl), parsed.getRegionUrl());
    }

    @Test
    public void testFromUserOnly() {
        String fromUser = "client", json = "{\"from_user\":\""+fromUser+"\"}";
        AnswerWebhook parsed = AnswerWebhook.fromJson(json);
        testJsonableBaseObject(parsed);
        assertEquals(fromUser, parsed.getFrom());
        assertNull(parsed.getEndpointType());
        assertNull(parsed.getTo());
        assertNull(parsed.getUuid());
        assertNull(parsed.getConversationUuid());
        assertNull(parsed.getRegionUrl());
        assertNull(parsed.getCustomData());
    }

    @Test
    public void testEndpointTypeFromString() {
        assertNull(EndpointType.fromString(null));
        assertEquals(EndpointType.PHONE, EndpointType.fromString("phone"));
        assertEquals(EndpointType.VBC, EndpointType.fromString("vbc"));
        assertEquals(EndpointType.APP, EndpointType.fromString("app"));
        assertEquals(EndpointType.SIP, EndpointType.fromString("sip"));
        assertEquals(EndpointType.WEBSOCKET, EndpointType.fromString("websocket"));
    }

    @Test
    public void testSipHeaderUserToUser() {
        String userToUserHeader = "1234567890abcdef;encoding=hex",
                json = "{\"SipHeader_User-to-User\":\"" + userToUserHeader + "\"}";
        AnswerWebhook parsed = AnswerWebhook.fromJson(json);
        testJsonableBaseObject(parsed);
        assertEquals(userToUserHeader, parsed.getSipHeaderUserToUser());
        assertNull(parsed.getEndpointType());
        assertNull(parsed.getTo());
        assertNull(parsed.getFrom());
        assertNull(parsed.getUuid());
        assertNull(parsed.getConversationUuid());
        assertNull(parsed.getRegionUrl());
        assertNull(parsed.getCustomData());
    }

    @Test
    public void testSipHeaderUserToUserWithOtherFields() {
        String userToUserHeader = "abcd1234;encoding=hex",
                to = "442079460000",
                from = "447700900000",
                uuid = "f7aebf19bd374d638f3532352f48901b",
                json = "{\n" +
                    "  \"to\": \"" + to + "\",\n" +
                    "  \"from\": \"" + from + "\",\n" +
                    "  \"uuid\": \"" + uuid + "\",\n" +
                    "  \"SipHeader_User-to-User\": \"" + userToUserHeader + "\"\n}";

        AnswerWebhook parsed = AnswerWebhook.fromJson(json);
        testJsonableBaseObject(parsed);
        assertEquals(to, parsed.getTo());
        assertEquals(from, parsed.getFrom());
        assertEquals(uuid, parsed.getUuid());
        assertEquals(userToUserHeader, parsed.getSipHeaderUserToUser());
    }

    @Test
    public void testSipHeaderUserToUserMaxLength() {
        // Test with a 256-character string (maximum allowed)
        StringBuilder sb = new StringBuilder(260);
        for (int i = 0; i < 246; i++) {
            sb.append('a');
        }
        sb.append(";encoding=hex"); // 246 + 14 = 260 chars
        String userToUserHeader = sb.toString();
        String json = "{\"SipHeader_User-to-User\":\"" + userToUserHeader + "\"}";
        AnswerWebhook parsed = AnswerWebhook.fromJson(json);
        testJsonableBaseObject(parsed);
        assertEquals(userToUserHeader, parsed.getSipHeaderUserToUser());
    }
}
