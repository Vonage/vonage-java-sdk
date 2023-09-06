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
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import java.util.UUID;

public class SearchThemeRoomsEndpointTest {
	private SearchThemeRoomsEndpoint endpoint;
	
	@Before
	public void setUp() {
		endpoint = new SearchThemeRoomsEndpoint(new HttpWrapper(new JWTAuthMethod("app-id", new byte[0])));
	}
	
	@Test
	public void testMakeRequestAllParameters() throws Exception {
		ListRoomsRequest request = new ListRoomsRequest(21, 33, 50, UUID.randomUUID());
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals("GET", builder.getMethod());
		String expectedUri = "https://api-eu.vonage.com/meetings/themes/"+request.themeId+
				"/rooms?start_id="+request.startId+"&end_id="+request.endId+"&page_size="+request.pageSize;
		assertEquals(expectedUri, builder.build().getURI().toString());
		Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
		assertEquals(3, params.size());

		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		String expectedResponse = "{\n" +
				"\"page_size\":"+request.pageSize+",\"_embedded\":[{},{},{}," +
				"{\"type\":\"instant\"},{\"nonsense\":0}],\"_links\":{\n" +
				"\"self\":{\"href\":\"https://vonage.com\"},\"prev\":{},\"next\":{\"href\":null,\"no\":1}}}";
		HttpResponse mockResponse = TestUtils.makeJsonHttpResponse(200, expectedResponse);
		ListRoomsResponse parsed = endpoint.parseResponse(mockResponse);
		assertEquals(request.pageSize, parsed.getPageSize());
		assertEquals(5, parsed.getMeetingRooms().size());
		assertEquals(RoomType.INSTANT, parsed.getMeetingRooms().get(3).getType());
		assertNull(parsed.getLinks().getNextUrl());
		assertNull(parsed.getLinks().getPrevUrl());
		assertNull(parsed.getLinks().getFirstUrl());
		assertEquals("https://vonage.com", parsed.getLinks().getSelfUrl().toString());
	}

	@Test
	public void testCustomUri() throws Exception {
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri(baseUri).build());
		endpoint = new SearchThemeRoomsEndpoint(wrapper);
		ListRoomsRequest request = new ListRoomsRequest(1, null, null, UUID.randomUUID());
		String expectedUri = baseUri + "/meetings/themes/"+request.themeId+"/rooms?start_id="+request.startId;
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals(expectedUri, builder.build().getURI().toString());
		Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
		assertEquals(1, params.size());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		assertEquals("GET", builder.getMethod());
	}

	@Test(expected = MeetingsResponseException.class)
	public void testUnsuccessfulResponse() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(400, "{}"));
	}
}