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

import com.vonage.client.TestUtils;
import com.vonage.client.VonageResponseParseException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GetStreamResponseTest {
	
	@Test
	public void testFromJsonAllFields() {
		VideoType videoType = VideoType.CAMERA;
		assertEquals("camera", videoType.toString());
		String name = "";
		UUID id = UUID.fromString("8b732909-0a06-46a2-8ea8-074e64d43422");
		List<String> layoutClassList = Arrays.asList("full");
	
		GetStreamResponse response = GetStreamResponse.fromJson("{\n" +
				"\"videoType\":\""+videoType+"\",\n" +
				"\"name\":\""+name+"\",\n" +
				"\"id\":\""+id+"\",\n" +
				"\"layoutClassList\":[\"full\"]\n" +
		"}");

		TestUtils.testJsonableBaseObject(response);
		assertEquals(videoType, response.getVideoType());
		assertEquals(name, response.getName());
		assertEquals(id, response.getId());
		assertEquals(layoutClassList, response.getLayoutClassList());
	}
	
	@Test
	public void testFromJsonInvalid() {
		assertThrows(VonageResponseParseException.class, () -> GetStreamResponse.fromJson("{malformed]"));
	}

	@Test
	public void testFromJsonEmpty() {
		GetStreamResponse response = GetStreamResponse.fromJson("{}");
		TestUtils.testJsonableBaseObject(response);
		assertNull(response.getVideoType());
		assertNull(response.getName());
		assertNull(response.getId());
		assertNull(response.getLayoutClassList());
	}

	@Test
	public void testInvalidVideoType() {
		assertEquals(VideoType.CUSTOM, GetStreamResponse.fromJson("{\"videoType\":\"custom\"}").getVideoType());
		GetStreamResponse gsr = GetStreamResponse.fromJson("{\"videoType\":\"Dashcam\"}");
		assertNull(gsr.getVideoType());
	}
}