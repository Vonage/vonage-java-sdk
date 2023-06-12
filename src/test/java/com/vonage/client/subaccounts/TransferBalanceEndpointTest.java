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

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import com.vonage.client.TestUtils;
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.auth.AuthMethod;
import com.vonage.client.auth.TokenAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.math.BigDecimal;
import java.util.UUID;

public class TransferBalanceEndpointTest {
	final String apiKey = "a1b2c3d4", apiSecret = "1234567890abcdef";
	final AuthMethod authMethod = new TokenAuthMethod(apiKey, apiSecret);
	TransferBalanceEndpoint endpoint;
	
	@Before
	public void setUp() {
		endpoint = new TransferBalanceEndpoint(new HttpWrapper(authMethod));
	}
	
	@Test
	public void testAuthMethod() {
		Class<?>[] authMethods = endpoint.getAcceptableAuthMethods();
		assertEquals(1, authMethods.length);
		assertEquals(authMethod.getClass(), authMethods[0]);
	}
	
	@Test
	public void testDefaultUri() throws Exception {
		BalanceTransfer request = BalanceTransfer.builder()
				.from("7c9738e6").to("ad6dc56f")
				.reference("This gets added to the audit log")
				.amount(BigDecimal.valueOf(123.45)).build();

		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals("POST", builder.getMethod());
		String expectedUri = "https://api.nexmo.com/accounts/"+apiKey+"/balance-transfers";
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		String expectedRequest = "{\"from\":\""+request.getFrom()+"\",\"to\":\""+request.getTo() +
				"\",\"amount\":"+request.getAmount()+",\"reference\":\""+request.getReference()+"\"}";
		assertEquals(expectedRequest, EntityUtils.toString(builder.getEntity()));
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		UUID transferId = UUID.randomUUID();
		String responseJson = expectedRequest.replace("{\"from",
				"{\"balance_transfer_id\":\""+transferId+"\",\"from"
		);
		BalanceTransfer parsed = endpoint.parseResponse(TestUtils.makeJsonHttpResponse(200, responseJson));
		assertEquals(request, parsed);
		assertEquals(transferId, parsed.getBalanceTransferId());
	}

	@Test
	public void testCustomUri() throws Exception {
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri(baseUri).build(), authMethod);
		endpoint = new TransferBalanceEndpoint(wrapper);
		BalanceTransfer request = BalanceTransfer.builder()
				.from("ad6dc56f").to("7c9738e6")
				.amount(BigDecimal.valueOf(67.89)).build();

		String expectedUri = baseUri + "/accounts/"+apiKey+"/balance-transfers";
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		String expectedRequest = "{\"from\":\""+request.getFrom() +
				"\",\"to\":\""+request.getTo() + "\",\"amount\":"+request.getAmount()+"}";
		assertEquals(expectedRequest, EntityUtils.toString(builder.getEntity()));
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		assertEquals("POST", builder.getMethod());
	}
	
	@Test
	public void testEmptyResponse() throws Exception {
		HttpResponse mockResponse = TestUtils.makeJsonHttpResponse(200, "{}");
		BalanceTransfer parsed = endpoint.parseResponse(mockResponse);
		assertNotNull(parsed);
		assertNull(parsed.getBalanceTransferId());
		assertNull(parsed.getCreatedAt());
		assertNull(parsed.getAmount());
		assertNull(parsed.getFrom());
		assertNull(parsed.getTo());
		assertNull(parsed.getReference());
	}
	
	@Test(expected = VonageResponseParseException.class)
	public void testInvalidResponse() {
		BalanceTransfer.fromJson("{malformed]");
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