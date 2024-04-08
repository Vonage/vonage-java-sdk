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
package com.vonage.client.messages.viber;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.net.URI;

/**
 * @since 7.2.0
 */
public final class Action extends JsonableBaseObject {
	private final URI url;
	private final String text;

	private Action(String url, String text) {
		this.url = URI.create(url);
		this.text = text;
	}

	static Action construct(String url, String text) {
		boolean noUrl = url == null || url.isEmpty(), noText = text == null || text.isEmpty();
		if (noUrl && noText) {
			return null;
		}
		else if (noUrl || noText) {
			throw new IllegalStateException("Both URL and text are required for Action.");
		}
		else {
			return new Action(url, text);
		}
	}

	@JsonProperty("url")
	public URI getUrl() {
		return url;
	}

	@JsonProperty("text")
	public String getText() {
		return text;
	}
}
