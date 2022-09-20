/*
 *   Copyright 222 Vonage
 *
 *   Licensed under the Apache License, Version 2. (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class SetStreamLayoutRequestTest {

	@Test
	public void testSerializeAllParameters() {
		String sessionId = UUID.randomUUID().toString();
		List<SessionStream> streams = Arrays.asList(
				SessionStream.builder("stream0")
						.layoutClassList(Collections.emptyList()).build(),
				SessionStream.builder("stream1")
						.layoutClassList(Arrays.asList("full")).build(),
				SessionStream.builder("stream2").build()
		);
		SetStreamLayoutRequest request = new SetStreamLayoutRequest(sessionId, streams);
		String expectedJson = "{\"items\":[{\"id\":\"stream0\",\"layoutClassList\":[]}," +
				"{\"id\":\"stream1\",\"layoutClassList\":[\"full\"]},{\"id\":\"stream2\"}]}";
		assertEquals(expectedJson, request.toJson());
		assertEquals(sessionId, request.sessionId);
	}

	@Test
	public void testSerializeNullAndEmptyParameters() {
		SetStreamLayoutRequest request = new SetStreamLayoutRequest(null, Collections.emptyList());
		assertEquals("{\"items\":[]}", request.toJson());
		request = new SetStreamLayoutRequest("", null);
		assertEquals("{}", request.toJson());
		assertEquals("", request.sessionId);
	}
}
