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
package com.vonage.client.application.capabilities;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;

/**
 * Network APIs capability configuration settings.
 *
 * @since 8.12.0
 */
public final class NetworkApis extends Capability {
    private URI redirectUri;
    private String networkApplicationId;

    private NetworkApis() {
    }

    private NetworkApis(Builder builder) {
        super(builder);
        redirectUri = builder.redirectUri;
        networkApplicationId = builder.networkApplicationId;
    }

    @Override
    public Type getType() {
        return Type.NETWORK;
    }

    /**
     * Gets the Redirect URL.
     *
     * @return The redirect URI, or {@code null} if absent.
     */
    @JsonProperty("redirect_uri")
    public URI getRedirectUri() {
        return redirectUri;
    }

    /**
     * Gets the network application ID (this is different from the Vonage application ID).
     *
     * @return The network application ID as a string, or {@code null} if absent.
     */
    @JsonProperty("network_application_id")
    public String getNetworkApplicationId() {
        return networkApplicationId;
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @return A new Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Capability.Builder<NetworkApis, Builder> {
        private URI redirectUri;
        private String networkApplicationId;

        private Builder() {}

        /**
         * Sets the Redirect URL.
         *
         * @param redirectUri The redirect URL as a string.
         * @return This builder.
         */
        public Builder redirectUri(String redirectUri) {
            return redirectUri(URI.create(redirectUri));
        }

        /**
         * Sets the Redirect URL.
         *
         * @param redirectUri The redirect URL.
         * @return This builder.
         */
        public Builder redirectUri(URI redirectUri) {
            this.redirectUri = redirectUri;
            return this;
        }

        /**
         * Sets the network application ID (this is different from the Vonage application ID).
         *
         * @param networkApplicationId The network application ID as a string.
         * @return This builder.
         */
        public Builder networkApplicationId(String networkApplicationId) {
            this.networkApplicationId = networkApplicationId;
            return this;
        }

        /**
         * Builds the NetworkApis object.
         *
         * @return A new Network APIs capability.
         */
        @Override
        public NetworkApis build() {
            return new NetworkApis(this);
        }
    }
}
