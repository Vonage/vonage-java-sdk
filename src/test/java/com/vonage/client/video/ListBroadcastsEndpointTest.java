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
package com.vonage.client.video;

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import com.vonage.client.TestUtils;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import java.util.UUID;

public class ListBroadcastsEndpointTest {
	private ListBroadcastsEndpoint endpoint;
	private final String applicationId = UUID.randomUUID().toString();
	
	@Before
	public void setUp() {
		endpoint = new ListBroadcastsEndpoint(new HttpWrapper(
			new JWTAuthMethod(applicationId, new byte[0])
		));
	}
	
	@Test
	public void testMakeRequestAllParameters() throws Exception {
		ListStreamCompositionsRequest request = ListStreamCompositionsRequest.builder()
				.offset(6).count(25).sessionId("SESSION_ID").build();
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals("GET", builder.getMethod());
		String expectedUri = "https://video.api.vonage.com/v2/project/"+applicationId +
				"/broadcast?offset=6&count=25&sessionId=SESSION_ID";
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
		assertEquals(3, params.size());
		assertEquals(request.getOffset().toString(), params.get("offset"));
		assertEquals(request.getCount().toString(), params.get("count"));
		assertEquals(request.getSessionId(), params.get("sessionId"));
		String expectedResponse = "{\"count\":2,\"items\":[{},{\"applicationId\":\""+applicationId+"\"}]}";
		HttpResponse mockResponse = TestUtils.makeJsonHttpResponse(200, expectedResponse);
		ListBroadcastsResponse parsed = endpoint.parseResponse(mockResponse);
		assertNotNull(parsed);
		assertEquals(2, parsed.getCount().intValue());
		assertEquals(2, parsed.getItems().size());
		assertNotNull(parsed.getItems().get(0));
		assertEquals(applicationId, parsed.getItems().get(1).getApplicationId().toString());
	}

	@Test
	public void testCustomUri() throws Exception {
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(
				HttpConfig.builder().videoBaseUri(baseUri).build(),
				new JWTAuthMethod(applicationId, new byte[0])
		);
		endpoint = new ListBroadcastsEndpoint(wrapper);
		String expectedUri = baseUri + "/v2/project/"+applicationId+"/broadcast";
		ListStreamCompositionsRequest request = ListStreamCompositionsRequest.builder().build();
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		assertEquals("GET", builder.getMethod());
		Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
		assertEquals(0, params.size());
	}

	@Test(expected = HttpResponseException.class)
	public void test500Response() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(500, ""));
	}
}