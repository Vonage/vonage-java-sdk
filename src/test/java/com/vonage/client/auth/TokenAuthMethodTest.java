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
package com.vonage.client.auth;

import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Base64;

public class TokenAuthMethodTest {

    @Test
    public void testApplyApiKeyAndSecret() {
        String key = "e4e5b6c3", secret = "0123456789abcdef";
        var auth = new TokenAuthMethod(key, secret);
        String payload = "{\"name\":\"app name\"}";
        RequestBuilder requestBuilder = RequestBuilder.get().setEntity(
                new StringEntity(payload, ContentType.APPLICATION_JSON)
        );

        var header = auth.getHeaderValue();
        var prefix = "Basic ";
        assertTrue(header.startsWith(prefix));
        var encoded = header.substring(prefix.length());
        var decoded = new String(Base64.getDecoder().decode(encoded));
        String[] split = decoded.split(":");
        assertEquals(2, split.length);
        assertEquals(key, split[0]);
        assertEquals(secret, split[1]);

        var params = auth.asQueryParams();
        assertNotNull(params);
        assertEquals(2, params.size());
        assertEquals(key, params.get("api_key"));
        assertEquals(secret, params.get("api_secret"));
    }
}
