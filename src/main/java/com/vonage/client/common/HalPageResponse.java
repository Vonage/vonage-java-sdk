/*
 *   Copyright 2023 Vonage
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
package com.vonage.client.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Abstract base class for responses that conform to the
 * <a href=https://datatracker.ietf.org/doc/html/draft-kelly-json-hal-07>HAL specification</a>.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class HalPageResponse {
	protected Integer page, pageSize, totalItems, totalPages;
	private HalLinks links;

	/**
	 * Current page.
	 *
	 * @return The current page number, or {@code null} if not applicable.
	 */
	@JsonProperty("page")
	public Integer getPage() {
		return page;
	}

	/**
	 * Size of each page.
	 *
	 * @return Number of results per page, or {@code null} if not applicable.
	 */
	@JsonProperty("page_size")
	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * Size of this page.
	 *
	 * @return Number of results on this page, or {@code null} if not applicable.
	 */
	@JsonProperty("total_items")
	public Integer getTotalItems() {
		return totalItems;
	}

	/**
	 * Number of results pages.
	 *
	 * @return Total number of available pages, or {@code null} if not applicable.
	 */
	@JsonProperty("total_pages")
	public Integer getTotalPages() {
		return totalPages;
	}

	/**
	 * The {@code _links} property in the HAL response.
	 *
	 * @return Navigation links wrapped in an object.
	 */
	@JsonProperty("_links")
	public HalLinks getLinks() {
		return links;
	}
}
