/*
 *   Copyright 2023 Vonage
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
package com.vonage.client.account;

import com.vonage.client.Jsonable;
import com.vonage.client.VonageUnexpectedException;
import io.openapitools.jackson.dataformat.hal.HALLink;
import io.openapitools.jackson.dataformat.hal.HALMapper;
import io.openapitools.jackson.dataformat.hal.annotation.EmbeddedResource;
import io.openapitools.jackson.dataformat.hal.annotation.Link;
import io.openapitools.jackson.dataformat.hal.annotation.Resource;
import java.io.IOException;
import java.util.Collection;

@Resource
public class ListSecretsResponse implements Jsonable {
    @Link
    private HALLink self;

    @EmbeddedResource
    private Collection<SecretResponse> secrets;

    @Deprecated
    public HALLink getSelf() {
        return self;
    }

    public Collection<SecretResponse> getSecrets() {
        return secrets;
    }

    @Override
    public void updateFromJson(String json) {
        try {
            ListSecretsResponse parsed = new HALMapper().readValue(json, ListSecretsResponse.class);
            self = parsed.self;
            secrets = parsed.secrets;
        }
        catch (IOException ex) {
            throw new VonageUnexpectedException("Failed to produce ListSecretsResponse from json.", ex);
        }
    }

    public static ListSecretsResponse fromJson(String json) {
        ListSecretsResponse response = new ListSecretsResponse();
        response.updateFromJson(json);
        return response;
    }
}
