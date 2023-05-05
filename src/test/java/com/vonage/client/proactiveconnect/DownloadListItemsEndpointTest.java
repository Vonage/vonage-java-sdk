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
package com.vonage.client.proactiveconnect;

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import com.vonage.client.TestUtils;
import com.vonage.client.VonageUnexpectedException;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import java.util.UUID;

public class DownloadListItemsEndpointTest {
	DownloadListItemsEndpoint endpoint;
	String listId = UUID.randomUUID().toString();
	
	@Before
	public void setUp() {
		endpoint = new DownloadListItemsEndpoint(new HttpWrapper());
	}

	@Test
	public void testAuthMethod() {
		Class<?>[] authMethods = endpoint.getAcceptableAuthMethods();
		assertEquals(1, authMethods.length);
		assertEquals(JWTAuthMethod.class, authMethods[0]);
	}
	
	@Test
	public void testNoFile() throws Exception {
		RequestBuilder builder = endpoint.makeRequest(new DownloadListItemsRequestWrapper(listId, null));
		assertEquals("GET", builder.getMethod());
		String expectedUri = "https://api-eu.vonage.com/v0.1/bulk/lists/"+listId+"/items/download";
		assertEquals(expectedUri, builder.build().getURI().toString());
		String expectedResponse = "{}";
		HttpResponse mockResponse = TestUtils.makeJsonHttpResponse(200, expectedResponse);
		byte[] parsed = endpoint.parseResponse(mockResponse);
		assertNotNull(parsed);
	}

	@Test
	public void testCustomUri() throws Exception {
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri(baseUri).build());
		endpoint = new DownloadListItemsEndpoint(wrapper);
		String expectedUri = baseUri + "/v0.1/bulk/lists/"+listId+"/items/download";
		RequestBuilder builder = endpoint.makeRequest(new DownloadListItemsRequestWrapper(listId, null));
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals("GET", builder.getMethod());
	}
	
	@Test(expected = VonageUnexpectedException.class)
	public void test500Response() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(500, ""));
	}
}