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
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.UUID;

public class UploadListItemsEndpointTest {
	UploadListItemsEndpoint endpoint;
	String listId = UUID.randomUUID().toString();
	
	@Before
	public void setUp() {
		endpoint = new UploadListItemsEndpoint(new HttpWrapper());
	}
	
	@Test
	public void testAuthMethod() {
		Class<?>[] authMethods = endpoint.getAcceptableAuthMethods();
		assertEquals(1, authMethods.length);
		assertEquals(JWTAuthMethod.class, authMethods[0]);
	}
	
	@Test
	public void testDefaultUri() throws Exception {
		UploadListItemsRequestWrapper request = new UploadListItemsRequestWrapper(listId, new byte[0]);
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals("POST", builder.getMethod());
		String expectedUri = "https://api-eu.vonage.com/v0.1/bulk/lists/"+request.listId+"/items/import";
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		assertEquals("", EntityUtils.toString(builder.getEntity()));
	}

	@Test
	public void testCustomUri() throws Exception {
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri(baseUri).build());
		endpoint = new UploadListItemsEndpoint(wrapper);
		UploadListItemsRequestWrapper request = new UploadListItemsRequestWrapper(listId, new byte[0]);
		String expectedUri = baseUri + "/v0.1/bulk/lists/"+request.listId+"/items/import";
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals("", EntityUtils.toString(builder.getEntity()));
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		assertEquals("POST", builder.getMethod());
	}

	@Test
	public void testFullResponse() throws Exception {
		String expectedResponse = "{\"inserted\":21,\"updated\":3,\"deleted\":0}";
		HttpResponse mockResponse = TestUtils.makeJsonHttpResponse(200, expectedResponse);
		UploadListItemsResponse parsed = endpoint.parseResponse(mockResponse);
		assertNotNull(parsed);
		assertEquals(21, parsed.getInserted().intValue());
		assertEquals(3, parsed.getUpdated().intValue());
		assertEquals(0, parsed.getDeleted().intValue());
	}

	@Test
	public void testEmptyResponse() throws Exception {
		HttpResponse mockResponse = TestUtils.makeJsonHttpResponse(200, "{}");
		UploadListItemsResponse parsed = endpoint.parseResponse(mockResponse);
		assertNotNull(parsed);
		assertNull(parsed.getInserted());
		assertNull(parsed.getUpdated());
		assertNull(parsed.getDeleted());
	}

	@Test(expected = VonageUnexpectedException.class)
	public void testInvalidResponse() {
		UploadListItemsResponse.fromJson("{malformed]");
	}

	@Test(expected = HttpResponseException.class)
	public void test400Response() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(400, "{}"));
	}
	
	@Test(expected = HttpResponseException.class)
	public void test500Response() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(500, "{}"));
	}
}