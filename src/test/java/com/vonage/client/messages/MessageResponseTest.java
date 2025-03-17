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
package com.vonage.client.messages;

import com.vonage.client.Jsonable;
import com.vonage.client.TestUtils;
import com.vonage.client.VonageResponseParseException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;

public class MessageResponseTest {

	@Test
	public void testConstructFromValidJson() {
		UUID uuid = UUID.randomUUID();
		MessageResponse response = Jsonable.fromJson("{\"message_uuid\":\""+uuid+"\"}");
		assertEquals(uuid, response.getMessageUuid());
		String toString = response.toString();
		assertTrue(toString.contains("MessageResponse"));
		assertTrue(toString.contains(uuid.toString()));
		TestUtils.testJsonableBaseObject(response);
	}

	@Test
	public void testConstructFromEmptyJson() {
		MessageResponse response = Jsonable.fromJson("{}");
		assertNull(response.getMessageUuid());
		TestUtils.testJsonableBaseObject(response);
	}

	@Test
	public void testConstructFromInvalidJson() {
		assertThrows(VonageResponseParseException.class, () ->
				Jsonable.fromJson("{_malformed_}", MessageResponse.class)
		);
	}
}
