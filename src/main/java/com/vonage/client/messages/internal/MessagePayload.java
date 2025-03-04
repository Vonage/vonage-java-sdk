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
package com.vonage.client.messages.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.net.URI;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Container object for audio, video, image and file message payload contents.
 * This class also validates the fields.
 */
public class MessagePayload extends JsonableBaseObject {
	protected URI url;
	protected String caption, name;

	public MessagePayload(String url) {
		if (Objects.requireNonNull(url, "URL is required.").trim().isEmpty()) {
			throw new IllegalArgumentException("URL cannot be blank.");
		}
		this.url = URI.create(url);
	}

	public MessagePayload(String url, String caption) {
		this(url);
		if ((this.caption = caption) != null && caption.isEmpty()) {
			throw new IllegalArgumentException("Caption cannot be blank.");
		}
	}

	public MessagePayload(String url, String caption, String name) {
		this(url, caption);
		this.name = name;
	}

	@JsonProperty("url")
	public URI getUrl() {
		return url;
	}

	@JsonProperty("caption")
	public String getCaption() {
		return caption;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	public static void validateExtension(String path, String... allowed) {
		int lastDot = path.lastIndexOf('.');
		if (lastDot < 1) return;
		String ext = path.substring(lastDot+1);
		Collection<String> extensions = Stream.of(allowed)
				.map(s -> s.startsWith(".") ? s.substring(1) : s)
				.collect(Collectors.toSet());
		if (!extensions.contains(ext)) {
			throw new IllegalArgumentException("Invalid extension: '"+ext+"'. Should be one of "+extensions+'.');
		}
	}

	public void validateUrlExtension(String... allowed) {
		validateExtension(url.getPath(), allowed);
	}

	public void validateCaptionLength(int max) {
		if (caption == null) return;
		if (caption.length() > max) {
			throw new IllegalArgumentException("Caption must be less than "+max+" characters.");
		}
	}

	public void validateUrlLength(int min, int max) {
		int length = getUrl().toString().length();
		if (length < min) {
			throw new IllegalArgumentException("URL must be longer than "+min+" characters.");
		}
		if (length > max) {
			throw new IllegalArgumentException("URL must be less than "+max+" characters.");
		}
	}
}
