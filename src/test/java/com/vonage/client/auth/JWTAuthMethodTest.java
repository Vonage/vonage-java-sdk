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

import com.vonage.client.TestUtils;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.*;
import java.nio.file.Paths;

public class JWTAuthMethodTest {
    private JWTAuthMethod auth;

    @BeforeEach
    public void setUp() throws Exception {
        byte[] keyBytes = new TestUtils().loadKey("test/keys/application_key");
        auth = new JWTAuthMethod("application-id", keyBytes);
    }

    @Test
    public void testSavedKeyUsingPath() throws Exception {
        auth = new JWTAuthMethod("application-id", Paths.get("src/test/resources/com/vonage/client/test/keys/application_key2"));
    }

    @Test
    public void testApply() throws Exception {
        String header = auth.getHeaderValue();
        assertNotNull(header);
        assertEquals("Bearer ", header.substring(0, 7));
    }
}
