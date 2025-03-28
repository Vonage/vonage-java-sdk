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
package com.vonage.client.messages.whatsapp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.messages.InboundMessage;
import com.vonage.client.common.MessageType;
import java.net.URI;

/**
 * This is only present for situations where a user has clicked on a 'WhatsApp' button embedded in an advertisement
 * or post on Facebook. Clicking on the button directs the user to the WhatsApp app from where they can send a message.
 * The inbound message will contain this object which includes details of the Facebook advertisement or post which
 * contained the embedded button. This is used in {@link InboundMessage#getWhatsappReferral()}.
 *
 * @since 8.1.0
 */
public final class Referral extends JsonableBaseObject {
	private String body, headline, sourceId, sourceType, clickId;
	private URI sourceUrl, imageUrl, videoUrl, thumbnailUrl;
	private MessageType mediaType;

	Referral() {}

	/**
	 * Body text of the referring advertisement or post.
	 *
	 * @return The referral body.
	 */
	@JsonProperty("body")
	public String getBody() {
		return body;
	}

	/**
	 * Headline text of the referring advertisement or post.
	 *
	 * @return The referral headline.
	 */
	@JsonProperty("headline")
	public String getHeadline() {
		return headline;
	}

	/**
	 * The click ID of the advertisement or post.
	 *
	 * @return The {@code ctwa_clid}, or {@code null} if absent / unknown / not applicable.
	 * @since 8.11.0
	 */
	@JsonProperty("ctwa_clid")
	public String getClickId() {
		return clickId;
	}

	/**
	 * Meta/WhatsApp ID of the referring advertisement or post.
	 *
	 * @return The source referral ID as a String.
	 */
	@JsonProperty("source_id")
	public String getSourceId() {
		return sourceId;
	}

	/**
	 * The type of the referring advertisement or post.
	 *
	 * @return The source referral type as a String.
	 */
	@JsonProperty("source_type")
	public String getSourceType() {
		return sourceType;
	}

	/**
	 * The type of media shown in the advertisement or post. Either {@linkplain MessageType#IMAGE}
	 * or {@linkplain MessageType#VIDEO}.
	 *
	 * @return The media type as an enum, or {@code null} if absent / not applicable.
	 * @since 8.11.0
	 */
	@JsonProperty("media_type")
	public MessageType getMediaType() {
		return mediaType;
	}

	/**
	 * A URL referencing the content of the media shown in the advertisement when the user clicked to send a message.
	 *
	 * @return Link to the advertised content.
	 */
	@JsonProperty("source_url")
	public URI getSourceUrl() {
		return sourceUrl;
	}

	/**
	 * URL of the image shown in the advertisement or post.
	 *
	 * @return The image URL, or {@code null} if absent / not applicable.
	 * @since 8.11.0
	 */
	@JsonProperty("image_url")
	public URI getImageUrl() {
		return imageUrl;
	}

	/**
	 * URL of the video shown in the advertisement or post.
	 *
	 * @return The video URL, or {@code null} if absent / not applicable.
	 * @since 8.11.0
	 */
	@JsonProperty("video_url")
	public URI getVideoUrl() {
		return videoUrl;
	}


	/**
	 * URL of the thumbnail image shown in the advertisement or post.
	 *
	 * @return The thumbnail URL, or {@code null} if absent / not applicable.
	 * @since 8.11.0
	 */
	@JsonProperty("thumbnail_url")
	public URI getThumbnailUrl() {
		return thumbnailUrl;
	}
}
