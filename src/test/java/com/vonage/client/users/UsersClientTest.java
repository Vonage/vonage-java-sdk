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

import com.vonage.client.ClientTest;
import com.vonage.client.DynamicEndpointTestSpec;
import com.vonage.client.RestEndpoint;
import com.vonage.client.auth.AuthMethod;
import com.vonage.client.auth.JWTAuthMethod;
import com.vonage.client.common.HttpMethod;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import java.util.*;
import java.util.function.Consumer;

public class UsersClientTest extends ClientTest<UsersClient> {
    static final String SAMPLE_USER_ID = "USR-" + UUID.randomUUID(),
            SAMPLE_USER = "{\n" +
                    "   \"id\": \""+SAMPLE_USER_ID+"\",\n" +
                    "   \"name\": \"my_user_name\",\n" +
                    "   \"display_name\": \"My User Name\",\n" +
                    "   \"image_url\": \"https://example.com/image.png\",\n" +
                    "   \"properties\": {\n" +
                    "      \"custom_data\": {\n" +
                    "         \"custom_key\": \"custom_value\"\n" +
                    "      }\n" +
                    "   },\n" +
                    "   \"channels\": {\n" +
                    "      \"pstn\": [\n" +
                    "         {\n" +
                    "            \"number\": 123457\n" +
                    "         }\n" +
                    "      ],\n" +
                    "      \"sip\": [\n" +
                    "         {\n" +
                    "            \"uri\": \"sip:4442138907@sip.example.com;transport=tls\",\n" +
                    "            \"username\": \"New SIP\",\n" +
                    "            \"password\": \"P4s5w0rd\"\n" +
                    "         }\n" +
                    "      ],\n" +
                    "      \"vbc\": [\n" +
                    "         {\n" +
                    "            \"extension\": \"403\"\n" +
                    "         }\n" +
                    "      ],\n" +
                    "      \"websocket\": [\n" +
                    "         {\n" +
                    "            \"uri\": \"wss://example.com/socket\",\n" +
                    "            \"content-type\": \"audio/l16;rate=16000\",\n" +
                    "            \"headers\": {\n" +
                    "               \"customer_id\": \"ABC123\"\n" +
                    "            }\n" +
                    "         }\n" +
                    "      ],\n" +
                    "      \"sms\": [\n" +
                    "         {\n" +
                    "            \"number\": \"447700900001\"\n" +
                    "         }\n" +
                    "      ],\n" +
                    "      \"mms\": [\n" +
                    "         {\n" +
                    "            \"number\": \"447700900002\"\n" +
                    "         }\n" +
                    "      ],\n" +
                    "      \"whatsapp\": [\n" +
                    "         {\n" +
                    "            \"number\": \"447700900003\"\n" +
                    "         }\n" +
                    "      ],\n" +
                    "      \"viber\": [\n" +
                    "         {\n" +
                    "            \"number\": \"447700900004\"\n" +
                    "         }\n" +
                    "      ],\n" +
                    "      \"messenger\": [\n" +
                    "         {\n" +
                    "            \"id\": \"12345abcd\"\n" +
                    "         }\n" +
                    "      ]\n" +
                    "   },\n" +
                    "   \"_links\": {\n" +
                    "      \"self\": {\n" +
                    "         \"href\": \"https://api.nexmo.com/v1/users/"+SAMPLE_USER_ID+"\"\n" +
                    "      }\n" +
                    "   }\n" +
                    "}";

    public UsersClientTest() {
        client = new UsersClient(wrapper);
    }

    static void assertEqualsSampleUser(User response) {
        assertNotNull(response);
        assertEquals(SAMPLE_USER_ID, response.getId());
    }

    void assertUserIdValidation(final Consumer<? super String> method) throws Exception {
        assertThrows(NullPointerException.class, () -> method.accept(null));
        assertThrows(IllegalArgumentException.class, () -> method.accept(""));
        assertThrows(IllegalArgumentException.class, () -> method.accept(UUID.randomUUID().toString()));
        assertThrows(IllegalArgumentException.class, () -> method.accept("abc123"));
        assertThrows(IllegalArgumentException.class, () -> method.accept("USR-a1b2c3d4e5"));
        assert400ResponseException(() -> method.accept(SAMPLE_USER_ID));
    }

    void assert400ResponseException(ThrowingRunnable invocation) throws Exception {
        String response = "{\n" +
                "   \"type\": \"https://developer.nexmo.com/api-errors/application#payload-validation\",\n" +
                "   \"title\": \"Bad Request\",\n" +
                "   \"detail\": \"The request failed due to validation errors\",\n" +
                "   \"invalid_parameters\": [\n" +
                "      {\n" +
                "         \"name\": \"capabilities.voice.webhooks.answer_url.http_method\",\n" +
                "         \"reason\": \"must be one of: GET, POST\"\n" +
                "      }\n" +
                "   ],\n" +
                "   \"instance\": \"797a8f199c45014ab7b08bfe9cc1c12c\"\n" +
                "}";
        assertApiResponseException(400, response, UsersResponseException.class, invocation);
    }

    @Test
    public void testCreateUser() throws Exception {
        stubResponse(201, SAMPLE_USER);
        User request = User.builder().name("Test user").build();
        assertEqualsSampleUser(client.createUser(request));
        assertThrows(NullPointerException.class, () -> client.createUser(null));
        assert400ResponseException(() -> client.createUser(request));
    }

    @Test
    public void testUpdateUser() throws Exception {
        stubResponse(200, SAMPLE_USER);
        User request = User.builder().displayName("New friendlyname").build();
        assertEqualsSampleUser(client.updateUser(SAMPLE_USER_ID, request));
        assertUserIdValidation(id -> client.updateUser(id, request));
        assertThrows(NullPointerException.class, () -> client.updateUser(SAMPLE_USER_ID, null));
        assert400ResponseException(() -> client.updateUser(SAMPLE_USER_ID, request));
    }

    @Test
    public void testGetUser() throws Exception {
        stubResponse(200, SAMPLE_USER);
        assertEqualsSampleUser(client.getUser(SAMPLE_USER_ID));
        assertUserIdValidation(client::getUser);
    }

    @Test
    public void testDeleteUser() throws Exception {
        stubResponseAndRun(204, () -> client.deleteUser(SAMPLE_USER_ID));
        assertUserIdValidation(client::deleteUser);
    }

    @Test
    public void testListUsersWithOneResult() throws Exception {

    }

    @Test
    public void testListUsersWithMultipleResults() throws Exception {

    }

    @Test
    public void testListUsersWithNoResults() throws Exception {
        String json = "{\"page\":1,\"_embedded\":{\"users\":[]}}";
        ListUsersResponse hal = stubResponseAndGet(json, () ->
                client.listUsers(ListUsersRequest.builder().build())
        );
        assertEquals(0, hal.getUsers().size());
        assertEquals(1, hal.getPage().intValue());
        assertEquals(0, stubResponseAndGet(json, client::listUsers).size());
        assertNotNull(stubResponseAndGet(json, () -> client.listUsers(null)));
        assert400ResponseException(client::listUsers);
    }

    static abstract class UserEndpointTestSpec<T, R> extends DynamicEndpointTestSpec<T, R> {

        @Override
        protected Collection<Class<? extends AuthMethod>> expectedAuthMethods() {
            return Collections.singletonList(JWTAuthMethod.class);
        }

        @Override
        protected Class<? extends Exception> expectedResponseExceptionType() {
            return UsersResponseException.class;
        }

        @Override
        protected String expectedDefaultBaseUri() {
            return "https://api.nexmo.com";
        }

        @Override
        protected String expectedEndpointUri(T request) {
            String base = "/v1/users", suffix;
            if (request instanceof String) {
                suffix = (String) request;
            }
            else if (request instanceof User && HttpMethod.PATCH.equals(expectedHttpMethod())) {
                suffix = ((User) request).getId();
            }
            else {
                suffix = null;
            }
            return suffix != null ? base + "/" + suffix : base;
        }

        @Override
        protected String sampleRequestBodyString() {
            return null;
        }
    }

    @Test
    public void testListUsersEndpoint() throws Exception {
        new UserEndpointTestSpec<ListUsersRequest, ListUsersResponse>() {

            @Override
            protected RestEndpoint<ListUsersRequest, ListUsersResponse> endpoint() {
                return client.listUsers;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.GET;
            }

            @Override
            protected ListUsersRequest sampleRequest() {
                return ListUsersRequest.builder().pageSize(25).build();
            }

            @Override
            protected Map<String, String> sampleQueryParams() {
                ListUsersRequest request = sampleRequest();
                Map<String, String> params = new LinkedHashMap<>();
                params.put("page_size", String.valueOf(request.getPageSize()));
                return params;
            }
        }
        .runTests();
    }

    @Test
    public void testCreateUserEndpoint() throws Exception {
        new UserEndpointTestSpec<User, User>() {

            @Override
            protected RestEndpoint<User, User> endpoint() {
                return client.createUser;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.POST;
            }

            @Override
            protected User sampleRequest() {
                return User.builder().name("Test_user").build();
            }

            @Override
            protected String sampleRequestBodyString() {
                return "{\"name\":\"Test_user\"}";
            }
        }
        .runTests();
    }

    @Test
    public void testGetUserEndpoint() throws Exception {
        new UserEndpointTestSpec<String, User>() {

            @Override
            protected RestEndpoint<String, User> endpoint() {
                return client.getUser;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.GET;
            }

            @Override
            protected String sampleRequest() {
                return SAMPLE_USER_ID;
            }
        }
        .runTests();
    }

    @Test
    public void testUpdateUserEndpoint() throws Exception {
        new UserEndpointTestSpec<User, User>() {

            @Override
            protected RestEndpoint<User, User> endpoint() {
                return client.updateUser;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.PATCH;
            }

            @Override
            protected User sampleRequest() {
                return User.fromJson(sampleRequestBodyString());
            }

            @Override
            protected String sampleRequestBodyString() {
                return "{\"id\":\""+SAMPLE_USER_ID+"\"}";
            }
        }
        .runTests();
    }

    @Test
    public void testDeleteUserEndpoint() throws Exception {
        new UserEndpointTestSpec<String, Void>() {

            @Override
            protected RestEndpoint<String, Void> endpoint() {
                return client.deleteUser;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.DELETE;
            }

            @Override
            protected String sampleRequest() {
                return SAMPLE_USER_ID;
            }
        }
        .runTests();
    }
}
