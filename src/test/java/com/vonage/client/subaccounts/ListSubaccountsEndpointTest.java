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
package com.vonage.client.subaccounts;

import com.vonage.client.*;
import com.vonage.client.auth.AuthMethod;
import com.vonage.client.auth.TokenAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;

public class ListSubaccountsEndpointTest {
	final String apiKey = "a1b2c3d4", apiSecret = "1234567890abcdef";
	final AuthMethod authMethod = new TokenAuthMethod(apiKey, apiSecret);
	ListSubaccountsEndpoint endpoint;

	@Before
	public void setUp() {
		endpoint = new ListSubaccountsEndpoint(new HttpWrapper(authMethod));
	}
	
	@Test
	public void testAuthMethod() {
		Class<?>[] authMethods = endpoint.getAcceptableAuthMethods();
		assertEquals(1, authMethods.length);
		assertEquals(authMethod.getClass(), authMethods[0]);
	}
	
	@Test
	public void testDefaultUri() throws Exception {
		RequestBuilder builder = endpoint.makeRequest(null);
		assertEquals("GET", builder.getMethod());
		String expectedUri = "https://api.nexmo.com/accounts/"+apiKey+"/subaccounts";
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
	}

	@Test
	public void testCustomUri() throws Exception {
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri(baseUri).build(), authMethod);
		endpoint = new ListSubaccountsEndpoint(wrapper);
		String expectedUri = baseUri + "/accounts/"+apiKey+"/subaccounts";
		RequestBuilder builder = endpoint.makeRequest(null);
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		assertEquals("GET", builder.getMethod());
	}

	@Test
	public void testFullResponse() throws Exception {
		String expectedResponse = "{}";
		HttpResponse mockResponse = TestUtils.makeJsonHttpResponse(200, expectedResponse);
		ListSubaccountsResponse parsed = endpoint.parseResponse(mockResponse);
		assertNotNull(parsed);
	}
	
	@Test
	public void testEmptyResponse() throws Exception {
		HttpResponse mockResponse = TestUtils.makeJsonHttpResponse(200, "{}");
		ListSubaccountsResponse parsed = endpoint.parseResponse(mockResponse);
		assertNotNull(parsed);
		assertNull(parsed.getPrimaryAccount());
		assertNull(parsed.getSubaccounts());
	}
	
	@Test(expected = VonageResponseParseException.class)
	public void testInvalidResponse() {
		ListSubaccountsResponse.fromJson("{malformed]");
	}

	@Test(expected = SubaccountsResponseException.class)
	public void test400Response() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(400, "{}"));
	}
	
	@Test(expected = SubaccountsResponseException.class)
	public void test500Response() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(500, "{}"));
	}
}