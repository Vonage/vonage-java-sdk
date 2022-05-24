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
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AuthCollectionTest {
    private final TestUtils testUtils = new TestUtils();

    @Test
    public void testGetAcceptableAuthMethod() throws Exception {
        JWTAuthMethod jAuth = new JWTAuthMethod("application_id", testUtils.loadKey("test/keys/application_key"));
        AuthCollection auths = new AuthCollection();
        auths.add(jAuth);

        Set<Class<?>> acceptableAuths = acceptableClassSet(JWTAuthMethod.class);

        assertEquals(jAuth, auths.getAcceptableAuthMethod(acceptableAuths));
    }

    @Test
    public void testMultipleAuthMethods() throws Exception {
        JWTAuthMethod jwtAuth = new JWTAuthMethod("application_id", testUtils.loadKey("test/keys/application_key"));
        TokenAuthMethod tokenAuth = new TokenAuthMethod("api_key", "api_secret");

        AuthCollection auths = new AuthCollection(
                jwtAuth, tokenAuth
        );

        assertEquals(jwtAuth, auths.getAcceptableAuthMethod(acceptableClassSet(JWTAuthMethod.class)));
        assertEquals(tokenAuth, auths.getAcceptableAuthMethod(acceptableClassSet(TokenAuthMethod.class)));
    }

    @Test
    public void testNoAcceptableAuthMethod() throws Exception {
        AuthCollection auths = new AuthCollection();

        Set<Class<?>> acceptableAuths = new HashSet<>();
        acceptableAuths.add(JWTAuthMethod.class);

        try {
            auths.getAcceptableAuthMethod(acceptableAuths);
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

        Set<Class<?>> acceptableAuths = new HashSet<>();
        acceptableAuths.add(JWTAuthMethod.class);

        assertEquals(jAuth, auths.getAcceptableAuthMethod(acceptableAuths));
    }

    @Test
    public void testIncompatibleAuths() throws Exception {
        TokenAuthMethod tAuth = new TokenAuthMethod("key", "secret");
        AuthCollection auths = new AuthCollection();
        auths.add(tAuth);

        Set<Class<?>> acceptableAuths = new HashSet<>();
        acceptableAuths.add(JWTAuthMethod.class);

        try {
            auths.getAcceptableAuthMethod(acceptableAuths);
            fail("No acceptable auth method should throw a VonageClientException");
        } catch (VonageUnacceptableAuthException ex) {
            assertEquals("No acceptable authentication type could be found. Acceptable types are: Application ID and Private Key. Supplied types were: API Key and Secret", ex.getMessage());
        }
    }

    public Set<Class<?>> acceptableClassSet(Class<?>... classes) {
        Set<Class<?>> result = new HashSet<>();
        Collections.addAll(result, classes);
        return result;
    }
}
