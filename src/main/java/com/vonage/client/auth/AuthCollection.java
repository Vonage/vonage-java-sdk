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

import java.util.*;

/**
 * Internal class, managing a collection of {@link AuthMethod}s.
 * <p>
 * This holds a collection of AuthMethod instances, in order of preference, and
 * allow for simple selection of an appropriate AuthMethod for a particular REST endpoint.
 */
public class AuthCollection {
    private SortedSet<AuthMethod> authList;

    /**
     * Create a new AuthCollection with an empty set of AuthMethods.
     */
    public AuthCollection() {
        authList = new TreeSet<>();
    }

    public AuthCollection(AuthMethod... authMethods) {
        this();
        for (AuthMethod method : authMethods) {
            add(method);
        }
    }

    public AuthCollection(SortedSet<AuthMethod> authMethods) {
        authList = authMethods;
    }

    /**
     * Add a new {@link AuthMethod} to the set managed by this AuthCollection
     *
     * @param auth AuthMethod method to be added to this collection
     */
    public void add(AuthMethod auth) {
        authList.add(auth);
    }

    /**
     * Obtain an AuthMethod of type T, if one is contained in this collection.
     *
     * @param type The type of AuthMethod to be located
     * @param <T>  The type of AuthMethod which will be returned
     *
     * @return An AuthMethod subclass matching type
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
        throw new VonageUnacceptableAuthException(authList, new HashSet<>(Arrays.asList(new Class[]{type})));
    }

    /**
     * Obtain an {@link AuthMethod} instance for a set of acceptable AuthMethod classes.
     *
     * @param acceptableAuthMethodClasses A Set of AuthMethod classes which are suitable for the target REST endpoint.
     *
     * @return the preferred AuthMethod from the provided set of acceptable AuthMethod classes
     *
     * @throws VonageUnacceptableAuthException if no appropriate AuthMethod is held by this AuthCollection
     */
    public AuthMethod getAcceptableAuthMethod(Set<Class> acceptableAuthMethodClasses) throws VonageUnacceptableAuthException {
        for (AuthMethod availableAuthMethod : authList) {
            if (acceptableAuthMethodClasses.contains(availableAuthMethod.getClass())) {
                return availableAuthMethod;
            }
        }
        throw new VonageUnacceptableAuthException(authList, acceptableAuthMethodClasses);
    }
}
