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
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.RequestBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import java.util.UUID;

public class ClearListEndpointTest {
	ClearListEndpoint endpoint;
	String listId = UUID.randomUUID().toString();
	
	@Before
	public void setUp() {
		endpoint = new ClearListEndpoint(new HttpWrapper());
	}
	
	@Test
	public void testMakeRequestAllParameters() throws Exception {
		RequestBuilder builder = endpoint.makeRequest(listId);
		assertEquals("POST", builder.getMethod());
		String expectedUri = "https://api-eu.vonage.com/v0.1/bulk/lists/"+listId+"/clear";
		assertEquals(expectedUri, builder.build().getURI().toString());
		HttpResponse mockResponse = TestUtils.makeJsonHttpResponse(200, "");
		assertNull(endpoint.parseResponse(mockResponse));
	}

	@Test
	public void testCustomUri() throws Exception {
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri(baseUri).build());
		endpoint = new ClearListEndpoint(wrapper);
		String expectedUri = baseUri + "/v0.1/bulk/lists/"+listId+"/clear";
		RequestBuilder builder = endpoint.makeRequest(listId);
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals("POST", builder.getMethod());
	}
	
	@Test(expected = HttpResponseException.class)
	public void test500Response() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(500, "{}"));
	}
}