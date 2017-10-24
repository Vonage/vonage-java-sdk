/*
 * Copyright (c) 2011-2017 Nexmo Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nexmo.client.auth;

import com.nexmo.client.NexmoClientException;

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
        this.authList = new TreeSet<>();
    }

    public AuthCollection(AuthMethod... authMethods) {
        this();
        for (AuthMethod method : authMethods) {
            add(method);
        }
    }

    /**
     * Add a new {@link AuthMethod} to the set managed by this AuthCollection
     *
     * @param auth AuthMethod method to be added to this collection
     */
    public void add(AuthMethod auth) {
        this.authList.add(auth);
    }

    /**
     * Obtain an AuthMethod of type T, if one is contained in this collection.
     *
     * @param type The type of AuthMethod to be located
     * @param <T>  The type of AuthMethod which will be returned
     * @return An AuthMethod subclass matching type
     * @throws NexmoUnacceptableAuthException if no matching AuthMethod is found.
     */
    public <T extends AuthMethod> T getAuth(Class<T> type) throws NexmoUnacceptableAuthException {
        for (AuthMethod availableAuthMethod : this.authList) {
            if (type.isInstance(availableAuthMethod)) {
                return (T) availableAuthMethod;
            }
        }
        throw new NexmoUnacceptableAuthException(this.authList, new HashSet<Class>(Arrays.asList(new Class[]{type})));
    }

    /**
     * Obtain an {@link AuthMethod} instance for a set of acceptable AuthMethod classes.
     *
     * @param acceptableAuthMethodClasses A Set of AuthMethod classes which are suitable for the target REST endpoint.
     * @return the preferred AuthMethod from the provided set of acceptable AuthMethod classes
     * @throws NexmoUnacceptableAuthException if no appropriate AuthMethod is held by this AuthCollection
     */
    public AuthMethod getAcceptableAuthMethod(Set<Class> acceptableAuthMethodClasses) throws NexmoUnacceptableAuthException {
        for (AuthMethod availableAuthMethod : this.authList) {
            if (acceptableAuthMethodClasses.contains(availableAuthMethod.getClass())) {
                return availableAuthMethod;
            }
        }
        throw new NexmoUnacceptableAuthException(this.authList, acceptableAuthMethodClasses);
    }
}
