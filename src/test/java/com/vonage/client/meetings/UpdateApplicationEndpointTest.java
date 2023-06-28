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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.*;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import java.util.UUID;

public class UpdateApplicationEndpointTest {
	private UpdateApplicationEndpoint endpoint;
	
	@Before
	public void setUp() {
		endpoint = new UpdateApplicationEndpoint(new HttpWrapper(new JWTAuthMethod("app-id", new byte[0])));
	}
	
	@Test
	public void testMakeRequest() throws Exception {
		UUID themeId = UUID.fromString("e86a7335-35fe-45e1-b961-5777d4748022");
		UpdateApplicationRequest request = UpdateApplicationRequest.builder()
			.defaultThemeId(themeId)
			.build();
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals("PATCH", builder.getMethod());
		String expectedUri = "https://api-eu.vonage.com/meetings/applications";
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		String expectedRequest = "{\"update_details\": {\"default_theme_id\":\""+themeId+"\"}}";
		assertEquals(expectedRequest, EntityUtils.toString(builder.getEntity()));
		String expectedResponse = "{\n" +
				"   \"application_id\": \"48ac72d0-a829-4896-a067-dcb1c2b0f30c\",\n" +
				"   \"account_id\": \"\",\n" +
				"   \"default_theme_id\": \""+themeId+"\", \"unknown\": 1\n" +
				"}";
		HttpResponse mockResponse = TestUtils.makeJsonHttpResponse(200, expectedResponse);
		Application parsed = endpoint.parseResponse(mockResponse);
		assertEquals(themeId, parsed.getDefaultThemeId());
		assertEquals("48ac72d0-a829-4896-a067-dcb1c2b0f30c", parsed.getApplicationId().toString());
		assertEquals("", parsed.getAccountId());
	}

	@Test
	public void testCustomUri() throws Exception {
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri(baseUri).build());
		endpoint = new UpdateApplicationEndpoint(wrapper);
		String expectedUri = baseUri + "/meetings/applications";
		UpdateApplicationRequest request = UpdateApplicationRequest.builder().defaultThemeId(UUID.randomUUID()).build();
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		assertEquals("PATCH", builder.getMethod());
	}

	@Test(expected = MeetingsResponseException.class)
	public void testUnsuccessfulResponse() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(400, "{}"));
	}

	@Test(expected = VonageUnexpectedException.class)
	public void triggerJsonProcessingException() {
		class SelfRefrencing extends UpdateApplicationRequest {
			@JsonProperty("self") final SelfRefrencing self = this;
			SelfRefrencing() {
				super(UpdateApplicationRequest.builder().defaultThemeId(UUID.randomUUID()));
			}
		}
		new SelfRefrencing().toJson();
	}

	@Test(expected = VonageResponseParseException.class)
	public void testParseMalformedResponse() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(200, "{malformed]"));
	}
}