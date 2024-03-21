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
 * A client for communicating with the Vonage Conversations API. The standard way to obtain an instance
 * of this class is to use {@link VonageClient#getConversationsClient()}.
 */
public class ConversationsClient {
	final RestEndpoint<ListConversationsRequest, ListConversationsResponse> listConversations;
	final RestEndpoint<Conversation, Conversation> createConversation;
	final RestEndpoint<String, Conversation> getConversation;
	final RestEndpoint<Conversation, Conversation> updateConversation;
	final RestEndpoint<String, Void> deleteConversation;
	final RestEndpoint<ListUserConversationsRequest, ListUserConversationsResponse> listUserConversations;
	final RestEndpoint<ListMembersRequest, ListMembersResponse> listMembers;
	final RestEndpoint<ConversationResourceRequestWrapper, Member> getMember;
	final RestEndpoint<Member, Member> createMember;
	final RestEndpoint<UpdateMemberRequest, Member> updateMember;
	final RestEndpoint<ConversationResourceRequestWrapper, Void> deleteEvent;
	final RestEndpoint<ConversationResourceRequestWrapper, Event> getEvent;
	final RestEndpoint<ListEventsRequest, ListEventsResponse> listEvents;
	final RestEndpoint<Event, Event> createEvent;

	/**
	 * Constructor.
	 *
	 * @param wrapper (REQUIRED) shared HTTP wrapper object used for making REST calls.
	 */
	@SuppressWarnings("unchecked")
	public ConversationsClient(HttpWrapper wrapper) {
		final String v1c = "/v1/conversations/", v1u = "/v1/users/", mems = "/members/", events = "/events/";

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
		listMembers = new Endpoint<>(req -> v1c+req.conversationId+mems, HttpMethod.GET);
		getMember = new Endpoint<>(req -> v1c+req.conversationId+mems+req.resourceId, HttpMethod.GET);
		createMember = new Endpoint<>(req -> v1c+req.getConversationId()+mems, HttpMethod.POST);
		updateMember = new Endpoint<>(req -> v1c+req.conversationId+mems+req.resourceId, HttpMethod.PATCH);
		deleteEvent = new Endpoint<>(req -> v1c+req.conversationId+events+req.resourceId, HttpMethod.DELETE);
		getEvent = new Endpoint<>(req -> v1c+req.conversationId+events+req.resourceId, HttpMethod.GET);
		listEvents = new Endpoint<>(req -> v1c+req.conversationId+events, HttpMethod.GET);
		createEvent = new Endpoint<>(req -> v1c+req.conversationId+events, HttpMethod.POST);
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

	static String validateMemberId(String id) {
		return validateId("MEM-", id);
	}

	private static String validateUserId(String id) {
		return validateId("USR-", id);
	}

	private static <T> T validateRequest(T request) {
		return Objects.requireNonNull(request, "Request parameter is required.");
	}

	private static <F extends AbstractConversationsFilterRequest, B extends
			AbstractConversationsFilterRequest.Builder<? extends F, ?>> F defaultFilterParams(B builder) {
		return builder.pageSize(100).build();
	}

	// ENDPOINTS

	/**
	 * Retrieve the first 100 Conversations in the application. Note that the returned conversations are
	 * incomplete, hence of type {@linkplain BaseConversation}. To get the full data, use the
	 * {@link #getConversation(String)} method, passing in the ID from {@linkplain BaseConversation#getId()}.
	 *
	 * @return A list of the first 100 conversations returned from the API, in default (ascending) order.
	 *
	 * @throws ConversationsResponseException If the API call fails due to a bad request (400).
	 * @see #listConversations(ListConversationsRequest)
	 */
	public List<BaseConversation> listConversations() {
		return listConversations(defaultFilterParams(ListConversationsRequest.builder())).getConversations();
	}

	/**
	 * Retrieve conversations in the application which match the specified filter criteria. Note that the
	 * returned conversations in {@linkplain ListConversationsResponse#getConversations()} are incomplete,
	 * hence type of {@linkplain BaseConversation}. To get the full data, use {@link #getConversation(String)}
	 * method, passing in the ID from {@linkplain BaseConversation#getId()}.
	 *
	 * @param filter Filter options to narrow down the search results.
	 *
	 * @return The search results along with HAL metadata.
	 *
	 * @throws ConversationsResponseException If the API call fails due to a bad request (400).
	 */
	public ListConversationsResponse listConversations(ListConversationsRequest filter) {
		return listConversations.execute(validateRequest(filter));
	}

	/**
	 * Creates a new Conversation within the application.
	 *
	 * @param request The Conversation parameters. Use {@code Conversation.builder().build()} for default settings.
	 *
	 * @return The created Conversation response with additional fields populated.
	 *
	 * @throws ConversationsResponseException If the Conversation name already exists (409), or any other API error.
	 */
	public Conversation createConversation(Conversation request) {
		return createConversation.execute(validateRequest(request));
	}

	/**
	 * Retrieve a conversation by its ID.
	 *
	 * @param conversationId Unique identifier of the conversation to look up.
	 *
	 * @return Details of the conversation corresponding to the specified ID.
	 *
	 * @throws ConversationsResponseException If the conversation was not found (404), or any other API error.
	 */
	public Conversation getConversation(String conversationId) {
		return getConversation.execute(validateConversationId(conversationId));
	}

	/**
	 * Update an existing conversation's settings / parameters.
	 *
	 * @param conversationId Unique conversation identifier.
	 * @param request Conversation object with the updated parameters. Any fields not set will be unchanged.
	 *
	 * @return The full updated conversation details.
	 *
	 * @throws ConversationsResponseException If the conversation was not found (404)
	 * or the parameters are invalid (400), e.g. the updated name already exists (409).
	 */
	public Conversation updateConversation(String conversationId, Conversation request) {
		validateRequest(request).id = validateConversationId(conversationId);
		return updateConversation.execute(request);
	}

	/**
	 * Delete an existing conversation by ID.
	 *
	 * @param conversationId Unique conversation identifier.
	 *
	 * @throws ConversationsResponseException If the conversation was not found (404), or any other API error.
	 */
	public void deleteConversation(String conversationId) {
		deleteConversation.execute(validateConversationId(conversationId));
	}

	/**
	 * List the first 100 conversations for a given user.
	 *
	 * @param userId Unique identifier for the user.
	 *
	 * @return The list of conversations the specified user is in, with default (ascending) order.
	 *
	 * @throws ConversationsResponseException If the user was not found (404), or any other API error.
	 *
	 * @see #listUserConversations(String, ListUserConversationsRequest)
	 * @see com.vonage.client.users
	 */
	public List<UserConversation> listUserConversations(String userId) {
		return listUserConversations(userId,
				defaultFilterParams(ListUserConversationsRequest.builder())
		).getConversations();
	}

	/**
	 * List the first 100 conversations for a given user.
	 *
	 * @param userId Unique identifier for the user.
	 * @param filter Filter options to narrow down the search results.
	 *
	 * @return The wrapped list of user conversations, along with HAL metadata.
	 *
	 * @throws ConversationsResponseException If the user was not found (404),
	 * the filter options were invalid (400) or any other API error.
	 *
	 * @see com.vonage.client.users
	 */
	public ListUserConversationsResponse listUserConversations(String userId, ListUserConversationsRequest filter) {
		validateRequest(filter).userId = validateUserId(userId);
		return listUserConversations.execute(filter);
	}

	/**
	 * List the first 100 Members for a given Conversation. Note that the returned members are
	 * incomplete, hence of type {@linkplain BaseMember}. To get the full data, use the
	 * {@link #getMember(String, String)} method, passing in the ID from {@linkplain BaseMember#getId()}.
	 *
	 * @param conversationId Unique conversation identifier.
	 *
	 * @return The list of members in default (ascending) order.
	 *
	 * @throws ConversationsResponseException If the conversation was not found (404), or any other API error.
	 *
	 * @see #listMembers(String, ListMembersRequest)
	 */
	public List<BaseMember> listMembers(String conversationId) {
		return listMembers(conversationId, ListMembersRequest.builder().pageSize(100).build()).getMembers();
	}

	/**
	 * Retrieve Members associated with a particular Conversation which match the specified filter criteria. Note
	 * that the returned members are incomplete, hence of type {@linkplain BaseMember}. To get the full data, use
	 * the {@link #getMember(String, String)} method, passing in the ID from {@linkplain BaseMember#getId()}.
	 *
	 * @param conversationId Unique conversation identifier.
	 * @param filter Filter options to narrow down the search results.
	 *
	 * @return The wrapped list of Members, along with HAL metadata.
	 *
	 * @throws ConversationsResponseException If the conversation was not found (404),
	 * the filter options were invalid (400) or any other API error.
	 */
	public ListMembersResponse listMembers(String conversationId, ListMembersRequest filter) {
		validateRequest(filter).conversationId = validateConversationId(conversationId);
		return listMembers.execute(filter);
	}

	/**
	 * Retrieve a conversation Member by its ID.
	 *
	 * @param conversationId Unique conversation identifier.
	 * @param memberId Unique identifier for the member.
	 *
	 * @return Details of the member corresponding to the specified ID.
	 *
	 * @throws ConversationsResponseException If the conversation or member was not found (404), or any other API error.
	 */
	public Member getMember(String conversationId, String memberId) {
		return getMember.execute(new ConversationResourceRequestWrapper(
				validateConversationId(conversationId), validateMemberId(memberId)
		));
	}

	/**
	 * Creates a new Member for the specified conversation.
	 *
	 * @param conversationId Unique conversation identifier.
	 * @param request The Members parameters. Use {@link Member#builder()}, remember to set the mandatory parameters.
	 *
	 * @return The created Member response with additional fields populated.
	 *
	 * @throws ConversationsResponseException If the conversation was not found (404),
	 * the request parameters were invalid (400) or any other API error.
	 */
	public Member createMember(String conversationId, Member request) {
		validateRequest(request).setConversationId(validateConversationId(conversationId));
		return createMember.execute(request);
	}

	/**
	 * Update an existing member's state.
	 *
	 * @param request Details of the member to update. Use {@link UpdateMemberRequest#builder()},
	 *                remember to set the mandatory parameters, including the conversation and member IDs.
	 *
	 * @return The updated Member object response.
	 *
	 * @throws ConversationsResponseException If the conversation or member were not found (404),
	 * the request parameters were invalid (400) or any other API error.
	 */
	public Member updateMember(UpdateMemberRequest request) {
		validateConversationId(validateRequest(request).conversationId);
		validateMemberId(request.resourceId);
		return updateMember.execute(request);
	}

	/**
	 * List the first 100 events for a given Conversation.
	 *
	 * @param conversationId Unique conversation identifier.
	 *
	 * @return The list of events in default (ascending) order.
	 *
	 * @throws ConversationsResponseException If the conversation was not found (404), or any other API error.
	 */
	public List<Event> listEvents(String conversationId) {
		return listEvents(conversationId, ListEventsRequest.builder().pageSize(100).build()).getEvents();
	}

	/**
	 * Retrieve Events associated with a particular Conversation which match the specified filter criteria.
	 *
	 * @param conversationId Unique conversation identifier.
	 * @param request Filter options to narrow down the search results.
	 *
	 * @return The wrapped list of Events, along with HAL metadata.
	 *
	 * @throws ConversationsResponseException If the conversation was not found (404), or any other API error.
	 */
	public ListEventsResponse listEvents(String conversationId, ListEventsRequest request) {
		validateRequest(request).conversationId = validateConversationId(conversationId);
		return listEvents.execute(request);
	}

	/**
	 * Retrieve a conversation Event by its ID.
	 *
	 * @param conversationId Unique conversation identifier.
	 * @param eventId Sequence ID of the event to retrieve as an integer.
	 *
	 * @return Details of the event corresponding to the specified ID.
	 *
	 * @throws ConversationsResponseException If the conversation or event was not found (404), or any other API error.
	 */
	public Event getEvent(String conversationId, int eventId) {
		return getEvent.execute(new ConversationResourceRequestWrapper(
				validateConversationId(conversationId), String.valueOf(eventId)
		));
	}

	/**
	 * Creates a new Event for the specified conversation.
	 *
	 * @param conversationId Unique conversation identifier.
	 * @param request Details of the event to create.
	 *
	 * @return The created Event response with additional fields populated.
	 *
	 * @throws ConversationsResponseException If the conversation was not found (404), or any other API error.
	 */
	@SuppressWarnings("unchecked")
	public <E extends Event> E createEvent(String conversationId, E request) {
		validateRequest(request).conversationId = validateConversationId(conversationId);
		return (E) createEvent.execute(request);
	}

	/**
	 * Deletes an event. Only message and custom events can be deleted.
	 *
	 * @param conversationId Unique conversation identifier.
	 * @param eventId Sequence ID of the event to retrieve as an integer.
	 *
	 * @throws ConversationsResponseException If the conversation or event was not found (404),
	 * the event could not be deleted, or any other API error.
	 */
	public void deleteEvent(String conversationId, int eventId) {
		deleteEvent.execute(new ConversationResourceRequestWrapper(
				validateConversationId(conversationId), String.valueOf(eventId)
		));
	}
}
