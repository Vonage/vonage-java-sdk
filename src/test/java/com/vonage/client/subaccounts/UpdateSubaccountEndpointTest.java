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
import org.apache.http.util.EntityUtils;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class UpdateSubaccountEndpointTest {
	final String apiKey = "a1b2c3d4", apiSecret = "1234567890abcdef", subApiKey = "def123ab";
	final AuthMethod authMethod = new TokenAuthMethod(apiKey, apiSecret);
	UpdateSubaccountEndpoint endpoint;
	
	@Before
	public void setUp() {
		endpoint = new UpdateSubaccountEndpoint(new HttpWrapper(authMethod));
	}
	
	@Test
	public void testAuthMethod() {
		Class<?>[] authMethods = endpoint.getAcceptableAuthMethods();
		assertEquals(1, authMethods.length);
		assertEquals(TokenAuthMethod.class, authMethods[0]);
	}
	
	@Test
	public void testDefaultUri() throws Exception {
		UpdateSubaccountRequest request = UpdateSubaccountRequest.builder(subApiKey)
				.name("Renamed A").usePrimaryAccountBalance(false).suspended(true).build();

		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals("PATCH", builder.getMethod());
		String expectedUri = "https://api.nexmo.com/accounts/"+apiKey+"/subaccounts/"+subApiKey;
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		String expectedRequest = "{\"name\":\""+request.getName() +
				"\",\"use_primary_account_balance\":"+request.getUsePrimaryAccountBalance()+",\"suspended\":true}";
		assertEquals(expectedRequest, EntityUtils.toString(builder.getEntity()));
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
	}

	@Test
	public void testCustomUri() throws Exception {
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri(baseUri).build(), authMethod);
		endpoint = new UpdateSubaccountEndpoint(wrapper);
		UpdateSubaccountRequest request = UpdateSubaccountRequest.builder(subApiKey).name("Custom").build();
		String expectedUri = baseUri + "/accounts/"+apiKey+"/subaccounts/"+subApiKey;
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		assertEquals("{\"name\":\"Custom\"}", EntityUtils.toString(builder.getEntity()));
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		assertEquals("PATCH", builder.getMethod());
	}
	
	@Test
	public void testEmptyResponse() throws Exception {
		HttpResponse mockResponse = TestUtils.makeJsonHttpResponse(200, "{}");
		Account parsed = endpoint.parseResponse(mockResponse);
		assertNotNull(parsed);
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