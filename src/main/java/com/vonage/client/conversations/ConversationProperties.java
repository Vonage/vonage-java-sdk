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
	private final Integer ttl;
	private final String type;
	private final String customSortKey;
	private final Map<String, Object> customData;

	ConversationProperties(Builder builder) {
		ttl = builder.ttl;
		type = builder.type;
		customSortKey = builder.customSortKey;
		customData = builder.customData;
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
	

	/**
	 * Entry point for constructing an instance of this class.
	 * 
	 * @return A new Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private Integer ttl;
		private String type;
		private String customSortKey;
		private Map<String, Object> customData;
	
		Builder() {}
	
		/**
		 * Number of seconds after which an empty conversation is deleted.
		 *
		 * @param ttl The empty time-to-live in seconds, or {@code null} if unspecified.
		 *
		 * @return This builder.
		 */
		public Builder ttl(Integer ttl) {
			this.ttl = ttl;
			return this;
		}

		/**
		 * Conversation type.
		 *
		 * @param type The conversation type as a string, or {@code null} if unknown.
		 *
		 * @return This builder.
		 */
		public Builder type(String type) {
			this.type = type;
			return this;
		}

		/**
		 * Custom sort key.
		 *
		 * @param customSortKey The custom sort key as a string, or {@code null} if unspecified.
		 *
		 * @return This builder.
		 */
		public Builder customSortKey(String customSortKey) {
			this.customSortKey = customSortKey;
			return this;
		}

		/**
		 * Custom key-value pairs to be included with conversation data.
		 *
		 * @param customData The custom properties as a Map, or {@code null} if unspecified.
		 *
		 * @return This builder.
		 */
		public Builder customData(Map<String, Object> customData) {
			this.customData = customData;
			return this;
		}

	
		/**
		 * Builds the {@linkplain ConversationProperties}.
		 *
		 * @return An instance of ConversationProperties, populated with all fields from this builder.
		 */
		public ConversationProperties build() {
			return new ConversationProperties(this);
		}
	}
}
