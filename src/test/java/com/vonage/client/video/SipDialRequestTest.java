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

import static org.junit.Assert.*;
import org.junit.Test;
import java.net.URI;
import java.util.Collections;

public class SipDialRequestTest {

	@Test
	public void testSerializeAllParams() {
		String sessionId = "flR1ZSBPY3QgMjkgMTI6MTM6MjMgUERUIDIwMTN",
				token = "X.Y.Z",
				uri = "sip:user@sip.partner.com",
				username = "fsurname123",
				password = "P@ssw0rd",
				from = "sender@example.com",
				h1k = "X-foo", h1v = "bar";

		SipDialRequest request = SipDialRequest.builder()
				.secure(true).observeForceMute(false).video(true)
				.addHeader(h1k, h1v)
				.uri(URI.create(uri), true)
				.password(password).username(username)
				.token(token).sessionId(sessionId)
				.from(from).build();

		String expectedJson = "{\"sessionId\":\""+sessionId+"\",\"token\":\""+token+"\",\"sip\":{\"uri\":\"" +
				uri+";transport=tls\",\"from\":\""+from+"\",\"headers\":{\""+h1k+"\":\""+h1v+"\"},\"auth\":{" +
				"\"username\":\""+username+"\",\"password\":\""+password+"\"},\"secure\":true,\"video\":true," +
				"\"observeForceMute\":false}}";
		assertEquals(expectedJson, request.toJson());

		assertEquals(sessionId, request.getSessionId());
		assertEquals(token, request.getToken());
		assertEquals(uri+";transport=tls", request.getUri());
		assertEquals(from, request.getFrom());
		assertEquals(username, request.getUsername());
		assertEquals(password, request.getPassword());
		assertEquals(1, request.getHeaders().size());
		assertEquals(h1v, request.getHeaders().get(h1k));
		assertTrue(request.getSecure());
		assertTrue(request.getVideo());
		assertFalse(request.getObserveForceMute());
	}

	@Test
	public void testSerializeRequiredParams() {
		String uri = "sip:name@sip.example.org", sessiondId = "SESSION", token = "TOKEN";
		SipDialRequest request = SipDialRequest.builder().uri(URI.create(uri), false)
				.sessionId(sessiondId).token(token).build();

		String expectedJson = "{\"sessionId\":\""+sessiondId+"\",\"token\":\""+token+"\"," +
				"\"sip\":{\"uri\":\""+uri+"\"}}";
		assertEquals(expectedJson, request.toJson());

		assertNull(request.getUsername());
		assertNull(request.getPassword());
		assertNull(request.getFrom());
		assertNull(request.getHeaders());
		assertNull(request.getVideo());
		assertNull(request.getSecure());
		assertNull(request.getObserveForceMute());
	}

	@Test(expected = NullPointerException.class)
	public void testConstructMissingUrl() {
		SipDialRequest.builder().token("TOKEN").sessionId("SESSION_ID").build();
	}

	@Test(expected = NullPointerException.class)
	public void testConstructMissingSessionId() {
		SipDialRequest.builder().token("TOKEN")
				.uri(URI.create("sip://user@example.com"), false).build();
	}

	@Test(expected = NullPointerException.class)
	public void testConstructMissingToken() {
		SipDialRequest.builder().sessionId("SESSION_ID")
				.uri(URI.create("sip://user@example.com"), false).build();
	}

	@Test(expected = IllegalStateException.class)
	public void testConstructMissingUsernameWhenPasswordProvided() {
		SipDialRequest.builder().token("TOKEN").sessionId("SESSION_ID")
				.uri(URI.create("sip://user@example.com"), false)
				.password("pa55WD").build();
	}

	@Test
	public void testSerializeEmptyHeaders() {
		SipDialRequest request = SipDialRequest.builder().token("TOKEN").sessionId("SESSION_ID")
				.uri(URI.create("sip://user@example.com"), false)
				.addHeaders(Collections.emptyMap()).build();

		String expectedJson = "{\"sessionId\":\"SESSION_ID\",\"token\":\"TOKEN\"," +
				"\"sip\":{\"uri\":\"sip://user@example.com\"}}";
		assertEquals(expectedJson, request.toJson());
	}
}
