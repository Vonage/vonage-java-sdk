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
package com.vonage.client.messages.sms;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.messages.MessageRequest;
import com.vonage.client.messages.internal.Channel;
import com.vonage.client.messages.internal.MessageType;
import com.vonage.client.messages.internal.Text;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public final class SmsRequest extends MessageRequest {

	String text;

	SmsRequest(Builder builder) {
		super(builder);
		text = new Text(builder.text, 1000).toString();
	}

	@JsonProperty("text")
	public String getText() {
		return text;
	}

	public static Builder builder() {
		return new Builder();
	}

	public final static class Builder extends MessageRequest.Builder<SmsRequest, Builder> {
		Builder() {
			messageType = MessageType.TEXT;
			channel = Channel.SMS;
		}

		String text;

		public Builder text(String text) {
			this.text = text;
			return this;
		}

		public SmsRequest build() {
			return new SmsRequest(this);
		}
	}
}