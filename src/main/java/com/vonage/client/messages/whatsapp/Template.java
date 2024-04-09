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
package com.vonage.client.messages.whatsapp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.util.List;
import java.util.Objects;

public final class Template extends JsonableBaseObject {
	private final String name;
	private final List<String> parameters;

	Template(String name, List<String> parameters) {
		this.name = Objects.requireNonNull(name, "Name cannot be null");
		this.parameters = parameters;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("parameters")
	public List<String> getParameters() {
		return parameters;
	}
}
