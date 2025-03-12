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
package com.vonage.client.messages.whatsapp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

import java.util.Objects;

public final class Whatsapp extends JsonableBaseObject {
	private Policy policy;
	private Locale locale;

	Whatsapp() {}

	public Whatsapp(Policy policy, Locale locale) {
		this.policy = policy;
		this.locale = Objects.requireNonNull(locale, "Locale is required");
	}

	@JsonProperty("policy")
	public Policy getPolicy() {
		return policy;
	}

	@JsonProperty("locale")
	public Locale getLocale() {
		return locale;
	}
}
