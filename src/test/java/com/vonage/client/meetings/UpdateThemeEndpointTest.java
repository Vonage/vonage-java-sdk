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
package com.vonage.client.meetings;

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import com.vonage.client.TestUtils;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.UUID;

public class UpdateThemeEndpointTest {
	private UpdateThemeEndpoint endpoint;
	
	@Before
	public void setUp() {
		endpoint = new UpdateThemeEndpoint(new HttpWrapper(new JWTAuthMethod("app-id", new byte[0])));
	}
	
	@Test
	public void testMakeRequestAllParameters() throws Exception {
		String themeId = "2f310f5a-995c-4f7e-9072-6aafbf6a4205";
		Theme request = Theme.builder()
				.shortCompanyUrl("developer.vonage.com").brandText("Vonage (purple)")
				.themeName("Vonage theme").mainColor("#8a1278").build();

		request.themeId = UUID.fromString(themeId);
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals("PATCH", builder.getMethod());
		String expectedUri = "https://api-eu.vonage.com/v1/meetings/themes/"+themeId;
		assertEquals(expectedUri, builder.build().getURI().toString());

		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		String expectedRequest = "{\"update_details\":{\"theme_name\":\"Vonage theme\",\"main_color\":" +
				"\"#8a1278\",\"short_company_url\":\"developer.vonage.com\",\"brand_text\":\"Vonage (purple)\"}}";
		assertEquals(expectedRequest, EntityUtils.toString(builder.getEntity()));
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		String expectedResponse = "{\"nonsense\":false,\"brand_image_colored_url\":\"ftp://example.com/logo.png\"}";
		Theme parsed = endpoint.parseResponse(TestUtils.makeJsonHttpResponse(200, expectedResponse));
		assertNotNull(parsed);
		assertEquals(request, parsed);
		assertEquals("ftp://example.com/logo.png", parsed.getBrandImageColoredUrl().toString());
	}

	@Test
	public void testCustomUri() throws Exception {
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri(baseUri).build());
		endpoint = new UpdateThemeEndpoint(wrapper);
		Theme request = Theme.builder().build();
		request.themeId = UUID.randomUUID();
		String expectedUri = baseUri + "/v1/meetings/themes/"+request.themeId;
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		assertEquals("{\"update_details\":{}}", EntityUtils.toString(builder.getEntity()));
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		assertEquals("PATCH", builder.getMethod());
	}

	@Test
	public void testParseResponseWithoutRequest() throws Exception {
		Theme parsed = endpoint.parseResponse(TestUtils.makeJsonHttpResponse(200, "{}"));
		assertNotNull(parsed);
	}

	@Test(expected = MeetingsResponseException.class)
	public void testUnsuccessfulResponse() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(400, "{}"));
	}
}