/*
 *   Copyright 2025 Vonage
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
package com.vonage.client.users;

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
import java.util.stream.Collectors;

/**
 * A client for talking to the Vonage Users API. The standard way to obtain an instance of
 * this class is to use {@link VonageClient#getUsersClient()}.
 */
public class UsersClient {
    final RestEndpoint<ListUsersRequest, ListUsersResponse> listUsers;
    final RestEndpoint<User, User> createUser;
    final RestEndpoint<String, User> getUser;
    final RestEndpoint<User, User> updateUser;
    final RestEndpoint<String, Void> deleteUser;

    public UsersClient(HttpWrapper wrapper) {
        @SuppressWarnings("unchecked")
        final class Endpoint<T, R> extends DynamicEndpoint<T, R> {
            Endpoint(Function<T, String> pathGetter, HttpMethod method, R... type) {
                super(DynamicEndpoint.<T, R> builder(type)
                        .responseExceptionType(UsersResponseException.class)
                        .wrapper(wrapper).requestMethod(method).authMethod(JWTAuthMethod.class)
                        .pathGetter((de, req) -> {
                            String base = de.getHttpWrapper().getHttpConfig().getApiBaseUri() + "/v1";
                            String path = base + "/users";
                            if (pathGetter != null) {
                                path += "/" + pathGetter.apply(req);
                            }
                            return path;
                        })
                );
            }
        }

        listUsers = new Endpoint<>(null, HttpMethod.GET);
        createUser = new Endpoint<>(null, HttpMethod.POST);
        getUser = new Endpoint<>(Function.identity(), HttpMethod.GET);
        updateUser = new Endpoint<>(User::getId, HttpMethod.PATCH);
        deleteUser = new Endpoint<>(Function.identity(), HttpMethod.DELETE);
    }

    private <U extends BaseUser> U validateUser(U request) {
        return Objects.requireNonNull(request, "User is required.");
    }

    private String validateUserId(String id) {
        Objects.requireNonNull(id, "User ID is required.");
        String prefix = "USR-";
        if (!id.startsWith(prefix)) {
            throw new IllegalArgumentException("Invalid user ID.");
        }
        return prefix + UUID.fromString(id.substring(prefix.length()));
    }

    /**
     * Create a new user.
     *
     * @param user The properties for the user to be created with.
     *
     * @return The user which has been created.
     *
     * @throws UsersResponseException If there was an error processing the request.
     */
    public User createUser(User user) throws UsersResponseException {
        return createUser.execute(validateUser(user));
    }

    /**
     * Update an existing user.
     *
     * @param userId Unique ID of the user to update.
     * @param user The properties for the user to be updated with.
     *
     * @return The user which has been updated.
     *
     * @throws UsersResponseException If there was an error processing the request.
     */
    public User updateUser(String userId, User user) throws UsersResponseException {
        validateUser(user).id = validateUserId(userId);
        return updateUser.execute(user);
    }

    /**
     * Retrieve a user.
     *
     * @param userId Unique ID of the user to retrieve.
     *
     * @return The corresponding user.
     *
     * @throws UsersResponseException If there was an error processing the request.
     */
    public User getUser(String userId) throws UsersResponseException {
        return getUser.execute(validateUserId(userId));
    }

    /**
     * Delete a user.
     *
     * @param userId Unique ID of the user to delete.
     *
     * @throws UsersResponseException If there was an error processing the request.
     */
    public void deleteUser(String userId) throws UsersResponseException {
        deleteUser.execute(validateUserId(userId));
    }

    /**
     * Lists users in the application based on the filter criteria.
     *
     * @param request Optional parameters to customise the search results.
     *
     * @return The HAL response containing the users.
     *
     * @throws UsersResponseException If there was an error processing the request.
     */
    public ListUsersResponse listUsers(ListUsersRequest request) throws UsersResponseException {
        return listUsers.execute(request != null ? request : ListUsersRequest.builder().build());
    }

    /**
     * Lists the first 100 users in the application, from newest to oldest. <br>
     * <b>NOTE: The users returned from this method will only contain the name and ID, not the full record.</b>
     *
     * @return The list of available users in creation order.
     *
     * @throws UsersResponseException If there was an error processing the request.
     *
     * @see #listUsers(ListUsersRequest)
     */
    public List<BaseUser> listUsers() throws UsersResponseException {
        return listUsers(ListUsersRequest.builder().pageSize(100).build()).getUsers();
    }

    /**
     * Convenience method that uses the metadata from the base user object to obtain all known fields about the user.
     *
     * @param minimalUser The user metadata.
     *
     * @return A new {@linkplain User} object with all known fields populated.
     *
     * @throws UsersResponseException If there was an error processing the request.
     */
    public User getUserDetails(BaseUser minimalUser) {
        return getUser(validateUser(minimalUser).getId());
    }

    /**
     * Convenience method that uses the metadata from the base users list to obtain all known fields about the users.
     * You may use this method to convert the minimal data returned from {@link #listUsers()} or
     * {@link ListUsersResponse#getUsers()} into fully populated records.
     *
     * @param minimalUsers The list of users' metadata.
     *
     * @return A list of users with all known fields populated in the same encounter order.
     *
     * @throws UsersResponseException If there was an error processing the request.
     */
    public List<User> getUserDetails(List<BaseUser> minimalUsers) {
        return Objects.requireNonNull(minimalUsers, "Users list is required")
                .stream().map(this::getUserDetails).collect(Collectors.toList());
    }
}
