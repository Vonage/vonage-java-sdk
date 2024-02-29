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
package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.*;
import com.vonage.client.JsonableBaseObject;

/**
 * Represents a communication channel. Note that this implementation is different to
 * {@linkplain com.vonage.client.users.channels.Channel} due to its usage and serialisation semantics.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Channel extends JsonableBaseObject {
	@JsonIgnore private String numberOrId;
	private ChannelType type;

	protected Channel() {}

	/**
	 * Type of this communication channel. This may be unspecified depending on the usage context.
	 * 
	 * @return The channel type as an enum, or {@code null} if implicit / unknown.
	 */
	@JsonProperty("type")
	public ChannelType getType() {
		return type;
	}

	/**
	 * Gets the number (E.164), username or ID associated with this channel, depending on the type.
	 *
	 * @return The relevant identifier for this channel, or {@code null} if unknown / not applicable.
	 */
	@JsonIgnore
	public String getNumberOrId() {
		return numberOrId;
	}

	@JsonIgnore
	private boolean isNumber() {
		return !(isId() || isUser());
	}

	@JsonIgnore
	private boolean isId() {
		return type == ChannelType.MESSENGER;
	}

	@JsonIgnore
	private boolean isUser() {
		return type == ChannelType.APP;
	}

	@JsonGetter("number")
	private String getNumber() {
		return isNumber() ? numberOrId : null;
	}

	@JsonGetter("id")
	private String getId() {
		return isId() ? numberOrId : null;
	}

	@JsonGetter("user")
	private String getUser() {
		return isUser() ? numberOrId : null;
	}

	@JsonSetter("number")
	private void setNumber(String number) {
		this.numberOrId = number;
	}

	@JsonSetter("id")
	private void setId(String id) {
		this.numberOrId = id;
	}

	@JsonSetter("user")
	private void setUser(String user) {
		this.numberOrId = user;
	}
}
