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
package com.vonage.client.messages.whatsapp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.messages.internal.MessageType;

import java.util.Map;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public final class WhatsappCustomRequest extends WhatsappRequest {
	Map<?, ?> custom;

	WhatsappCustomRequest(Builder builder) {
		super(builder);
		custom = builder.custom;
	}

	@JsonProperty("custom")
	public Map<?, ?> getCustom() {
		return custom;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends WhatsappRequest.Builder<WhatsappCustomRequest, Builder> {
		Map<?, ?> custom;

		Builder() {}

		@Override
		protected MessageType getMessageType() {
			return MessageType.CUSTOM;
		}

		/**
		 * A custom payload, which is passed directly to WhatsApp for certain features such as templates and
		 * interactive messages. The schema of a custom object can vary widely.
		 * <a href=https://developer.vonage.com/messages/concepts/custom-objects>Read about Custom Objects here</a>.
		 *
		 * @param custom A serializable Map.
		 * @return This builder.
		 */
		public Builder custom(Map<?, ?> custom) {
			this.custom = custom;
			return this;
		}

		@Override
		public WhatsappCustomRequest build() {
			return new WhatsappCustomRequest(this);
		}
	}
}
