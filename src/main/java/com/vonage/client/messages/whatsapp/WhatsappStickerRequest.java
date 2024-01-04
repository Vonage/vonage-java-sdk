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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.messages.MessageType;

/**
 * See <a href=https://developer.vonage.com/en/messages/concepts/whatsapp-stickers>the documentation</a>
 * for more information on sending stickers.
 *
 * @since 7.2.0
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public final class WhatsappStickerRequest extends WhatsappRequest {
	final Sticker sticker;

	WhatsappStickerRequest(Builder builder) {
		super(builder, MessageType.STICKER);
		sticker = new Sticker(builder.url, builder.id);
	}

	@JsonProperty("sticker")
	public Sticker getSticker() {
		return sticker;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends WhatsappRequest.Builder<WhatsappStickerRequest, Builder> {
		String url, id;

		Builder() {}

		/**
		 * (REQUIRED if {@link #id(String)} is not specified)
		 * The publicly accessible URL of the sticker image. Must be in {@code .webp} format.
		 * You must specify only the ID or the URL, but not both.
		 *
		 * @param url The sticker URL as a string.
		 * @return This builder.
		 */
		public Builder url(String url) {
			this.url = url;
			return this;
		}

		/**
		 * (REQUIRED if {@link #url(String)} is not specified)
		 * The ID of the sticker in relation to a specific WhatsApp deployment.
		 * You must specify only the ID or the URL, but not both.
		 *
		 * @param id The sticker's unique identifier as a string.
		 * @return This builder.
		 */
		public Builder id(String id) {
			this.id = id;
			return this;
		}

		@Override
		public WhatsappStickerRequest build() {
			return new WhatsappStickerRequest(this);
		}
	}
}
