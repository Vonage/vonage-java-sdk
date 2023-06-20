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
package com.vonage.client.messages;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vonage.client.VonageUnexpectedException;
import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.util.Currency;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * This class represents the data of a callback which is invoked after a message has been sent.
 * It provides metadata about the message, such as its status, how much it cost, when it was sent,
 * what service (channel) it was sent via, sender and recipient, message response ID, client reference etc.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class MessageStatus {

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
	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	public static final class Error {
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

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Error error = (Error) o;
			return Objects.equals(type, error.type) &&
					Objects.equals(title, error.title) &&
					Objects.equals(detail, error.detail) &&
					Objects.equals(instance, error.instance);
		}

		@Override
		public int hashCode() {
			return Objects.hash(type, title, detail, instance);
		}

		@Override
		public String toString() {
			return getClass().getSimpleName() +
					" {type='"+type + '\'' +
					", title='"+title + '\'' +
					", detail='"+detail + '\'' +
					", instance='"+instance + "'}";
		}
	}

	/**
	 * Describes the charge incurred for sending the message.
	 */
	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class Usage {
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

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Usage usage = (Usage) o;
			return Double.compare(usage.price, price) == 0 &&
					Objects.equals(currency, usage.currency);
		}

		@Override
		public int hashCode() {
			return Objects.hash(price, currency);
		}

		@Override
		public String toString() {
			return getClass().getSimpleName() +
					" {price="+price +
					", currency="+currency + '}';
		}
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
	 * Catch-all for properties which are not mapped by this class during deserialization.
	 *
	 * @return Additional (unknown) properties as a Map, or {@code null} if absent.
	 */
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
	public static MessageStatus fromJson(String json) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			return mapper.readValue(json, MessageStatus.class);
		}
		catch (IOException ex) {
			throw new VonageUnexpectedException("Failed to produce MessageStatus from json.", ex);
		}
	}

	/**
	 * Generates a JSON string from this status object.
	 *
	 * @return JSON representation of this MessageStatus object.
	 */
	public String toJson() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			return mapper.writeValueAsString(this);
		}
		catch (JsonProcessingException jpe) {
			throw new VonageUnexpectedException("Failed to produce JSON from "+getClass().getSimpleName()+" object.", jpe);
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()+' '+toJson();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MessageStatus that = (MessageStatus) o;
		return Objects.equals(timestamp, that.timestamp) &&
				Objects.equals(messageUuid, that.messageUuid) &&
				Objects.equals(to, that.to) && Objects.equals(from, that.from) &&
				status == that.status && channel == that.channel &&
				Objects.equals(clientRef, that.clientRef) &&
				Objects.equals(error, that.error) && Objects.equals(usage, that.usage);
	}

	@Override
	public int hashCode() {
		return Objects.hash(timestamp, messageUuid, to, from, status, channel, clientRef, error, usage);
	}
}
