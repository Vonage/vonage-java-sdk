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
package com.vonage.client.voice.ncco;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.net.URI;
import java.util.Collections;

public class SipEndpointTest {

    @Test
    public void testAllFields() {
        assertThrows(IllegalArgumentException.class, () -> SipEndpoint.builder(",}").build());
        String uri = "sip:test@example.com";
        String user2UserHeader = "342342ef34;encoding=hex";
        SipEndpoint endpoint = SipEndpoint.builder(URI.create("foo"))
                .uri(uri).headers(Collections.singletonMap("k1", "v1"))
                .userToUserHeader(null).userToUserHeader(user2UserHeader).build();

        ConnectAction connect = ConnectAction.builder(endpoint).build();

        String expectedJson = "[{\"endpoint\":[{\"uri\":\""+uri+"\"," +
                "\"headers\":{\"k1\":\"v1\"}," +
                "\"standardHeaders\":{\"User-to-User\":\""+user2UserHeader+"\"}," +
                "\"type\":\"sip\"}],\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testUriOnly() {
        String uri = "sip:vonage.com";
        SipEndpoint endpoint = SipEndpoint.builder().uri(uri).build();

        ConnectAction connect = ConnectAction.builder(endpoint).build();

        String expectedJson = "[{\"endpoint\":[{\"uri\":\"" + uri + "\",\"type\":\"sip\"}],\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testDomainOnly() {
        String domain = "example";
        SipEndpoint endpoint = SipEndpoint.builder().domain(domain).build();

        ConnectAction connect = ConnectAction.builder(endpoint).build();

        String expectedJson = "[{\"endpoint\":[{\"domain\":\""+domain+"\",\"type\":\"sip\"}],\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testDomainAndUserOnly() {
        String domain = "Nexmo";
        String user = "My_user";
        SipEndpoint endpoint = SipEndpoint.builder().domain(domain).user(user).build();

        ConnectAction connect = ConnectAction.builder(endpoint).build();

        String expectedJson = "[{\"endpoint\":[{\"domain\":\""+domain+"\",\"user\":\""+user+"\",\"type\":\"sip\"}],\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testDomainAndUri() {
        assertThrows(IllegalStateException.class, () -> SipEndpoint.builder()
                .domain("example").uri("sip:test@example.com").build()
        );
    }

    @Test
    public void testUserOnly() {
        assertThrows(IllegalStateException.class, () -> SipEndpoint.builder().user("my_user").build());
    }

    @Test
    public void testUserAndUri() {
        assertThrows(IllegalStateException.class, () -> SipEndpoint.builder()
                .user("my_user").uri(URI.create("sip:my_user@example.com")).build()
        );
    }
}
