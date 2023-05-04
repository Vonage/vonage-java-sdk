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
package com.vonage.client.auth;

import com.vonage.client.TestUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import java.util.Collections;
import java.util.Set;

public class AuthCollectionTest {
    private final TestUtils testUtils = new TestUtils();
    private static final Set<Class<? extends AuthMethod>>
            JWT_AUTH_CLASS_SET = Collections.singleton(JWTAuthMethod.class),
            TOKEN_AUTH_CLASS_SET = Collections.singleton(TokenAuthMethod.class);

    @Test
    public void testGetAcceptableAuthMethod() throws Exception {
        JWTAuthMethod jAuth = new JWTAuthMethod("application_id", testUtils.loadKey("test/keys/application_key"));
        AuthCollection auths = new AuthCollection();
        auths.add(jAuth);
        assertEquals(jAuth, auths.getAcceptableAuthMethod(JWT_AUTH_CLASS_SET));
    }

    @Test
    public void testMultipleAuthMethods() throws Exception {
        JWTAuthMethod jwtAuth = new JWTAuthMethod("application_id", testUtils.loadKey("test/keys/application_key"));
        TokenAuthMethod tokenAuth = new TokenAuthMethod("api_key", "api_secret");
        AuthCollection auths = new AuthCollection(jwtAuth, tokenAuth);
        assertEquals(jwtAuth, auths.getAcceptableAuthMethod(JWT_AUTH_CLASS_SET));
        assertEquals(tokenAuth, auths.getAcceptableAuthMethod(TOKEN_AUTH_CLASS_SET));
    }

    @Test
    public void testNoAcceptableAuthMethod() throws Exception {
        AuthCollection auths = new AuthCollection();

        try {
            auths.getAcceptableAuthMethod(JWT_AUTH_CLASS_SET);
            fail("No acceptable auth method should throw a VonageClientException");
        } catch (VonageUnacceptableAuthException ex) {
            assertEquals(
                    "No acceptable authentication type could be found. Acceptable types are: Application ID and Private Key. Supplied types were: ",
                    ex.getMessage());
        }
    }

    @Test
    public void testAuthMethodPrecedence() throws Exception {
        JWTAuthMethod jAuth = new JWTAuthMethod("application_id", testUtils.loadKey("test/keys/application_key"));
        TokenAuthMethod tAuth = new TokenAuthMethod("key", "secret");
        AuthCollection auths = new AuthCollection();
        auths.add(tAuth);
        auths.add(jAuth);
        assertEquals(jAuth, auths.getAcceptableAuthMethod(JWT_AUTH_CLASS_SET));
        assertEquals(tAuth, auths.getAcceptableAuthMethod(TOKEN_AUTH_CLASS_SET));
        assertEquals(jAuth, auths.getAcceptableAuthMethod(Collections.singleton(AuthMethod.class)));
    }

    @Test
    public void testIncompatibleAuths() throws Exception {
        TokenAuthMethod tAuth = new TokenAuthMethod("key", "secret");
        AuthCollection auths = new AuthCollection();
        auths.add(tAuth);

        try {
            auths.getAcceptableAuthMethod(JWT_AUTH_CLASS_SET);
            fail("No acceptable auth method should throw a VonageClientException");
        } catch (VonageUnacceptableAuthException ex) {
            assertEquals("No acceptable authentication type could be found. Acceptable types are: Application ID and Private Key. Supplied types were: API Key and Secret", ex.getMessage());
        }
    }
}
