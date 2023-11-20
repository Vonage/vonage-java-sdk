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
import org.junit.jupiter.api.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class SetStreamLayoutRequestTest {
	final String sessionId = UUID.randomUUID().toString();

	@Test
	public void testSerializeAllParameters() {
		String
				id0 = UUID.randomUUID().toString(),
				id1 = UUID.randomUUID().toString(),
				id2 = UUID.randomUUID().toString(),
				id3 = UUID.randomUUID().toString(),
				id4 = UUID.randomUUID().toString();
		List<SessionStream> streams = Arrays.asList(
				SessionStream.builder(id0).layoutClassList(Collections.emptyList()).build(),
				SessionStream.builder(id1).layoutClassList(Arrays.asList("full")).build(),
				SessionStream.builder(id2).build(),
				SessionStream.builder(id3).layoutClassList().build(),
				SessionStream.builder(id4).layoutClassList("focus", "min").build()
		);
		SetStreamLayoutRequest request = new SetStreamLayoutRequest(sessionId, streams);
		String expectedJson = "{\"items\":[" +
				"{\"id\":\""+id0+"\",\"layoutClassList\":[]}," +
				"{\"id\":\""+id1+"\",\"layoutClassList\":[\"full\"]}," +
				"{\"id\":\""+id2+"\"}," +
				"{\"id\":\""+id3+"\",\"layoutClassList\":[]}," +
				"{\"id\":\""+id4+"\",\"layoutClassList\":[\"focus\",\"min\"]}" +
			"]}";
		Assertions.assertEquals(expectedJson, request.toJson());
		Assertions.assertEquals(sessionId, request.sessionId);
	}

	@Test
	public void testConstructInvalidStreamId() {
		assertThrows(IllegalArgumentException.class, () ->
				SessionStream.builder("abc123").build()
		);
	}

	@Test
	public void testEmptyStreamList() {
		assertThrows(IllegalArgumentException.class, () ->
				new SetStreamLayoutRequest(sessionId, Collections.emptyList())
		);
	}

	@Test
	public void testNullStreamList() {
		assertThrows(IllegalArgumentException.class, () ->
				new SetStreamLayoutRequest(sessionId, null)
		);
	}
}
