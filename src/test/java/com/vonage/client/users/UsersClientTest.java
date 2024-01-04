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
package com.vonage.client.users;

import com.vonage.client.ClientTest;
import com.vonage.client.RestEndpoint;
import com.vonage.client.common.HalLinks;
import com.vonage.client.common.HttpMethod;
import com.vonage.client.users.channels.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
                    "         \"custom_key\": \"custom_value\",\n" +
                    "         \"K2\": \"value 2\"\n" +
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
                    "            \"extension\": \"991\"\n" +
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
                    "         },\n" +
                    "         {\n" +
                    "            \"number\": \"447700900002\"\n" +
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
                    "         },\n" +
                    "         {\n" +
                    "            \"number\": \"447700900001\"\n" +
                    "         },\n" +
                    "         {\n" +
                    "            \"number\": \"447700900002\"\n" +
                    "         }\n" +
                    "      ],\n" +
                    "      \"viber\": [\n" +
                    "         {\n" +
                    "            \"number\": \"12017000000\"\n" +
                    "         }\n" +
                    "      ],\n" +
                    "      \"messenger\": [\n" +
                    "         {\n" +
                    "            \"id\": \"1234abcd5\"\n" +
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
        assertEquals("my_user_name", response.getName());
        assertEquals("My User Name", response.getDisplayName());
        assertEquals(URI.create("https://example.com/image.png"), response.getImageUrl());
        Map<String, ?> customData = response.getCustomData();
        assertNotNull(customData);
        assertEquals(2, customData.size());
        assertEquals("custom_value", customData.get("custom_key"));
        assertEquals("value 2", customData.get("K2"));
        Channels channels = response.getChannels();
        assertNotNull(channels);

        List<Pstn> pstn = channels.getPstn();
        assertNotNull(pstn);
        assertEquals(1, pstn.size());
        Pstn pstn0 = pstn.get(0);
        assertNotNull(pstn0);
        assertEquals("123457", pstn0.getNumber());

        List<Sip> sip = channels.getSip();
        assertNotNull(sip);
        assertEquals(1, sip.size());
        Sip sip0 = sip.get(0);
        assertNotNull(sip0);
        assertEquals(URI.create("sip:4442138907@sip.example.com;transport=tls"), sip0.getUri());
        assertEquals("New SIP", sip0.getUsername());
        assertEquals("P4s5w0rd", sip0.getPassword());

        List<Vbc> vbc = channels.getVbc();
        assertNotNull(vbc);
        assertEquals(1, vbc.size());
        Vbc vbc0 = vbc.get(0);
        assertNotNull(vbc0);
        assertEquals("991", vbc0.getExtension());

        List<Websocket> websocket = channels.getWebsocket();
        assertNotNull(websocket);
        assertEquals(1, websocket.size());
        Websocket websocket0 = websocket.get(0);
        assertNotNull(websocket0);
        assertEquals(URI.create("wss://example.com/socket"), websocket0.getUri());
        assertEquals(Websocket.ContentType.AUDIO_L16_16K, websocket0.getContentType());
        Map<String, ?> headers = websocket0.getHeaders();
        assertNotNull(headers);
        assertEquals(1, headers.size());
        assertEquals("ABC123", headers.get("customer_id"));

        List<Sms> sms = channels.getSms();
        assertNotNull(sms);
        assertEquals(2, sms.size());
        Sms sms0 = sms.get(0);
        assertNotNull(sms0);
        assertEquals("447700900001", sms0.getNumber());
        Sms sms1 = sms.get(1);
        assertNotNull(sms1);
        assertEquals("447700900002", sms1.getNumber());

        List<Mms> mms = channels.getMms();
        assertNotNull(mms);
        assertEquals(1, mms.size());
        Mms mms0 = mms.get(0);
        assertNotNull(mms0);
        assertEquals("447700900002", mms0.getNumber());

        List<Whatsapp> whatsapp = channels.getWhatsapp();
        assertNotNull(whatsapp);
        assertEquals(3, whatsapp.size());
        Whatsapp whatsapp0 = whatsapp.get(0);
        assertNotNull(whatsapp0);
        assertEquals("447700900003", whatsapp0.getNumber());
        Whatsapp whatsapp1 = whatsapp.get(1);
        assertNotNull(whatsapp1);
        assertEquals("447700900001", whatsapp1.getNumber());
        Whatsapp whatsapp2 = whatsapp.get(2);
        assertNotNull(whatsapp2);
        assertEquals("447700900002", whatsapp2.getNumber());

        List<Viber> viber = channels.getViber();
        assertNotNull(viber);
        assertEquals(1, viber.size());
        Viber viber0 = viber.get(0);
        assertNotNull(viber0);
        assertEquals("12017000000", viber0.getNumber());

        List<Messenger> messengers = channels.getMessenger();
        assertNotNull(messengers);
        assertEquals(1, messengers.size());
        Messenger messenger0 = messengers.get(0);
        assertNotNull(messenger0);
        assertEquals("1234abcd5", messenger0.getId());
    }

    void assertUserIdValidation(final Consumer<? super String> method) throws Exception {
        assertThrows(NullPointerException.class, () -> method.accept(null));
        assertThrows(IllegalArgumentException.class, () -> method.accept(""));
        assertThrows(IllegalArgumentException.class, () -> method.accept(UUID.randomUUID().toString()));
        assertThrows(IllegalArgumentException.class, () -> method.accept("abc123"));
        assertThrows(IllegalArgumentException.class, () -> method.accept("USR-a1b2c3d4e5"));
        assert429ResponseException(() -> method.accept(SAMPLE_USER_ID));
    }

    void assert429ResponseException(Executable invocation) throws Exception {
        String title = "Too Many Requests.",
                type = "https://developer.nexmo.com/api/conversation#http:error:too-many-request",
                code = "http:error:too-many-request",
                detail = "You have exceeded your request limit. You can try again shortly.",
                instance = "00a5916655d650e920ccf0daf40ef4ee";

        String response = "{\n" +
                "   \"title\": \""+title+"\",\n" +
                "   \"type\": \""+type+"\",\n" +
                "   \"code\": \""+code+"\",\n" +
                "   \"detail\": \""+detail+"\",\n" +
                "   \"instance\": \""+instance+"\"\n" +
                "}";
        UsersResponseException ex = assertApiResponseException(429, response, UsersResponseException.class, invocation);
        assertEquals(title, ex.getTitle());
        assertEquals(URI.create(type), ex.getType());
        assertEquals(code, ex.getCode());
        assertEquals(detail, ex.getDetail());
        assertEquals(instance, ex.getInstance());
    }

    @Test
    public void testCreateUser() throws Exception {
        stubResponse(201, SAMPLE_USER);
        User request = User.builder().name("Test user").build();
        assertEqualsSampleUser(client.createUser(request));
        assertThrows(NullPointerException.class, () -> client.createUser(null));
        assert429ResponseException(() -> client.createUser(request));
    }

    @Test
    public void testUpdateUser() throws Exception {
        stubResponse(200, SAMPLE_USER);
        User request = User.builder().displayName("New friendlyname").build();
        assertEqualsSampleUser(client.updateUser(SAMPLE_USER_ID, request));
        assertUserIdValidation(id -> client.updateUser(id, request));
        assertThrows(NullPointerException.class, () -> client.updateUser(SAMPLE_USER_ID, null));
        assert429ResponseException(() -> client.updateUser(SAMPLE_USER_ID, request));
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
        String id = "USR-a98271b1-c5ea-4193-88a0-00af324b4a7a";
        String name = "NAM-234eeb31-a031-4e5e-8fa1-d5488eef4293";
        String cursor = "0LBW2B0hv78cfsYpdYVtRpwrm00oh6S7DFibiYBMFUknvLduVQJzLlk%3D";
        String self = "https://api-us-3.vonage.com/v1/users?page_size=10&cursor="+cursor;
        stubResponse(200, "{\n" +
                "    \"page_size\": 10,\n" +
                "    \"_embedded\": {\n" +
                "        \"users\": [\n" +
                "            {\n" +
                "                \"id\": \""+id+"\",\n" +
                "                \"name\": \""+name+"\",\n" +
                "                \"_links\": {\n" +
                "                    \"self\": {\n" +
                "                        \"href\": \"https://api-us-3.vonage.com/v1/users/"+id+"\"\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"_links\": {\n" +
                "        \"first\": {\n" +
                "            \"href\": \"https://api-us-3.vonage.com/v1/users?page_size=10\"\n" +
                "        },\n" +
                "        \"self\": {\n" +
                "            \"href\": \""+self+"\"\n" +
                "        },\n" +
                "        \"next\": {\n" +
                "            \"href\": \"https://api-us-3.vonage.com/v1/users?page_size=10&cursor=HSgXW9q9rSCNJNpLGbzbmxTugq3ur7nqg4edAXdXR%2FZ07gGM1yXlrDIRaDz1YG5YY8QJ0tut4Sp8P8CrtZRy8uDnD68Brb9KTvMdcnPspkhd2h5TFQuNIFzEcZw%3D\"\n" +
                "        }\n" +
                "    }\n" +
                "}");
        ListUsersRequest request = ListUsersRequest.builder()
                .name(name).pageSize(10).cursor(URI.create(self))
                .order(ListUsersRequest.SortOrder.DESC).build();

        assertEquals(name, request.getName());
        assertEquals(10, request.getPageSize());
        assertEquals(cursor, request.getCursor());
        assertEquals("desc", request.getOrder().toString());

        ListUsersResponse parsed = client.listUsers(request);
        assertNotNull(parsed);
        List<BaseUser> users = parsed.getUsers();
        assertNotNull(users);
        assertEquals(1, users.size());
        BaseUser user = users.get(0);
        assertNotNull(user);
        assertEquals(id, user.getId());
        assertEquals(name, user.getName());
        HalLinks links = parsed.getLinks();
        assertNotNull(links);
        assertEquals("page_size=10", links.getFirstUrl().getQuery());
        String startsWith = links.getFirstUrl() + "&cursor=";
        assertTrue(links.getSelfUrl().toString().startsWith(startsWith));
        assertTrue(links.getNextUrl().toString().startsWith(startsWith));
    }

    @Test
    public void testListUsersWithMultipleResults() throws Exception {
        String id = "USR-"+UUID.randomUUID();
        String name = "Unique_t3s7_u5eR";
        stubResponse(200, "{\n" +
                "    \"_embedded\": {\n" +
                "        \"users\": [\n" +
                "            {},{},{},\n" +
                "            {\n" +
                "                \"id\": \""+id+"\",\n" +
                "                \"name\": \""+name+"\",\n" +
                "                \"_links\": {\n" +
                "                    \"self\": {\n" +
                "                        \"href\": \"https://api-us-3.vonage.com/v1/users/"+id+"\"\n" +
                "                    }\n" +
                "                }\n" +
                "            },{}\n" +
                "        ]\n" +
                "    },\n" +
                "    \"_links\": {\n" +
                "    }\n" +
                "}");
        ListUsersResponse parsed = client.listUsers(ListUsersRequest.builder().build());
        assertNotNull(parsed);
        assertNull(parsed.getPageSize());

        HalLinks links = parsed.getLinks();
        assertNotNull(links);
        assertNull(links.getSelfUrl());
        assertNull(links.getNextUrl());
        assertNull(links.getPrevUrl());
        assertNull(links.getFirstUrl());
        assertNull(links.getLastUrl());

        List<BaseUser> users = parsed.getUsers();
        assertNotNull(users);
        assertEquals(5, users.size());
        for (int i = 0; i < 3 || i == 4; i++) {
            BaseUser user = users.get(i);
            assertNotNull(user);
            assertNull(user.getId());
            assertNull(user.getName());
            if (i == 2) i = 4;
        }
        BaseUser user3 = users.get(3);
        assertNotNull(user3);
        assertEquals(id, user3.getId());
        assertEquals(name, user3.getName());
    }

    @Test
    public void testListUsersWithNoResults() throws Exception {
        String json = "{\"page_size\":3,\"_embedded\":{\"users\":[]}}";
        ListUsersResponse hal = stubResponseAndGet(json, () ->
                client.listUsers(ListUsersRequest.builder().build())
        );
        assertNotNull(hal.getUsers());
        assertEquals(0, hal.getUsers().size());
        assertEquals(3, hal.getPageSize().intValue());
        assertEquals(0, stubResponseAndGet(json, client::listUsers).size());
        assertNotNull(stubResponseAndGet(json, () -> client.listUsers(null)));
        assert429ResponseException(client::listUsers);
    }

    @Test
    public void testListUsersRequestPageSizeBoundaries() {
        ListUsersRequest.Builder builder = ListUsersRequest.builder();
        assertThrows(IllegalArgumentException.class, () -> builder.pageSize(101).build());
        assertThrows(IllegalArgumentException.class, () -> builder.pageSize(0).build());
        assertEquals(100, builder.pageSize(100).build().getPageSize());
        assertEquals(1, builder.pageSize(1).build().getPageSize());
    }

    @Test
    public void testGetUserDetails() throws Exception {
        stubResponse(200, "{\n" +
                "    \"page_size\": 10,\n" +
                "    \"_embedded\": {\n" +
                "        \"users\": [\n" +
                "            {\n" +
                "                \"id\": \"USR-ff93b026-7371-4892-b8da-17bfc87e43a2\",\n" +
                "                \"name\": \"NAM-329bf330-f5e7-4a11-b82f-8303fe88fac4\",\n" +
                "                \"_links\": {\n" +
                "                    \"self\": {\n" +
                "                        \"href\": \"https://api-us-3.vonage.com/v1/users/USR-ff93b026-7371-4892-b8da-17bfc87e43a2\"\n" +
                "                    }\n" +
                "                }\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": \"USR-fff5b42c-908a-412d-9d87-c2e9a7f812e7\",\n" +
                "                \"name\": \"NAM-40f31e28-534c-4c8e-a654-c1e34ce4122c\",\n" +
                "                \"_links\": {\n" +
                "                    \"self\": {\n" +
                "                        \"href\": \"https://api-us-3.vonage.com/v1/users/USR-fff5b42c-908a-412d-9d87-c2e9a7f812e7\"\n" +
                "                    }\n" +
                "                }\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": \"USR-641919c2-e17f-495f-b2cf-1790dadfce38\",\n" +
                "                \"name\": \"NAM-9ea69e6d-770e-4c2f-91e8-9b36751abb44\",\n" +
                "                \"_links\": {\n" +
                "                    \"self\": {\n" +
                "                        \"href\": \"https://api-us-3.vonage.com/v1/users/USR-641919c2-e17f-495f-b2cf-1790dadfce38\"\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"_links\": {\n" +
                "        \"first\": {\n" +
                "            \"href\": \"https://api-us-3.vonage.com/v1/users?page_size=10\"\n" +
                "        },\n" +
                "        \"self\": {\n" +
                "            \"href\": \"https://api-us-3.vonage.com/v1/users?page_size=10&cursor=uCcjZijcWuVGhMQGXSzDeSlHvNyZ9MVGm%2BQ3h9l%2B%2Bt%2B2ddz1oGnBwWg%3D\"\n" +
                "        },\n" +
                "        \"next\": {\n" +
                "            \"href\": \"https://api-us-3.vonage.com/v1/users?page_size=10&cursor=%2FHMGlXiJOiG%2BEOkdGWEWEkP%2BBcKIHzphsbn3C44FU8SiJsfRpAF3TzglF%2BdZUnydcnWOMFZv2ibcQr5aYcFdRImqT0Iq7K0VRd1DoBpyC1W%2FECn4qwWcm7Ts2RQ%3D\"\n" +
                "        }\n" +
                "    }\n" +
                "}");

        List<BaseUser> baseUsers = client.listUsers(null).getUsers();
        assertNotNull(baseUsers);
        assertEquals(3, baseUsers.size());

        stubResponse(200, "{\n" +
                "    \"id\": \"USR-cf43cfdb-0322-41f4-84ed-0d2b816f030c\",\n" +
                "    \"name\": \"Test user\",\n" +
                "    \"properties\": {},\n" +
                "    \"_links\": {\n" +
                "        \"self\": {\n" +
                "            \"href\": \"https://api-us-3.vonage.com/v1/users/USR-cf43cfdb-0322-41f4-84ed-0d2b816f030c\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"channels\": {\n" +
                "        \"websocket\": [\n" +
                "            {}\n" +
                "        ],\n" +
                "        \"sip\": [\n" +
                "            {\n" +
                "                \"uri\": \"invalid\",\n" +
                "                \"foo\": \"bar\",\n" +
                "                \"nonsense\": true\n" +
                "            }\n" +
                "        ],\n" +
                "       \"pstn\": [\n" +
                "            {},{\n" +
                "                \"number\": 1800555555\n" +
                "            }\n" +
                "       ],\n" +
                "       \"vbc\": [],\n" +
                "       \"sms\":[{}],\n" +
                "       \"mms\": null\n" +
                "    }\n" +
                "}");

            List<User> fullUsers = client.getUserDetails(baseUsers);
            assertNotNull(fullUsers);
            assertEquals(baseUsers.size(), fullUsers.size());

            // Assertions will only work for the first user due to the way mocking is done.
            User user = fullUsers.get(0);
            assertEquals("USR-cf43cfdb-0322-41f4-84ed-0d2b816f030c", user.getId());
            assertEquals("Test user", user.getName());
            assertNull(user.getCustomData());
            Channels channels = user.getChannels();
            assertNotNull(channels);
            List<Websocket> websocket = channels.getWebsocket();
            assertNotNull(websocket);
            assertEquals(1, websocket.size());
            Websocket websocket0 = websocket.get(0);
            assertNotNull(websocket0);
            assertNull(websocket0.getContentType());
            assertNull(websocket0.getUri());
            assertNull(websocket0.getHeaders());
            List<Sip> sip = channels.getSip();
            assertNotNull(sip);
            assertEquals(1, sip.size());
            Sip sip0 = sip.get(0);
            assertNotNull(sip0);
            assertEquals(URI.create("invalid"), sip0.getUri());
            assertNull(sip0.getUsername());
            assertNull(sip0.getPassword());
            List<Pstn> pstn = channels.getPstn();
            assertNotNull(pstn);
            assertEquals(2, pstn.size());
            Pstn pstn0 = pstn.get(0);
            assertNotNull(pstn0);
            assertNull(pstn0.getNumber());
            Pstn pstn1 = pstn.get(1);
            assertNotNull(pstn1);
            assertEquals("1800555555", pstn1.getNumber());
            List<Vbc> vbc = channels.getVbc();
            assertNotNull(vbc);
            assertEquals(0, vbc.size());
            List<Sms> sms = channels.getSms();
            assertNotNull(sms);
            assertEquals(1, sms.size());
            Sms sms0 = sms.get(0);
            assertNotNull(sms0);
            assertNull(sms0.getNumber());
            assertNull(channels.getMessenger());
            assertNull(channels.getViber());
            assertNull(channels.getMms());
            assertNull(channels.getWhatsapp());
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
                params.put("order", "asc");
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
