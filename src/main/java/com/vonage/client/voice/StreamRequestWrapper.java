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
package com.vonage.client.voice;

/**
 * The request object to stream an audio file.
 * <p>
 * Contains the {@code uuid} of the {@link Call} and the {@link StreamPayload} to be sent in the request.
 */
class StreamRequestWrapper {
    final StreamPayload payload;
    final String uuid;

    public StreamRequestWrapper(String uuid, StreamPayload payload) {
        this.payload = payload;
        this.uuid = uuid;
    }
}
