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
package com.vonage.client.messages;

import com.vonage.client.AbstractClientTest;
import com.vonage.client.Jsonable;
import com.vonage.client.messages.whatsapp.ReplyingIndicator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ReplyingIndicatorTest extends AbstractClientTest<ReplyingIndicator> {

	@Test
	public void testBuilderAllParameters() {
		ReplyingIndicator indicator = ReplyingIndicator.builder()
				.show(true)
				.type("text")
				.build();
		
		assertTrue(indicator.getShow());
		assertEquals("text", indicator.getType());
	}

	@Test
	public void testBuilderShowOnly() {
		ReplyingIndicator indicator = ReplyingIndicator.builder()
				.show(true)
				.build();
		
		assertTrue(indicator.getShow());
		assertNull(indicator.getType());
	}

	@Test
	public void testBuilderTypeOnly() {
		ReplyingIndicator indicator = ReplyingIndicator.builder()
				.type("text")
				.build();
		
		assertNull(indicator.getShow());
		assertEquals("text", indicator.getType());
	}

	@Test
	public void testBuilderShowFalse() {
		ReplyingIndicator indicator = ReplyingIndicator.builder()
				.show(false)
				.type("text")
				.build();
		
		assertFalse(indicator.getShow());
		assertEquals("text", indicator.getType());
	}

	@Test
	public void testBuilderEmpty() {
		ReplyingIndicator indicator = ReplyingIndicator.builder().build();
		
		assertNull(indicator.getShow());
		assertNull(indicator.getType());
	}

	@Test
	public void testSerializationAllFields() {
		String expectedJson = "{\"show\":true,\"type\":\"text\"}";
		ReplyingIndicator indicator = ReplyingIndicator.builder()
				.show(true)
				.type("text")
				.build();
		
		assertEquals(expectedJson, indicator.toJson());
	}

	@Test
	public void testSerializationShowOnly() {
		String expectedJson = "{\"show\":true}";
		ReplyingIndicator indicator = ReplyingIndicator.builder()
				.show(true)
				.build();
		
		assertEquals(expectedJson, indicator.toJson());
	}

	@Test
	public void testSerializationTypeOnly() {
		String expectedJson = "{\"type\":\"text\"}";
		ReplyingIndicator indicator = ReplyingIndicator.builder()
				.type("text")
				.build();
		
		assertEquals(expectedJson, indicator.toJson());
	}

	@Test
	public void testSerializationShowFalse() {
		String expectedJson = "{\"show\":false,\"type\":\"text\"}";
		ReplyingIndicator indicator = ReplyingIndicator.builder()
				.show(false)
				.type("text")
				.build();
		
		assertEquals(expectedJson, indicator.toJson());
	}

	@Test
	public void testDeserializationAllFields() {
		String json = "{\"show\":true,\"type\":\"text\"}";
		ReplyingIndicator indicator = Jsonable.fromJson(json, ReplyingIndicator.class);
		
		assertTrue(indicator.getShow());
		assertEquals("text", indicator.getType());
	}

	@Test
	public void testDeserializationShowOnly() {
		String json = "{\"show\":true}";
		ReplyingIndicator indicator = Jsonable.fromJson(json, ReplyingIndicator.class);
		
		assertTrue(indicator.getShow());
		assertNull(indicator.getType());
	}

	@Test
	public void testDeserializationTypeOnly() {
		String json = "{\"type\":\"text\"}";
		ReplyingIndicator indicator = Jsonable.fromJson(json, ReplyingIndicator.class);
		
		assertNull(indicator.getShow());
		assertEquals("text", indicator.getType());
	}

	@Test
	public void testDeserializationEmpty() {
		String json = "{}";
		ReplyingIndicator indicator = Jsonable.fromJson(json, ReplyingIndicator.class);
		
		assertNull(indicator.getShow());
		assertNull(indicator.getType());
	}

	@Test
	public void testDeserializationShowFalse() {
		String json = "{\"show\":false,\"type\":\"text\"}";
		ReplyingIndicator indicator = Jsonable.fromJson(json, ReplyingIndicator.class);
		
		assertFalse(indicator.getShow());
		assertEquals("text", indicator.getType());
	}
}
