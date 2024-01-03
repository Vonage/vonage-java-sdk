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
package com.vonage.client.messages.whatsapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.messages.InboundMessage;
import java.net.URI;

/**
 * This is only present for situations where a user has clicked on a 'WhatsApp' button embedded in an advertisement
 * or post on Facebook. Clicking on the button directs the user to the WhatsApp app from where they can send a message.
 * The inbound message will contain this object which includes details of the Facebook advertisement or post which
 * contained the embedded button. This is used in {@link InboundMessage#getWhatsappReferral()}.
 *
 * @since 8.1.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Referral {
	private String body, headline, sourceId, sourceType;
	private URI sourceUrl;

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
	 * A URL referencing the content of the media shown in the advertisement when the user clicked to send a message.
	 *
	 * @return Link to the advertised content.
	 */
	@JsonProperty("source_url")
	public URI getSourceUrl() {
		return sourceUrl;
	}
}
