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
package com.vonage.client.messages;

import com.fasterxml.jackson.annotation.*;
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.messages.whatsapp.ConversationType;
import java.net.URI;
import java.time.Instant;
import java.util.Currency;
import java.util.Map;
import java.util.UUID;

/**
 * This class represents the data of a callback which is invoked after a message has been sent.
 * It provides metadata about the message, such as its status, how much it cost, when it was sent,
 * what service (channel) it was sent via, sender and recipient, message response ID, client reference etc.
 */
public class MessageStatus extends JsonableBaseObject {

	public enum Status {
		SUBMITTED,
		DELIVERED,
		REJECTED,
		UNDELIVERABLE,
		READ;

		@JsonCreator
		public static Status fromString(String value) {
			if (value == null) return null;
			return Status.valueOf(value.toUpperCase());
		}

		@JsonValue
		@Override
		public String toString() {
			return name().toLowerCase();
		}
	}

	/**
	 * Describes the error that was encountered when sending the message.
	 */
		public static final class Error extends JsonableBaseObject {
		@JsonProperty("type") URI type;
		@JsonProperty("title") String title;
		@JsonProperty("detail") String detail;
		@JsonProperty("instance") String instance;

		/**
		 * The type of error encountered. Follow this URL for more details.
		 *
		 * @return The error type as a URI.
		 */
		public URI getType() {
			return type;
		}

		/**
		 * The error code encountered when sending the message. See
		 * <a href="https://developer.nexmo.com/api-errors/messages-olympus#_ga=2.161830718.381409930.1654502864-1953235162.1648459099">
	     * our error list</a> for possible values.
		 *
		 * @return The error code as a String.
		 */
		public String getTitle() {
			return title;
		}

		/**
		 * Text describing the error. See
		 * <a href="https://developer.nexmo.com/api-errors/messages-olympus#_ga=2.201342739.381409930.1654502864-1953235162.1648459099">
         * our error list</a> for possible values.
		 *
		 * @return The error message description.
		 */
		public String getDetail() {
			return detail;
		}

		/**
		 * The record id of this error's occurrence.
		 *
		 * @return The error instance ID.
		 */
		public String getInstance() {
			return instance;
		}
	}

	/**
	 * Describes the charge incurred for sending the message.
	 */
	public static final class Usage extends JsonableBaseObject {
		@JsonProperty("price") double price;
		@JsonProperty("currency") Currency currency;

		void setCurrency(String currency) {
			this.currency = Currency.getInstance(currency);
		}

		/**
		 * The charge currency in ISO 4217 format. Usually will be {@code EUR} (Euros).
		 *
		 * @return The Currency.
		 */
		public Currency getCurrency() {
			return currency;
		}

		void setPrice(String price) {
			this.price = Double.parseDouble(price);
		}

		/**
		 * The amount charged for the message.
		 *
		 * @return The amount, as a double.
		 */
		public double getPrice() {
			return price;
		}
	}

	static class Destination extends JsonableBaseObject {
		@JsonProperty("network_code") String networkCode;
	}

	static class Sms extends JsonableBaseObject {
		@JsonProperty("count_total") Integer countTotal;
	}

	static class Whatsapp extends JsonableBaseObject {
		static class Conversation extends JsonableBaseObject {
			static class Origin extends JsonableBaseObject {
				@JsonProperty("type") ConversationType type;
			}
			@JsonProperty("id") String id;
			@JsonProperty("origin") Origin origin;
		}
		@JsonProperty("conversation") Conversation conversation;
	}

	protected MessageStatus() {
	}

	@JsonAnySetter protected Map<String, Object> unknownProperties;

	@JsonProperty("timestamp") protected Instant timestamp;
	@JsonProperty("message_uuid") protected UUID messageUuid;
	@JsonProperty("to") protected String to;
	@JsonProperty("from") protected String from;
	@JsonProperty("status") protected Status status;
	@JsonProperty("channel") protected Channel channel;
	@JsonProperty("client_ref") protected String clientRef;
	@JsonProperty("error") protected Error error;
	@JsonProperty("usage") protected Usage usage;

	@JsonProperty("destination") private Destination destination;
	@JsonProperty("sms") private Sms sms;
	@JsonProperty("whatsapp") private Whatsapp whatsapp;


	/**
	 * Unique identifier of the message that was sent, as returned in {@link MessageResponse#getMessageUuid()}.
	 *
	 * @return The UUID of the message.
	 */
	public UUID getMessageUuid() {
		return messageUuid;
	}

	/**
	 * The 'to' number used in the outbound {@link MessageRequest}.
	 *
	 * @return The recipient number or ID.
	 */
	public String getTo() {
		return to;
	}

	/**
	 * The 'from' number used in the outbound {@link MessageRequest}.
	 *
	 * @return The sender number or ID.
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * The datetime of when the event occurred.
	 *
	 * @return The timestamp as an Instant.
	 */
	public Instant getTimestamp() {
		return timestamp;
	}

	/**
	 * The status of the message.
	 *
	 * @return The message status as an enum.
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * The service used to send the message.
	 *
	 * @return The channel, as an enum.
	 */
	public Channel getChannel() {
		return channel;
	}

	/**
	 * Client reference of up to 40 characters. The reference will be present if set
	 * in the original outbound {@link MessageRequest}.
	 *
	 * @return The client reference, or {@code null} if unset.
	 */
	public String getClientRef() {
		return clientRef;
	}

	/**
	 * If the message encountered a problem a descriptive error will be supplied in this object.
	 *
	 * @return The error object, or {@code null} if there was no problem to report.
	 */
	public Error getError() {
		return error;
	}

	/**
	 * Describes the cost of the message that was sent.
	 *
	 * @return The usage object, or {@code null} if absent.
	 */
	public Usage getUsage() {
		return usage;
	}

	/**
	 * If {@linkplain #getChannel()} is {@linkplain Channel#SMS} or {@linkplain Channel#MMS},
	 * returns the network code for the destination.
	 *
	 * @return The mobile network code as a string, or {@code null} if not applicable.
	 *
	 * @since 8.1.0
	 */
	@JsonIgnore
	public String getDestinationNetworkCode() {
		return destination != null ? destination.networkCode : null;
	}

	/**
	 * {@linkplain #getChannel()} is {@linkplain Channel#SMS}, returns the number of SMS messages concatenated together
	 * to comprise the submitted message. SMS messages are 160 characters, if a submitted message exceeds that size it
	 * is sent as multiple SMS messages. This number indicates how many SMS messages are required.
	 *
	 * @return The number of SMS messages used for this message, or {@code null} if not applicable.
	 *
	 * @since 8.1.0
	 */
	@JsonIgnore
	public Integer getSmsTotalCount() {
		return sms != null ? sms.countTotal : null;
	}

	/**
	 * If the {@linkplain #getChannel()} is {@linkplain Channel#WHATSAPP} and {@linkplain #getStatus()} is
	 * {@linkplain Status#DELIVERED}, returns the conversation's origin type.
	 *
	 * @return The WhatsApp conversation category as an enum, {@code null} if absent or not applicable.
	 *
	 * @since 8.1.0
	 */
	@JsonIgnore
	public ConversationType getWhatsappConversationType() {
		return whatsapp != null &&
				whatsapp.conversation != null &&
				whatsapp.conversation.origin != null ?
				whatsapp.conversation.origin.type : null;
	}

	/**
	 * If the {@linkplain #getChannel()} is {@linkplain Channel#WHATSAPP} and {@linkplain #getStatus()} is
	 * {@linkplain Status#DELIVERED}, returns the conversation ID of the message that triggered this callback.
	 *
	 * @return The WhatsApp conversation ID, {@code null} if absent or not applicable.
	 *
	 * @since 8.1.0
	 */
	@JsonIgnore
	public String getWhatsappConversationId() {
        return whatsapp != null && whatsapp.conversation != null ? whatsapp.conversation.id : null;
    }

	/**
	 * Catch-all for properties which are not mapped by this class during deserialization.
	 *
	 * @return Additional (unknown) properties as a Map, or {@code null} if absent.
	 */
	@JsonAnyGetter
	public Map<String, ?> getAdditionalProperties() {
		return unknownProperties;
	}

	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 *
	 * @return An instance of this class with the fields populated, if present.
	 */
	@JsonCreator
	public static MessageStatus fromJson(String json) {
		return Jsonable.fromJson(json);
	}
}
