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
package com.vonage.client.common;

import com.vonage.client.AbstractQueryParamsRequest;
import java.time.Instant;
import java.util.Map;

/**
 * Provides basic filtering parameters for HAL resources.
 *
 * @since 8.4.0
 */
public abstract class HalFilterRequest extends AbstractQueryParamsRequest {
	protected final String cursor;
	protected final Integer page, pageSize;
	protected final SortOrder order;
	protected final Instant startDate, endDate;

	protected HalFilterRequest(Builder<?, ?> builder) {
		cursor = builder.cursor;
		page = validatePage(builder.page);
		pageSize = validatePageSize(builder.pageSize);
		order = builder.order;
		startDate = builder.startDate;
		endDate = builder.endDate;
	}

	protected Integer validatePage(Integer page) {
		if (page != null && page < 1) {
			throw new IllegalArgumentException("Page must be positive.");
		}
		return page;
	}

	protected Integer validatePageSize(Integer pageSize) {
		if (pageSize != null && (pageSize < 1 || pageSize > 1000)) {
			throw new IllegalArgumentException("Page size must be between 1 and 1000.");
		}
		return pageSize;
	}

	protected HalFilterRequest(Integer page, Integer pageSize, SortOrder order) {
		this.page = validatePage(page);
		this.pageSize = validatePageSize(pageSize);
		this.order = order;
		cursor = null;
		startDate = null;
		endDate = null;
	}

	@Override
	public Map<String, String> makeParams() {
		Map<String, String> params = super.makeParams();
		conditionalAdd("cursor", cursor);
		conditionalAdd("page", page);
		conditionalAdd("page_size", pageSize);
		conditionalAdd("order", order);
		return params;
	}

	/**
	 * Page number to navigate to in the response.
	 *
	 * @return The page as an integer, or {@code null} if not specified.
	 */
	protected Integer getPage() {
		return page;
	}

	/**
	 * Number of results per page.
	 *
	 * @return The page size as an integer, or {@code null} if not specified.
	 */
	protected Integer getPageSize() {
		return pageSize;
	}

	/**
	 * Order to sort the results by.
	 *
	 * @return The result sort order as an enum, or {@code null} if not specified.
	 */
	protected SortOrder getOrder() {
		return order;
	}

	/**
	 * Filter records that occurred after this point in time.
	 *
	 * @return The start timestamp for results, or {@code null} if unspecified.
	 */
	protected Instant getStartDate() {
		return startDate;
	}

	/**
	 * Filter records that occurred before this point in time.
	 *
	 * @return The end timestamp for results, or {@code null} if unspecified.
	 */
	protected Instant getEndDate() {
		return endDate;
	}

	/**
	 * The cursor to start returning results from. This can be obtained from
	 * the URL in the relevant section from {@link HalPageResponse#getLinks()}.
	 *
	 * @return The page navigation cursor as a string, or {@code null} if unspecified.
	 */
	protected String getCursor() {
		return cursor;
	}

	@SuppressWarnings("unchecked")
	protected abstract static class Builder<F extends HalFilterRequest, B extends Builder<? extends F, ? extends B>> {
		protected String cursor;
		protected Integer page, pageSize;
		protected SortOrder order;
		protected Instant startDate, endDate;

		/**
		 * The cursor to start returning results from. This can be obtained from the URL in the
		 * relevant section from {@link HalPageResponse#getLinks()}.
		 *
		 * @param cursor The page navigation cursor as a string.
		 *
		 * @return This builder.
		 */
		protected B cursor(String cursor) {
			this.cursor = cursor;
			return (B) this;
		}

		/**
		 * Page to navigate to in the response.
		 *
		 * @param page The page as an int.
		 *
		 * @return This builder.
		 */
		protected B page(int page) {
			this.page = page;
			return (B) this;
		}

		/**
		 * Number of results per page.
		 *
		 * @param pageSize The page size as an int.
		 *
		 * @return This builder.
		 */
		protected B pageSize(int pageSize) {
			this.pageSize = pageSize;
			return (B) this;
		}

		/**
		 * Order to sort the results by.
		 *
		 * @param order The results sort order as an enum.
		 *
		 * @return This builder.
		 */
		protected B order(SortOrder order) {
			this.order = order;
			return (B) this;
		}

		/**
		 * Filter records that occurred after this point in time.
		 *
		 * @param startDate The start timestamp for results.
		 *
		 * @return This builder.
		 */
		protected B startDate(Instant startDate) {
			this.startDate = startDate;
			return (B) this;
		}

		/**
		 * Filter records that occurred before this point in time.
		 *
		 * @param endDate The end timestamp for results.
		 *
		 * @return This builder.
		 */
		protected B endDate(Instant endDate) {
			this.endDate = endDate;
			return (B) this;
		}

		/**
		 * Builds the filter request.
		 *
		 * @return A new FilterRequest with this builder's properties.
		 */
		public abstract F build();
	}
}
