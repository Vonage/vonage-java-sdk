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
        for (AuthMethod auth : availableAuths) {
            availableTypes.add(AUTH_DESCRIPTION_MAP.getOrDefault(auth.getClass(), auth.getClass().getSimpleName()));
        }

        SortedSet<String> acceptableTypes = new TreeSet<>();
        for (Class klass : acceptableAuthClasses) {
            acceptableTypes.add(AUTH_DESCRIPTION_MAP.getOrDefault(klass, klass.getSimpleName()));
        }

        return String.format("No acceptable authentication type could be found. Acceptable types are: %s. Supplied " +
                "types " +
                "were: %s", StringUtils.join(acceptableTypes, ", "), StringUtils.join(availableTypes, ", "));
    }
}