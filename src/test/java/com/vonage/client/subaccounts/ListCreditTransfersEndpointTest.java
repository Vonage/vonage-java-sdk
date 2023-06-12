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
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.time.Instant;
import java.util.List;

public class ListCreditTransfersEndpointTest {
	final String apiKey = "a1b2c3d4", apiSecret = "1234567890abcdef";
	final AuthMethod authMethod = new TokenAuthMethod(apiKey, apiSecret);
	ListCreditTransfersEndpoint endpoint;

	@Before
	public void setUp() {
		endpoint = new ListCreditTransfersEndpoint(new HttpWrapper(authMethod));
	}

	@Test
	public void testAuthMethod() {
		Class<?>[] authMethods = endpoint.getAcceptableAuthMethods();
		assertEquals(1, authMethods.length);
		assertEquals(authMethod.getClass(), authMethods[0]);
	}

	@Test
	public void testDefaultUri() throws Exception {
		String sub1 = "def123ab", sub2 = "567a90bc";
		String startDate = "2022-06-01T08:00:00Z", endDate = "2023-06-08T09:01:40Z";
		ListTransfersFilter request = ListTransfersFilter.builder()
				.startDate(Instant.parse(startDate))
				.endDate(Instant.parse(endDate))
				.subaccounts(sub1, sub2, sub2).build();
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals("GET", builder.getMethod());
		String expectedUri = "https://api.nexmo.com/accounts/"+apiKey+"/credit-transfers?" +
				"start_date=2022-06-01T08%3A00%3A00Z&end_date=2023-06-08T09%3A01%3A40Z" +
				"&subaccount=" + sub1 + "&subaccount=" + sub2;
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		List<NameValuePair> params = builder.getParameters();
		assertEquals(4, params.size());
		assertEquals(2, request.getSubaccounts().size());
		assertEquals(Instant.parse(startDate), request.getStartDate());
		assertEquals(Instant.parse(endDate), request.getEndDate());
	}

	@Test
	public void testCustomUri() throws Exception {
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri(baseUri).build(), authMethod);
		endpoint = new ListCreditTransfersEndpoint(wrapper);
		String expectedUri = baseUri + "/accounts/"+apiKey+"/credit-transfers?start_date=1970-01-01T00%3A00%3A00Z";
		ListTransfersFilter request = ListTransfersFilter.builder().build();
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals(1, builder.getParameters().size());
		assertEquals(Instant.EPOCH, request.getStartDate());
		assertNull(request.getEndDate());
		assertNull(request.getSubaccounts());
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		assertEquals("GET", builder.getMethod());
	}

	@Test(expected = VonageResponseParseException.class)
	public void testMalformedResponse() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(200, "{\"_embedded}"));
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