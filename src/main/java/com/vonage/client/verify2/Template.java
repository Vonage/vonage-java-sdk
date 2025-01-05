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
package com.vonage.client.verify2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Represents a custom template.
 *
 * @since 8.13.0
 */
public final class Template extends JsonableBaseObject {
	private static final Pattern NAME_REGEX = Pattern.compile("^[a-zA-Z0-9_\\-]+$");
	private boolean isUpdateRequest;
	@JsonProperty("name") private String name;
	@JsonProperty("is_default") private Boolean isDefault;
	@JsonProperty("template_id") UUID id;

	private Template() {}

	Template(String name, Boolean isDefault, UUID id) {
		if ((this.name = name) != null && !NAME_REGEX.matcher(name).matches()) {
			throw new IllegalArgumentException("Invalid template name. Must match pattern: "+NAME_REGEX.pattern());
		}
		if ((this.isDefault = isDefault) == null && name == null) {
			throw new IllegalArgumentException("Must provide at least one field to update.");
		}
		this.id = id;
		isUpdateRequest = true;
	}

	/**
	 * Reference name for the template.
	 *
	 * @return The template name.
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	/**
	 * Unique template identifier.
	 *
	 * @return The template ID, or {@code null} if this is a request object.
	 */
	@JsonProperty("template_id")
	public UUID getId() {
		return isUpdateRequest ? null : id;
	}

	/**
	 * Whether this is the default template.
	 *
	 * @return {@code true} if this is the default template, or {@code null} if this is a request object.
	 */
	@JsonProperty("is_default")
	public Boolean isDefault() {
		return isDefault;
	}

	@Override
	public void updateFromJson(String json) {
		isUpdateRequest = false;
		super.updateFromJson(json);
	}
}
