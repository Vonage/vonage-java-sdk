/*
 *   Copyright 2022 Vonage
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
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class SetStreamLayoutEndpointTest {
	private SetStreamLayoutEndpoint endpoint;
	private final String applicationId = "8b732909-0a06-46a2-8ea8-074e64d43422";
	
	@Before
	public void setUp() throws Exception {
		endpoint = new SetStreamLayoutEndpoint(new HttpWrapper(
			new JWTAuthMethod(applicationId, new byte[0])
		));
	}
	
	@Test
	public void testMakeRequestAllParameters() throws Exception {
		String sessionId = UUID.randomUUID().toString(),
				streamId0 = UUID.randomUUID().toString(),
				streamId1 = UUID.randomUUID().toString();

		SetStreamLayoutRequest request = new SetStreamLayoutRequest(sessionId, Arrays.asList(
				SessionStream.builder(streamId0).build(),
				SessionStream.builder(streamId1).layoutClassList(Arrays.asList("min", "full")).build()
		));
		
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals("PUT", builder.getMethod());
		String expectedUri = "https://video.api.vonage.com/v2/project/"+applicationId+"/session/"+sessionId+"/stream";
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		String actualJson = EntityUtils.toString(builder.getEntity());
		String expectedJson = "{\"items\":[{\"id\":\""+streamId0+"\"},"+
				"{\"id\":\""+streamId1 +"\",\"layoutClassList\":[\"min\",\"full\"]}]}";
		assertEquals(expectedJson, actualJson);
	}
	
	@Test
	public void testMakeRequestRequiredParameters() {
		SetStreamLayoutRequest request = new SetStreamLayoutRequest("", Collections.emptyList());
		RequestBuilder builder = endpoint.makeRequest(request);
		Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
		assertEquals(0, params.size());
	}

	@Test
	public void testParseValidResponse() throws Exception {
		HttpResponse mockHttpResponse = TestUtils.makeJsonHttpResponse(200, "");
		assertNull(endpoint.parseResponse(mockHttpResponse));
	}
}