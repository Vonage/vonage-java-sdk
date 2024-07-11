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

import com.vonage.jwt.Jwt;

public class JWTAuthMethod extends BearerAuthMethod {
    private static final int SORT_KEY = 10;

    private final Jwt jwt;
    private final String applicationId, privateKeyContents;

    public JWTAuthMethod(final String applicationId, final byte[] privateKey) {
        jwt = Jwt.builder()
                .applicationId(this.applicationId = applicationId)
                .privateKeyContents(this.privateKeyContents = new String(privateKey)).build();
    }

    public String generateToken() {
        return jwt.generate();
    }

    public String getApplicationId() {
        return applicationId;
    }

    /**
     * Convenience method for creating custom JWTs.
     *
     * @return A new {@link Jwt.Builder} with the application ID and private key already set.
     * @since 8.0.0-beta2
     */
    public Jwt.Builder newJwt() {
        return Jwt.builder().applicationId(applicationId).privateKeyContents(privateKeyContents);
    }

    @Override
    protected final String getBearerToken() {
        return generateToken();
    }

    @Override
    public int getSortKey() {
        return SORT_KEY;
    }
}
