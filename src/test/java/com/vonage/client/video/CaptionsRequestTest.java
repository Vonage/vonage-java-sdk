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

import static com.vonage.client.TestUtils.testJsonableBaseObject;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.net.URI;

public class CaptionsRequestTest {
	private final String sessionId = "flR1ZSBPY3QgMjkgMTI6MTM6MjMgUERUIDIwMTN", token = "X.Y.Z";

	@Test
	public void testSerializeAllParams() {
		int maxDuration = 7200;
		boolean partialCaptions = false;
		Language language = Language.JA_JP;
		String statusCallbackUrl = "https://example.org/video/cb/status";

		var request = CaptionsRequest.builder()
				.partialCaptions(partialCaptions)
				.maxDuration(maxDuration)
				.sessionId(sessionId)
				.languageCode(language)
				.statusCallbackUrl(statusCallbackUrl)
				.token(token)
				.build();

		String expectedJson = "{\"sessionId\":\"" + sessionId + "\",\"token\":\"" + token +
				"\",\"languageCode\":\"ja-JP\",\"maxDuration\":" + maxDuration +
				",\"partialCaptions\":" + partialCaptions +
				",\"statusCallbackUrl\":\"" + statusCallbackUrl + "\"}";
		assertEquals(expectedJson, request.toJson());

		testJsonableBaseObject(request);
		assertEquals(sessionId, request.getSessionId());
		assertEquals(token, request.getToken());
		assertEquals(partialCaptions, request.partialCaptions());
		assertEquals(maxDuration, request.getMaxDuration());
		assertEquals(URI.create(statusCallbackUrl), request.getStatusCallbackUrl());
		assertEquals(language, request.getLanguageCode());
	}

	@Test
	public void testSerializeRequiredParams() {
		var request = CaptionsRequest.builder().sessionId(sessionId).token(token).build();

		String expectedJson = "{\"sessionId\":\""+sessionId+"\",\"token\":\""+token+"\"}";
		assertEquals(expectedJson, request.toJson());

		testJsonableBaseObject(request);
		assertEquals(sessionId, request.getSessionId());
		assertEquals(token, request.getToken());
		assertNull(request.getLanguageCode());
		assertNull(request.getMaxDuration());
		assertNull(request.getStatusCallbackUrl());
	}

	@Test
	public void testMaxDurationBounds() {
		var builder = CaptionsRequest.builder().sessionId(sessionId).token(token);
		int min = 300, max = 14400;
		assertEquals(min, builder.maxDuration(min).build().getMaxDuration());
		assertEquals(max, builder.maxDuration(max).build().getMaxDuration());
		assertThrows(IllegalArgumentException.class, () -> builder.maxDuration(min - 1).build());
		assertThrows(IllegalArgumentException.class, () -> builder.maxDuration(max + 1).build());
	}

	@Test
	public void testStatusCallbackUriLength() {
		var builder = CaptionsRequest.builder().sessionId(sessionId).token(token);
		String fourteen = "https://t.co/b";
		assertEquals(14, fourteen.length());
		assertThrows(IllegalArgumentException.class, () -> builder.statusCallbackUrl(fourteen).build());
		assertEquals(15, builder
				.statusCallbackUrl(fourteen + 'a')
				.build().getStatusCallbackUrl().toString().length()
		);
		String twoThousandAndFourtySeven = fourteen + "lahb".repeat(508) + 'x';
		assertEquals(2047, twoThousandAndFourtySeven.length());
		assertEquals(2048, builder
				.statusCallbackUrl(twoThousandAndFourtySeven + 'y')
				.build().getStatusCallbackUrl().toString().length()
		);
		assertThrows(IllegalArgumentException.class, () ->
				builder.statusCallbackUrl(twoThousandAndFourtySeven + "yz").build()
		);
	}

	@Test
	public void testConstructMissingSessionId() {
		assertThrows(NullPointerException.class,
				() -> CaptionsRequest.builder().token(token).build()
		);
	}

	@Test
	public void testConstructMissingToken() {
		assertThrows(NullPointerException.class,
				() -> CaptionsRequest.builder().sessionId(sessionId).build()
		);
	}

	@Test
	public void testUnknownLanguage() {
		assertNull(Language.fromString("fa-IR"));
	}

	@Test
	public void testNullLanguage() {
		assertNull(Language.fromString(null));
	}
}
