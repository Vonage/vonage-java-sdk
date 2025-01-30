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
import static com.vonage.client.TestUtils.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Collections;
import java.util.Set;

public class AuthCollectionTest {
    private static final Set<Class<? extends AuthMethod>>
            JWT_AUTH_CLASS_SET = Collections.singleton(JWTAuthMethod.class),
            TOKEN_AUTH_CLASS_SET = Collections.singleton(ApiKeyHeaderAuthMethod.class);

    final byte[] privateKeyContents = new TestUtils().loadKey("test/keys/application_key");

    final JWTAuthMethod jwtAuth = new JWTAuthMethod(APPLICATION_ID_STR, privateKeyContents);

    public AuthCollectionTest() throws Exception {
    }


    @Test
    public void testGetAcceptableAuthMethod() {
        AuthCollection auths = new AuthCollection();
        auths.add(jwtAuth);
        assertEquals(jwtAuth, auths.getAcceptableAuthMethod(JWT_AUTH_CLASS_SET));
    }

    @Test
    public void testMultipleAuthMethods() {
        ApiKeyHeaderAuthMethod tokenAuth = new ApiKeyHeaderAuthMethod("api_key", "api_secret");
        AuthCollection auths = new AuthCollection(jwtAuth, tokenAuth);
        assertEquals(jwtAuth, auths.getAcceptableAuthMethod(JWT_AUTH_CLASS_SET));
        assertEquals(tokenAuth, auths.getAcceptableAuthMethod(TOKEN_AUTH_CLASS_SET));
        assertNotEquals(jwtAuth, tokenAuth);
        assertNotEquals(tokenAuth.hashCode(), jwtAuth.hashCode());
        var altAuth = new ApiKeyQueryParamsAuthMethod(tokenAuth.getApiKey(), "api_secret");
        assertNotEquals(tokenAuth, altAuth);
    }

    @Test
    public void testUnknownAuthMethod() {
        class ConcreteBasicAuthMethod extends BasicAuthMethod {
            @Override
            protected String getBasicToken() {
                return "T1=";
            }

            @Override
            public int getSortKey() {
                return Short.MAX_VALUE;
            }
        }
        var am = new ConcreteBasicAuthMethod();
        var ac = new AuthCollection(am);
        assertEquals(am, ac.getAcceptableAuthMethod(Set.of(ConcreteBasicAuthMethod.class)));
        assertEquals(am, ac.getAcceptableAuthMethod(Set.of(BasicAuthMethod.class)));
        ac.add(jwtAuth);
        assertEquals(jwtAuth, ac.getAcceptableAuthMethod(JWT_AUTH_CLASS_SET));
        assertNotEquals(jwtAuth, am);
        boolean equalsNull = am.equals(null);
        assertFalse(equalsNull);
        boolean equalsObject = am.equals(new Object());
        assertFalse(equalsObject);

        class ConcreteBasicAuthMethod2 extends ConcreteBasicAuthMethod {
            @Override
            public int getSortKey() {
                return Byte.MAX_VALUE;
            }
        }

        var am2 = new ConcreteBasicAuthMethod2();
        assertNotEquals(am, am2);

        class ConcreteAuthMethod extends AbstractAuthMethod {
            @Override
            public int getSortKey() {
                return 0;
            }
        }

        var am3 = new ConcreteAuthMethod();
        assertNotEquals(am, am3);
        assertNotEquals(am3, am2);
        equalsObject = am3.equals(new Object());
        assertFalse(equalsObject);
    }

    @Test
    public void testNoAcceptableAuthMethod() {
        AuthCollection auths = new AuthCollection();

        try {
            auths.getAcceptableAuthMethod(JWT_AUTH_CLASS_SET);
            fail("No acceptable auth method should throw a VonageClientException");
        } catch (VonageUnacceptableAuthException ex) {
            assertEquals("No acceptable authentication type could be found. Acceptable types are: Application ID and Private Key. Supplied types were: ", ex.getMessage());
        }
    }

    @Test
    public void testAuthMethodPrecedence() {
        ApiKeyHeaderAuthMethod tAuth = new ApiKeyHeaderAuthMethod(API_KEY, API_SECRET);
        AuthCollection auths = new AuthCollection();
        auths.add(tAuth);
        auths.add(jwtAuth);
        assertEquals(jwtAuth, auths.getAcceptableAuthMethod(JWT_AUTH_CLASS_SET));
        assertEquals(tAuth, auths.getAcceptableAuthMethod(TOKEN_AUTH_CLASS_SET));
        assertEquals(jwtAuth, auths.getAcceptableAuthMethod(Collections.singleton(AuthMethod.class)));
    }

    @Test
    public void testIncompatibleAuths() {
        ApiKeyHeaderAuthMethod tAuth = new ApiKeyHeaderAuthMethod(API_KEY, API_SECRET);
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
    public void testLongConstructorJwtAndApiSecret() {
        var ac = new AuthCollection(APPLICATION_ID, privateKeyContents, API_KEY, API_SECRET, null, null);

        assertTrue(ac.hasAuthMethod(JWTAuthMethod.class));
        assertTrue(ac.hasAuthMethod(ApiKeyQueryParamsAuthMethod.class));
        assertTrue(ac.hasAuthMethod(ApiKeyHeaderAuthMethod.class));
        assertFalse(ac.hasAuthMethod(SignatureAuthMethod.class));

        Set<Class<? extends AuthMethod>> apiKeyClasses = Set.of(ApiKeyAuthMethod.class);
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
    public void testAddReplacesExistingAuthMethod() {
        class CustomJwt extends JWTAuthMethod {
            public CustomJwt() {
                super(APPLICATION_ID_STR, privateKeyContents);
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
