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
package com.vonage.client.messages.viber;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ViberFileRequestTest {

	@Test
	public void testSerializeValid() {
		String url = "file:///path/to/", name = "attachment.docx";
		String json = ViberFileRequest.builder()
				.from("Amy").to("447900000001")
				.url(url).name(name)
				.build().toJson();
		assertTrue(json.contains("\"file\":{\"url\":\""+url+"\",\"name\":\""+name+"\"}"));
		assertTrue(json.contains("\"message_type\":\"file\""));
		assertTrue(json.contains("\"channel\":\"viber_service\""));
	}

	@Test
	public void testConstructNoName() {
		String url = "ftp:///example.com/sheet.xls";
		String json = ViberFileRequest.builder()
				.from("447900000001").to("317900000002").url(url)
				.build().toJson();
		assertTrue(json.contains("\"file\":{\"url\":\""+url+"\"}"));
	}

	@Test
	public void testConstructNoUrl() {
		assertThrows(NullPointerException.class, () -> ViberFileRequest.builder()
				.name("report.pdf").from("447900000001").to("317900000002").build()
		);
	}

	@Test
	public void testConstructInvalidExtension() {
		assertThrows(IllegalArgumentException.class, () -> ViberFileRequest.builder()
				.from("447900000001").url("ftp://rel/path/to/hack.js").to("317900000002").build()
		);
	}

	@Test
	public void testValidateFileExtensions() {
		String baseUrl = "http://www.example.org",
				fileName = "file", invalidExt = ".dll",
				baseUrlWithFile = baseUrl+"/"+fileName;
		String[] validExtensions = {"doc", "docx", "rtf", "dot", "dotx", "odt", "odf", "fodt", "txt", "info",
				"pdf", "xps", "pdax", "eps", "xls", "xlsx", "ods", "fods", "csv", "xlsm", "xltx"};

		var builder = ViberFileRequest.builder().from("447900000001").to("447900000009");
		for (String extension : validExtensions) {
			var urlOnly = builder.url(baseUrlWithFile + extension).build();
			var nameOnly = builder.url(baseUrl).name(fileName + extension).build();
			var urlAndFile = builder.url(baseUrlWithFile + extension).name(fileName + extension).build();
			assertEquals(urlOnly.getFile().getUrl(), urlAndFile.getFile().getUrl());
			assertEquals(nameOnly.getFile().getName(), urlAndFile.getFile().getName());
		}

		assertThrows(IllegalArgumentException.class, () -> builder.url(baseUrlWithFile + invalidExt).name(null).build());
		assertThrows(IllegalArgumentException.class, () -> builder.url(baseUrl).name(fileName + invalidExt).build());
	}
}
