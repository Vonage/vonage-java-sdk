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
package com.vonage.client.messages.sms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Used to describe inbound SMS metadata.
 *
 * @since 7.2.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class SmsInboundMetadata {
	private Integer numMessages, totalCount;
	private String keyword;

	SmsInboundMetadata() {}

	/**
	 * The number of inbound SMS messages concatenated together to comprise this message. SMS messages are 160
	 * characters, if an inbound message exceeds that size they are concatenated together to form a single message.
	 * This number indicates how many messages formed this webhook.
	 *
	 * @return The total number of SMS messages used to create the text message.
	 */
	@JsonProperty("num_messages")
	public Integer getNumMessages() {
		return numMessages;
	}

	/**
	 * The number of inbound SMS messages concatenated together to comprise this message. SMS messages are 160
	 * characters, if an inbound message exceeds that size they are concatenated together to form a single message.
	 * This number indicates how many messages formed this webhook.
	 *
	 * @return The total number of SMS messages used to create the text message.
	 */
	@JsonProperty("total_count")
	public Integer getTotalCount() {
		return totalCount;
	}

	/**
	 * The first word of the message sent to uppercase.
	 *
	 * @return The keyword.
	 */
	@JsonProperty("keyword")
	public String getKeyword() {
		return keyword;
	}
}
