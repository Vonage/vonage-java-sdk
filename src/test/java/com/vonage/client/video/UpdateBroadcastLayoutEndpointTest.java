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
import com.vonage.client.VonageBadRequestException;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import java.util.UUID;

public class UpdateBroadcastLayoutEndpointTest {
	private UpdateBroadcastLayoutEndpoint endpoint;
	private final String applicationId = UUID.randomUUID().toString();

	@Before
	public void setUp() {
		endpoint = new UpdateBroadcastLayoutEndpoint(new HttpWrapper(
			new JWTAuthMethod(applicationId, new byte[0])
		));
	}
	
	@Test
	public void testMakeRequestAllParameters() throws Exception {
		String broadcastId = UUID.randomUUID().toString();
		UpdateStreamCompositionLayoutRequestWrapper request = new  UpdateStreamCompositionLayoutRequestWrapper(
				broadcastId,
				StreamCompositionLayout.builder(ScreenLayoutType.VERTICAL).build()
		);
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals("PUT", builder.getMethod());
		String expectedUri = "https://video.api.vonage.com/v2/project/"+applicationId +
				"/broadcast/"+broadcastId+"/layout";
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		String expectedRequest = "{\"type\":\"verticalPresentation\"}";
		assertEquals(expectedRequest, EntityUtils.toString(builder.getEntity()));
	}

	@Test
	public void testCustomUri() throws Exception {
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(
				HttpConfig.builder().videoBaseUri(baseUri).build(),
				new JWTAuthMethod(applicationId, new byte[0])
		);
		endpoint = new UpdateBroadcastLayoutEndpoint(wrapper);
		String broadcastId = UUID.randomUUID().toString();
		UpdateStreamCompositionLayoutRequestWrapper request = new  UpdateStreamCompositionLayoutRequestWrapper(
				broadcastId,
				StreamCompositionLayout.builder(ScreenLayoutType.BEST_FIT)
						.screenshareType(ScreenLayoutType.PIP).build()
		);
		String expectedUri = baseUri + "/v2/project/"+applicationId+"/broadcast/"+broadcastId+"/layout";
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		String expectedRequest = "{\"type\":\"bestFit\",\"screenshareType\":\"pip\"}";
		assertEquals(expectedRequest, EntityUtils.toString(builder.getEntity()));
		assertEquals("PUT", builder.getMethod());
	}

	@Test(expected = VonageBadRequestException.class)
	public void test400Response() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(400, ""));
	}
}