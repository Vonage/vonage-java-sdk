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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import com.vonage.client.TestUtils;
import com.vonage.client.VonageUnexpectedException;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import java.time.Instant;
import java.util.UUID;

public class UpdateRoomEndpointTest {
	private UpdateRoomEndpoint endpoint;
	
	@Before
	public void setUp() {
		endpoint = new UpdateRoomEndpoint(new HttpWrapper(new JWTAuthMethod("app-id", new byte[0])));
	}
	
	@Test
	public void testMakeRequestAllParameters() throws Exception {
		UUID roomId = MeetingsClientTest.ROOM_ID, themeId = UUID.fromString("5af77e5e-410d-489c-a30e-21aaf8482715");
		UpdateRoomRequest request = UpdateRoomRequest.builder()
			.expireAfterUse(false)
			.initialJoinOptions(InitialJoinOptions.builder().microphoneState(MicrophoneState.ON).build())
			.callbackUrls(CallbackUrls.builder().build())
			.availableFeatures(AvailableFeatures.builder().build())
			.themeId(themeId)
			.joinApprovalLevel(JoinApprovalLevel.AFTER_OWNER_ONLY)
			.expiresAt(Instant.MAX.minusSeconds(86400))
			.build();

		request.roomId = roomId;
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals("PATCH", builder.getMethod());
		String expectedUri = "https://api-eu.vonage.com/meetings/rooms/"+roomId;
		assertEquals(expectedUri, builder.build().getURI().toString());

		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		String expectedRequest = "{\"update_details\":{\"expire_after_use\":false,\"initial_join_options\":{\"microphone_state\":\"on\"}," +
				"\"callback_urls\":{},\"available_features\":{},\"join_approval_level\":\"after_owner_only\"," +
				"\"theme_id\":\"5af77e5e-410d-489c-a30e-21aaf8482715\",\"expires_at\":\"+1000000000-12-30T23:59:59.999Z\"}}";
		assertEquals(expectedRequest, EntityUtils.toString(builder.getEntity()));
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		MeetingRoom parsed = endpoint.parseResponse(TestUtils.makeJsonHttpResponse(200, "{}"));
		assertNotNull(parsed);
	}

	@Test
	public void testCustomUri() throws Exception {
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri(baseUri).build());
		endpoint = new UpdateRoomEndpoint(wrapper);
		String expectedUri = baseUri + "/meetings/rooms/"+MeetingsClientTest.RANDOM_ID;
		UpdateRoomRequest request = UpdateRoomRequest.builder().build();
		request.roomId = MeetingsClientTest.RANDOM_ID;
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		assertEquals("{\"update_details\":{}}", EntityUtils.toString(builder.getEntity()));
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		assertEquals("PATCH", builder.getMethod());
	}

	@Test(expected = MeetingsResponseException.class)
	public void testUnsuccessfulResponse() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(400, "{}"));
	}

	@Test(expected = VonageUnexpectedException.class)
	public void triggerJsonProcessingException() {
		class SelfRefrencing extends UpdateRoomRequest {
			@JsonProperty("self") final SelfRefrencing self = this;
			SelfRefrencing() {
				super(UpdateRoomRequest.builder());
			}
		}
		new SelfRefrencing().toJson();
	}
}