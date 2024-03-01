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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.common.ChannelType;
import com.vonage.client.users.channels.Channel;
import java.io.IOException;
import java.util.Objects;

/**
 * Contains the channel properties for {@link Member#getChannel()}.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = MemberChannel.Deserializer.class)
public class MemberChannel extends JsonableBaseObject {
	private ChannelType type;
	private Channel from, to;

	protected MemberChannel() {
	}

	/**
	 * Main channel type.
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

	static class Deserializer extends StdDeserializer<MemberChannel> {
		private MemberChannel mc;

		protected Deserializer() {
			super(MemberChannel.class);
		}

		@Override
		public MemberChannel deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
			return deserialize(p, ctxt, new MemberChannel());
		}

		@Override
		public MemberChannel deserialize(JsonParser p, DeserializationContext ctxt, MemberChannel intoValue) throws IOException {
			mc = Objects.requireNonNull(intoValue);
			JsonNode rootNode = p.readValueAsTree();
			mc.type = ChannelType.fromString(rootNode.get("type").asText());
			mc.from = inferConcreteChannel(rootNode.get("from"));
			mc.to = inferConcreteChannel(rootNode.get("to"));
			return mc;
		}

		private Channel inferConcreteChannel(JsonNode node) {
			if (node == null || !node.isObject()) return null;
			JsonNode nodeType = node.get("type");
			ChannelType fromType = nodeType != null ? ChannelType.fromString(nodeType.asText()) : mc.type;
			Class<? extends Channel> concreteClass = Channel.getConcreteClass(fromType);
			if (concreteClass == null) {
				throw new IllegalStateException("Unmapped class for type "+fromType);
			}
			return Jsonable.fromJson(node.toString(), concreteClass);
		}
	}
}
