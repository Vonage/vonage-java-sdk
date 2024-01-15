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
package com.vonage.client.messages.messenger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public final class Messenger extends JsonableBaseObject {
	private final Category category;
	private final Tag tag;

	private Messenger(Category category, Tag tag) {
		this.category = category;
		this.tag = tag;
	}

	static Messenger construct(Category category, Tag tag) {
		if (category == null && tag == null) {
			return null;
		}
		if (category == Category.MESSAGE_TAG && tag == null) {
			throw new IllegalArgumentException("Tag is mandatory for category "+category);
		}
		return new Messenger(category, tag);
	}

	@JsonProperty("category")
	public Category getCategory() {
		return category;
	}

	@JsonProperty("tag")
	public Tag getTag() {
		return tag;
	}
}
