/*
 * Copyright (c) 2020 Vonage
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
package com.vonage.client.auth;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class VonageUnacceptableAuthException extends VonageAuthException {
    private static final Map<Class, String> AUTH_DESCRIPTION_MAP = new HashMap<>();

    private final Iterable<AuthMethod> availableAuths;
    private final Iterable<Class> acceptableAuthClasses;

    static {
        AUTH_DESCRIPTION_MAP.put(TokenAuthMethod.class, "API Key and Secret");
        AUTH_DESCRIPTION_MAP.put(SignatureAuthMethod.class, "API Key and Signature Secret");
        AUTH_DESCRIPTION_MAP.put(JWTAuthMethod.class, "Application ID and Private Key");
    }

    public VonageUnacceptableAuthException(Collection<AuthMethod> availableAuths, Collection<Class>
            acceptableAuthClasses) {
        super();
        this.availableAuths = new ArrayList<>(availableAuths);
        this.acceptableAuthClasses = new ArrayList<>(acceptableAuthClasses);
    }

    public String getMessage() {
        return generateErrorMessage();
    }

    private String generateErrorMessage() {
        SortedSet<String> availableTypes = new TreeSet<>();
        for (AuthMethod auth : this.availableAuths) {
            availableTypes.add(AUTH_DESCRIPTION_MAP.getOrDefault(auth.getClass(), auth.getClass().getSimpleName()));
        }

        SortedSet<String> acceptableTypes = new TreeSet<>();
        for (Class klass : this.acceptableAuthClasses) {
            acceptableTypes.add(AUTH_DESCRIPTION_MAP.getOrDefault(klass, klass.getSimpleName()));
        }

        return String.format("No acceptable authentication type could be found. Acceptable types are: %s. Supplied " +
                "types " +
                "were: %s", StringUtils.join(acceptableTypes, ", "), StringUtils.join(availableTypes, ", "));
    }
}