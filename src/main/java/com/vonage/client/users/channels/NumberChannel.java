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
package com.vonage.client.users.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.common.E164;
import java.util.Objects;

/**
 * Base class for channels with an E.164 phone number.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
abstract class NumberChannel extends Channel {
	private String number;

	NumberChannel() {}

	/**
	 * Creates a new channel with the specified number.
	 *
	 * @param number The phone number in E.164 format.
	 */
	NumberChannel(String number) {
		this.number = new E164(number).toString();
	}

	/**
	 * Phone number for this channel.
	 *
	 * @return The number in E.164 format.
	 */
	@JsonProperty("number")
	public String getNumber() {
		return number;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		NumberChannel that = (NumberChannel) o;
		return Objects.equals(number, that.number);
	}

	@Override
	public int hashCode() {
		return Objects.hash(number);
	}
}
