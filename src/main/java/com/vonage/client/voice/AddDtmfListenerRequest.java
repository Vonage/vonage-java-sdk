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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;

/**
 * Request wrapper for {@linkplain VoiceClient#addDtmfListener(String, String)}.
 *
 * @since 8.12.0
 */
class AddDtmfListenerRequest extends JsonableBaseObject {
    @JsonIgnore final String uuid;
    @JsonProperty("event_url") final Collection<URI> eventUrl;

    /**
     * Creates a new DTMF listener request.
     *
     * @param uuid ID of the call to add the DTMF listener to.
     * @param eventUrl URL to send the DTMF events to.
     */
    public AddDtmfListenerRequest(String uuid, URI eventUrl) {
        this.uuid = uuid;
        this.eventUrl = Collections.singletonList(eventUrl);
    }
}
