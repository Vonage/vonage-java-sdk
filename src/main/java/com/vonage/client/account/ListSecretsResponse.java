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
package com.vonage.client.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.common.HalPageResponse;
import java.util.List;

/**
 * HAL response for {@link AccountClient#listSecrets(String)}.
 */
public class ListSecretsResponse extends HalPageResponse {
    @JsonProperty("_embedded") private Embedded _embedded;

    ListSecretsResponse() {}

    private static final class Embedded extends JsonableBaseObject {
        @JsonProperty("secrets") private List<SecretResponse> secrets;
    }

    /**
     * Gets the secrets contained in the {@code _embedded} resource.
     *
     * @return The list of {@linkplain SecretResponse}, or {@code null} if absent from the response.
     */
    @JsonIgnore
    public List<SecretResponse> getSecrets() {
        return _embedded != null ? _embedded.secrets: null;
    }
}
