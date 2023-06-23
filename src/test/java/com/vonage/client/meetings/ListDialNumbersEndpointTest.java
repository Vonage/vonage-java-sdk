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
package com.vonage.client.meetings;

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import com.vonage.client.TestUtils;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.RequestBuilder;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.Locale;

public class ListDialNumbersEndpointTest {
	private ListDialNumbersEndpoint endpoint;
	
	@Before
	public void setUp() {
		endpoint = new ListDialNumbersEndpoint(new HttpWrapper(new JWTAuthMethod("app-id", new byte[0])));
	}
	
	@Test
	public void testDefaultUri() throws Exception {
		RequestBuilder builder = endpoint.makeRequest(null);
		assertEquals("GET", builder.getMethod());
		String expectedUri = "https://api-eu.vonage.com/meetings/dial-in-numbers";
		assertEquals(expectedUri, builder.build().getURI().toString());
		String expectedPayload = "[\n" +
				"{},   {\n" +
				"      \"number\": \"17323338801\",\n" +
				"      \"locale\": \"en-US\",\n" +
				"      \"displayName\": \"United States\"\n" +
				"   }," +
				"  {\"locale\": \"de-DE\",\"unknown property\":0}\n" +
				"]";
		HttpResponse mockResponse = TestUtils.makeJsonHttpResponse(200, expectedPayload);
		List<DialInNumber> parsed = endpoint.parseResponse(mockResponse);
		assertEquals(3, parsed.size());

		DialInNumber empty = parsed.get(0);
		assertNotNull(empty);
		assertNull(empty.getNumber());
		assertNull(empty.getDisplayName());
		assertNull(empty.getLocale());

		DialInNumber usa = parsed.get(1);
		assertEquals("17323338801", usa.getNumber());
		assertEquals("United States", usa.getDisplayName());
		assertEquals(Locale.US, usa.getLocale());

		DialInNumber germany = parsed.get(2);
		assertEquals(Locale.GERMANY, germany.getLocale());
		assertNull(germany.getDisplayName());
		assertNull(germany.getNumber());
	}

	@Test
	public void testCustomUri() throws Exception {
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri(baseUri).build());
		endpoint = new ListDialNumbersEndpoint(wrapper);
		String expectedUri = baseUri + "/meetings/dial-in-numbers";
		RequestBuilder builder = endpoint.makeRequest(null);
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals("GET", builder.getMethod());
	}

	@Test(expected = HttpResponseException.class)
	public void testUnsuccessfulResponse() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(400, ""));
	}
}