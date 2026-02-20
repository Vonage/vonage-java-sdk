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
package com.vonage.client.video;

import com.vonage.client.Jsonable;
import com.vonage.client.TestUtils;
import com.vonage.client.VonageResponseParseException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ListConnectionsResponseTest {

	private String loadJsonResource(String filename) throws IOException {
		try (InputStream is = getClass().getResourceAsStream(filename)) {
			if (is == null) {
				throw new IOException("Could not find resource: " + filename);
			}
			byte[] buffer = new byte[1024];
			StringBuilder sb = new StringBuilder();
			int bytesRead;
			while ((bytesRead = is.read(buffer)) != -1) {
				sb.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
			}
			return sb.toString().trim();
		}
	}

	@Test
	public void testEmptyConstructor() {
		ListConnectionsResponse response = new ListConnectionsResponse();
		assertNull(response.getCount());
		assertNull(response.getItems());
		assertNull(response.getProjectId());
		assertNull(response.getSessionId());
	}

	@Test
	public void testFromJsonAllFields() throws Exception {
		UUID connectionId0 = UUID.randomUUID();
		UUID connectionId1 = UUID.randomUUID();
		UUID projectId = UUID.randomUUID();
		String sessionId = "2_MX40NTMyMTgwMn5-MTU4QzE0N2QyNzY0NzY1MjYyNV5T";
		Integer count = 2;
		Long createdAt0 = 1384221730000L;
		Long createdAt1 = 1384221740000L;
		
		String json = loadJsonResource("list-connections-response-full.json")
				.replace("PROJECT_ID_PLACEHOLDER", projectId.toString())
				.replace("SESSION_ID_PLACEHOLDER", sessionId)
				.replace("CONNECTION_ID_0_PLACEHOLDER", connectionId0.toString())
				.replace("CONNECTION_ID_1_PLACEHOLDER", connectionId1.toString());
		
		ListConnectionsResponse response = Jsonable.fromJson(json);

		TestUtils.testJsonableBaseObject(response);
		assertEquals(count, response.getCount());
		assertEquals(projectId, response.getProjectId());
		assertEquals(sessionId, response.getSessionId());
		assertEquals(count.intValue(), response.getItems().size());
		
		Connection conn0 = response.getItems().get(0);
		assertEquals(connectionId0, conn0.getConnectionId());
		assertEquals(ConnectionState.CONNECTED, conn0.getConnectionState());
		assertEquals(createdAt0, conn0.getCreatedAt());
		
		Connection conn1 = response.getItems().get(1);
		assertEquals(connectionId1, conn1.getConnectionId());
		assertEquals(ConnectionState.CONNECTING, conn1.getConnectionState());
		assertEquals(createdAt1, conn1.getCreatedAt());
	}
	
	@Test
	public void testFromJsonInvalid() {
		assertThrows(VonageResponseParseException.class,
				() -> Jsonable.fromJson("{malformed]", ListConnectionsResponse.class)
		);
	}

	@Test
	public void testFromJsonEmpty() {
		ListConnectionsResponse response = Jsonable.fromJson("{}");
		assertNull(response.getCount());
		assertNull(response.getItems());
		assertNull(response.getProjectId());
		assertNull(response.getSessionId());
	}
}
