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
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.auth.JWTAuthMethod;
import com.vonage.client.common.HalLinks;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public class ListEventsEndpointTest {
	ListEventsEndpoint endpoint;

	@Before
	public void setUp() {
		endpoint = new ListEventsEndpoint(new HttpWrapper());
	}
	
	@Test
	public void testAuthMethod() {
		Class<?>[] authMethods = endpoint.getAcceptableAuthMethods();
		assertEquals(1, authMethods.length);
		assertEquals(JWTAuthMethod.class, authMethods[0]);
	}
	
	@Test
	public void testDefaultUriAllParams() throws Exception {
		ListEventsFilter request = ListEventsFilter.builder()
				.order(SortOrder.ASC)
				.runId("51aca838-2cf6-4100-b0d2-e74ac0e95c88")
				.runItemId("d6f1d012-227e-4025-a388-3d1aaa05bc29")
				.invocationId("29bab76c-156e-42e4-ab38-f6a465f0048e")
				.actionId("99ea10e0-1f14-4f55-b976-3c88ea8ec4cd")
				.traceId("a3c7472d-495a-4f6f-b29a-1646ffe70643")
				.recipientId("15551584817")
				.sourceContext("uk-customers")
				.sourceType(SourceType.EVENT_HANDLER)
				.startDate(Instant.EPOCH)
				.endDate(Instant.parse("2023-06-29T12:39:34.319723Z"))
				.build();

		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals("GET", builder.getMethod());
		String expectedUri = "https://api-eu.vonage.com/v0.1/bulk/events?";
		assertTrue(builder.build().getURI().toString().startsWith(expectedUri));
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());

		Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
		assertEquals(13, params.size());
		assertEquals("1", params.get("page"));
		assertEquals("1000", params.get("page_size"));
		assertEquals("asc", params.get("order"));
		assertEquals("51aca838-2cf6-4100-b0d2-e74ac0e95c88", params.get("run_id"));
		assertEquals("d6f1d012-227e-4025-a388-3d1aaa05bc29", params.get("run_item_id"));
		assertEquals("29bab76c-156e-42e4-ab38-f6a465f0048e", params.get("invocation_id"));
		assertEquals("99ea10e0-1f14-4f55-b976-3c88ea8ec4cd", params.get("action_id"));
		assertEquals("a3c7472d-495a-4f6f-b29a-1646ffe70643", params.get("trace_id"));
		assertEquals("15551584817", params.get("recipient_id"));
		assertEquals("uk-customers", params.get("src_ctx"));
		assertEquals("event-handler", params.get("src_type"));
		assertEquals("1970-01-01T00:00:00Z", params.get("date_start"));
		assertEquals("2023-06-29T12:39:34Z", params.get("date_end"));

		assertNotNull(request.getOrder());
		assertNotNull(request.getActionId());
		assertNotNull(request.getInvocationId());
		assertNotNull(request.getRecipientId());
		assertNotNull(request.getRunItemId());
		assertNotNull(request.getRunId());
		assertNotNull(request.getTraceId());
		assertNotNull(request.getSourceContext());
		assertNotNull(request.getSourceType());
		assertNotNull(request.getStartDate());
		assertNotNull(request.getEndDate());
	}

	@Test
	public void testCustomUri() throws Exception {
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri(baseUri).build());
		endpoint = new ListEventsEndpoint(wrapper);
		String expectedUri = baseUri + "/v0.1/bulk/events?page=1&page_size=1000";
		RequestBuilder builder = endpoint.makeRequest(ListEventsFilter.builder().build());
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		assertEquals("GET", builder.getMethod());
	}

	@Test
	public void testEmptyResponse() throws Exception {
		HttpResponse mockResponse = TestUtils.makeJsonHttpResponse(200, "{}");
		ListEventsResponse parsed = endpoint.parseResponse(mockResponse);
		assertNotNull(parsed);
		assertNull(parsed.getEvents());
		assertNull(parsed.getLinks());
		assertNull(parsed.getPage());
		assertNull(parsed.getPageSize());
		assertNull(parsed.getTotalItems());
		assertNull(parsed.getTotalPages());
	}

	@Test
	public void testFullResponse() throws Exception {
		String expectedResponse = "{\n" +
				"   \"page_size\": 50,\n" +
				"   \"page\": 7,\n" +
				"   \"total_pages\": 9,\n" +
				"   \"total_items\": 42,\n" +
				"   \"_links\": {\n" +
				"      \"first\": {\n" +
				"         \"href\": \"https://api-eu.vonage.com/v0.1/bulk/lists?page=5&page_size=10\"\n" +
				"      },\n" +
				"      \"self\": {\n" +
				"         \"href\": \"https://api-eu.vonage.com/v0.1/bulk/lists?page=5&page_size=10\"\n" +
				"      },\n" +
				"      \"prev\": {\n" +
				"         \"href\": \"https://api-eu.vonage.com/v0.1/bulk/lists?page=5&page_size=10\"\n" +
				"      },\n" +
				"      \"next\": {\n" +
				"         \"href\": \"https://api-eu.vonage.com/v0.1/bulk/lists?page=5&page_size=10\"\n" +
				"      }\n" +
				"   },\n" +
				"   \"_embedded\": {\n" +
				"      \"events\": [\n" +
				"         {},{},{}\n" +
				"      ]\n" +
				"   }\n" +
				"}";
		HttpResponse mockResponse = TestUtils.makeJsonHttpResponse(200, expectedResponse);
		ListEventsResponse parsed = endpoint.parseResponse(mockResponse);
		assertNotNull(parsed);
		HalLinks links = parsed.getLinks();
		assertNotNull(links);
		assertNotNull(links.getSelfUrl());
		assertNotNull(links.getFirstUrl());
		assertNotNull(links.getNextUrl());
		assertNotNull(links.getPrevUrl());
		assertEquals(7, parsed.getPage().intValue());
		assertEquals(50, parsed.getPageSize().intValue());
		assertEquals(9, parsed.getTotalPages().intValue());
		assertEquals(42, parsed.getTotalItems().intValue());
		List<Event> events = parsed.getEvents();
		assertNotNull(events);
		assertEquals(3, events.size());
		events.forEach(Assert::assertNotNull);
	}

	@Test(expected = VonageResponseParseException.class)
	public void testInvalidResponse() {
		ListEventsResponse.fromJson("{malformed]");
	}

	@Test(expected = ProactiveConnectResponseException.class)
	public void test400Response() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(400, "{}"));
	}
	
	@Test(expected = ProactiveConnectResponseException.class)
	public void test500Response() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(500, "{}"));
	}
}