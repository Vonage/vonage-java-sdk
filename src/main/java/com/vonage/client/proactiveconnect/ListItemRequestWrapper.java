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
package com.vonage.client.proactiveconnect;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.JsonableBaseObject;
import java.util.Map;
import java.util.UUID;

final class ListItemRequestWrapper extends JsonableBaseObject {
	@JsonIgnore final UUID listId, itemId;
	@JsonProperty("data") final Map<String, ?> data;

	ListItemRequestWrapper(UUID listId, UUID itemId, Map<String, ?> data) {
		this.listId = listId;
		this.itemId = itemId;
		this.data = data;
	}

	@Override
	protected ObjectMapper createJsonObjectMapper() {
		return super.createJsonObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS);
	}
}
