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
import com.vonage.client.common.UrlContainer;
import com.vonage.client.messages.mms.Content;
import com.vonage.client.messages.sms.SmsInboundMetadata;
import com.vonage.client.messages.whatsapp.*;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Convenience class representing an inbound message webhook. This maps all known fields for all message types.
 * <p>
 * Refer to the
 * <a href=https://developer.vonage.com/api/messages-olympus#webhooks>Messages API Webhook reference</a>
 * for more details on the response object structure.
 *
 * @since 7.2.0
 */
public class InboundMessage extends JsonableBaseObject {

	protected static class UrlWrapper extends JsonableBaseObject {
		@JsonProperty("url") protected URI url;
		@JsonProperty("name") protected String name;
	}

	protected static class UrlWrapperWithCaption extends UrlWrapper {
		@JsonProperty("caption") protected String caption;
	}

	protected static class Whatsapp extends JsonableBaseObject {
		@JsonProperty("referral") protected Referral referral;
	}

	protected static class Origin extends JsonableBaseObject {
		@JsonProperty("network_code") protected String networkCode;
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
	@JsonProperty("_self") UrlContainer self;

	@JsonProperty("text") protected String text;
	@JsonProperty("image") protected UrlWrapperWithCaption image;
	@JsonProperty("audio") protected UrlWrapper audio;
	@JsonProperty("video") protected UrlWrapper video;
	@JsonProperty("file") protected UrlWrapper file;
	@JsonProperty("vcard") protected UrlWrapper vcard;
	@JsonProperty("sticker") protected UrlWrapper sticker;
	@JsonProperty("reaction") protected Reaction reaction;
	@JsonProperty("button") protected Button button;
	@JsonProperty("profile") protected Profile whatsappProfile;
	@JsonProperty("context_status") protected ContextStatus whatsappContextStatus;
	@JsonProperty("context") protected Context whatsappContext;
	@JsonProperty("location") protected Location whatsappLocation;
	@JsonProperty("reply") protected Reply whatsappReply;
	@JsonProperty("order") protected Order whatsappOrder;
	@JsonProperty("usage") protected MessageStatus.Usage usage;
	@JsonProperty("sms") protected SmsInboundMetadata smsMetadata;
	@JsonProperty("origin") protected Origin origin;
	@JsonProperty("whatsapp") private Whatsapp whatsapp;
	@JsonProperty("content") protected List<Content> content;

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
	 * The URL for the message resource, including the geo-specific base URI.
	 *
	 * @return The {@code _self.href} property as a URI, or {@code null} if absent.
	 * @since 8.11.0
	 */
	@JsonIgnore
	public URI getSelfUrl() {
		return self != null ? self.getHref() : null;
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
	@JsonIgnore
	public URI getImageUrl() {
		return image != null ? image.url : null;
	}

	/**
	 * Additional text accompanying the image. Applicable to MMS image messages only.
	 *
	 * @return The image caption if present, or {@code null} if not applicable.
	 *
	 * @since 8.1.0
	 */
	@JsonIgnore
	public String getImageCaption() {
		return image != null ? image.caption : null;
	}

	/**
	 * If {@linkplain #getMessageType()} is {@linkplain MessageType#AUDIO}, returns the URL of the audio.
	 *
	 * @return The audio URL, or {@code null} if not applicable.
	 */
	@JsonIgnore
	public URI getAudioUrl() {
		return audio != null ? audio.url : null;
	}

	/**
	 * If {@linkplain #getMessageType()} is {@linkplain MessageType#VIDEO}, returns the URL of the video.
	 *
	 * @return The video URL, or {@code null} if not applicable.
	 */
	@JsonIgnore
	public URI getVideoUrl() {
		return video != null ? video.url : null;
	}

	/**
	 * If {@linkplain #getMessageType()} is {@linkplain MessageType#FILE}, returns the URL of the file.
	 *
	 * @return The file URL, or {@code null} if not applicable.
	 */
	@JsonIgnore
	public URI getFileUrl() {
		return file != null ? file.url : null;
	}

	/**
	 * If {@linkplain #getMessageType()} is {@linkplain MessageType#VCARD}, returns the URL of the vCard.
	 *
	 * @return The vCard URL, or {@code null} if not applicable.
	 */
	@JsonIgnore
	public URI getVcardUrl() {
		return vcard != null ? vcard.url : null;
	}

	/**
	 * If {@linkplain #getMessageType()} is {@linkplain MessageType#VCARD} and {@linkplain #getChannel()} is
	 * {@linkplain Channel#RCS}, returns the name of the vCard.
	 *
	 * @return The vCard attachment file name, or {@code null} if absent / not applicable.
	 * @since 8.11.0
	 */
	@JsonIgnore
	public String getVcardName() {
		return vcard != null ? vcard.name : null;
	}

	/**
	 * If {@linkplain #getMessageType()} is {@linkplain MessageType#STICKER}, returns the URL of the sticker.
	 *
	 * @return The sticker URL, or {@code null} if not applicable.
	 */
	@JsonIgnore
	public URI getStickerUrl() {
		return sticker != null ? sticker.url : null;
	}

	/**
	 * If {@linkplain #getMessageType()} is {@linkplain MessageType#REACTION}, returns the reaction.
	 *
	 * @return The reaction details, or {@code null} if not applicable.
	 * @since 8.11.0
	 */
	public Reaction getReaction() {
		return reaction;
	}

	/**
	 * If {@linkplain #getMessageType()} is {@linkplain MessageType#BUTTON}, returns the button.
	 *
	 * @return The button details, or {@code null} if not applicable.
	 * @since 8.11.0
	 */
	public Button getButton() {
		return button;
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
	 * If the {@linkplain #getChannel()} is {@linkplain Channel#WHATSAPP}, returns an enum indicating whether there
	 * is a context for this inbound message. If there is a context, and it is available, the context details will be
	 * contained in a context object. If there is a context, but it is unavailable,or if there is no context for
	 * message ({@linkplain ContextStatus#NONE}), then there will be no context object included in the body.
	 *
	 * @return The deserialized WhatsApp context status, or {@code null} if not applicable.
	 *
	 * @since 8.1.0
	 */
	public ContextStatus getWhatsappContextStatus() {
		return whatsappContextStatus;
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
	 * If the {@linkplain #getChannel()} is {@linkplain Channel#SMS} or {@linkplain Channel#MMS},
	 * return the network code from which the message originated (if available).
	 *
	 * @return The origin network code if applicable, or {@code null} if unknown.
	 *
	 * @since 8.7.0
	 */
	@JsonIgnore
	public String getNetworkCode() {
		return origin != null ? origin.networkCode : null;
	}

	/**
	 * If the {@linkplain #getChannel()} is {@linkplain Channel#WHATSAPP} and a content referral is present in
	 * the message, returns the metadata related to the post or advertisement that the user clicked on.
	 *
	 * @return The Whatsapp referral object, or {@code null} if not present or applicable.
	 *
	 * @since 8.1.0
	 */
	@JsonIgnore
	public Referral getWhatsappReferral() {
		return whatsapp != null ? whatsapp.referral : null;
	}

	/**
	 * If the {@linkplain #getChannel()} is {@linkplain Channel#MMS}, returns a list of one or more objects
	 * representing image, audio, video, vCard, or file attachments. Only present for messages that have
	 * more than one attachment.
	 *
	 * @return The list of MMS attachments, or {@code null} if not applicable.
	 * @since 8.11.0
	 */
	@JsonProperty("content")
	public List<Content> getContent() {
		return content;
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
