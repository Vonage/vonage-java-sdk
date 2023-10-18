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
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.Before;
import org.junit.Test;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateListItemEndpointTest {
	CreateListItemEndpoint endpoint;
	final UUID listId = UUID.randomUUID();

	@Before
	public void setUp() {
		endpoint = new CreateListItemEndpoint(new HttpWrapper());
	}

	@Test
	public void testAuthMethod() {
		Class<?>[] authMethods = endpoint.getAcceptableAuthMethods();
		assertEquals(1, authMethods.length);
		assertEquals(JWTAuthMethod.class, authMethods[0]);
	}
	
	@Test
	public void testDefaultUri() throws Exception {
		Map<String, ?> data =  Collections.singletonMap("Property", "Value");
		ListItemRequestWrapper request = new ListItemRequestWrapper(listId, null, data);

		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals("POST", builder.getMethod());
		String expectedUri = "https://api-eu.vonage.com/v0.1/bulk/lists/"+listId+"/items";
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		String dataStr = "\"data\":{\"Property\":\"Value\"}";
		String expectedRequest = "{"+dataStr+"}";
		assertEquals(expectedRequest, EntityUtils.toString(builder.getEntity()));
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		UUID itemId = UUID.randomUUID();
		String expectedResponse = "{\"id\":\""+itemId+"\",\"list_id\":\""+listId+"\"," +
				dataStr + ",\"created_at\":\"2022-06-19T17:59:28.085Z\"}";
		HttpResponse mockResponse = TestUtils.makeJsonHttpResponse(200, expectedResponse);
		ListItem parsed = endpoint.parseResponse(mockResponse);
		assertNotNull(parsed);
		assertEquals(1, parsed.getData().size());
		assertEquals("Value", parsed.getData().get("Property"));
		assertEquals(listId, parsed.getListId());
		assertEquals(itemId, parsed.getId());
		assertTrue(parsed.getCreatedAt().isBefore(Instant.now()));
		assertNull(parsed.getUpdatedAt());
	}

	@Test
	public void testCustomUri() throws Exception {
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri(baseUri).build());
		endpoint = new CreateListItemEndpoint(wrapper);
		String expectedUri = baseUri + "/v0.1/bulk/lists/"+listId+"/items";
		ListItemRequestWrapper request = new ListItemRequestWrapper(listId, null, Collections.emptyMap());
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		String expectedRequest = "{\"data\":{}}";
		assertEquals(expectedRequest, EntityUtils.toString(builder.getEntity()));
		assertEquals("POST", builder.getMethod());
	}

	@Test(expected = ProactiveConnectResponseException.class)
	public void test400Response() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(400, "{}"));
	}
	
	@Test(expected = ProactiveConnectResponseException.class)
	public void test500Response() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(500, "{}"));
	}

	@Test(expected = VonageUnexpectedException.class)
	public void triggerJsonProcessingException() {
		Map<String, Object> data = new HashMap<>(2);
		ListItemRequestWrapper wrapper = new ListItemRequestWrapper(null, null, data);
		data.put("self", wrapper);
		wrapper.toJson();
	}
}