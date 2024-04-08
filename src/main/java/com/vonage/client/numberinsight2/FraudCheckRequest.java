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
package com.vonage.client.numberinsight2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.common.E164;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

class FraudCheckRequest extends JsonableBaseObject {
	private final String type, phone;
	private final Set<Insight> insights;

	public FraudCheckRequest(String phoneNumber, Insight insight, Insight... others) {
		type = "phone";
		phone = new E164(phoneNumber).toString();
		insights = EnumSet.of(Objects.requireNonNull(insight, "Insight type is required."), others);
	}

	/**
	 * Accepted value is "phone" when a phone number is provided.
	 * 
	 * @return The request type as a string.
	 */
	@JsonProperty("type")
	public String getType() {
		return type;
	}

	/**
	 * A single phone number that you need insight about in the E.164 format. Don't use a leading + or 00.
	 * 
	 * @return The phone number in E.164 format.
	 */
	@JsonProperty("phone")
	public String getPhone() {
		return phone;
	}

	/**
	 * The required insights. Must provide at least one.
	 * 
	 * @return The requested insights.
	 */
	@JsonProperty("insights")
	public Set<Insight> getInsights() {
		return insights;
	}
}
