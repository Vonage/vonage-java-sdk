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
package com.vonage.client.messages.viber;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public final class ViberService {
	private final Category category;
	private final Integer ttl, duration, fileSize;
	private final String type;
	private final Action action;

	private ViberService(Category category, Integer ttl, String type, Action action, Integer duration, Integer fileSize) {
		this.category = category;
		this.ttl = ttl;
		this.type = type;
		this.action = action;
		this.duration = duration;
		this.fileSize = fileSize;
	}

	static ViberService construct(Category category, Integer ttl, String type, Action action,  Integer duration, Integer fileSize) {
		if (category == null && ttl == null && type == null && action == null && duration == null && fileSize == null) {
			return null;
		}
		if (ttl != null && (ttl < 30 || ttl > 259200)) {
			throw new IllegalArgumentException("Time-to-live (ttl) must be between 30 and 259200 seconds");
		}
		if (duration != null && (duration < 1 || duration > 600)) {
			throw new IllegalArgumentException("Duration must be between 1 and 600 seconds.");
		}
		if (fileSize != null && (fileSize < 1 || fileSize > 200)) {
			throw new IllegalArgumentException("File size must be between 1 and 200 MB.");
		}
		return new ViberService(category, ttl, type, action, duration, fileSize);
	}

	@JsonProperty("category")
	public Category getCategory() {
		return category;
	}

	@JsonProperty("ttl")
	public Integer getTtl() {
		return ttl;
	}

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	@JsonProperty("action")
	public Action getAction() {
		return action;
	}

	@JsonProperty("duration")
	public Integer getDuration() {
		return duration;
	}

	@JsonProperty("file_size")
	public Integer getFileSize() {
		return fileSize;
	}
}
