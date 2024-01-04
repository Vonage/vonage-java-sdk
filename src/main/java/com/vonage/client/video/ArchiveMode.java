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

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Defines values for the archiveMode parameter of the
 * {@link CreateSessionRequest.Builder#archiveMode(ArchiveMode archiveMode)} method.
 */
public enum ArchiveMode {
    /**
     * The session is not archived automatically. To archive the session, you can call the
     * {@link VideoClient#createArchive(Archive)} method.
     */
    MANUAL,

    /**
     * The session is archived automatically (as soon as there are clients connected
     * to the session).
     */
    ALWAYS;

    @JsonValue
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
