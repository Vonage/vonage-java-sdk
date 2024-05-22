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

import com.vonage.client.AbstractClientTest;
import com.vonage.client.TestUtils;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Collections;
import java.util.Set;

public class AuthCollectionTest {
    private static final Set<Class<? extends AuthMethod>>
            JWT_AUTH_CLASS_SET = Collections.singleton(JWTAuthMethod.class),
            TOKEN_AUTH_CLASS_SET = Collections.singleton(ApiKeyHeaderAuthMethod.class);

    final byte[] privateKeyContents = new TestUtils().loadKey("test/keys/application_key");
    final String applicationId = AbstractClientTest.APPLICATION_ID,
            apiKey = AbstractClientTest.API_KEY, apiSecret = AbstractClientTest.API_SECRET;

    final JWTAuthMethod jwtAuth = new JWTAuthMethod(applicationId, privateKeyContents);

    public AuthCollectionTest() throws Exception {
    }


    @Test
    public void testGetAcceptableAuthMethod() throws Exception {
        AuthCollection auths = new AuthCollection();
        auths.add(jwtAuth);
        assertEquals(jwtAuth, auths.getAcceptableAuthMethod(JWT_AUTH_CLASS_SET));
    }

    @Test
    public void testMultipleAuthMethods() throws Exception {
        ApiKeyHeaderAuthMethod tokenAuth = new ApiKeyHeaderAuthMethod("api_key", "api_secret");
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
            assertEquals("No acceptable authentication type could be found. Acceptable types are: Application ID and Private Key. Supplied types were: ", ex.getMessage());
        }
    }

    @Test
    public void testAuthMethodPrecedence() throws Exception {
        ApiKeyHeaderAuthMethod tAuth = new ApiKeyHeaderAuthMethod(apiKey, apiSecret);
        AuthCollection auths = new AuthCollection();
        auths.add(tAuth);
        auths.add(jwtAuth);
        assertEquals(jwtAuth, auths.getAcceptableAuthMethod(JWT_AUTH_CLASS_SET));
        assertEquals(tAuth, auths.getAcceptableAuthMethod(TOKEN_AUTH_CLASS_SET));
        assertEquals(jwtAuth, auths.getAcceptableAuthMethod(Collections.singleton(AuthMethod.class)));
    }

    @Test
    public void testIncompatibleAuths() throws Exception {
        ApiKeyHeaderAuthMethod tAuth = new ApiKeyHeaderAuthMethod(apiKey, apiSecret);
        AuthCollection auths = new AuthCollection();
        auths.add(tAuth);

        try {
            auths.getAcceptableAuthMethod(JWT_AUTH_CLASS_SET);
            fail("No acceptable auth method should throw a VonageClientException");
        } catch (VonageUnacceptableAuthException ex) {
            assertEquals("No acceptable authentication type could be found. Acceptable types are: Application ID and Private Key. Supplied types were: API Key and Secret", ex.getMessage());
        }
    }

    @Test
    public void testLongConstructorJwtAndApiSecret() throws Exception {
        var ac = new AuthCollection(applicationId, privateKeyContents, apiKey, apiSecret, null, null);

        assertTrue(ac.hasAuthMethod(JWTAuthMethod.class));
        assertTrue(ac.hasAuthMethod(ApiKeyQueryParamsAuthMethod.class));
        assertTrue(ac.hasAuthMethod(ApiKeyHeaderAuthMethod.class));
        assertFalse(ac.hasAuthMethod(SignatureAuthMethod.class));

        var apiKeyClasses = Set.of(ApiKeyQueryParamsAuthMethod.class, ApiKeyHeaderAuthMethod.class);
        assertNotNull(ac.getAcceptableAuthMethod(JWT_AUTH_CLASS_SET));
        assertEquals(ApiKeyQueryParamsAuthMethod.class, ac.getAcceptableAuthMethod(apiKeyClasses).getClass());
        assertThrows(VonageUnacceptableAuthException.class, () ->
                ac.getAcceptableAuthMethod(Set.of(SignatureAuthMethod.class))
        );

        assertEquals(JWTAuthMethod.class, ac.getAuth(JWTAuthMethod.class).getClass());
        assertEquals(JWTAuthMethod.class, ac.getAuth(HeaderAuthMethod.class).getClass());
        assertEquals(ApiKeyQueryParamsAuthMethod.class, ac.getAuth(QueryParamsAuthMethod.class).getClass());
        assertEquals(ApiKeyHeaderAuthMethod.class, ac.getAuth(BasicAuthMethod.class).getClass());
    }

    @Test
    public void testAddReplacesExistingAuthMethod() throws Exception {
        class CustomJwt extends JWTAuthMethod {
            public CustomJwt() {
                super(applicationId, privateKeyContents);
            }
        }

        var ac = new AuthCollection(jwtAuth);
        assertEquals(jwtAuth, ac.getAuth(AuthMethod.class));
        var custom = new CustomJwt();
        ac.add(custom);
        var retrieved = ac.getAuth(AuthMethod.class);
        assertEquals(custom, retrieved);
        assertEquals(CustomJwt.class, retrieved.getClass());
    }
}
