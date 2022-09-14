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

import com.vonage.client.VonageUnexpectedException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import java.util.UUID;

public class ListStreamsResponseTest {

	@Test
	public void testFromJsonAllFields() {
		String id1 = UUID.randomUUID().toString(), name0 = "LiveStream 1";
		Integer count = 2;
		ListStreamsResponse response = ListStreamsResponse.fromJson("{\n" +
				"\"count\":\""+count+"\",\n" +
				"\"items\":[{\n" +
				"  \"id\": \"8b732909-0a06-46a2-8ea8-074e64d43422\",\n" +
				"  \"videoType\": \"camera\",\n" +
				"  \"name\": \""+name0+"\",\n" +
				"  \"layoutClassList\": [\n" +
				"    \"full\"\n" +
				"   ]\n" +
				"},{\n" +
				"  \"id\": \""+id1+"\",\n" +
				"  \"layoutClassList\": []\n" +
				"}]}"
		);
		
		assertEquals(count, response.getCount());
		assertEquals(count.intValue(), response.getItems().size());
		GetStreamResponse gsr0 = response.getItems().get(0), gsr1 = response.getItems().get(1);
		assertEquals(VideoType.CAMERA, gsr0.getVideoType());
		assertEquals(name0, gsr0.getName());
		assertEquals(1, gsr0.getLayoutClassList().size());
		assertNull(gsr1.getVideoType());
		assertNull(gsr1.getName());
		assertEquals(id1, gsr1.getId());
		assertEquals(0, gsr1.getLayoutClassList().size());
	}
	
	@Test(expected = VonageUnexpectedException.class)
	public void testFromJsonInvalid() {
		ListStreamsResponse.fromJson("{malformed]");
	}

	@Test
	public void testFromJsonEmpty() {
		ListStreamsResponse response = ListStreamsResponse.fromJson("{}");
		assertNull(response.getCount());
		assertNull(response.getItems());
	}
}