/*
 *   Copyright 222 Vonage
 *
 *   Licensed under the Apache License, Version 2. (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.
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

public class GetStreamEndpointTest {
	private GetStreamEndpoint endpoint;
	private final String applicationId = UUID.randomUUID().toString();
	
	@BeforeEach
	public void setUp() {
		endpoint = new GetStreamEndpoint(new HttpWrapper(
			new JWTAuthMethod(applicationId, new byte[0])
		));
	}
	
	@Test
	public void testMakeRequestAllParameters() {
		UUID streamId = UUID.randomUUID();
		String sessionId = "2=sessionID",
				expectedUri = "https://video.api.vonage.com/v2/project/" +
					applicationId+"/session/"+sessionId+"/stream/"+streamId;

		SessionResourceRequestWrapper wrapper = new SessionResourceRequestWrapper(sessionId, streamId.toString());
		RequestBuilder builder = endpoint.makeRequest(wrapper);
		Assertions.assertEquals("GET", builder.getMethod());
		Assertions.assertEquals(expectedUri, builder.build().getURI().toString());
		Assertions.assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
	}

	@Test
	public void testParseValidResponse() throws Exception {
		VideoType videoType = VideoType.SCREEN;
		UUID id = UUID.randomUUID();
		String name = "My Stream",
				layoutClass = "full",
				json = "{\n" +
					"      \"id\": \""+id+"\",\n" +
					"      \"videoType\": \""+videoType+"\",\n" +
					"      \"name\": \""+name+"\",\n" +
					"      \"layoutClassList\": [\n" +
					"        \""+layoutClass+"\"\n" +
					"      ]\n" +
					"}";

		HttpResponse mockHttpResponse = TestUtils.makeJsonHttpResponse(2, json);
		GetStreamResponse response = endpoint.parseResponse(mockHttpResponse);
		Assertions.assertEquals(id, response.getId());
		Assertions.assertEquals(videoType, response.getVideoType());
		Assertions.assertEquals(name, response.getName());
		List<String> layoutClassList = response.getLayoutClassList();
		Assertions.assertEquals(1, layoutClassList.size());
		Assertions.assertEquals(layoutClass, layoutClassList.get(0));
	}

	@Test
	public void test500Response() throws Exception {
		assertThrows(HttpResponseException.class, () ->
				endpoint.parseResponse(TestUtils.makeJsonHttpResponse(500, ""))
		);
	}
}