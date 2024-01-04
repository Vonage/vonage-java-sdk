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
package com.vonage.client.video;

import com.vonage.jwt.Jwt;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Defines values for the {@code options} parameter of the
 * {@link VideoClient#generateToken(String sessionId, TokenOptions tokenOptions)} method.
 */
public class TokenOptions {
    private final Role role;
    private final Duration ttl;
    private final String data;
    private final List<String> initialLayoutClassList;

    private TokenOptions(Builder builder) {
        role = Objects.requireNonNull(builder.role, "Role cannot be null.");

        ttl = Objects.requireNonNull(builder.ttl, "Time-to-Live cannot be null.");
        if (ttl.toMillis() > Duration.ofDays(30).toMillis()) {
            throw new IllegalArgumentException("Time-to-Live cannot exceed 30 days.");
        }

        // default value of null means to omit the key "connection_data" from the token
        if ((data = builder.data) != null && data.length() > 1000) {
            throw new IllegalArgumentException("Connection data cannot exceed 1000 characters.");
        }

        // default value of null means to omit the key "initialLayoutClassList" from the token
        initialLayoutClassList = builder.initialLayoutClassList;
    }

    protected void addClaims(Jwt.Builder jwt) {
        jwt.expiresAt(ZonedDateTime.now().plus(ttl));
        jwt.addClaim("role", role.toString());
        if (data != null) {
            jwt.addClaim("connection_data", data);
        }
        if (initialLayoutClassList != null) {
            jwt.addClaim("initial_layout_class_list", String.join(" ", initialLayoutClassList));
        }
    }

    /**
     * Returns the role assigned to the token.
     *
     * @return The role, as an enum.
     * @see Builder#role(Role)
    */
    public Role getRole() {
        return role;
    }

    /**
     * Returns the time-to-live for the JWT.
     *
     * @return The TTL duration.
     * @see Builder#expiryLength(Duration) 
    */
    public Duration getExpiryLength() {
        return ttl;
    }

    /**
     * Returns the connection metadata assigned to the token.
     *
     * @return The connection data, as a string.
     * @see Builder#data(String) 
     */
    public String getData() {
        return data;
    }

    /**
     * Returns the initial layout class list for streams published by the client using this token.
     *
     * @return The initial layout classes, as a List of strings.
     * @see Builder#initialLayoutClassList(List) 
    */
    public List<String> getInitialLayoutClassList() {
        return initialLayoutClassList;
    }

    /**
     * Entry point for constructing an instance of TokenOptions.
     *
     * @return A new Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Use this class to create a TokenOptions object.
     */
    public static class Builder {
        private Role role = Role.PUBLISHER;
        private Duration ttl = Duration.ofHours(24);
        private String data;
        private List<String> initialLayoutClassList;

        Builder() {}

        /**
         * Sets the role for the token. Each role defines a set of permissions granted to the token.
         *
         * @param role The role for the token. Valid values are defined in the Role class:
         * <ul>
         *   <li> {@code SUBSCRIBER} &mdash; A subscriber can only subscribe to streams.</li>
         *
         *   <li> {@code PUBLISHER} &mdash; A publisher can publish streams, subscribe to
         *      streams, and signal. (This is the default value if you do not specify a role.)</li>
         *
         *   <li> {@code MODERATOR} &mdash; In addition to the privileges granted to a
         *     publisher, a moderator can perform moderation functions, such as forcing clients to
         *     disconnect, to stop publishing streams, or to mute audio in published streams. See the
         *     <a href="https://tokbox.com/developer/guides/moderation/">Moderation developer guide</a>.
         *     </li>
         * </ul>
         */
        public Builder role(Role role) {
            this.role = role;
            return this;
        }

         /**
         * Sets the expiration time for the token.
         *
         * @param ttl The expiration length (time-to-live) The maximum duration is 30 days. Default is 24 hours.
         */
        public Builder expiryLength(Duration ttl) {
            this.ttl = ttl;
            return this;
        }

         /**
         * A string containing connection metadata describing the end-user. For example, you
         * can pass the user ID, name, or other data describing the end-user. The length of the
         * string is limited to 1000 characters. This data cannot be updated once it is set.
         *
         * @param data The connection metadata.
         */
        public Builder data(String data) throws IllegalArgumentException {
            this.data = data;
            return this;
        }

        /**
        * A List of class names (strings) to be used as the initial layout classes
        * for streams published by the client. Layout classes are used in customizing the layout
        * of videos in
        * <a href="https://tokbox.com/developer/guides/broadcast/live-streaming/">live streaming
        * broadcasts</a> and
        * <a href="https://tokbox.com/developer/guides/archiving/layout-control.html">composed
        * archives</a>. 
        *
        * @param initialLayoutClassList The initial layout class list.
        */
        public Builder initialLayoutClassList (List<String> initialLayoutClassList) {
            this.initialLayoutClassList = initialLayoutClassList;
            return this;
        }

        /**
         * Builds the TokenOptions object.
         *
         * @return A new TokenOptions instance with this builder's properties.
         */
        public TokenOptions build() {
            return new TokenOptions(this);
        }
    }
}
