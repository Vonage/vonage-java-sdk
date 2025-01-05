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
package com.vonage.client.messages.rcs;

import com.vonage.client.messages.MessageType;
import java.util.Map;

/**
 * {@link com.vonage.client.messages.Channel#RCS}, {@link MessageType#CUSTOM} request.
 *
 * @since 8.11.0
 */
public final class RcsCustomRequest extends RcsRequest {

	RcsCustomRequest(Builder builder) {
		super(builder, MessageType.CUSTOM);
	}

	@Override
	public Map<String, ?> getCustom() {
		return super.getCustom();
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends RcsRequest.Builder<RcsCustomRequest, Builder> {

		Builder() {}

		/**
		 * A custom payload. The schema of a custom object can vary widely.
		 * <a href=https://developer.vonage.com/en/messages/guides/rcs/rcs-custom-messages>
		 * Read more about RCS Custom Messages.</a>
		 *
		 * @param payload The custom payload properties to send as a Map.
		 * @return This builder.
		 */
		@Override
		public Builder custom(Map<String, ?> payload) {
			return super.custom(payload);
		}

		@Override
		public RcsCustomRequest build() {
			return new RcsCustomRequest(this);
		}
	}
}
