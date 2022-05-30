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
package com.vonage.client.messages.messenger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public final class Messenger {
	private final MessageCategory category;
	private final MessageTag tag;

	private Messenger(MessageCategory category, MessageTag tag) {
		this.category = category;
		this.tag = tag;
	}

	static Messenger construct(MessageCategory category, MessageTag tag) {
		if (category == null && tag == null) {
			return null;
		}
		if (category == MessageCategory.MESSAGE_TAG && tag == null) {
			throw new IllegalArgumentException("Tag is mandatory for category "+category);
		}
		return new Messenger(category, tag);
	}

	@JsonProperty("category")
	public MessageCategory getCategory() {
		return category;
	}

	@JsonProperty("tag")
	public MessageTag getTag() {
		return tag;
	}
}
