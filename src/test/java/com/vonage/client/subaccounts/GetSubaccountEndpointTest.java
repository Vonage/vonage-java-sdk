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

public class GetSubaccountEndpointTest {
	final String apiKey = "a1b2c3d4", apiSecret = "1234567890abcdef", subApiKey = "def123ab";
	final AuthMethod authMethod = new TokenAuthMethod(apiKey, apiSecret);
	GetSubaccountEndpoint endpoint;

	@Before
	public void setUp() {
		endpoint = new GetSubaccountEndpoint(new HttpWrapper(authMethod));
	}
	
	@Test
	public void testAuthMethod() {
		Class<?>[] authMethods = endpoint.getAcceptableAuthMethods();
		assertEquals(1, authMethods.length);
		assertEquals(authMethod.getClass(), authMethods[0]);
	}
	
	@Test
	public void testDefaultUri() throws Exception {
		RequestBuilder builder = endpoint.makeRequest(subApiKey);
		assertEquals("GET", builder.getMethod());
		String expectedUri = "https://api.nexmo.com/accounts/"+apiKey+"/subaccounts/"+subApiKey;
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
	}

	@Test
	public void testCustomUri() throws Exception {
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri(baseUri).build(), authMethod);
		endpoint = new GetSubaccountEndpoint(wrapper);
		String expectedUri = baseUri + "/accounts/"+apiKey+"/subaccounts/"+subApiKey;
		RequestBuilder builder = endpoint.makeRequest(subApiKey);
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		assertEquals("GET", builder.getMethod());
	}

	@Test
	public void testFullResponse() throws Exception {
		String expectedResponse = "{\n" +
				"   \"api_key\": \"bbe6222f\",\n" +
				"   \"name\": \"Subaccount department A\",\n" +
				"   \"primary_account_api_key\": \"acc6111f\",\n" +
				"   \"use_primary_account_balance\": true,\n" +
				"   \"created_at\": \"2018-03-02T16:34:49Z\",\n" +
				"   \"suspended\": false,\n" +
				"   \"balance\": 100.25,\n" +
				"   \"credit_limit\": -99.33\n" +
				"}";
		HttpResponse mockResponse = TestUtils.makeJsonHttpResponse(200, expectedResponse);
		Account parsed = endpoint.parseResponse(mockResponse);
		assertNotNull(parsed);
		assertEquals("bbe6222f", parsed.getApiKey());
		assertEquals("Subaccount department A", parsed.getName());
		assertTrue(parsed.getUsePrimaryAccountBalance());
		assertEquals(1520008489L, parsed.getCreatedAt().getEpochSecond());
		assertFalse(parsed.getSuspended());
		assertEquals(100.25, parsed.getBalance().doubleValue(), 0.001);
		assertEquals(-99.33, parsed.getCreditLimit().doubleValue(), 0.001);
	}
	
	@Test
	public void testEmptyResponse() throws Exception {
		HttpResponse mockResponse = TestUtils.makeJsonHttpResponse(200, "{}");
		Account parsed = endpoint.parseResponse(mockResponse);
		assertNotNull(parsed);
		assertNull(parsed.getApiKey());
		assertNull(parsed.getPrimaryAccountApiKey());
		assertNull(parsed.getName());
		assertNull(parsed.getUsePrimaryAccountBalance());
		assertNull(parsed.getSuspended());
		assertNull(parsed.getCreatedAt());
		assertNull(parsed.getBalance());
		assertNull(parsed.getCreditLimit());
	}
	
	@Test(expected = VonageResponseParseException.class)
	public void testInvalidResponse() {
		Account.fromJson("{malformed]");
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