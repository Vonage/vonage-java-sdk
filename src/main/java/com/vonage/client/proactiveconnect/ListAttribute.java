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
package com.vonage.client.proactiveconnect;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Proactive Connect list's {@code attributes} property.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListAttribute {
	private String alias, name;
	private Boolean key;

	protected ListAttribute() {
	}

	ListAttribute(Builder builder) {
		alias = builder.alias;
		name = builder.name;
		key = builder.key;
	}

	/**
	 * Alternative name to use for this attribute. Use when you wish to correlate between 2 or more lists
	 * that are using different attribute names for the same semantic data.
	 *
	 * @return The attribute alias or {@code null} if unset.
	 */
	@JsonProperty("alias")
	public String getAlias() {
		return alias;
	}

	/**
	 * List attribute name.
	 *
	 * @return The attribute name or {@code null} if unset.
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	/**
	 * Will be {@code true} if this attribute should be used to correlate between 2 or more lists.
	 *
	 * @return Whether this attribute is used as a key, or {@code null} if unknown.
	 */
	@JsonProperty("key")
	public Boolean getKey() {
		return key;
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
		protected String name, alias;
		protected Boolean key;

		Builder() {}

		/**
		 * Sets the list attribute name.
		 *
		 * @param name The name.
		 *
		 * @return This builder.
		 */
		public Builder name(String name) {
			this.name = name;
			return this;
		}

		/**
		 * Sets the list attribute alias.
		 *
		 * @param alias The alias.
		 *
		 * @return This builder.
		 */
		public Builder alias(String alias) {
			this.alias = alias;
			return this;
		}

		/**
		 * Set to {@code true} if this attribute should be used to correlate between 2 or more lists.
		 *
		 * @param key Whether this attribute is used as a key.
		 *
		 * @return This builder.
		 */
		public Builder key(boolean key) {
			this.key = key;
			return this;
		}

		/**
		 * Builds the list attribute object.
		 *
		 * @return A new ListAttribute with this builder's properties.
		 */
		public ListAttribute build() {
			return new ListAttribute(this);
		}
	}
}
