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
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;

public class ListArchivesResponseTest {
	
	@Test
	public void testFromJsonAllFields() {
		Integer count = 4;
		String archive0Json = "{\"reason\":\"An excuse\",\"status\":\"paused\"," +
				"\"outputMode\":\"composed\",\"resolution\":\"1080x1920\"}",
				archive1Json = VideoClientTest.archiveJson,
				archive2Json = "{}",
				archive3Json = "{\"status\":\"u\",\"outputMode\":\"o\",\"resolution\":\"x\"," +
						"\"streamMode\":\"s\",\"hasVideo\":false,\"hasAudio\":true,\"name\":\"n\"}";
		List<String> archiveListJson = Arrays.asList(archive0Json, archive1Json, archive2Json, archive3Json);
	
		ListArchivesResponse response = ListArchivesResponse.fromJson("{\n" +
				"\"count\":"+count+",\n" +
				"\"items\":"+archiveListJson+"\n" +
		"}");
		
		assertEquals(count, response.getCount());
		List<Archive> items = response.getItems();
		assertEquals(count.intValue(), items.size());
		Archive archive0 = items.get(0), archive1 = items.get(1), archive2 = items.get(2), archive3 = items.get(3);
		VideoClientTest.assertArchiveEqualsExpectedJson(archive1);
		assertNotNull(archive2);
		assertNull(archive2.getId());
		assertNull(archive2.hasVideo());
		assertNull(archive2.hasAudio());
		assertNull(archive2.getStreams());
		assertNull(archive2.getStreamMode());
		assertEquals("An excuse", archive0.getReason());
		assertEquals(Resolution.FHD_PORTRAIT, archive0.getResolution());
		assertEquals(ArchiveStatus.PAUSED, archive0.getStatus());
		assertEquals(OutputMode.COMPOSED, archive0.getOutputMode());
		assertEquals(archive0Json, archive0.toJson());
		assertNull(archive3.getOutputMode());
		assertNull(archive3.getStatus());
		assertNull(archive3.getResolution());
		assertNull(archive3.getStreamMode());
		assertFalse(archive3.hasVideo());
		assertTrue(archive3.hasAudio());
		assertEquals("n", archive3.getName());
		assertEquals("{\"name\":\"n\",\"hasVideo\":false,\"hasAudio\":true}", archive3.toJson());
	}
	
	@Test(expected = VonageUnexpectedException.class)
	public void testFromJsonInvalid() {
		ListArchivesResponse.fromJson("{malformed]");
	}

	@Test
	public void testFromJsonEmpty() {
		ListArchivesResponse response = ListArchivesResponse.fromJson("{}");
		assertNull(response.getCount());
		assertNull(response.getItems());
	}

	@Test
	public void testFromJsonZeroItems() {
		ListArchivesResponse response = ListArchivesResponse.fromJson("{\"count\":0,\"items\":[]}");
		assertEquals(0, response.getCount().intValue());
		assertTrue(response.getItems().isEmpty());
	}
}