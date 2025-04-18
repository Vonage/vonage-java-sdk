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
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.util.Arrays;

public class VonageUnacceptableAuthExceptionTest {

    @Test
    public void testAllAuthMethodHaveAppropriateDescriptions() throws IOException {
        VonageUnacceptableAuthException exception = new VonageUnacceptableAuthException(
                Arrays.asList(new ApiKeyHeaderAuthMethod(null, null),
                        new SignatureAuthMethod(null, null),
                        new JWTAuthMethod(TestUtils.APPLICATION_ID_STR, new TestUtils().loadKey("test/keys/application_key"))),
                Arrays.asList(ApiKeyHeaderAuthMethod.class, SignatureAuthMethod.class, JWTAuthMethod.class)
        );

        assertEquals("No acceptable authentication type could be found. Acceptable types are: API Key and Secret, API Key and Signature Secret, Application ID and Private Key. Supplied types were: API Key and Secret, API Key and Signature Secret, Application ID and Private Key", exception.getMessage());
    }
}
