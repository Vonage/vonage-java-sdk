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

import org.apache.http.client.methods.RequestBuilder;

public interface AuthMethod extends Comparable<AuthMethod> {

    @Override
    default int compareTo(AuthMethod other) {
        return Integer.compare(this.getSortKey(), other.getSortKey());
    }

    /**
     * Apply the authentication to the header as basic authentication.
     *
     * @param requestBuilder The request being built
     *
     * @return RequestBuilder for more building of the request.
     */
    default RequestBuilder applyAsBasicAuth(RequestBuilder requestBuilder) {
        throw new UnsupportedOperationException(
                "applyAsBasicAuth not implemented for " + getClass().getCanonicalName()
        );
    }

    /**
     * Apply the authentication by adding it to the entity payload.
     *
     * @param requestBuilder The request being built
     *
     * @return RequestBuilder for more building of the request.
     */
    default RequestBuilder applyAsJsonProperties(RequestBuilder requestBuilder) {
        throw new UnsupportedOperationException(
                "applyAsJsonProperties not implemented for " + getClass().getCanonicalName()
        );
    }

    RequestBuilder apply(RequestBuilder request);

    int getSortKey();
}