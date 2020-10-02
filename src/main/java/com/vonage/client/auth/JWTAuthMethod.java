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

import com.nexmo.jwt.Jwt;
import org.apache.http.client.methods.RequestBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JWTAuthMethod extends AbstractAuthMethod {
    private static final int SORT_KEY = 10;
    private Jwt jwt;

    public JWTAuthMethod(final String applicationId, final byte[] privateKey) {
        jwt = Jwt.builder().applicationId(applicationId).privateKeyContents(new String(privateKey)).build();
    }

    public JWTAuthMethod(String applicationId, Path path) throws IOException {
        this(applicationId, Files.readAllBytes(path));
    }

    public String generateToken() {
        return jwt.generate();
    }

    @Override
    public RequestBuilder apply(RequestBuilder request) {
        String token = jwt.generate();

        request.setHeader("Authorization", "Bearer " + token);
        return request;
    }

    @Override
    public int getSortKey() {
        return SORT_KEY;
    }
}
