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
package com.vonage.client.video;

/**
 * Defines values for the mediaMode parameter of the
 * {@link CreateSessionRequest.Builder#mediaMode(MediaMode mediaMode)} method.
 */
public enum MediaMode {
    /**
     * The session will transmit streams using the Media Server. Value is "disabled".
     */
    ROUTED ("disabled"),
    /**
     * The session will attempt to transmit streams directly between clients. If two clients
     * cannot send and receive each other's streams, due to firewalls on the clients' networks,
     * their streams will be relayed using the TURN Server. Value is "enabled".
     */
    RELAYED ("enabled");

    private final String serialized;

    MediaMode(String s) {
        serialized = s;
    }

    @Override
    public String toString() {
        return serialized;
    }
}
