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
package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.util.Map;

/**
 * Additional properties for a {@linkplain Conversation}.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConversationProperties extends JsonableBaseObject {
	private Integer ttl;
	private String type, customSortKey;
	private Map<String, Object> customData;

	protected ConversationProperties() {}

	protected ConversationProperties(Integer ttl, String type, String customSortKey, Map<String, Object> customData) {
		this.ttl = ttl;
		this.type = type;
		this.customSortKey = customSortKey;
		this.customData = customData;
	}

	/**
	 * Number of seconds after which an empty conversation is deleted.
	 * 
	 * @return The empty time-to-live in seconds, or {@code null} if unspecified.
	 */
	@JsonProperty("ttl")
	public Integer getTtl() {
		return ttl;
	}

	/**
	 * Conversation type.
	 * 
	 * @return The conversation type as a string, or {@code null} if unknown.
	 */
	@JsonProperty("type")
	public String getType() {
		return type;
	}

	/**
	 * Custom sort key.
	 * 
	 * @return The custom sort key as a string, or {@code null} if unspecified.
	 */
	@JsonProperty("custom_sort_key")
	public String getCustomSortKey() {
		return customSortKey;
	}

	/**
	 * Custom key-value pairs to be included with conversation data.
	 * 
	 * @return The custom properties as a Map, or {@code null} if unspecified.
	 */
	@JsonProperty("custom_data")
	public Map<String, Object> getCustomData() {
		return customData;
	}
}
