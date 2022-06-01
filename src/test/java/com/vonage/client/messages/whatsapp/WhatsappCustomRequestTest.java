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
package com.vonage.client.messages.whatsapp;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WhatsappCustomRequestTest {

	@Test
	public void testSerializeValid() {
		Map<String, Object> customObject = new LinkedHashMap<>();
		customObject.put("key1", "a string");
		customObject.put("key2", Arrays.asList("listVal1", "listVal2"));
		Map<String, Object> nestedMap = new LinkedHashMap<>();
		nestedMap.put("nestedKey1", "nestedString");
		nestedMap.put("nestedKey2", Arrays.asList("nestedListVal1", "nestedListVal2", "nestedListVal3"));
		customObject.put("key3", nestedMap);

		String json = WhatsappCustomRequest.builder()
				.from("Acme Corp").to("447900000001")
				.custom(customObject).build().toJson();

		assertTrue(json.contains("\"message_type\":\"custom\""));
		assertTrue(json.contains("\"channel\":\"whatsapp\""));
		assertTrue(json.contains("\"custom\":{\"key1\":\"a string\",\"key2\":[\"listVal1\",\"listVal2\"],\"key3\":{\"nestedKey1\":\"nestedString\",\"nestedKey2\":[\"nestedListVal1\",\"nestedListVal2\",\"nestedListVal3\"]}}"));
	}

	@Test
	public void testSerializeNoMap() {
		String json = WhatsappCustomRequest.builder()
				.from("Acme Corp").to("447900000001").build().toJson();

		assertTrue(json.contains("\"message_type\":\"custom\""));
		assertTrue(json.contains("\"channel\":\"whatsapp\""));
		assertFalse(json.contains("\"custom\":"));
	}

	@Test
	public void testSerializeEmptyMap() {
		String json = WhatsappCustomRequest.builder()
				.from("Acme Corp").to("447900000001")
				.custom(new HashMap<>(0)).build().toJson();

		assertTrue(json.contains("\"message_type\":\"custom\""));
		assertTrue(json.contains("\"channel\":\"whatsapp\""));
		assertTrue(json.contains("\"custom\":{}"));
	}
}
