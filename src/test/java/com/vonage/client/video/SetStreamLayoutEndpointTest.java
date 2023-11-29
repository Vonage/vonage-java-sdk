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
import com.vonage.client.VonageBadRequestException;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class SetStreamLayoutEndpointTest {
	private SetStreamLayoutEndpoint endpoint;
	private final String applicationId = "8b732909-0a06-46a2-8ea8-074e64d43422";
	
	@BeforeEach
	public void setUp() throws Exception {
		endpoint = new SetStreamLayoutEndpoint(new HttpWrapper(
			new JWTAuthMethod(applicationId, new byte[0])
		));
	}
	
	@Test
	public void testMakeRequestAllParameters() throws Exception {
		String sessionId = UUID.randomUUID().toString(),
				streamId0 = UUID.randomUUID().toString(),
				streamId1 = UUID.randomUUID().toString(),
				streamId2 = UUID.randomUUID().toString();

		SetStreamLayoutRequest request = new SetStreamLayoutRequest(sessionId, Arrays.asList(
				SessionStream.builder(streamId0).build(),
				SessionStream.builder(streamId1).layoutClassList(Arrays.asList("min", "full")).build(),
				SessionStream.builder(streamId2).layoutClassList().build()
		));
		
		RequestBuilder builder = endpoint.makeRequest(request);
		Assertions.assertEquals("PUT", builder.getMethod());
		String expectedUri = "https://video.api.vonage.com/v2/project/"+applicationId+"/session/"+sessionId+"/stream";
		Assertions.assertEquals(expectedUri, builder.build().getURI().toString());
		Assertions.assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		String expectedPayload = "{\"items\":[{\"id\":\""+streamId0+"\"},"+
				"{\"id\":\""+streamId1+"\",\"layoutClassList\":[\"min\",\"full\"]}," +
				"{\"id\":\""+streamId2+"\",\"layoutClassList\":[]}]}";
		Assertions.assertEquals(expectedPayload, EntityUtils.toString(builder.getEntity()));
	}
	
	@Test
	public void testMakeRequestRequiredParameters() throws Exception {
		String streamId = UUID.randomUUID().toString();
		List<SessionStream> streams = Collections.singletonList(SessionStream.builder(streamId).build());
		SetStreamLayoutRequest request = new SetStreamLayoutRequest("", streams);
		RequestBuilder builder = endpoint.makeRequest(request);
		String expectedPayload = "{\"items\":[{\"id\":\""+streamId+"\"}]}";
		Assertions.assertEquals(expectedPayload, EntityUtils.toString(builder.getEntity()));
	}

	@Test
	public void testParseValidResponse() throws Exception {
		HttpResponse mockHttpResponse = TestUtils.makeJsonHttpResponse(200, "");
		Assertions.assertNull(endpoint.parseResponse(mockHttpResponse));
	}

	@Test
	public void test500Response() throws Exception {
		assertThrows(VonageBadRequestException.class, () ->
				endpoint.parseResponse(TestUtils.makeJsonHttpResponse(500, ""))
		);
	}
}