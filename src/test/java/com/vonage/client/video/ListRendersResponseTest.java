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

import com.vonage.client.Jsonable;
import com.vonage.client.TestUtils;
import com.vonage.client.VonageResponseParseException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.UUID;

public class ListRendersResponseTest {

	@Test
	public void testEmptyConstructor() {
		ListRendersResponse response = new ListRendersResponse();
		assertNull(response.getCount());
		assertNull(response.getItems());
	}

	@Test
	public void testFromJsonSingleEmptyItem() {
		ListRendersResponse response = Jsonable.fromJson("{\"count\":\"0\",\n\"items\":[{}]}");
		TestUtils.testJsonableBaseObject(response);
		assertEquals(0, response.getCount());
		var items = response.getItems();
		assertNotNull(items);
		assertEquals(1, items.size());
		TestUtils.testJsonableBaseObject(items.getFirst());
	}
	
	@Test
	public void testFromJsonInvalid() {
		assertThrows(VonageResponseParseException.class,
				() -> Jsonable.fromJson("{malformed]", ListRendersResponse.class)
		);
	}

	@Test
	public void testFromJsonEmpty() {
		ListRendersResponse response = Jsonable.fromJson("{}");
		assertNull(response.getCount());
		assertNull(response.getItems());
	}
}