/*
 *   Copyright 2023 Vonage
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

import com.vonage.client.VonageResponseParseException;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;
import java.util.Arrays;
import java.util.List;

public class ListArchivesResponseTest {
	
	@Test
	public void testFromJsonAllFields() {
		Integer count = 4;
		String archive0Json = "{\"resolution\":\"1080x1920\",\"reason\":\"An excuse\"," +
				"\"status\":\"paused\",\"outputMode\":\"composed\"}",
				archive1Json = VideoClientTest.archiveJson,
				archive2Json = "{}",
				archive3Json = "{\"status\":\"u\",\"outputMode\":\"o\",\"resolution\":\"x\"," +
						"\"streamMode\":\"s\",\"hasVideo\":false,\"hasAudio\":true,\"name\":\"n\"}";
		List<String> archiveListJson = Arrays.asList(archive0Json, archive1Json, archive2Json, archive3Json);
	
		ListArchivesResponse response = ListArchivesResponse.fromJson("{\n" +
				"\"count\":"+count+",\n" +
				"\"items\":"+archiveListJson+"\n" +
		"}");
		
		Assertions.assertEquals(count, response.getCount());
		List<Archive> items = response.getItems();
		Assertions.assertEquals(count.intValue(), items.size());
		Archive archive0 = items.get(0), archive1 = items.get(1), archive2 = items.get(2), archive3 = items.get(3);
		VideoClientTest.assertArchiveEqualsExpectedJson(archive1);
		Assertions.assertNotNull(archive2);
		Assertions.assertNull(archive2.getId());
		Assertions.assertNull(archive2.hasVideo());
		Assertions.assertNull(archive2.hasAudio());
		Assertions.assertNull(archive2.getStreams());
		Assertions.assertNull(archive2.getStreamMode());
		Assertions.assertEquals("An excuse", archive0.getReason());
		Assertions.assertEquals(Resolution.FHD_PORTRAIT, archive0.getResolution());
		Assertions.assertEquals(ArchiveStatus.PAUSED, archive0.getStatus());
		Assertions.assertEquals(OutputMode.COMPOSED, archive0.getOutputMode());
		Assertions.assertEquals(archive0Json, archive0.toJson());
		Assertions.assertNull(archive3.getOutputMode());
		Assertions.assertNull(archive3.getStatus());
		Assertions.assertNull(archive3.getResolution());
		Assertions.assertNull(archive3.getStreamMode());
		Assertions.assertFalse(archive3.hasVideo());
		Assertions.assertTrue(archive3.hasAudio());
		Assertions.assertEquals("n", archive3.getName());
		Assertions.assertEquals("{\"hasVideo\":false,\"hasAudio\":true,\"name\":\"n\"}", archive3.toJson());
	}
	
	@Test
	public void testFromJsonInvalid() {
		assertThrows(VonageResponseParseException.class, () -> ListArchivesResponse.fromJson("{malformed]"));
	}

	@Test
	public void testFromJsonEmpty() {
		ListArchivesResponse response = ListArchivesResponse.fromJson("{}");
		Assertions.assertNull(response.getCount());
		Assertions.assertNull(response.getItems());
	}

	@Test
	public void testFromJsonZeroItems() {
		ListArchivesResponse response = ListArchivesResponse.fromJson("{\"count\":0,\"items\":[]}");
		Assertions.assertEquals(0, response.getCount().intValue());
		Assertions.assertTrue(response.getItems().isEmpty());
	}
}