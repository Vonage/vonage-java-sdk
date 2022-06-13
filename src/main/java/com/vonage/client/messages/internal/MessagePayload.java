/*
 *   Copyright 2022 Vonage
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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;

/**
 * Container object for audio, video, image and file message payload contents.
 * This class also validates the fields.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class MessagePayload {
	protected URI url;
	protected String caption;

	public MessagePayload(String url) {
		this.url = URI.create(url);
	}

	public MessagePayload(String url, String caption) {
		this(url);
		if ((this.caption = caption) != null && caption.isEmpty()) {
			throw new IllegalArgumentException("Caption cannot be blank");
		}
	}

	@JsonProperty("url")
	public String getUrl() {
		return url.toString();
	}

	@JsonProperty("caption")
	public String getCaption() {
		return caption;
	}

	public void validateUrlExtension(String... allowed) {
		String path = url.getPath();
		int lastDot = path.lastIndexOf('.');
		if (lastDot < 1) return;
		String ext = path.substring(lastDot+1);
		Collection<String> extensions = Arrays.asList(allowed);
		if (!extensions.contains(ext)) {
			throw new IllegalArgumentException("Invalid extension: '"+ext+"'. Should be one of "+extensions);
		}
	}

	public void validateCaptionLength(int max) {
		if (caption == null) return;
		if (caption.length() > max) {
			throw new IllegalArgumentException("Caption must be less than "+max+" characters");
		}
	}

	public void validateUrlLength(int min, int max) {
		if (url == null) return;
		int length = getUrl().length();
		if (length < min) {
			throw new IllegalArgumentException("URL must be longer than "+min+" characters");
		}
		if (length > max) {
			throw new IllegalArgumentException("URL must be less than "+max+" characters");
		}
	}
}
