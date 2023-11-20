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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.UUID;

public class ListStreamsEndpointTest {
	private ListStreamsEndpoint endpoint;
	private final String applicationId = UUID.randomUUID().toString();
	
	@BeforeEach
	public void setUp() {
		endpoint = new ListStreamsEndpoint(new HttpWrapper(
			new JWTAuthMethod(applicationId, new byte[0])
		));
	}
	
	@Test
	public void testMakeRequestAllParameters() throws Exception {
		String sessionId = UUID.randomUUID().toString();
		RequestBuilder builder = endpoint.makeRequest(sessionId);
		Assertions.assertEquals("GET", builder.getMethod());
		String expectedUri = "https://video.api.vonage.com/v2/project/"+applicationId+"/session/"+sessionId+"/stream";
		Assertions.assertEquals(expectedUri, builder.build().getURI().toString());
		Assertions.assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
	}

	@Test
	public void testParseValidResponse() throws Exception {
		Integer count = 1;
		VideoType videoType0 = VideoType.CUSTOM;
		UUID id0 = UUID.randomUUID();
		String name0 = "My Stream",
			layoutClass0 = "full",
			json = "{\n" +
				"  \"count\": "+count+",\n" +
				"  \"items\": [\n" +
				"    {\n" +
				"      \"id\": \""+id0+"\",\n" +
				"      \"videoType\": \""+videoType0+"\",\n" +
				"      \"name\": \""+name0+"\",\n" +
				"      \"layoutClassList\": [\n" +
				"        \""+layoutClass0+"\"\n" +
				"      ]\n" +
				"    }\n" +
				"  ]\n" +
				"}";

		HttpResponse mockHttpResponse = TestUtils.makeJsonHttpResponse(200, json);
		ListStreamsResponse response = endpoint.parseResponse(mockHttpResponse);
		Assertions.assertEquals(count, response.getCount());
		List<GetStreamResponse> streams = response.getItems();
		Assertions.assertEquals(1, streams.size());
		GetStreamResponse stream0 = streams.get(0);
		Assertions.assertEquals(id0, stream0.getId());
		Assertions.assertEquals(videoType0, stream0.getVideoType());
		Assertions.assertEquals(name0, stream0.getName());
		List<String> layoutClassList0 = stream0.getLayoutClassList();
		Assertions.assertEquals(1, layoutClassList0.size());
		Assertions.assertEquals(layoutClass0, layoutClassList0.get(0));
	}

	@Test
	public void test500Response() throws Exception {
		assertThrows(HttpResponseException.class, () ->
				endpoint.parseResponse(TestUtils.makeJsonHttpResponse(500, ""))
		);
	}
}