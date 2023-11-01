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

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import com.vonage.client.messages.sms.SmsInboundMetadata;
import com.vonage.client.messages.whatsapp.*;
import java.net.URI;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Convenience class representing an inbound message webhook.
 * <p>
 * Refer to the
 * <a href=https://developer.vonage.com/api/messages-olympus#webhooks>Messages API Webhook reference</a>
 * for more details on the response object structure.
 *
 * @since 7.2.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InboundMessage implements Jsonable {
	@JsonIgnoreProperties(ignoreUnknown = true)
	protected static class UrlWrapper {
		@JsonProperty("url") protected URI url;
	}

	protected InboundMessage() {}

	@JsonAnySetter protected Map<String, Object> unknownProperties;

	@JsonProperty("timestamp") protected Instant timestamp;
	@JsonProperty("channel") protected Channel channel;
	@JsonProperty("message_type") protected MessageType messageType;
	@JsonProperty("message_uuid") protected UUID messageUuid;
	@JsonProperty("to") protected String to;
	@JsonProperty("from") protected String from;
	@JsonProperty("client_ref") protected String clientRef;
	@JsonProperty("provider_message") String providerMessage;

	@JsonProperty("text") protected String text;
	@JsonProperty("image") protected UrlWrapper image;
	@JsonProperty("audio") protected UrlWrapper audio;
	@JsonProperty("video") protected UrlWrapper video;
	@JsonProperty("file") protected UrlWrapper file;
	@JsonProperty("vcard") protected UrlWrapper vcard;
	@JsonProperty("sticker") protected UrlWrapper sticker;

	@JsonProperty("profile") protected Profile whatsappProfile;
	@JsonProperty("context") protected Context whatsappContext;
	@JsonProperty("location") protected Location whatsappLocation;
	@JsonProperty("reply") protected Reply whatsappReply;
	@JsonProperty("order") protected Order whatsappOrder;
	@JsonProperty("usage") protected MessageStatus.Usage usage;
	@JsonProperty("sms") protected SmsInboundMetadata smsMetadata;

	/**
	 * This is a catch-all method which encapsulates all fields in the response JSON
	 * that are not already a field in this class.
	 *
	 * @return The Map of unknown properties to their values.
	 */
	@JsonAnyGetter
	public Map<String, ?> getUnmappedProperties() {
		return unknownProperties;
	}

	/**
	 * The media type of the message.
	 *
	 * @return The message type, as an enum.
	 */
	public MessageType getMessageType() {
		return messageType;
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
	 * Unique identifier of the message that was sent.
	 *
	 * @return The UUID of the message.
	 */
	public UUID getMessageUuid() {
		return messageUuid;
	}

	/**
	 * The number this message was sent to.
	 *
	 * @return The recipient number or ID.
	 */
	public String getTo() {
		return to;
	}

	/**
	 * The number this message was sent from.
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
	 * The client reference will be present if set in the original outbound message.
	 *
	 * @return The client reference, or {@code null} if unset.
	 */
	public String getClientRef() {
		return clientRef;
	}

	/**
	 * If {@linkplain #getMessageType()} is {@linkplain MessageType#TEXT}, returns the message text.
	 *
	 * @return The message text, or {@code null} if not applicable.
	 */
	public String getText() {
		return text;
	}

	/**
	 * If {@linkplain #getMessageType()} is {@linkplain MessageType#IMAGE}, returns the URL of the image.
	 *
	 * @return The image URL, or {@code null} if not applicable.
	 */
	public URI getImageUrl() {
		return image != null ? image.url : null;
	}

	/**
	 * If {@linkplain #getMessageType()} is {@linkplain MessageType#AUDIO}, returns the URL of the audio.
	 *
	 * @return The audio URL, or {@code null} if not applicable.
	 */
	public URI getAudioUrl() {
		return audio != null ? audio.url : null;
	}

	/**
	 * If {@linkplain #getMessageType()} is {@linkplain MessageType#VIDEO}, returns the URL of the video.
	 *
	 * @return The video URL, or {@code null} if not applicable.
	 */
	public URI getVideoUrl() {
		return video != null ? video.url : null;
	}

	/**
	 * If {@linkplain #getMessageType()} is {@linkplain MessageType#FILE}, returns the URL of the file.
	 *
	 * @return The file URL, or {@code null} if not applicable.
	 */
	public URI getFileUrl() {
		return file != null ? file.url : null;
	}

	/**
	 * If {@linkplain #getMessageType()} is {@linkplain MessageType#VCARD}, returns the URL of the vCard.
	 *
	 * @return The vCard URL, or {@code null} if not applicable.
	 */
	public URI getVcardUrl() {
		return vcard != null ? vcard.url : null;
	}

	/**
	 * If {@linkplain #getMessageType()} is {@linkplain MessageType#STICKER}, returns the URL of the sticker.
	 *
	 * @return The sticker URL, or {@code null} if not applicable.
	 */
	public URI getStickerUrl() {
		return sticker != null ? sticker.url : null;
	}

	/**
	 * If {@linkplain #getChannel()} is {@linkplain Channel#WHATSAPP}, returns the {@code provider_message} field.
	 * This is a message from the channel provider, which may contain a description, error codes or other information.
	 *
	 * @return The provider message or {@code null} if not applicable.
	 */
	public String getProviderMessage() {
		return providerMessage;
	}

	/**
	 * If {@linkplain #getMessageType()} is {@linkplain MessageType#LOCATION} and {@linkplain #getChannel()} is
	 * {@linkplain Channel#WHATSAPP}, returns the location.
	 *
	 * @return The deserialized WhatsApp location, or {@code null} if not applicable.
	 */
	public Location getWhatsappLocation() {
		return whatsappLocation;
	}

	/**
	 * If {@linkplain #getMessageType()} is {@linkplain MessageType#REPLY} and {@linkplain #getChannel()} is
	 * {@linkplain Channel#WHATSAPP}, returns the reply.
	 *
	 * @return The deserialized WhatsApp reply, or {@code null} if not applicable.
	 */
	public Reply getWhatsappReply() {
		return whatsappReply;
	}

	/**
	 * If {@linkplain #getMessageType()} is {@linkplain MessageType#ORDER} and {@linkplain #getChannel()} is
	 * {@linkplain Channel#WHATSAPP}, returns the order.
	 *
	 * @return The deserialized WhatsApp order, or {@code null} if not applicable.
	 */
	public Order getWhatsappOrder() {
		return whatsappOrder;
	}

	/**
	 * If the {@linkplain #getChannel()} is {@linkplain Channel#WHATSAPP}, returns information
	 * about the sender's WhatsApp profile.
	 *
	 * @return The deserialized WhatsApp profile, or {@code null} if not applicable.
	 */
	public Profile getWhatsappProfile() {
		return whatsappProfile;
	}

	/**
	 * If the {@linkplain #getChannel()} is {@linkplain Channel#WHATSAPP}, returns the WhatsApp message context.
	 * This is only present where the user is quoting another message. It provides information about the quoted
	 * message and/or the product message being responded to.
	 *
	 * @return The deserialized WhatsApp context, or {@code null} if not applicable.
	 */
	public Context getWhatsappContext() {
		return whatsappContext;
	}

	/**
	 * If the {@linkplain #getChannel()} is {@linkplain Channel#SMS}, returns the usage
	 * information (charged incurred for the message).
	 *
	 * @return The deserialized usage object, or {@code null} if not applicable.
	 */
	public MessageStatus.Usage getUsage() {
		return usage;
	}

	/**
	 * If the {@linkplain #getChannel()} is {@linkplain Channel#SMS},
	 * returns additional information about the message.
	 *
	 * @return The SMS metadata, or {@code null} if not applicable.
	 */
	public SmsInboundMetadata getSmsMetadata() {
		return smsMetadata;
	}

	/**
	 * Constructs an instance of this class from a JSON payload. Known fields will be mapped, whilst
	 * unknown fields can be manually obtained through the {@linkplain #getUnmappedProperties()} method.
	 *
	 * @param json The webhook response JSON string.
	 *
	 * @return The deserialized webhook response object.
	 * @throws com.vonage.client.VonageResponseParseException If the response could not be deserialized.
	 */
	public static InboundMessage fromJson(String json) {
		return Jsonable.fromJson(json);
	}
}
