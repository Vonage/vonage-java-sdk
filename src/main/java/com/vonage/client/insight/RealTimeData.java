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
package com.vonage.client.insight;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.io.IOException;

/**
 * Real time data about the number.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RealTimeData {

	static class ActiveStatusDeserializer extends JsonDeserializer<Boolean> {
		@Override
		public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
			String text = p.getText();
			if (text == null) return null;
			switch (text.toLowerCase()) {
				default:
					return null;
				case "true":
				case "active":
					return true;
				case "false":
				case "inactive":
					return false;
			}
		}
	}

	@JsonDeserialize(using = ActiveStatusDeserializer.class)
	protected Boolean activeStatus;
	protected String handsetStatus;

	/**
	 * @return Whether the end-user's phone number is active within an operator's network.
	 * Note that this could be {@code null}.
	 */
	@JsonProperty("active_status")
	public Boolean getActiveStatus() {
		return activeStatus;
	}

	/**
	 * @return Whether the end-user's handset is turned on or off.
	 */
	@JsonProperty("handset_status")
	public String getHandsetStatus() {
		return handsetStatus;
	}
}
