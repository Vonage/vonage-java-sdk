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
package com.vonage.client.auth;

import com.vonage.client.TestUtils;
import static com.vonage.client.TestUtils.APPLICATION_ID_STR;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class JWTAuthMethodTest {
    private byte[] keyBytes;
    private JWTAuthMethod auth;

    @BeforeEach
    public void setUp() throws Exception {
        keyBytes = new TestUtils().loadKey("test/keys/application_key");
        auth = new JWTAuthMethod(APPLICATION_ID_STR, keyBytes);
    }

    @Test
    public void testSavedKeyUsingPath() throws Exception {
        auth = new JWTAuthMethod(APPLICATION_ID_STR, Files.readAllBytes(Paths.get(
                "src/test/resources/com/vonage/client/test/keys/application_key2"
        )));
    }

    @Test
    public void testApply() {
        String header = auth.getHeaderValue();
        assertNotNull(header);
        assertEquals("Bearer ", header.substring(0, 7));
    }

    @Test
    public void testEqualsAndHashCode() {
        assertNotEquals(new Object(), auth);
        var clone = new JWTAuthMethod(APPLICATION_ID_STR, keyBytes);
        assertEquals(auth, clone);
        assertEquals(auth.hashCode(), clone.hashCode());
        var random = new JWTAuthMethod(UUID.randomUUID().toString(), keyBytes);
        assertNotEquals(auth, random);
        clone = new JWTAuthMethod(random.getApplicationId(), keyBytes);
        assertNotEquals(auth, clone);
        boolean equalsNull = auth.equals(null);
        assertFalse(equalsNull);
        boolean equalsObject = auth.equals(new Object());
        assertFalse(equalsObject);

        class CustomJwtAuthMethod extends JWTAuthMethod {
            public CustomJwtAuthMethod() {
                super(APPLICATION_ID_STR, keyBytes);
            }

            @Override
            public int getSortKey() {
                return 37;
            }
        }

        clone = new CustomJwtAuthMethod();
        assertNotEquals(auth, clone);
    }
}
