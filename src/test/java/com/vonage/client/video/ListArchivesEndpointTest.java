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

import com.vonage.client.HttpWrapper;
import com.vonage.client.TestUtils;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.Map;
import java.util.UUID;

public class ListArchivesEndpointTest {
	private ListArchivesEndpoint endpoint;
	private final String applicationId = UUID.randomUUID().toString();
	
	@BeforeEach
	public void setUp() {
		endpoint = new ListArchivesEndpoint(new HttpWrapper(
			new JWTAuthMethod(applicationId, new byte[0])
		));
	}
	
	@Test
	public void testMakeRequestAllParameters() throws Exception {
		String sessionId = UUID.randomUUID().toString();
		int count = 6, offset = 2;
		ListStreamCompositionsRequest request = ListStreamCompositionsRequest.builder()
			.offset(offset).count(count).sessionId(sessionId)
			.build();
		
		RequestBuilder builder = endpoint.makeRequest(request);
		Assertions.assertEquals("GET", builder.getMethod());
		String expectedUri = "https://video.api.vonage.com/v2/project/"+applicationId+"/archive?";
		Assertions.assertTrue(builder.build().getURI().toString().startsWith(expectedUri));
		Assertions.assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
		Assertions.assertEquals(3, params.size());
		Assertions.assertEquals(sessionId, params.get("sessionId"));
		Assertions.assertEquals(offset, Integer.parseInt(params.get("offset")));
		Assertions.assertEquals(count, Integer.parseInt(params.get("count")));
		String expectedPayload = VideoClientTest.listArchiveJson;
		HttpResponse mockResponse = TestUtils.makeJsonHttpResponse(200, expectedPayload);
		ListArchivesResponse response = endpoint.parseResponse(mockResponse);
		VideoClientTest.assertArchiveEqualsExpectedJson(response.getItems().get(0));
		Assertions.assertEquals(Integer.valueOf(1), response.getCount());
	}

	@Test
	public void testMakeRequestNullParameters() {
		ListStreamCompositionsRequest wrapper = ListStreamCompositionsRequest.builder().build();
		RequestBuilder builder = endpoint.makeRequest(wrapper);
		Assertions.assertEquals("GET", builder.getMethod());
		String expectedUri = "https://video.api.vonage.com/v2/project/"+applicationId+"/archive";
		Assertions.assertEquals(expectedUri, builder.build().getURI().toString());
	}

	@Test
	public void test500Response() throws Exception {
		assertThrows(HttpResponseException.class, () ->
				endpoint.parseResponse(TestUtils.makeJsonHttpResponse(500, ""))
		);
	}
}