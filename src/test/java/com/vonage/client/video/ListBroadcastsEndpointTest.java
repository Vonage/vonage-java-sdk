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
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.Map;
import java.util.UUID;

public class ListBroadcastsEndpointTest {
	private ListBroadcastsEndpoint endpoint;
	private final String applicationId = UUID.randomUUID().toString();
	
	@BeforeEach
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
		Assertions.assertEquals("GET", builder.getMethod());
		String expectedUri = "https://video.api.vonage.com/v2/project/"+applicationId +
				"/broadcast?offset=6&count=25&sessionId=SESSION_ID";
		Assertions.assertEquals(expectedUri, builder.build().getURI().toString());
		Assertions.assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
		Assertions.assertEquals(3, params.size());
		Assertions.assertEquals(request.getOffset().toString(), params.get("offset"));
		Assertions.assertEquals(request.getCount().toString(), params.get("count"));
		Assertions.assertEquals(request.getSessionId(), params.get("sessionId"));
		String expectedResponse = "{\"count\":2,\"items\":[{},{\"applicationId\":\""+applicationId+"\"}]}";
		HttpResponse mockResponse = TestUtils.makeJsonHttpResponse(200, expectedResponse);
		ListBroadcastsResponse parsed = endpoint.parseResponse(mockResponse);
		Assertions.assertNotNull(parsed);
		Assertions.assertEquals(2, parsed.getCount().intValue());
		Assertions.assertEquals(2, parsed.getItems().size());
		Assertions.assertNotNull(parsed.getItems().get(0));
		Assertions.assertEquals(applicationId, parsed.getItems().get(1).getApplicationId().toString());
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
		Assertions.assertEquals(expectedUri, builder.build().getURI().toString());
		Assertions.assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		Assertions.assertEquals("GET", builder.getMethod());
		Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
		Assertions.assertEquals(0, params.size());
	}

	@Test
	public void test500Response() throws Exception {
		assertThrows(HttpResponseException.class, () ->
				endpoint.parseResponse(TestUtils.makeJsonHttpResponse(500, ""))
		);
	}

	@Test
	public void testInvalidResponseJson() throws Exception {
		assertThrows(VonageResponseParseException.class, () -> ListBroadcastsResponse.fromJson("{malformed]"));
	}
}