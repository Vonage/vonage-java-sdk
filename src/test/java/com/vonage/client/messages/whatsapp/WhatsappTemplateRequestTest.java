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
import java.util.Collections;

import static org.junit.Assert.*;

public class WhatsappTemplateRequestTest {

	@Test
	public void testSerializeMandatoryFields() {
		String json = WhatsappTemplateRequest.builder()
				.from("Acme Corp").to("447900000001")
				.name("verify").locale("en-GB")
				.build().toJson();

		assertTrue(json.contains("\"from\":\"Acme Corp\""));
		assertTrue(json.contains("\"to\":\"447900000001\""));
		assertTrue(json.contains("\"channel\":\"whatsapp\""));
		assertTrue(json.contains("\"message_type\":\"template\""));
		assertTrue(json.contains("\"template\":{\"name\":\"verify\"}"));
		assertTrue(json.contains("\"whatsapp\":{\"policy\":\"deterministic\",\"locale\":\"en-GB\"}"));
	}

	@Test
	public void testSerializeAllFields() {
		String json = WhatsappTemplateRequest.builder()
				.from("Acme Corp").to("447900000001")
				.name("verify").locale("en-GB").policy(Policy.DETERMINISTIC)
				.parameters(Arrays.asList(Collections.singletonMap("k1", "v1"), "blah"))
				.build().toJson();

		assertTrue(json.contains("\"channel\":\"whatsapp\""));
		assertTrue(json.contains("\"message_type\":\"template\""));
		assertTrue(json.contains("\"template\":{\"name\":\"verify\",\"parameters\":[{\"k1\":\"v1\"},\"blah\"]}"));
		assertTrue(json.contains("\"whatsapp\":{\"policy\":\"deterministic\",\"locale\":\"en-GB\"}"));
	}

	@Test(expected = NullPointerException.class)
	public void testConstructNullPolicy() {
		WhatsappTemplateRequest.builder()
				.from("Acme Corp").to("447900000001")
				.name("verify").locale("en-GB").policy(null)
				.build();
	}

	@Test(expected = NullPointerException.class)
	public void testConstructNoName() {
		WhatsappTemplateRequest.builder()
				.from("Acme Corp").to("447900000001")
				.locale("en-GB").build();
	}

	@Test(expected = NullPointerException.class)
	public void testConstructNullLocale() {
		WhatsappTemplateRequest.builder()
				.from("Acme Corp").to("447900000001")
				.name("verify").locale(null).build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructEmptyLocale() {
		WhatsappTemplateRequest.builder()
				.locale("").name("verify").from("Acme Corp")
				.to("447900000001").build();
	}

	@Test
	public void testSerializeEmptyParameters() {
		String json = WhatsappTemplateRequest.builder()
				.from("Acme Corp").to("447900000001")
				.name("verify").locale("en-GB")
				.parameters(Collections.emptyList())
				.build().toJson();

		assertTrue(json.contains("\"template\":{\"name\":\"verify\",\"parameters\":[]}"));
	}

	@Test
	public void testSerializeParametersEmptyMap() {
		String json = WhatsappTemplateRequest.builder()
				.from("Acme Corp").to("447900000001")
				.name("verify").locale("en-GB")
				.parameters(Collections.singletonList(Collections.emptyMap()))
				.build().toJson();

		assertTrue(json.contains("\"template\":{\"name\":\"verify\",\"parameters\":[{}]}"));
	}

	@Test
	public void testSerializeParametersMultipleEmptyMaps() {
		String json = WhatsappTemplateRequest.builder()
				.from("Acme Corp").to("447900000001")
				.name("verify").locale("en-GB")
				.parameters(Arrays.asList(Collections.emptyMap(), Collections.emptyMap()))
				.build().toJson();

		assertTrue(json.contains("\"template\":{\"name\":\"verify\",\"parameters\":[{},{}]}"));
	}
}
