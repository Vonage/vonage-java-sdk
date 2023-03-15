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
package com.vonage.client.verify2;

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import com.vonage.client.common.HttpMethod;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import java.util.UUID;

public class VerifyCodeEndpointTest {
	VerifyCodeEndpoint endpoint;

	@Before
	public void setUp() {
		endpoint = new VerifyCodeEndpoint(new HttpWrapper());
	}

	@Test
	public void testVerifySuccess() throws Exception {
		VerifyCodeRequestWrapper request = new VerifyCodeRequestWrapper(UUID.randomUUID().toString(), "54321");

		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		assertEquals(HttpMethod.POST.name(), builder.getMethod());
		assertEquals("https://api.nexmo.com/v2/verify/"+request.requestId, builder.getUri().toString());

		String expectedJson = "{\"code\":\""+request.code+"\"}";
		assertEquals(expectedJson, EntityUtils.toString(builder.getEntity()));
	}

	@Test
	public void testCustomUri() throws Exception {
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().apiBaseUri(baseUri).build());
		endpoint = new VerifyCodeEndpoint(wrapper);

		VerifyCodeRequestWrapper request = new VerifyCodeRequestWrapper(UUID.randomUUID().toString(), "4567890");
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		assertEquals("POST", builder.getMethod());
		assertEquals(baseUri+"/v2/verify/"+request.requestId, builder.getUri().toString());

		String expectedJson = "{\"code\":\""+request.code+"\"}";
		assertEquals(expectedJson, EntityUtils.toString(builder.getEntity()));
	}
}
