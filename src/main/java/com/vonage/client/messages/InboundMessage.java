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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import java.io.IOException;
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
public class InboundMessage {
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

	@JsonProperty("text") protected String text;
	@JsonProperty("image") protected UrlWrapper image;
	@JsonProperty("audio") protected UrlWrapper audio;
	@JsonProperty("video") protected UrlWrapper video;
	@JsonProperty("file") protected UrlWrapper file;
	@JsonProperty("vcard") protected UrlWrapper vcard;

	/**
	 * This is a catch-all method which encapsulates all fields in the response JSON
	 * that are not already a field in this class. Channel-specific response objects
	 * are deserialized into this Map. For example, an SMS message may have the following
	 * response object which is not deserialized into a field by this class:<br>
	 * {@code
	 *     "sms": {
	 *     "num_messages": "2",
	 *     "keyword": "HELLO"
	 *   }
	 * } <br><br>
	 *
	 * To obtain the "num_messages" from the map, you could do the following:<br>
	 *
	 * {@code
	 *      Map<String, ?> additionalProps = inboundMessage.getAdditionalProperties();
	 *      Map<String, String> smsObj = (Map<String, String>) additionalProps.get("sms");
	 *      int numMessages = Integer.parseInt(smsObj.get("num_messages"));
	 * }
	 *
	 * @return The Map of unknown properties to their values.
	 */
	@JsonAnyGetter
	public Map<String, ?> getAdditionalProperties() {
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
	 * Convenience method for obtaining the credit usage of an SMS message.
	 *
	 * @return The deserialized usage object, or <code>null</code> if absent or not applicable.
	 * @throws IllegalStateException If the channel is not {@linkplain Channel#SMS}.
	 */
	@SuppressWarnings("unchecked")
	public MessageStatus.Usage getSmsUsage() {
		if (channel != Channel.SMS) {
			throw new IllegalStateException("Usage only applies to SMS messages");
		}
		Map<String, String> usageMap = (Map<String, String>) unknownProperties.get("usage");
		if (usageMap == null) return null;
		MessageStatus.Usage usageObj = new MessageStatus.Usage();
		usageObj.setCurrency(usageMap.get("currency"));
		usageObj.setPrice(usageMap.get("price"));
		return usageObj;
	}

	/**
	 * Constructs an instance of this class from a JSON payload. Known fields will be mapped, whilst
	 * unknown fields can be manually obtained through the {@linkplain #getAdditionalProperties()} method.
	 *
	 * @param json The webhook response JSON string.
	 *
	 * @return The deserialized webhook response object.
	 * @throws VonageUnexpectedException If the response could not be deserialized.
	 */
	public static InboundMessage fromJson(String json) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, InboundMessage.class);
		}
		catch (IOException ex) {
			throw new VonageUnexpectedException("Failed to produce InboundMessage from json.", ex);
		}
	}
}
