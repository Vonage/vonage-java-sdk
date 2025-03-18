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
package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Main body container for a {@link MemberInvitedEvent}.
 *
 * @since 8.20.0
 */
public class MemberInvitedEventBody extends Member {
    private String cname, sdp;

    MemberInvitedEventBody() {}

    /**
     * Gets the {@code cname} field.
     *
     * @return The cname, or {@code null} if absent.
     */
    @JsonProperty("cname")
    public String getCname() {
        return cname;
    }

    /**
     * Gets the {@code sdp} field.
     *
     * @return The SDP, or {@code null} if absent.
     */
    @JsonProperty("sdp")
    public String getSdp() {
        return sdp;
    }
}
