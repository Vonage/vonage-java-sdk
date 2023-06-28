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
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;

public class ListRoomsEndpointTest {
	private ListRoomsEndpoint endpoint;
	
	@Before
	public void setUp() {
		endpoint = new ListRoomsEndpoint(new HttpWrapper(new JWTAuthMethod("app-id", new byte[0])));
	}
	
	@Test
	public void testDefaultUri() throws Exception {
		ListRoomsRequest request = new ListRoomsRequest(null, null, null, null);
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals("GET", builder.getMethod());
		String expectedUri = "https://api-eu.vonage.com/meetings/rooms";
		assertEquals(expectedUri, builder.build().getURI().toString());
		Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
		assertEquals(0, params.size());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		String expectedResponse = "{\n" +
				"\"page_size\":"+request.pageSize+",\"_embedded\":[{}],\"total_items\":3}";
		HttpResponse mockResponse = TestUtils.makeJsonHttpResponse(200, expectedResponse);
		ListRoomsResponse parsed = endpoint.parseResponse(mockResponse);
		assertNull(parsed.getLinks());
		assertEquals(1, parsed.getMeetingRooms().size());
		assertNotNull(parsed.getMeetingRooms().get(0));
		assertEquals(3, parsed.getTotalItems().intValue());
	}

	@Test
	public void testCustomUri() throws Exception {
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri(baseUri).build());
		endpoint = new ListRoomsEndpoint(wrapper);
		ListRoomsRequest request = new ListRoomsRequest(51, 150, 25, null);
		String expectedUri = baseUri + "/meetings/rooms?" +
				"start_id="+request.startId+"&end_id="+request.endId+"&page_size="+request.pageSize;
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals(expectedUri, builder.build().getURI().toString());
		Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
		assertEquals(3, params.size());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		assertEquals("GET", builder.getMethod());
	}

	@Test(expected = MeetingsResponseException.class)
	public void testUnsuccessfulResponse() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(400, "{}"));
	}

	@Test(expected = VonageResponseParseException.class)
	public void testParseMalformedResponse() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(200, "{malformed]"));
	}
}