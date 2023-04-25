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
package com.vonage.client.proactiveconnect;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ListAttribute {
	private final String alias, name;
	private final Boolean key;

	protected ListAttribute(String alias, String name, Boolean key) {
		this.alias = alias;
		this.name = name;
		this.key = key;
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
	 * Set to true if this attribute should be used to correlate between 2 or more lists.
	 *
	 * @return Whether this attribute is used as a key, or {@code null} if unknown.
	 */
	@JsonProperty("key")
	public Boolean getKey() {
		return key;
	}
}
