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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

/**
 * Represents the {@code _links} section in a paginated response.
 *
 * @deprecated Will be replaced by {@link com.vonage.client.common.HalLinks} in a future release.
 */
@Deprecated
public class PageLinks extends JsonableBaseObject {
    private PageLink self, next, prev, first, last;

    @JsonProperty("self")
    public PageLink getSelf() {
        return self;
    }

    @JsonProperty("next")
    public PageLink getNext() {
        return next;
    }

    @JsonProperty("prev")
    public PageLink getPrev() {
        return prev;
    }

    @JsonProperty("first")
    public PageLink getFirst() {
        return first;
    }

    @JsonProperty("last")
    public PageLink getLast() {
        return last;
    }
}