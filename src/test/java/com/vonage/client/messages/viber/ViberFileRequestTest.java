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
package com.vonage.client.messages.viber;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;

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

	@Test(expected = NullPointerException.class)
	public void testConstructNoUrl() {
		ViberFileRequest.builder()
				.name("report.pdf")
				.from("447900000001")
				.to("317900000002")
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructInvalidExtension() {
		ViberFileRequest.builder()
				.from("447900000001")
				.url("ftp://rel/path/to/hack.js")
				.to("317900000002")
				.build();
	}

	@Test
	public void testValidateFileExtensions() {
		String baseUrl = "http://www.example.org",
				fileName = "file", invalidExt = ".dll",
				baseUrlWithFile = baseUrl+"/"+fileName;
		String[] validExtensions = {"doc", "docx", "rtf", "dot", "dotx", "odt", "odf", "fodt", "txt", "info",
				"pdf", "xps", "pdax", "eps", "xls", "xlsx", "ods", "fods", "csv", "xlsm", "xltx"};

		for (String extension : validExtensions) {
			File urlOnly = new File(baseUrlWithFile + extension, null);
			File nameOnly = new File(baseUrl, fileName + extension);
			File urlAndFile = new File(baseUrlWithFile + extension, fileName + extension);
			assertEquals(urlOnly.getUrl(), urlAndFile.getUrl());
			assertEquals(nameOnly.getName(), urlAndFile.getName());
			assertNull(urlOnly.getName());
		}

		assertThrows(IllegalArgumentException.class, () -> new File(baseUrlWithFile + invalidExt, null));
		assertThrows(IllegalArgumentException.class, () -> new File(baseUrl, fileName + invalidExt));
	}
}
