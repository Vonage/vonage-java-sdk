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
                super(DynamicEndpoint.<T, R> builder((Class<R>) type.getClass().getComponentType())
                        .responseExceptionType(UsersResponseException.class)
                        .wrapper(wrapper).requestMethod(method).authMethod(JWTAuthMethod.class)
                        .pathGetter((de, req) -> {
                            String base = de.getHttpWrapper().getHttpConfig().getVersionedApiBaseUri("v1");
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

    private User validateUser(User request) {
        return Objects.requireNonNull(request, "User request is required.");
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
     * @param user The application properties for the user to be updated with.
     *
     * @return The user which has been updated.
     *
     * @throws UsersResponseException If there was an error processing the request.
     */
    public User updateUser(User user) throws UsersResponseException {
        return updateUser.execute(validateUser(user));
    }

    /**
     * Retrieve a user.
     *
     * @param userId The unique ID of the user to retrieve.
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
     * @param userId Unique ID of the user to delete as a string.
     *
     * @throws UsersResponseException If there was an error processing the request.
     */
    public void deleteUser(String userId) throws UsersResponseException {
        deleteUser.execute(validateUserId(userId));
    }

    /**
     * Lists the first 100 users in the application.
     *
     * @return The list of available users.
     *
     * @throws UsersResponseException If there was an error processing the request.
     */
    public List<User> listUsers() throws UsersResponseException {
        return listUsers(ListUsersRequest.builder().build()).getUsers();
    }

    /**
     * Lists the first 100 users in the application.
     *
     * @return The HAL response containing the users.
     *
     * @throws UsersResponseException If there was an error processing the request.
     */
    public ListUsersResponse listUsers(ListUsersRequest request) throws UsersResponseException {
        return listUsers.execute(ListUsersRequest.builder().build());
    }
}
