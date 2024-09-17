/*
 *   Copyright 2024 Vonage
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
package com.vonage.client.messages.rcs;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.Map;

public class RcsCustomRequestTest {

	@Test
	public void testSerializeValid() throws Exception {
		String from = "447900000001", to = "317900000002";
		var payload = Map.of("Foo", List.of("bar", -3.1459, false, Map.of()));

		var rcs = RcsCustomRequest.builder().from(from).custom(payload).to(to).build();

		String json = rcs.toJson();
		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
		assertTrue(json.contains("\"message_type\":\"custom\""));
		assertTrue(json.contains("\"channel\":\"rcs\""));
		assertTrue(json.contains("\"custom\":"+new ObjectMapper().writeValueAsString(payload)));
	}

	@Test
	public void testConstructNoPayload() {
		var rcs = RcsCustomRequest.builder().from("447900000001").to("317900000002").build();
		assertTrue(rcs.toJson().contains("\"custom\":{}"));
	}
}
