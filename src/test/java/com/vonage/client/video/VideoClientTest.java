/*
 *   Copyright 2022 Vonage
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

import com.vonage.client.ClientTest;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import java.util.UUID;

public class VideoClientTest extends ClientTest<VideoClient> {
	final String appId = UUID.randomUUID().toString();
	final String sessionId = UUID.randomUUID().toString();

	@Before
	public void setUp() {
		client = new VideoClient(wrapper);
	}

	@Test
	public void testCreateSession() throws Exception {
		String msUrl = "http://example.com/resource",
			createDt = "abc123",
			responseJson = "{\n" +
				"    \"session_id\": \""+sessionId+"\",\n" +
				"    \"application_id\": \""+appId+"\",\n" +
				"    \"create_dt\": \""+createDt+"\",\n" +
				"    \"media_server_url\": \""+msUrl+"\"\n" +
				"}";
		wrapper.setHttpClient(stubHttpClient(200, responseJson));
		CreateSessionRequest request = CreateSessionRequest.builder().build();
		CreateSessionResponse response = client.createSession(request);
		assertEquals(sessionId, response.getSessionId());
		assertEquals(appId, response.getApplicationId());
		assertEquals(createDt, response.getCreateDt());
		assertEquals(msUrl, response.getMediaServerUrl());
	}

}
