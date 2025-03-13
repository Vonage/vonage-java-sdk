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

import com.vonage.client.Jsonable;
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
        var response = Jsonable.fromJson("{}", ListSecretsResponse.class);
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
                () -> Jsonable.fromJson("{malformed]", ListSecretsResponse.class)
        );
    }

    @Test
    public void testSingleItem() {
        var response = Jsonable.fromJson("{\"_embedded\":{\"secrets\":[{}]}}", ListSecretsResponse.class);
        assertNotNull(response);
        var secrets = response.getSecrets();
        assertNotNull(secrets);
        assertEquals(1, secrets.size());
        assertNotNull(secrets.getFirst());
    }

    @Test
    public void testEmptySecrets() {
        var response = Jsonable.fromJson("{\"_embedded\":{\"secrets\":[]}}", ListSecretsResponse.class);
        assertNotNull(response);
        var secrets = response.getSecrets();
        assertNotNull(secrets);
        assertEquals(0, secrets.size());
    }

    @Test
    public void testNoSecrets() {
        var response = Jsonable.fromJson("{}", ListSecretsResponse.class);
        assertNotNull(response);
        assertNull(response.getSecrets());
    }

    @Test
    public void testNullAndEmptyJson() {
        assertNotNull(Jsonable.fromJson(null, ListSecretsResponse.class));
        assertNotNull(Jsonable.fromJson("", ListSecretsResponse.class));
    }
}
