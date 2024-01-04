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
package com.vonage.client.messages.mms;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MmsVcardRequestTest {

	@Test
	public void testSerializeValid() {
		String from = "447900000001", to = "317900000002",
				url = "https://foo.tld/path/to/resource.vcf", caption = "!Alt text";

		MmsVcardRequest mms = MmsVcardRequest.builder().url(url).from(from).to(to).caption(caption).build();
		String json = mms.toJson();
		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
		assertTrue(json.contains("\"message_type\":\"vcard\""));
		assertTrue(json.contains("\"channel\":\"mms\""));
		assertTrue(json.contains("\"vcard\":{\"url\":\""+url+"\",\"caption\":\""+caption+"\"}"));
	}

	@Test
	public void testSerializeNoCaption() {
		String from = "447900000001", to = "317900000002",
				url = "https://foo.tld/path/to/resource.vcf";

		MmsVcardRequest mms = MmsVcardRequest.builder().url(url).from(from).to(to).build();
		String json = mms.toJson();
		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
		assertTrue(json.contains("\"message_type\":\"vcard\""));
		assertTrue(json.contains("\"channel\":\"mms\""));
		assertTrue(json.contains("\"vcard\":{\"url\":\""+url+"\"}"));
	}

	@Test
	public void testConstructNoUrl() {
		assertThrows(NullPointerException.class, () ->
				MmsVcardRequest.builder().caption("Cap").from("447900000001").to("317900000002").build()
		);
	}

	@Test
	public void testConstructInvalidExtension() {
		assertThrows(IllegalArgumentException.class, () -> MmsVcardRequest.builder()
				.to("317900000002").from("447900000001")
				.url("http://foo.tld/path/to/file.csv").build()
		);
	}
}
