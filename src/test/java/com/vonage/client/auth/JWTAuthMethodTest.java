/*
 *   Copyright 2020 Vonage
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


import com.vonage.client.TestUtils;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class JWTAuthMethodTest {
    private TestUtils testUtils;
    private JWTAuthMethod auth;

    @Before
    public void setUp() throws Exception {
        testUtils = new TestUtils();

        byte[] keyBytes = testUtils.loadKey("test/keys/application_key");
        auth = new JWTAuthMethod("application-id", keyBytes);
    }

    @Test
    public void testSavedKeyUsingPath() throws Exception {
        auth = new JWTAuthMethod("application-id", Paths.get("src/test/resources/com/vonage/client/test/keys/application_key2"));
    }

    @Test
    public void testApply() throws Exception {
        RequestBuilder req = RequestBuilder.get();
        auth.apply(req);

        assertEquals(1, req.getHeaders("Authorization").length);
        assertEquals("Bearer ", req.getFirstHeader("Authorization").getValue().substring(0, 7));
    }
}
