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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.vonage.client.JsonableBaseObject;

/**
 * Base class for datasource types used in Proactive Connect lists.
 */
@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		property = "type"
)
@JsonSubTypes({
		@JsonSubTypes.Type(value = ManualDatasource.class, name = "manual"),
		@JsonSubTypes.Type(value = SalesforceDatasource.class, name = "salesforce")
})
public abstract class Datasource extends JsonableBaseObject {
	protected final String type;

	protected Datasource(String type) {
		this.type = type;
	}

	/**
	 * Name of the datasource.
	 *
	 * @return The datasource type as a string.
	 */
	@JsonProperty("type")
	public String getType() {
		return type;
	}
}
