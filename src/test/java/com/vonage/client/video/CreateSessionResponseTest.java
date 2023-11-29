/* Copyright 2023 Vonage
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
package com.vonage.client.video;

import com.vonage.client.VonageResponseParseException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.net.URI;
import java.util.UUID;

public class CreateSessionResponseTest {

	@Test
	public void testFromJsonAllFields() {
		UUID applicationId = UUID.randomUUID();
		URI mediaServerUrl = URI.create("ftp://myserver.data/resource");
		String sessionId = "SESSION_ID-123",
			createDt = "abc123";

		CreateSessionResponse response = CreateSessionResponse.fromJson("[{\n" +
				"\"session_id\":\""+sessionId+"\",\n" +
				"\"application_id\":\""+applicationId+"\",\n" +
				"\"create_dt\":\""+createDt+"\",\n" +
				"\"media_server_url\":\""+mediaServerUrl+"\"\n" +
		"}]");

		Assertions.assertEquals(sessionId, response.getSessionId());
		Assertions.assertEquals(applicationId, response.getApplicationId());
		Assertions.assertEquals(createDt, response.getCreateDt());
		Assertions.assertEquals(mediaServerUrl, response.getMediaServerUrl());
	}

	@Test
	public void testFromJsonInvalid() {
		assertThrows(VonageResponseParseException.class, () -> CreateSessionResponse.fromJson("{malformed]"));
	}

	@Test
	public void testFromJsonEmptyObject() {
		CreateSessionResponse response = CreateSessionResponse.fromJson("[{}]");
		Assertions.assertNull(response.getApplicationId());
		Assertions.assertNull(response.getSessionId());
		Assertions.assertNull(response.getMediaServerUrl());
		Assertions.assertNull(response.getCreateDt());
	}

	@Test
	public void testFromJsonEmptyArray() {
		CreateSessionResponse response = CreateSessionResponse.fromJson("[]");
		Assertions.assertNotNull(response);
	}

	@Test
	public void testFromJsonMultipleEntries() {
		String sessionId = "TheSessionIdYouWant";
		String json = "[{\"session_id\":\""+sessionId+"\"},{},{\"session_id\":\"fake\"}]";
		CreateSessionResponse response = CreateSessionResponse.fromJson(json);
		Assertions.assertNotNull(response);
		Assertions.assertEquals(sessionId, response.getSessionId());
		Assertions.assertNull(response.getApplicationId());
		Assertions.assertNull(response.getMediaServerUrl());
		Assertions.assertNull(response.getCreateDt());
	}
}
