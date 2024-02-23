/*
 *   Copyright 2024 Vonage
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
package com.vonage.client.conversations;

import com.vonage.client.ClientTest;
import com.vonage.client.RestEndpoint;
import com.vonage.client.TestUtils;
import com.vonage.client.common.HttpMethod;
import com.vonage.client.common.SortOrder;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import java.net.URI;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class ConversationsClientTest extends ClientTest<ConversationsClient> {
	static final int PAGE = 2, PAGE_SIZE = 30, SEQUENCE_NUMBER = 159, TTL = 60;
	static final SortOrder ORDER = SortOrder.DESCENDING;
	static final ConversationStatus CONVERSATION_STATE = ConversationStatus.INACTIVE;
	static final Map<String, Object> CONVERSATION_CUSTOM_DATA = Map.of(
			"property1", "value1",
			"prop2", "Val 2"
	);

	static final String
			INVALID_UUID_STR = "12345678-9abc-defg-hijk-lmnopqrstuvw",
			APPLICATION_ID_STR = "fa185f5e-2a5a-492c-9e0a-ba3ab6844441",
			CONVERSATION_ID = "CON-d66d47de-5bcb-4300-94f0-0c9d4b948e9a",
			MEMBER_ID = "MEM-df8e57d8-1c8e-4573-bf4d-29d5414dcb42",
			USER_ID = "USR-82e028d9-5201-4f1e-8188-604b2d3471ec",
			SESSION_ID = "SES-63f61863-4a51-4f6b-86e1-46edebio0391",
			START_DATE_STR = "2017-12-30 10:08:59",
			END_DATE_STR = "2018-01-03 12:00:00",
			TIMESTAMP_CREATED_STR = "2019-09-03T18:40:24.324Z",
			TIMESTAMP_UPDATED_STR = "2019-09-03T18:40:24.324Z",
			TIMESTAMP_DESTROYED_STR = "2022-02-03T04:58:59.601Z",
			CONVERSATION_NAME = "customer_chat",
			CONVERSATION_DISPLAY_NAME = "Chat with Customer",
			CONVERSATION_IMAGE_URL_STR = "https://example.com/image.png",
			CONVERSATION_STATE_STR = "INACTIVE",
			CONVERSATION_TYPE = "quick_chat",
			CONVERSATION_CUSTOM_SORT_KEY = "CSK_1",
			CONVERSATION_CUSTOM_DATA_STR = "{\"property1\":\"value1\",\"prop2\",\"Val 2\"}",
			CONVERSATION_CURSOR = "7EjDNQrAcipmOnc0HCzpQRkhBULzY44ljGUX4lXKyUIVfiZay5pv9wg=",
			SAMPLE_BASE_CONVERSATION_RESPONSE_PARTIAL = STR."""
				{
				   "id": "\{CONVERSATION_ID}",
				   "name": "\{CONVERSATION_NAME}",
				   "display_name": "\{CONVERSATION_DISPLAY_NAME}",
				   "image_url": "\{CONVERSATION_IMAGE_URL_STR}",
				   "timestamp": {
					  "created": "\{TIMESTAMP_CREATED_STR}",
					  "updated": "\{TIMESTAMP_UPDATED_STR}",
					  "destroyed": "\{TIMESTAMP_DESTROYED_STR}"
				   },""",
			SAMPLE_BASE_CONVERSATION_RESPONSE = STR."\{SAMPLE_BASE_CONVERSATION_RESPONSE_PARTIAL
                    .substring(0, SAMPLE_BASE_CONVERSATION_RESPONSE_PARTIAL.length() - 1)}\n\t}",
			SAMPLE_CONVERSATION_RESPONSE = SAMPLE_BASE_CONVERSATION_RESPONSE_PARTIAL + STR."""
				   "state": "\\{CONVERSATION_STATE_STR}",
				   "sequence_number": \\{SEQUENCE_NUMBER},
				   "properties": {
					  "ttl": \{TTL},
					  "type": "\{CONVERSATION_TYPE}",
					  "custom_sort_key": "\{CONVERSATION_CUSTOM_SORT_KEY}",
					  "custom_data": \{CONVERSATION_CUSTOM_DATA_STR}
				   },
				   "_links": {
					  "self": {
						 "href": "https://api.nexmo.com/v1/conversations/\{CONVERSATION_ID}"
					  }
				   }
				}
			""",
			SAMPLE_LIST_CONVERSATIONS_RESPONSE = STR."""
   				{
				   "page_size": \{PAGE_SIZE},
				   "_embedded": {
					  "conversations": [
						 {}, \{SAMPLE_BASE_CONVERSATION_RESPONSE}, {"id":null}
					  ]
				   },
				   "_links": {
					  "first": {
						 "href": "https://api.nexmo.com/v1/conversations?order=desc&page_size=10"
					  },
					  "self": {
						 "href": "https://api.nexmo.com/v1/conversations?order=desc&page_size=10&cursor=7EjDNQrAcipmOnc0HCzpQRkhBULzY44ljGUX4lXKyUIVfiZay5pv9wg="
					  },
					  "next": {
						 "href": "https://api.nexmo.com/v1/conversations?order=desc&page_size=10&cursor=7EjDNQrAcipmOnc0HCzpQRkhBULzY44ljGUX4lXKyUIVfiZay5pv9wg="
					  },
					  "prev": {
						 "href": "https://api.nexmo.com/v1/conversations?order=desc&page_size=10&cursor=7EjDNQrAcipmOnc0HCzpQRkhBULzY44ljGUX4lXKyUIVfiZay5pv9wg="
					  }
				   }
				}
			""";

	static final UUID APPLICATION_ID = UUID.fromString(APPLICATION_ID_STR);
	static final URI CONVERSATION_IMAGE_URL = URI.create(CONVERSATION_IMAGE_URL_STR);
	static final Instant
			START_DATE = Instant.parse(START_DATE_STR.replace(' ','T')+'Z'),
			END_DATE = Instant.parse(END_DATE_STR.replace(' ','T')+'Z'),
			TIMESTAMP_CREATED = Instant.parse(TIMESTAMP_CREATED_STR),
			TIMESTAMP_UPDATED = Instant.parse(TIMESTAMP_UPDATED_STR),
			TIMESTAMP_DESTROYED = Instant.parse(TIMESTAMP_DESTROYED_STR);


	public ConversationsClientTest() {
		client = new ConversationsClient(wrapper);
	}

	void assert401ResponseException(Executable invocation) throws Exception {
		assert401ApiResponseException(ConversationsResponseException.class, invocation);
	}

	static void assertEqualsSampleBaseConversation(BaseConversation parsed) {
		TestUtils.testJsonableBaseObject(parsed);
		assertEquals(CONVERSATION_ID, parsed.getId());
		assertEquals(CONVERSATION_NAME, parsed.getName());
		assertEquals(CONVERSATION_DISPLAY_NAME, parsed.getDisplayName());
		assertEquals(CONVERSATION_IMAGE_URL, parsed.getImageUrl());
		var timestamp = parsed.getTimestamp();
		assertNotNull(timestamp);
		assertEquals(TIMESTAMP_CREATED, timestamp.getCreated());
		assertEquals(TIMESTAMP_UPDATED, timestamp.getUpdated());
		assertEquals(TIMESTAMP_DESTROYED, timestamp.getDestroyed());
	}

	static void assertEqualsSampleConversation(Conversation parsed) {
		assertEqualsSampleBaseConversation(parsed);
		assertEquals(CONVERSATION_STATE, parsed.getState());
		assertEquals(SEQUENCE_NUMBER, parsed.getSequenceNumber());
		var properties = parsed.getProperties();
		assertNotNull(properties);
		assertEquals(TTL, properties.getTtl());
		assertEquals(CONVERSATION_TYPE, properties.getType());
		assertEquals(CONVERSATION_CUSTOM_SORT_KEY, properties.getCustomSortKey());
		assertEquals(CONVERSATION_CUSTOM_DATA, properties.getCustomData());
	}
	
	static void assertEmptyBaseConversation(BaseConversation parsed) {
		TestUtils.testJsonableBaseObject(parsed);
		assertNull(parsed.getId());
		assertNull(parsed.getName());
		assertNull(parsed.getDisplayName());
		assertNull(parsed.getImageUrl());
		assertNull(parsed.getTimestamp());
	}

	static void assertEqualsSampleListConversation(ListConversationsResponse parsed) {
		TestUtils.testJsonableBaseObject(parsed);
		var conversations = parsed.getConversations();
		assertNotNull(conversations);
		assertEquals(3, conversations.size());
		assertEmptyBaseConversation(conversations.getFirst());
		assertEqualsSampleBaseConversation(conversations.get(1));
		assertEmptyBaseConversation(conversations.getLast());
	}
	
	// CONVERSATIONS

	@Test
	public void testListConversations() throws Exception {
		ListConversationsRequest request = ListConversationsRequest.builder().build();
		stubResponseAndRun(SAMPLE_LIST_CONVERSATIONS_RESPONSE, client::listConversations);
		stubResponseAndRun(SAMPLE_LIST_CONVERSATIONS_RESPONSE,
				() -> client.listConversations(request)
		);
		stubResponseAndAssertThrows(200,
				() -> client.listConversations(null), NullPointerException.class
		);
		stubResponseAndAssertThrows(409,
				() -> client.listConversations(request),
				ConversationsResponseException.class
		);
		assert401ResponseException(() -> client.listConversations(request));
	}

	@Test
	public void testListConversationsEndpoint() throws Exception {
		new ConversationsEndpointTestSpec<ListConversationsRequest, ListConversationsResponse>() {

			@Override
			protected RestEndpoint<ListConversationsRequest, ListConversationsResponse> endpoint() {
				return client.listConversations;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(ListConversationsRequest request) {
				return "/v1/conversations";
			}

			@Override
			protected ListConversationsRequest sampleRequest() {
				return ListConversationsRequest.builder()
						.page(PAGE).pageSize(PAGE_SIZE).order(ORDER)
						.startDate(START_DATE).endDate(END_DATE).build();
			}

			@Override
			protected Map<String, String> sampleQueryParams() {
				return Map.of(
						"page", String.valueOf(PAGE),
						"page_size", String.valueOf(PAGE_SIZE),
						"order", String.valueOf(ORDER),
						"date_start", START_DATE_STR,
						"date_end", END_DATE_STR
				);
			}
		}
		.runTests();
	}


	@Test
	public void testCreateConversation() throws Exception {
		var request = Conversation.builder().build();
		stubResponseAndRun(201, SAMPLE_CONVERSATION_RESPONSE, () -> client.createConversation(request));
		stubResponseAndAssertThrows(201,
				() -> client.createConversation(null),
				NullPointerException.class
		);
		stubResponseAndAssertThrows(409,
				() -> client.createConversation(request),
				ConversationsResponseException.class
		);
		assert401ResponseException(() -> client.createConversation(request));
	}

	@Test
	public void testCreateConversationEndpoint() throws Exception {
		new ConversationsEndpointTestSpec<Conversation, Conversation>() {
			final String
					eventMask = "Test value",
					url = "http://example.com/callback",
					nccoUrl = "http://example.com/ncco";
			final CallbackHttpMethod method = CallbackHttpMethod.POST;

			@Override
			protected RestEndpoint<Conversation, Conversation> endpoint() {
				return client.createConversation;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected String expectedEndpointUri(Conversation request) {
				return "/v1/conversations";
			}

			@Override
			protected Conversation sampleRequest() {
				return Conversation.builder()
						.name(CONVERSATION_NAME)
						.displayName(CONVERSATION_DISPLAY_NAME)
						.imageUrl(CONVERSATION_IMAGE_URL_STR)
						.properties(ConversationProperties.builder()
							.ttl(TTL).type(CONVERSATION_TYPE)
							.customSortKey(CONVERSATION_CUSTOM_SORT_KEY)
							.customData(CONVERSATION_CUSTOM_DATA).build()
						)
						.callback(Callback.builder().url(url).eventMask(eventMask)
								.nccoUrl(nccoUrl).method(method).build()
						)
						.numbers().build();
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{}";
			}
		}
		.runTests();
	}


	@Test
	public void testGetConversation() throws Exception {
		stubResponse(200, SAMPLE_CONVERSATION_RESPONSE);
		assertEqualsSampleBaseConversation(client.getConversation(CONVERSATION_ID));
		stubResponseAndAssertThrows(200,
				() -> client.getConversation(null),
				NullPointerException.class
		);
		stubResponseAndAssertThrows(200,
				() -> client.getConversation(MEMBER_ID),
				IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(404,
				() -> client.getConversation(CONVERSATION_ID),
				ConversationsResponseException.class
		);
		assert401ResponseException(() -> client.getConversation(CONVERSATION_ID));
	}

	@Test
	public void testGetConversationEndpoint() throws Exception {
		new ConversationsEndpointTestSpec<String, Conversation>() {

			@Override
			protected RestEndpoint<String, Conversation> endpoint() {
				return client.getConversation;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(String request) {
				return "/v1/conversations/"+request;
			}

			@Override
			protected String sampleRequest() {
				return CONVERSATION_ID;
			}
		}
		.runTests();
	}


	@Test
	public void testUpdateConversation() throws Exception {
		var request = Conversation.builder()
				.imageUrl("ftp:///path/to/local/image.tiff")
				.displayName("Support").build();
		stubResponse(200, SAMPLE_CONVERSATION_RESPONSE);
		assertEqualsSampleConversation(client.updateConversation(CONVERSATION_ID, request));

		stubResponseAndAssertThrows(200,
				() -> client.updateConversation(null, request),
				NullPointerException.class
		);
		stubResponseAndAssertThrows(200,
				() -> client.updateConversation(CONVERSATION_ID, null),
				NullPointerException.class
		);
		stubResponseAndAssertThrows(200,
				() -> client.updateConversation("CON-"+SESSION_ID, request),
				IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(404,
				() -> client.updateConversation(CONVERSATION_ID, request),
				ConversationsResponseException.class
		);
		assert401ResponseException(() -> client.updateConversation(CONVERSATION_ID, request));
	}

	@Test
	public void testUpdateConversationEndpoint() throws Exception {
		new ConversationsEndpointTestSpec<Conversation, Conversation>() {

			@Override
			protected RestEndpoint<Conversation, Conversation> endpoint() {
				return client.updateConversation;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.PUT;
			}

			@Override
			protected String expectedEndpointUri(Conversation request) {
				return "/v1/conversations/"+request.getId();
			}

			@Override
			protected Conversation sampleRequest() {
				var request = Conversation.builder()
						.imageUrl(CONVERSATION_IMAGE_URL_STR)
						.displayName(CONVERSATION_DISPLAY_NAME)
						.name(CONVERSATION_NAME).build();
				request.id = CONVERSATION_ID;
				return request;
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{}";
			}
		}
		.runTests();
	}


	@Test
	public void testDeleteConversation() throws Exception {
		stubResponseAndRun(204, SAMPLE_CONVERSATION_RESPONSE,
				() -> client.deleteConversation(CONVERSATION_ID)
		);
		stubResponseAndAssertThrows(204,
				() -> client.deleteConversation(null),
				NullPointerException.class
		);
		stubResponseAndAssertThrows(204,
				() -> client.deleteConversation("CON-"+INVALID_UUID_STR),
				IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(404,
				() -> client.deleteConversation(CONVERSATION_ID),
				ConversationsResponseException.class
		);
		assert401ResponseException(() -> client.deleteConversation(CONVERSATION_ID));
	}

	@Test
	public void testDeleteConversationEndpoint() throws Exception {
		new ConversationsEndpointTestSpec<String, Void>() {

			@Override
			protected RestEndpoint<String, Void> endpoint() {
				return client.deleteConversation;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.DELETE;
			}

			@Override
			protected String expectedEndpointUri(String request) {
				return "/v1/conversations/"+request;
			}

			@Override
			protected String sampleRequest() {
				return CONVERSATION_ID;
			}
		}
		.runTests();
	}

	// USERS

	@Test
	public void testListUserConversations() throws Exception {
		var request = ListUserConversationsRequest.builder().build();
		String responseJson = "{}";
		stubResponseAndRun(responseJson, () -> client.listUserConversations(request));
		stubResponseAndAssertThrows(200, () -> client.listUserConversations(null), NullPointerException.class);
		stubResponseAndAssertThrows(401, () -> client.listUserConversations(request), ConversationsResponseException.class);
		assert401ResponseException(() -> client.listUserConversations(request));
	}

	@Test
	public void testListUserConversationsEndpoint() throws Exception {
		new ConversationsEndpointTestSpec<ListUserConversationsRequest, ListConversationsResponse>() {

			@Override
			protected RestEndpoint<ListUserConversationsRequest, ListConversationsResponse> endpoint() {
				return client.listUserConversations;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(ListUserConversationsRequest request) {
				return "/v1/users/"+request.userId+"/conversations";
			}

			@Override
			protected ListUserConversationsRequest sampleRequest() {
				var request = ListUserConversationsRequest.builder()
						.state(MemberState.INVITED)
						.orderBy(OrderBy.CUSTOM_SORT_KEY)
						.includeCustomData(true)
						.startDate(START_DATE)
						.build();
				request.userId = USER_ID;
				return request;
			}

			@Override
			protected Map<String, String> sampleQueryParams() {
				return Map.of(
						"user_id", USER_ID,
						"state", "INVITED",
						"order_by", "custom_sort_key",
						"include_custom_data", "true",
						"date_start", START_DATE_STR
				);
			}
		}
		.runTests();
	}

	@Test
	public void testListUserSessions() throws Exception {
		ListUserSessionsRequest request = null;
		String responseJson = "{}";
		stubResponseAndRun(responseJson, () -> client.listUserSessions(request));
		stubResponseAndAssertThrows(200, () -> client.listUserSessions(null), NullPointerException.class);
		stubResponseAndAssertThrows(401, () -> client.listUserSessions(request), ConversationsResponseException.class);
		assert401ResponseException(() -> client.listUserSessions(request));
	}

	@Test
	public void testListUserSessionsEndpoint() throws Exception {
		new ConversationsEndpointTestSpec<ListUserSessionsRequest, ListUserSessionsResponse>() {

			@Override
			protected RestEndpoint<ListUserSessionsRequest, ListUserSessionsResponse> endpoint() {
				return client.listUserSessions;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(ListUserSessionsRequest request) {
				return "/v1/users/"+request.userId+"/sessions";
			}

			@Override
			protected ListUserSessionsRequest sampleRequest() {
				var request = ListUserSessionsRequest.builder().build();
				request.userId = USER_ID;
				return request;
			}

			@Override
			protected Map<String, String> sampleQueryParams() {
				return Map.of();
			}
		}
		.runTests();
	}

	// MEMBERS

	/*@Test
	public void testListMembers() throws Exception {
		ListMembersRequest request = null;
		String responseJson = "{}";
		stubResponseAndRun(responseJson, () -> client.listMembers(request));
		stubResponseAndAssertThrows(200, () -> client.listMembers(null), NullPointerException.class);
		stubResponseAndAssertThrows(401, () -> client.listMembers(request), ConversationsResponseException.class);
		assert401ResponseException(() -> client.listMembers(request));
	}

	@Test
	public void testListMembersEndpoint() throws Exception {
		new ConversationsEndpointTestSpec<ListMembersRequest, ListMembersResponse>() {

			@Override
			protected RestEndpoint<ListMembersRequest, ListMembersResponse> endpoint() {
				return client.listMembers;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(ListMembersRequest request) {
				return "/v1/conversations/"+request.conversationId+"/members";
			}

			@Override
			protected ListMembersRequest sampleRequest() {
				var request = ListMembersRequest.builder().build();
				request.conversationId = CONVERSATION_ID;
				return request;
			}

			@Override
			protected Map<String, String> sampleQueryParams() {
				return Map.of();
			}
		}
		.runTests();
	}


	@Test
	public void testGetMember() throws Exception {
		ConversationResourceRequestWrapper request = null;
		String responseJson = "{}";
		stubResponseAndRun(responseJson, () -> client.getMember(request));
		stubResponseAndAssertThrows(200, () -> client.getMember(null), NullPointerException.class);
		stubResponseAndAssertThrows(401, () -> client.getMember(request), ConversationsResponseException.class);
		assert401ResponseException(() -> client.getMember(request));
	}

	@Test
	public void testGetMemberEndpoint() throws Exception {
		new ConversationsEndpointTestSpec<ConversationResourceRequestWrapper, Member>() {

			@Override
			protected RestEndpoint<ConversationResourceRequestWrapper, Member> endpoint() {
				return client.getMember;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(ConversationResourceRequestWrapper request) {
				return "/v1/conversations/"+request.conversationId+"/members/"+request.resourceId;
			}

			@Override
			protected ConversationResourceRequestWrapper sampleRequest() {
				return new ConversationResourceRequestWrapper(CONVERSATION_ID, MEMBER_ID);
			}
		}
		.runTests();
	}


	@Test
	public void testCreateMember() throws Exception {
		Member request = null;
		String responseJson = "{}";
		stubResponseAndRun(responseJson, () -> client.createMember(request));
		stubResponseAndAssertThrows(200, () -> client.createMember(null), NullPointerException.class);
		stubResponseAndAssertThrows(401, () -> client.createMember(request), ConversationsResponseException.class);
		assert401ResponseException(() -> client.createMember(request));
	}

	@Test
	public void testCreateMemberEndpoint() throws Exception {
		new ConversationsEndpointTestSpec<Member, Member>() {

			@Override
			protected RestEndpoint<Member, Member> endpoint() {
				return client.createMember;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected String expectedEndpointUri(Member request) {
				return "/v1/conversations/"+request.conversationId+"/members";
			}

			@Override
			protected Member sampleRequest() {
				var request = Member.builder().build();
				request.conversationId = CONVERSATION_ID;
				return request;
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{}";
			}
		}
		.runTests();
	}


	@Test
	public void testUpdateMember() throws Exception {
		UpdateMemberRequest request = null;
		String responseJson = "{}";
		stubResponseAndRun(responseJson, () -> client.updateMember(request));
		stubResponseAndAssertThrows(200, () -> client.updateMember(null), NullPointerException.class);
		stubResponseAndAssertThrows(401, () -> client.updateMember(request), ConversationsResponseException.class);
		assert401ResponseException(() -> client.updateMember(request));
	}

	@Test
	public void testUpdateMemberEndpoint() throws Exception {
		new ConversationsEndpointTestSpec<UpdateMemberRequest, Member>() {

			@Override
			protected RestEndpoint<UpdateMemberRequest, Member> endpoint() {
				return client.updateMember;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.PATCH;
			}

			@Override
			protected String expectedEndpointUri(UpdateMemberRequest request) {
				return "/v1/conversations/"+request.conversationId+"/members/"+request.resourceId;
			}

			@Override
			protected UpdateMemberRequest sampleRequest() {
				return UpdateMemberRequest.builder()
						.conversationId(CONVERSATION_ID)
						.memberId(MEMBER_ID)
						.state(MemberState.JOINED)
						.from("")
						.build();
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{}";
			}
		}
		.runTests();
	}*/
}