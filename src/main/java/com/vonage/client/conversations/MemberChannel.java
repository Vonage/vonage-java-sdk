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
package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.common.ChannelType;
import com.vonage.client.users.channels.Channel;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * Contains the channel properties for {@link Member#getChannel()}.
 */
@JsonDeserialize(using = MemberChannel.Deserializer.class)
public class MemberChannel extends JsonableBaseObject {
	String id;
	ChannelType type;
	Channel from, to;
	Map<String, ?> headers;

	protected MemberChannel() {
	}

	/**
	 * Initialises a new instance of this class for sending as a request.
	 *
	 * @param builder The builder containing the required fields.
	 * @since 8.19.0
	 */
	MemberChannel(Builder builder) {
		id = builder.id;
		type = builder.type;
		from = builder.from;
		to = builder.to;
		headers = builder.headers;
	}

	/**
	 * ID field for this channel.
	 *
	 * @return The channel ID, or {@code null} if unknown / not applicable.
	 * @since 8.19.0
	 */
	@JsonProperty("id")
	public String getId() {
		return id;
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
	 * Additional headers.
	 *
	 * @return The custom headers, or {@code null} if unspecified.
	 * @since 8.19.0
	 */
	@JsonProperty("headers")
	public Map<String, ?> getHeaders() {
		return headers;
	}

	/**
	 * Entry point for constructing an instance of this class.
	 *
	 * @return A new Builder.
	 * @since 8.19.0
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder for constructing the {@code channel} field of an event.
	 *
	 * @since 8.19.0
	 */
	public static final class Builder {
		private String id;
		private ChannelType type;
		private Channel from, to;
		private Map<String, ?> headers;

		Builder() {}

		/**
		 * Set the channel type.
		 *
		 * @param type The channel type.
		 * @return This builder.
		 */
		public Builder type(ChannelType type) {
			this.type = type;
			return this;
		}

		/**
		 * Set the channel ID.
		 *
		 * @param id The channel ID.
		 * @return This builder.
		 */
		public Builder id(String id) {
			this.id = id;
			return this;
		}

		/**
		 * Set the sender channel.
		 *
		 * @param from The sender channel.
		 * @return This builder.
		 */
		public Builder from(Channel from) {
			this.from = from;
			return this;
		}

		/**
		 * Set the receiver channel.
		 *
		 * @param to The receiver channel.
		 * @return This builder.
		 */
		public Builder to(Channel to) {
			this.to = to;
			return this;
		}

		/**
		 * Set the custom headers.
		 *
		 * @param headers The custom headers.
		 * @return This builder.
		 */
		public Builder headers(Map<String, ?> headers) {
			this.headers = headers;
			return this;
		}

		/**
		 * Build the {@linkplain MemberChannel} object.
		 *
		 * @return An instance of MemberChannel, populated with all fields from this builder.
		 */
		public MemberChannel build() {
			return new MemberChannel(this);
		}
	}

	/**
	 * Custom deserialize method to handle polymorphic deserialization of the {@code from} and {@code to} fields.
	 */
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
			JsonNode rootNode = p.readValueAsTree(),
					typeNode = rootNode.get("type"),
					idNode = rootNode.get("id"),
					headersNode = rootNode.get("headers");
			if (typeNode != null) {
				mc.type = ChannelType.fromString(typeNode.asText());
			}
			if (idNode != null) {
				mc.id = idNode.asText();
			}
			if (headersNode != null) {
				mc.headers = new ObjectMapper().convertValue(headersNode, new TypeReference<Map<String, ?>>(){});
			}
			mc.from = inferConcreteChannel(rootNode.get("from"));
			mc.to = inferConcreteChannel(rootNode.get("to"));
			return mc;
		}

		private Channel inferConcreteChannel(JsonNode node) {
			if (node == null || !node.isObject()) return null;
			JsonNode typeNode = node.get("type");
			ChannelType fromType = typeNode != null ? ChannelType.fromString(typeNode.asText()) : mc.type;
			Class<? extends Channel> concreteClass = Channel.getConcreteClass(fromType);
			if (concreteClass == null) {
				throw new IllegalStateException("Unmapped class for type "+fromType);
			}
			return Jsonable.fromJson(node.toString(), concreteClass);
		}
	}
}
