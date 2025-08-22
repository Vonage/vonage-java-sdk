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
import static com.vonage.client.video.VideoClientTest.*;
import com.vonage.client.video.Websocket.AudioRate;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.net.URI;
import java.util.Collections;
import java.util.Map;

public class ConnectRequestTest {
	final String uri = "wss://socket.example.org/audioStream";

	@Test
	public void testSerializeAllParams() {
		ConnectRequest request = ConnectRequest.builder()
				.token(token).uri(uri).sessionId(sessionId)
				.streams(Collections.singleton(VideoClientTest.randomId))
				.audioRate(AudioRate.L16_16K)
				.headers(Map.of("k1", "v1"))
				.bidirectional(true)
				.build();

		String expectedJson = "{\"sessionId\":\""+sessionId+"\",\"token\":\""+token+"\",\"websocket\":{\"uri\":\"" +
				uri+"\",\"streams\":[\""+randomId+"\"],\"headers\":{\"k1\":\"v1\"},\"audioRate\":16000,\"bidirectional\":true}}";

		assertEquals(expectedJson, request.toJson());
		testJsonableBaseObject(request);
		assertEquals(Boolean.TRUE, request.getWebsocket().getBidirectional());
	}

	@Test
	public void testSerializeRequiredParams() {
		ConnectRequest request = ConnectRequest.builder().token(token).sessionId(sessionId).uri(uri).build();
		String expectedJson = "{\"sessionId\":\""+sessionId+"\",\"token\":\""+token +
				"\",\"websocket\":{\"uri\":\""+uri+"\"}}";

		assertEquals(expectedJson, request.toJson());
		assertNull(request.getWebsocket().getBidirectional());
	}

	@Test
	public void testEmptyStreamsAndHeaders() {
		ConnectRequest request = ConnectRequest.builder()
				.token(token).uri(uri).sessionId(sessionId)
				.streams().headers(Map.of()).build();

		String expectedJson = "{\"sessionId\":\""+sessionId+"\",\"token\":\""+token +
				"\",\"websocket\":{\"uri\":\""+uri+"\",\"streams\":[],\"headers\":{}}}";

		assertEquals(expectedJson, request.toJson());
		testJsonableBaseObject(request);
	}

	@Test
	public void testUriProtocol() {
		ConnectRequest.Builder builder = ConnectRequest.builder().sessionId(sessionId).token(token);
		assertEquals(URI.create(uri), builder.uri(uri).build().getWebsocket().getUri());
		assertThrows(IllegalArgumentException.class, () -> builder.uri("https://example.com/ws-endpoint").build());
		assertNotNull(builder.uri("ws://example.com/ws-endpoint").build().getWebsocket().getUri());
		assertThrows(IllegalArgumentException.class, () -> builder.uri("ftp://example.com/ws-endpoint").build());
	}

	@Test
	public void testConstructMissingUri() {
		assertThrows(NullPointerException.class,
				() -> ConnectRequest.builder().token(token).sessionId(sessionId).build()
		);
	}

	@Test
	public void testConstructMissingSessionId() {
		assertThrows(NullPointerException.class,
				() -> ConnectRequest.builder().token(token).uri(uri).build()
		);
	}

	@Test
	public void testConstructMissingToken() {
		assertThrows(NullPointerException.class,
				() -> ConnectRequest.builder().sessionId(sessionId).uri(uri).build()
		);
	}

	@Test
	public void testParseAudioRate() {
		assertEquals(AudioRate.L16_8K, AudioRate.fromInt(8));
		assertEquals(AudioRate.L16_8K, AudioRate.fromInt(8000));
		assertEquals(AudioRate.L16_16K, AudioRate.fromInt(16));
		assertEquals(AudioRate.L16_16K, AudioRate.fromInt(16000));
		assertThrows(IllegalArgumentException.class, () -> AudioRate.fromInt(12_000));
	}
}
