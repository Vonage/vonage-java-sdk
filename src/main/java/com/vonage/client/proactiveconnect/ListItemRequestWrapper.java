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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import java.util.Map;

class ListItemRequestWrapper {
	final String listId, itemId;
	final Map<String, ?> data;

	ListItemRequestWrapper(String listId, String itemId, Map<String, ?> data) {
		this.listId = listId;
		this.itemId = itemId;
		this.data = data;
	}

	String toJson() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return "{\"data\":" + mapper.writeValueAsString(data) + "}";
		}
		catch (JsonProcessingException jpe) {
			throw new VonageUnexpectedException("Failed to produce JSON from "+data, jpe);
		}
	}
}
