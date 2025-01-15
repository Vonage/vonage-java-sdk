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
package com.vonage.client.account;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ListSecretsResponseTest {

    @Test
    public void testEmptyConstructor() {
        var response = new ListSecretsResponse();
        assertNull(response.getSecrets());
        assertNull(response.getLinks());
        assertNull(response.getPage());
        assertNull(response.getPageSize());
        assertNull(response.getTotalPages());
    }

    @Test
    public void testFromJsonEmpty() {
        var response = ListSecretsResponse.fromJson("{}");
        assertNotNull(response);
        assertNull(response.getSecrets());
        assertNull(response.getLinks());
        assertNull(response.getPage());
        assertNull(response.getPageSize());
        assertNull(response.getTotalPages());
    }

    @Test
    public void testFromJsonInvalid() {
        assertThrows(com.vonage.client.VonageResponseParseException.class,
                () -> ListSecretsResponse.fromJson("{malformed]")
        );
    }

    @Test
    public void testSingleItem() {
        var response = ListSecretsResponse.fromJson("{\"_embedded\":{\"secrets\":[{}]}}");
        assertNotNull(response);
        var secrets = response.getSecrets();
        assertNotNull(secrets);
        assertEquals(1, secrets.size());
        assertNotNull(secrets.getFirst());
    }

    @Test
    public void testEmptySecrets() {
        var response = ListSecretsResponse.fromJson("{\"_embedded\":{\"secrets\":[]}}");
        assertNotNull(response);
        var secrets = response.getSecrets();
        assertNotNull(secrets);
        assertEquals(0, secrets.size());
    }

    @Test
    public void testNoSecrets() {
        var response = ListSecretsResponse.fromJson("{}");
        assertNotNull(response);
        assertNull(response.getSecrets());
    }

    @Test
    public void testNullAndEmptyJson() {
        assertNotNull(ListSecretsResponse.fromJson(null));
        assertNotNull(ListSecretsResponse.fromJson(""));
    }
}
