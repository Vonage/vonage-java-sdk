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
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import java.util.UUID;

public class PatchArchiveStreamEndpointTest {
	private PatchArchiveStreamEndpoint endpoint;
	private final String applicationId = UUID.randomUUID().toString();
	
	@Before
	public void setUp() {
		endpoint = new PatchArchiveStreamEndpoint(new HttpWrapper(
			new JWTAuthMethod(applicationId, new byte[0])
		));
	}
	
	@Test
	public void testAddStream() throws Exception {
		String archiveId = UUID.randomUUID().toString(),
				streamId = UUID.randomUUID().toString();
		PatchComposedStreamsRequest request = new PatchComposedStreamsRequest(streamId, true, false);
		request.id = archiveId;
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals("PATCH", builder.getMethod());
		String expectedUri = "https://video.api.vonage.com/v2/project/" +
				applicationId+"/archive/"+archiveId+"/streams";
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		String expectedPayload = "{\"addStream\":\""+streamId+"\",\"hasAudio\":true,\"hasVideo\":false}";
		assertEquals(expectedPayload, EntityUtils.toString(builder.getEntity()));
	}

	@Test
	public void testRemoveStream() throws Exception {
		String archiveId = UUID.randomUUID().toString(),
				streamId = UUID.randomUUID().toString();
		PatchComposedStreamsRequest request = new PatchComposedStreamsRequest(streamId);
		request.id = archiveId;
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals("PATCH", builder.getMethod());
		String expectedUri = "https://video.api.vonage.com/v2/project/" +
				applicationId+"/archive/"+archiveId+"/streams";
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		String expectedPayload = "{\"removeStream\":\""+streamId+"\"}";
		assertEquals(expectedPayload, EntityUtils.toString(builder.getEntity()));
	}

	@Test(expected = VonageBadRequestException.class)
	public void test500Response() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(500, ""));
	}
}