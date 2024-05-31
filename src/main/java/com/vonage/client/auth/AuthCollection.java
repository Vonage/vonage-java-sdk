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

import com.vonage.client.auth.hashutils.HashUtil;
import java.util.*;

/**
 * Internal class, managing a collection of {@link AuthMethod}s.
 * <p>
 * This holds a collection of AuthMethod instances, in order of preference, and
 * allow for simple selection of an appropriate AuthMethod for a particular REST endpoint.
 */
public class AuthCollection {
    private final SortedSet<AuthMethod> authList;

    /**
     * Create a new AuthCollection with an empty set of AuthMethods.
     */
    public AuthCollection() {
        this(new TreeSet<>());
    }

    public AuthCollection(AuthMethod... authMethods) {
        this(new TreeSet<>(Arrays.asList(authMethods)));
    }

    public AuthCollection(SortedSet<AuthMethod> authMethods) {
        authList = authMethods;
    }

    public AuthCollection(UUID applicationId, byte[] privateKeyContents, String key, String secret, HashUtil.HashType hashType, String signature) {

        this();

        if (key != null && secret == null && signature == null) {
            throw new IllegalStateException(
                    "You must provide an API secret or signature secret in addition to your API key.");
        }
        if (secret != null && key == null) {
            throw new IllegalStateException("You must provide an API key in addition to your API secret.");
        }
        if (signature != null && key == null) {
            throw new IllegalStateException("You must provide an API key in addition to your signature secret.");
        }
        if (applicationId == null && privateKeyContents != null) {
            throw new IllegalStateException("You must provide an application ID in addition to your private key.");
        }
        if (applicationId != null && privateKeyContents == null) {
            throw new IllegalStateException("You must provide a private key in addition to your application id.");
        }

        if (key != null && secret != null) {
            authList.add(new ApiKeyHeaderAuthMethod(key, secret));
            authList.add(new ApiKeyQueryParamsAuthMethod(key, secret));
        }
        if (key != null && signature != null) {
            authList.add(new SignatureAuthMethod(key, signature, hashType));
        }
        if (applicationId != null) {
            authList.add(new JWTAuthMethod(applicationId.toString(), privateKeyContents));
        }
    }

    /**
     * Add a new {@link AuthMethod} to the set managed by this AuthCollection.
     * If an auth method of this type already exists, this method will replace it with
     * the new provided value.
     *
     * @param auth AuthMethod method to be added to this collection.
     */
    public void add(AuthMethod auth) {
        authList.remove(auth);
        authList.add(auth);
    }

    /**
     * Obtain an AuthMethod of type T, if one is contained in this collection.
     *
     * @param type The type of AuthMethod to be located.
     * @param <T>  The type of AuthMethod which will be returned.
     *
     * @return An AuthMethod subclass matching type.
     *
     * @throws VonageUnacceptableAuthException if no matching AuthMethod is found.
     */
    @SuppressWarnings("unchecked")
    public <T extends AuthMethod> T getAuth(Class<T> type) throws VonageUnacceptableAuthException {
        for (AuthMethod availableAuthMethod : authList) {
            if (type.isInstance(availableAuthMethod)) {
                return (T) availableAuthMethod;
            }
        }
        throw new VonageUnacceptableAuthException(authList, Collections.singletonList(type));
    }

    /**
     * Obtain an {@link AuthMethod} instance for a set of acceptable AuthMethod classes.
     *
     * @param acceptableAuthMethodClasses A Set of AuthMethod classes which are suitable for the target REST endpoint.
     *
     * @return the preferred AuthMethod from the provided set of acceptable AuthMethod classes.
     *
     * @throws VonageUnacceptableAuthException if no appropriate AuthMethod is held by this AuthCollection.
     */
    public AuthMethod getAcceptableAuthMethod(Set<Class<? extends AuthMethod>> acceptableAuthMethodClasses) throws VonageUnacceptableAuthException {
        for (AuthMethod availableAuthMethod : authList) {
            for (Class<? extends AuthMethod> acceptable : acceptableAuthMethodClasses) {
                if (acceptable.isAssignableFrom(availableAuthMethod.getClass())) {
                    return availableAuthMethod;
                }
            }
        }
        throw new VonageUnacceptableAuthException(authList, acceptableAuthMethodClasses);
    }

    /**
     * Utility method for determining whether a certain authentication method has been registered.
     *
     * @param authMethod The authentication method type.
     *
     * @return {@code true} if the specified auth method is available, {@code false} otherwise.
     *
     * @since 7.3.0
     */
    public boolean hasAuthMethod(Class<? extends AuthMethod> authMethod) {
        return authList.stream().map(AuthMethod::getClass).anyMatch(authMethod::equals);
    }
}
