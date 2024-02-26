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

import com.vonage.client.DynamicEndpoint;
import com.vonage.client.HttpWrapper;
import com.vonage.client.RestEndpoint;
import com.vonage.client.VonageClient;
import com.vonage.client.auth.JWTAuthMethod;
import com.vonage.client.common.HttpMethod;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

/**
 * A client for talking to the Vonage ConversationsClient API. The standard way to obtain an instance of this class is to use
 * {@link VonageClient#getConversationsClient()}.
 */
public class ConversationsClient {
	final RestEndpoint<ListConversationsRequest, ListConversationsResponse> listConversations;
	final RestEndpoint<Conversation, Conversation> createConversation;
	final RestEndpoint<String, Conversation> getConversation;
	final RestEndpoint<Conversation, Conversation> updateConversation;
	final RestEndpoint<String, Void> deleteConversation;
	final RestEndpoint<ListUserConversationsRequest, ListUserConversationsResponse> listUserConversations;
	final RestEndpoint<ListUserSessionsRequest, ListUserSessionsResponse> listUserSessions;
	final RestEndpoint<ListMembersRequest, ListMembersResponse> listMembers;
	final RestEndpoint<ConversationResourceRequestWrapper, Member> getMember;
	final RestEndpoint<Member, Member> createMember;
	final RestEndpoint<UpdateMemberRequest, Member> updateMember;

	/**
	 * Constructor.
	 *
	 * @param wrapper (REQUIRED) shared HTTP wrapper object used for making REST calls.
	 */
	@SuppressWarnings("unchecked")
	public ConversationsClient(HttpWrapper wrapper) {
		final String v1c = "/v1/conversations/", v1u = "/v1/users/", mems = "/members/";

		class Endpoint<T, R> extends DynamicEndpoint<T, R> {
			Endpoint(Function<T, String> pathGetter, HttpMethod method, R... type) {
				super(DynamicEndpoint.<T, R> builder(type)
					.authMethod(JWTAuthMethod.class)
					.responseExceptionType(ConversationsResponseException.class)
					.requestMethod(method).wrapper(wrapper).pathGetter((de, req) -> {
						String base = de.getHttpWrapper().getHttpConfig().getApiBaseUri();
						return base + pathGetter.apply(req);
					})
				);
			}
		}

		listConversations = new Endpoint<>(req -> v1c, HttpMethod.GET);
		createConversation = new Endpoint<>(req -> v1c, HttpMethod.POST);
		getConversation = new Endpoint<>(id -> v1c+id, HttpMethod.GET);
		updateConversation = new Endpoint<>(req -> v1c+req.getId(), HttpMethod.PUT);
		deleteConversation = new Endpoint<>(id -> v1c+id, HttpMethod.DELETE);
		listUserConversations = new Endpoint<>(req -> v1u+req.userId+"/conversations", HttpMethod.GET);
		listUserSessions = new Endpoint<>(req -> v1u+req.userId+"/sessions", HttpMethod.GET);
		listMembers = new Endpoint<>(req -> v1c+req.conversationId+mems, HttpMethod.GET);
		getMember = new Endpoint<>(req -> v1c+req.conversationId+mems+req.resourceId, HttpMethod.GET);
		createMember = new Endpoint<>(req -> v1c+req.getConversationId()+mems, HttpMethod.POST);
		updateMember = new Endpoint<>(req -> v1c+req.conversationId+mems+req.resourceId, HttpMethod.PATCH);
	}

	// VALIDATION

	private static String validateId(String prefix, String arg) {
		final int prefixLength = prefix.length(), expectedLength = prefixLength + 36;
		if (arg == null || arg.length() != expectedLength) {
			throw new IllegalArgumentException(
					"Invalid ID: '"+arg+"' is not "+expectedLength+" characters in length."
			);
		}
		if (!arg.startsWith(prefix)) {
			String actualPrefix = arg.substring(0, prefixLength);
			throw new IllegalArgumentException(
					"Invalid ID: expected prefix '"+prefix+"' but got '"+actualPrefix+"'."
			);
		}
		return prefix + UUID.fromString(arg.substring(prefixLength));
	}

	private static String validateConversationId(String id) {
		return validateId("CON-", id);
	}

	private static String validateMemberId(String id) {
		return validateId("MEM-", id);
	}

	private static String validateUserId(String id) {
		return validateId("USR-", id);
	}

	private static String validateSessionId(String id) {
		return validateId("SES-", id);
	}

	private static <T> T validateRequest(T request) {
		return Objects.requireNonNull(request, "Request parameter is required.");
	}

	// ENDPOINTS

	public List<Conversation> listConversations() {
		return listConversations(ListConversationsRequest.builder().pageSize(100).build()).getConversations();
	}

	public ListConversationsResponse listConversations(ListConversationsRequest filter) {
		return listConversations.execute(validateRequest(filter));
	}

	public Conversation createConversation(Conversation request) {
		return createConversation.execute(validateRequest(request));
	}

	public Conversation getConversation(String conversationId) {
		return getConversation.execute(validateConversationId(conversationId));
	}

	public Conversation updateConversation(String conversationId, Conversation request) {
		validateRequest(request).id = validateConversationId(conversationId);
		return updateConversation.execute(request);
	}

	public void deleteConversation(String conversationId) {
		deleteConversation.execute(validateConversationId(conversationId));
	}

	public List<UserConversation> listUserConversations(String userId) {
		return listUserConversations(userId,
				ListUserConversationsRequest.builder().pageSize(100).build()
		).getConversations();
	}

	public ListUserConversationsResponse listUserConversations(String userId, ListUserConversationsRequest filter) {
		validateRequest(filter).userId = validateUserId(userId);
		return listUserConversations.execute(filter);
	}

	public List<UserSession> listUserSessions(String userId) {
		return listUserSessions(userId, ListUserSessionsRequest.builder().pageSize(100).build()).getSessions();
	}

	public ListUserSessionsResponse listUserSessions(String userId, ListUserSessionsRequest filter) {
		validateRequest(filter).userId = validateUserId(userId);
		return listUserSessions.execute(filter);
	}

	public List<BaseMember> listMembers(String conversationId) {
		return listMembers(conversationId, ListMembersRequest.builder().pageSize(100).build()).getMembers();
	}

	public ListMembersResponse listMembers(String conversationId, ListMembersRequest filter) {
		validateRequest(filter).conversationId = validateConversationId(conversationId);
		return listMembers.execute(filter);
	}

	public Member getMember(String conversationId, String memberId) {
		return getMember.execute(new ConversationResourceRequestWrapper(
				validateConversationId(conversationId), validateMemberId(memberId)
		));
	}

	public Member createMember(Member request) {
		return createMember.execute(validateRequest(request));
	}

	public Member updateMember(UpdateMemberRequest request) {
		return updateMember.execute(validateRequest(request));
	}
}
