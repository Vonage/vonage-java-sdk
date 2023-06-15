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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.*;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class UpdateListItemEndpointTest {
	UpdateListItemEndpoint endpoint;
	String listId = UUID.randomUUID().toString();
	String itemId = UUID.randomUUID().toString();
	
	@Before
	public void setUp() {
		endpoint = new UpdateListItemEndpoint(new HttpWrapper());
	}
	
	@Test
	public void testAuthMethod() {
		Class<?>[] authMethods = endpoint.getAcceptableAuthMethods();
		assertEquals(1, authMethods.length);
		assertEquals(JWTAuthMethod.class, authMethods[0]);
	}
	
	@Test
	public void testDefaultUri() throws Exception {
		Map<String, ?> data = Collections.singletonMap("foo", "bar");
		ListItemRequestWrapper request = new ListItemRequestWrapper(listId, itemId, data);
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals("PUT", builder.getMethod());
		String expectedUri = "https://api-eu.vonage.com/v0.1/bulk/lists/"+request.listId+"/items/"+request.itemId;
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		String expectedRequest = "{\"data\":{\"foo\":\"bar\"}}";
		assertEquals(expectedRequest, EntityUtils.toString(builder.getEntity()));
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
	}

	@Test
	public void testCustomUri() throws Exception {
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri(baseUri).build());
		endpoint = new UpdateListItemEndpoint(wrapper);
		ListItemRequestWrapper request = new ListItemRequestWrapper(listId, itemId, Collections.emptyMap());
		String expectedUri = baseUri + "/v0.1/bulk/lists/"+request.listId+"/items/"+request.itemId;
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		String expectedRequest = "{\"data\":{}}";
		assertEquals(expectedRequest, EntityUtils.toString(builder.getEntity()));
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		assertEquals("PUT", builder.getMethod());
	}

	@Test
	public void testFullResponse() throws Exception {
		LinkedHashMap<String, Object> data = new LinkedHashMap<>(8);
		data.put("firstName", "Alice");
		data.put("registered", true);
		data.put("number", 15550067383L);
		data.put("props", Collections.emptyMap());
		data.put("keywords", Collections.singleton("Test"));
		Instant createdAt = Instant.now();
		Instant updatedAt = createdAt.plusSeconds(3600);
		UUID id = UUID.fromString("29192c4a-4058-49da-86c2-3e349d1065b7");
		UUID listId = UUID.fromString("4cb98f71-a879-49f7-b5cf-2314353eb52c");
		String expectedResponse = "{\n" +
				"\"data\":"+new ObjectMapper().writeValueAsString(data)+",\n" +
				"\"created_at\":\""+createdAt+"\",\n" +
				"\"updated_at\":\""+updatedAt+"\",\n" +
				"\"id\":\""+id+"\",\n" +
				"\"list_id\":\""+listId+"\"\n}";
		HttpResponse mockResponse = TestUtils.makeJsonHttpResponse(200, expectedResponse);
		ListItem parsed = endpoint.parseResponse(mockResponse);
		assertNotNull(parsed);
		assertNotNull(parsed.getData());
		assertEquals(data.size(), parsed.getData().size());
		assertEquals(data.keySet(), parsed.getData().keySet());
		assertEquals(data.values().toString(), parsed.getData().values().toString());
		assertEquals(createdAt, parsed.getCreatedAt());
		assertEquals(updatedAt, parsed.getUpdatedAt());
		assertEquals(id, parsed.getId());
		assertEquals(listId, parsed.getListId());
	}

	@Test
	public void testEmptyResponse() throws Exception {
		HttpResponse mockResponse = TestUtils.makeJsonHttpResponse(200, "{}");
		ListItem parsed = endpoint.parseResponse(mockResponse);
		assertNotNull(parsed);
		assertNull(parsed.getData());
		assertNull(parsed.getCreatedAt());
		assertNull(parsed.getUpdatedAt());
		assertNull(parsed.getId());
		assertNull(parsed.getListId());
	}

	@Test(expected = VonageResponseParseException.class)
	public void testInvalidResponse() {
		ListItem.fromJson("{malformed]");
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