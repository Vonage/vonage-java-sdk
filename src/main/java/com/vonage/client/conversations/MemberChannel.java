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
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;

/**
 * Contains the channel properties for {@link Member#getChannel()}.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberChannel extends JsonableBaseObject {
	private ChannelType type;
	private Channel from, to;

	protected MemberChannel() {
	}

	/**
	 * Channel type.
	 * 
	 * @return The channel type as an enum, or {@code null} if unspecified.
	 */
	@JsonProperty("type")
	public ChannelType getType() {
		return type;
	}

	/**
	 * Sender channel.
	 * 
	 * @return The from channel, or {@code null} if unspecified.
	 */
	@JsonProperty("from")
	public Channel getFrom() {
		return from;
	}

	/**
	 * Receiver channel.
	 * 
	 * @return The to channel, or {@code null} if unspecified.
	 */
	@JsonProperty("to")
	public Channel getTo() {
		return to;
	}
	
	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static MemberChannel fromJson(String json) {
		return Jsonable.fromJson(json);
	}
}
