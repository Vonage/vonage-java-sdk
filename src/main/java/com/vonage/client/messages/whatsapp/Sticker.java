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
package com.vonage.client.messages.whatsapp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.messages.internal.MessagePayload;
import java.net.URI;
import java.util.UUID;

/**
 * @since 7.2.0
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public final class Sticker {
	private final URI url;
	private final UUID id;

	Sticker(String url, String id) {
		boolean noId = id == null, noUrl = url == null;
		if ((noId && noUrl) || (!noId && !noUrl)) {
			throw new IllegalStateException("Must specify either an ID or URL for sticker, but not both.");
		}
		if (noId) {
			MessagePayload.validateExtension(url, "webp");
			this.url = URI.create(url);
			this.id = null;
		}
		else {
			this.id = UUID.fromString(id);
			this.url = null;
		}
	}

	@JsonProperty("url")
	public URI getUrl() {
		return url;
	}

	@JsonProperty("id")
	public UUID getId() {
		return id;
	}
}
