/*
 *   Copyright 2022 Vonage
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
package com.vonage.client.messages.messenger;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents a Facebook Message Tag. <br>
 * See the <a href="https://developers.facebook.com/docs/messenger-platform/send-messages/message-tags">
 * documentation</a> for details on what these mean.
 */
public enum Tag {
	CONFIRMED_EVENT_UPDATE,
	POST_PURCHASE_UPDATE,
	ACCOUNT_UPDATE,
	HUMAN_AGENT,
	CUSTOMER_FEEDBACK;

	@JsonValue
	@Override
	public String toString() {
		return name();
	}
}
