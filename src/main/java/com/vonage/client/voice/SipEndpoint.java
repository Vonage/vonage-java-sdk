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
package com.vonage.client.voice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.users.channels.Sip;
import static com.vonage.client.voice.SipHeader.USER_TO_USER;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Endpoint for connecting to a SIP URI.
 */
public class SipEndpoint extends JsonableBaseObject implements Endpoint {
    private String uri;
    private Map<String, ?> headers;
    private Map<SipHeader, String> standardHeaders;

    protected SipEndpoint() {
    }

    /**
     * Creates a new SIP endpoint request.
     *
     * @param uri (REQUIRED) SIP URI to connect to.
     */
    public SipEndpoint(String uri) {
        this(uri, null, null);
    }

    /**
     * Creates a new SIP endpoint request.
     *
     * @param uri (REQUIRED) SIP URI to connect to.
     * @param userToUserHeader (OPTIONAL) The User-to-User header, as per RFC 7433.
     * @since 8.9.0
     */
    public SipEndpoint(String uri, String userToUserHeader) {
        this(uri, null, userToUserHeader);
    }

    /**
     * Creates a new SIP endpoint request.
     *
     * @param uri (REQUIRED) URI of the websocket to connect to.
     * @param headers (OPTIONAL) Custom headers to include.
     * @since 8.9.0
     */
    public SipEndpoint(String uri, Map<String, ?> headers) {
        this(uri, headers, null);
    }

    /**
     * Creates a new SIP endpoint request.
     *
     * @param uri (REQUIRED) SIP URI to connect to.
     * @param headers (OPTIONAL) Custom headers to include.
     * @param userToUserHeader (OPTIONAL) The User-to-User header, as per RFC 7433.
     * @since 8.9.0
     */
    public SipEndpoint(String uri, Map<String, ?> headers, String userToUserHeader) {
        this.uri = uri;
        this.headers = headers;
        if (userToUserHeader != null) {
            standardHeaders = Collections.singletonMap(USER_TO_USER, userToUserHeader);
        }
    }

    /**
     * SIP URI to connect to.
     *
     * @return The SIP URI as a String.
     */
    @JsonProperty("uri")
    public String getUri() {
        return uri;
    }

    @Override
    public String getType() {
        return EndpointType.SIP.toString();
    }

    /**
     * Defines custom headers to be sent as part of the SIP INVITE request.
     * All keys will be prepended with the {@code X-} prefix.
     *
     * @return The custom headers as a Map, or {@code null} if unspecified.
     * @since 8.9.0
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
    @JsonProperty("standard_headers")
    public Map<SipHeader, String> getStandardHeaders() {
        return standardHeaders;
    }

    @Override
    public String toLog() {
        return uri;
    }
}