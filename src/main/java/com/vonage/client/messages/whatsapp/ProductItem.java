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
package com.vonage.client.messages.whatsapp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.util.Currency;
import java.util.Objects;

/**
 * Used for inbound product orders.
 *
 * @since 7.2.0
 */
public final class ProductItem extends JsonableBaseObject {
	private String productRetailerId;
	private Integer quantity;
	private Double itemPrice;
	private Currency currency;

	ProductItem() {}

	ProductItem(String productRetailerId) {
		this.productRetailerId = Objects.requireNonNull(productRetailerId, "Product SKU is required.");
	}

	/**
	 * The ID of the specific product being ordered.
	 *
	 * @return The product ID.
	 */
	@JsonProperty("product_retailer_id")
	public String getProductRetailerId() {
		return productRetailerId;
	}

	/**
	 * The quantity ordered for this specific item.
	 *
	 * @return The order quantity as an integer, or {@code null} if not applicable.
	 */
	@JsonProperty("quantity")
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * The unit price for this specific item.
	 *
	 * @return The order item price as a double, or {@code null} if not applicable.
	 */
	@JsonProperty("item_price")
	public Double getItemPrice() {
		return itemPrice;
	}

	/**
	 * The currency code representing the currency for this specific item.
	 *
	 * @return The currency for the order, or {@code null} if not applicable.
	 */
	@JsonProperty("currency")
	public Currency getCurrency() {
		return currency;
	}
}
