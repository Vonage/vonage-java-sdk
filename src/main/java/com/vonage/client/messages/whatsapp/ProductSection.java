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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @since 7.2.0
 */
final class ProductSection extends JsonableBaseObject {
	@JsonProperty("title") final String title;
	@JsonProperty("product_items") final List<ProductItem> products;

	public ProductSection(String title, List<String> products) {
		this.title = Objects.requireNonNull(title, "Section title is required.");
		if (products == null || products.isEmpty()) {
			throw new IllegalArgumentException("At least one product is required for each section.");
		}
		this.products = products.stream().map(ProductItem::new).collect(Collectors.toList());
	}

	@JsonProperty("title")
	public String getTitle() {
		return title;
	}

	@JsonProperty("product_items")
	public List<ProductItem> getProducts() {
		return products;
	}
}
