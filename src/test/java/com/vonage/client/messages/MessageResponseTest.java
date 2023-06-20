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
package com.vonage.client.messages;

import com.vonage.client.VonageResponseParseException;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.UUID;

public class MessageResponseTest {

	@Test
	public void testConstructFromValidJson() {
		UUID uuid = UUID.randomUUID();
		MessageResponse response = MessageResponse.fromJson("{\"message_uuid\":\""+uuid+"\"}");
		assertEquals(uuid, response.getMessageUuid());
		String toString = response.toString();
		assertTrue(toString.contains("MessageResponse"));
		assertTrue(toString.contains(uuid.toString()));
	}

	@Test
	public void testConstructFromEmptyJson() {
		MessageResponse response = MessageResponse.fromJson("{}");
		assertNull(response.getMessageUuid());
	}

	@Test(expected = VonageResponseParseException.class)
	public void testConstructFromInvalidJson() {
		MessageResponse.fromJson("{_malformed_}");
	}

	@Test(expected = VonageResponseParseException.class)
	public void testConstructFromEmptyString() {
		MessageResponse.fromJson("");
	}
}
