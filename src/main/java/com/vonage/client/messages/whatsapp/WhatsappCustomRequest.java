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
package com.vonage.client.messages.whatsapp;

import com.vonage.client.messages.MessageType;
import java.util.Map;

public final class WhatsappCustomRequest extends WhatsappRequest {

	WhatsappCustomRequest(Builder builder) {
		super(builder, MessageType.CUSTOM);
	}

	@Override
	public Map<String, ?> getCustom() {
		return super.getCustom();
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends WhatsappRequest.Builder<WhatsappCustomRequest, Builder> {

		Builder() {}

		/**
		 * (OPTIONAL)
		 * A custom payload, which is passed directly to WhatsApp for certain features such as templates and
		 * interactive messages. The schema of a custom object can vary widely.
		 * <a href=https://developer.vonage.com/messages/concepts/custom-objects>Read about Custom Objects here</a>.
		 *
		 * @param payload A serializable Map.
		 * @return This builder.
		 */
		@Override
		public Builder custom(Map<String, ?> payload) {
			return super.custom(payload);
		}

		@Override
		public WhatsappCustomRequest build() {
			return new WhatsappCustomRequest(this);
		}
	}
}
