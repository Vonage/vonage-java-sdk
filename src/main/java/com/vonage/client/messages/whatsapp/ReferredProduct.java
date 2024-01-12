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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

/**
 * Contains details of a product from a product message being quoted or replied to
 * using the 'Message Business' option.
 *
 * @since 7.2.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ReferredProduct extends JsonableBaseObject {
	private String productRetailerId, catalogId;

	ReferredProduct() {}

	/**
	 * The ID of the product from the product message being quoted or replied to
	 * using the 'Message Business' option.
	 *
	 * @return The product ID.
	 */
	@JsonProperty("product_retailer_id")
	public String getProductRetailerId() {
		return productRetailerId;
	}

	/**
	 * The ID of the catalog associated with the product from the product message being quoted or replied to
	 * using the 'Message Business' option.
	 *
	 * @return The product catalog ID.
	 */
	@JsonProperty("catalog_id")
	public String getCatalogId() {
		return catalogId;
	}
}
