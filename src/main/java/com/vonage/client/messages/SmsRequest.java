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
package com.vonage.client.messages;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SmsRequest extends SendMessageRequest {

	protected Text text;

	public SmsRequest(String from, String to, String text) {
		super(from, to, MessageType.TEXT, Channel.SMS);
		this.text = new Text(text);
	}

	public SmsRequest(String from, String to, String text, String clientRef) {
		this(from, to, text);
		this.clientRef = clientRef;
	}

	@JsonProperty("text")
	public String getText() {
		return text.toString();
	}
}
