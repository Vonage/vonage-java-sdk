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
package com.vonage.client.voice;

/**
 * Response from successfully streaming an audio file or stopping a stream to an active {@link Call}.
 * This is returned by {@link VoiceClient#startStream(String, String)} or {@link VoiceClient#stopStream(String)}.
 */
public final class StreamResponse extends AbstractAudioResponse {

    /**
     * Constructor used reflectively by Jackson for instantiation.
     */
    private StreamResponse() {}
}
