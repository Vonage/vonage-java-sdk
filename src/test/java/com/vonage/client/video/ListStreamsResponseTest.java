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
import java.util.Arrays;
import java.util.List;

public class ListStreamsResponseTest {
	private ListStreamsResponse endpoint;

	@Test
	public void testFromJsonAllFields() {
		Integer count = 1;
		List<GetStreamResponse> items = Arrays.asList();
	
		ListStreamsResponse response = ListStreamsResponse.fromJson("{\n" +
				"\"count\":\""+count+"\",\n" +
				"\"items\":[]\n" +
		"}");
		
		assertEquals(count, response.getCount());
		assertEquals(items, response.getItems());
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