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
import com.vonage.client.messages.MessageType;
import java.util.*;

/**
 * See <a href=https://developer.vonage.com/en/messages/concepts/whatsapp-product-messages>
 * WhatsApp Product Messages documentation for details.</a>
 *
 * @since 7.2.0
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public final class WhatsappSingleProductRequest extends WhatsappRequest {
	final Map<String, Object> custom;

	WhatsappSingleProductRequest(Builder builder) {
		super(builder, MessageType.CUSTOM);
		Map<String, Object> action = new LinkedHashMap<>(4);
		action.put("catalog_id", Objects.requireNonNull(
				builder.catalogId, "Catalog ID is required."
		));
		action.put("product_retailer_id", Objects.requireNonNull(
				builder.productRetailerId, "Product retailer ID is required."
		));
		Map<String, Object> interactive = new LinkedHashMap<>(8);
		interactive.put("type", "product");
		if (builder.bodyText != null) {
			interactive.put("body", Collections.singletonMap("text", builder.bodyText));
		}
		if (builder.footerText != null) {
			interactive.put("footer", Collections.singletonMap("text", builder.footerText));
		}
		interactive.put("action", action);
		custom = new LinkedHashMap<>(4);
		custom.put("type", "interactive");
		custom.put("interactive", interactive);
	}

	@JsonProperty("custom")
	public Map<?, ?> getCustom() {
		return custom;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends WhatsappRequest.Builder<WhatsappSingleProductRequest, Builder> {
		String bodyText, footerText, catalogId, productRetailerId;

		Builder() {}

		/**
		 * (REQUIRED)
		 * ID for the catalog you want to use for this message. Retrieve this ID via Commerce Manager.
		 *
		 * @param catalogId The catalog ID.
		 * @return This builder.
		 */
		public Builder catalogId(String catalogId) {
			this.catalogId = catalogId;
			return this;
		}

		/**
		 * (REQUIRED)
		 * A product’s unique identifier.
		 *
		 * @param productRetailerId The product ID.
		 * @return This builder.
		 */
		public Builder productRetailerId(String productRetailerId) {
			this.productRetailerId = productRetailerId;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * The main message text.
		 *
		 * @param bodyText The body text.
		 * @return This builder.
		 */
		public Builder bodyText(String bodyText) {
			this.bodyText = bodyText;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * The text which appears at the end of the message.
		 *
		 * @param footerText The footer text.
		 * @return This builder.
		 */
		public Builder footerText(String footerText) {
			this.footerText = footerText;
			return this;
		}

		@Override
		public WhatsappSingleProductRequest build() {
			return new WhatsappSingleProductRequest(this);
		}
	}
}
