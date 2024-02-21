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
	final RestEndpoint<ListUserConversationsRequest, ListConversationsResponse> listUserConversations;
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
		final String v1c = "/v1/conversations/";

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
		listUserConversations = new Endpoint<>(req -> "/v1/users/"+req.getUserId()+"/conversations", HttpMethod.GET);
		listUserSessions = new Endpoint<>(req -> "/v1/users/"+req.getUserId()+"/sessions", HttpMethod.GET);
		listMembers = new Endpoint<>(req -> v1c+req.getConversationId()+"/members", HttpMethod.GET);
		getMember = new Endpoint<>(req -> v1c+req.conversationId+"/members/"+req.resourceId, HttpMethod.GET);
		createMember = new Endpoint<>(req -> v1c+req.conversationId+"/members", HttpMethod.POST);
		updateMember = new Endpoint<>(req -> v1c+req.conversationId+"/members/"+req.resourceId, HttpMethod.PATCH);
	}

	public ListConversationsResponse listConversations(ListConversationsRequest request) {
		return listConversations.execute(request);
	}

	public Conversation createConversation(Conversation request) {
		return createConversation.execute(request);
	}

	public Conversation getConversation(String request) {
		return getConversation.execute(request);
	}

	public Conversation updateConversation(Conversation request) {
		return updateConversation.execute(request);
	}

	public void deleteConversation(String request) {
		deleteConversation.execute(request);
	}

	public ListConversationsResponse listUserConversations(ListUserConversationsRequest request) {
		return listUserConversations.execute(request);
	}

	public ListUserSessionsResponse listUserSessions(ListUserSessionsRequest request) {
		return listUserSessions.execute(request);
	}

	public ListMembersResponse listMembers(ListMembersRequest request) {
		return listMembers.execute(request);
	}

	public Member getMember(String conversationId, String memberId) {
		return getMember.execute(new ConversationResourceRequestWrapper(conversationId, memberId));
	}

	public Member createMember(Member request) {
		return createMember.execute(request);
	}

	public Member updateMember(UpdateMemberRequest request) {
		return updateMember.execute(request);
	}
}
