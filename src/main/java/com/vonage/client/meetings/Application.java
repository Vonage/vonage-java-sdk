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
package com.vonage.client.meetings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Application extends JsonableBaseObject {
	private String accountId;
	private UUID applicationId, defaultThemeId;

	protected Application() {
	}

	/**
	 * ID of the application.
	 *
	 * @return The application ID.
	 */
	@JsonProperty("application_id")
	public UUID getApplicationId() {
		return applicationId;
	}

	/**
	 * ID of the account application.
	 *
	 * @return The account ID.
	 */
	@JsonProperty("account_id")
	public String getAccountId() {
		return accountId;
	}

	/**
	 * ID of the default theme.
	 *
	 * @return The application default theme ID.
	 */
	@JsonProperty("default_theme_id")
	public UUID getDefaultThemeId() {
		return defaultThemeId;
	}
	
	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static Application fromJson(String json) {
		return Jsonable.fromJson(json);
	}
}
