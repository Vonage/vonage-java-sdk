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
package com.vonage.client.voice.ncco;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.voice.EndpointType;
import com.vonage.client.voice.SipHeader;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a SIP endpoint used in a {@link ConnectAction}. See
 * <a href=https://developer.vonage.com/voice/voice-api/ncco-reference#sip-endpoint>the documentation</a>
 * for an example.
 */
public class SipEndpoint extends JsonableBaseObject implements Endpoint {
    private final URI uri;
    private final String domain, user;
    private final Map<String, ?> headers;
    private final Map<SipHeader, String> standardHeaders;

    private SipEndpoint(Builder builder) {
        uri = builder.uri;
        domain = builder.domain;
        user = builder.user;
        if (uri == null && domain == null) {
            throw new IllegalStateException("Domain or SIP URI must be specified.");
        }
        if (uri != null && (domain != null || user != null)) {
            throw new IllegalStateException("Either SIP URI or domain/user must be specified, not both.");
        }
        headers = builder.headers;
        standardHeaders = builder.standardHeaders;
    }

    @Override
    public String getType() {
        return EndpointType.SIP.toString();
    }

    /**
     * URI of the SIP endpoint.
     *
     * @return The URI.
     */
    @JsonProperty("uri")
    public URI getUri() {
        return uri;
    }

    /**
     * Identifier for a trunk created using the dashboard. The URIs provisioned in the trunk will be used along
     * the user property to create the full SIP URI.
     *
     * @return The domain name, or {@code null} if not set.
     * @since 8.17.0
     */
    @JsonProperty("domain")
    public String getDomain() {
        return domain;
    }

    /**
     * User component of the URI. It will be used along the domain property to create the full SIP URI.
     *
     * @return The domain user, or {@code null} if unset or not applicable.
     * @since 8.17.0
     */
    @JsonProperty("user")
    public String getUser() {
        return user;
    }

    /**
     * Defines custom headers to be sent as part of the SIP INVITE request.
     * All keys will be prepended with the {@code X-} prefix.
     *
     * @return The custom headers as a Map, or {@code null} if unspecified.
     */
    @JsonProperty("headers")
    public Map<String, ?> getHeaders() {
        return headers;
    }

    /**
     * Headers that are RFC standards, i.e. not prepended with {@code X-}.
     *
     * @return The standard headers, or {@code null} if unspecified.
     * @since 8.9.0
     */
    @JsonProperty("standardHeaders")
    public Map<SipHeader, String> getStandardHeaders() {
        return standardHeaders;
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @param uri The SIP URI as a string.
     *
     * @return A new Builder.
     */
    public static Builder builder(String uri) {
        return new Builder().uri(uri);
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @param uri The SIP URI.
     *
     * @return A new Builder.
     */
    public static Builder builder(URI uri) {
        return new Builder().uri(uri);
    }

    /**
     * Entry point for constructing an instance of this class.
     * You must specify either the URI or domain, but not both.
     *
     * @return A new Builder.
     * @since 8.17.0
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for a SIP endpoint.
     */
    public static class Builder {
        private URI uri;
        private String domain, user;
        private Map<String, ?> headers;
        private Map<SipHeader, String> standardHeaders;

        private Builder() {}

        private Builder addStandardHeader(SipHeader key, String value) {
            if (standardHeaders == null) {
                standardHeaders = new LinkedHashMap<>(2);
            }
            standardHeaders.put(key, value);
            return this;
        }

        /**
         * Domain username. This will be used along the {@linkplain #domain(String)} property to create the full
         * SIP URI. If you set this property, you must also set the {@linkplain #domain(String)} property and
         * leave the {@linkplain #uri(URI)} property unset.
         *
         * @param user The user component of the SIP URI.
         *
         * @return This builder,
         * @since 8.17.0
         */
        public Builder user(String user) {
            this.user = user;
            return this;
        }

        /**
         * Identifier for a trunk created using the dashboard. The URIs provisioned in the trunk will be used
         * along the user property to create the full SIP URI. If you set this property, you must leave the
         * {@linkplain #uri(URI)} property unset.
         *
         * @param domain The SIP domain name to use.
         *
         * @return This builder,
         * @since 8.17.0
         */
        public Builder domain(String domain) {
            this.domain = domain;
            return this;
        }

        /**
         * Sets the URI.
         *
         * @param uri The URI.
         * @return This builder.
         */
        public Builder uri(URI uri) {
            this.uri = uri;
            return this;
        }

        /**
         * Sets the URI.
         *
         * @param uri The URI as a string.
         * @return This builder.
         */
        public Builder uri(String uri) {
            return uri(URI.create(uri));
        }

        /**
         * Sets the custom headers, which will be prepended with {@code X-} by Vonage.
         *
         * @param headers The custom headers as a Map.
         * @return This builder.
         */
        public Builder headers(Map<String, ?> headers) {
            this.headers = headers;
            return this;
        }

        /**
         * (OPTIONAL) The User-to-User header, as per RFC 7433.
         *
         * @param value Value of the {@code User-to-User} header as string.
         *
         * @return This builder.
         * @since 8.9.0
         */
        public Builder userToUserHeader(String value) {
            return addStandardHeader(SipHeader.USER_TO_USER, value);
        }

        /**
         * Builds the SIP endpoint.
         *
         * @return A new SipEndpoint with this builder's properties.
         */
        public SipEndpoint build() {
            return new SipEndpoint(this);
        }
    }
}
