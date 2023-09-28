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
package com.vonage.client.messages.whatsapp;

import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

public class WhatsappTemplateRequestTest {

	private static final String FROM = "12124567890", TO = "447900000001", NAME = "verify";

	@Test
	public void testSerializeMandatoryFields() {
		String json = WhatsappTemplateRequest.builder()
				.from(FROM).to(TO).name(NAME).build().toJson();

		assertTrue(json.contains("\"from\":\""+FROM+"\""));
		assertTrue(json.contains("\"to\":\""+TO+"\""));
		assertTrue(json.contains("\"channel\":\"whatsapp\""));
		assertTrue(json.contains("\"message_type\":\"template\""));
		assertTrue(json.contains("\"template\":{\"name\":\""+NAME+"\"}"));
		assertTrue(json.contains("\"whatsapp\":{\"locale\":\"en\"}"));
	}

	@Test
	public void testSerializeAllFields() {
		String json = WhatsappTemplateRequest.builder()
				.from(FROM).to(TO).name("verify")
				.locale(Locale.ENGLISH_UK).policy(Policy.DETERMINISTIC)
				.parameters(Arrays.asList("{k1}", "blah"))
				.build().toJson();

		assertTrue(json.contains("\"channel\":\"whatsapp\""));
		assertTrue(json.contains("\"message_type\":\"template\""));
		assertTrue(json.contains("\"template\":{\"name\":\""+NAME+"\",\"parameters\":[\"{k1}\",\"blah\"]}"));
		assertTrue(json.contains("\"whatsapp\":{\"policy\":\"deterministic\",\"locale\":\"en_GB\"}"));
	}

	@Test(expected = NullPointerException.class)
	public void testConstructNoName() {
		WhatsappTemplateRequest.builder().from(FROM).to(TO).build();
	}

	@Test(expected = NullPointerException.class)
	public void testConstructNullLocale() {
		WhatsappTemplateRequest.builder()
				.locale(null).name(NAME)
				.from(FROM).to(TO).build();
	}

	@Test
	public void testSerializeEmptyParameters() {
		String json = WhatsappTemplateRequest.builder()
				.from(FROM).to(TO)
				.parameters(Collections.emptyList())
				.name(NAME).build().toJson();

		assertTrue(json.contains("\"template\":{\"name\":\"verify\",\"parameters\":[]}"));
	}

	@Test
	public void testSerializeParametersEmptyString() {
		String json = WhatsappTemplateRequest.builder()
				.from(FROM).to(TO).name(NAME)
				.parameters(Collections.singletonList(""))
				.build().toJson();

		assertTrue(json.contains("\"template\":{\"name\":\""+NAME+"\",\"parameters\":[\"\"]}"));
	}

	@Test
	public void testSerializeParametersMultipleStrings() {
		String json = WhatsappTemplateRequest.builder()
				.from(FROM).to(TO).name(NAME)
				.parameters(Arrays.asList("{1}","{2}"))
				.build().toJson();

		assertTrue(json.contains("\"template\":{\"name\":\""+NAME+"\",\"parameters\":[\"{1}\",\"{2}\"]}"));
	}
}
