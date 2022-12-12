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

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class MuteSessionRequestTest {
	final String sessionId = UUID.randomUUID().toString();

	@Test
	public void testSerialize() {
		List<String> excludedStreamIds = Arrays.asList("ID_0", "ID_1", "ID_2");
		MuteSessionRequest request = new MuteSessionRequest(sessionId, true, excludedStreamIds);
		String expectedJson = "{\"active\":true,\"excludedStreamIds\":[\"ID_0\",\"ID_1\",\"ID_2\"]}";
		assertEquals(expectedJson, request.toJson());
	}

	@Test
	public void testSerializeEmptyList() {
		MuteSessionRequest request = new MuteSessionRequest(sessionId, false, Collections.emptyList());
		String expectedJson = "{\"active\":false,\"excludedStreamIds\":[]}";
		assertEquals(expectedJson, request.toJson());
	}

	@Test
	public void testSerializeNullIds() {
		List<String> streamIds = null;
		MuteSessionRequest request = new MuteSessionRequest(sessionId, false, streamIds);
		String expectedJson = "{\"active\":false}";
		assertEquals(expectedJson, request.toJson());
	}

}