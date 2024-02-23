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
package com.vonage.client.common;

import com.vonage.client.QueryParamsRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Provides basic filtering parameters for HAL resources.
 *
 * @since 8.4.0
 */
public abstract class HalFilterRequest implements QueryParamsRequest {
	protected final Integer page, pageSize;
	protected final SortOrder order;

	protected HalFilterRequest(Builder<?, ?> builder) {
		this(builder.page, builder.pageSize, builder.order);
	}

	protected HalFilterRequest(Integer page, Integer pageSize, SortOrder order) {
		this.page = page;
		this.pageSize = pageSize;
		this.order = order;
	}

	@Override
	public Map<String, String> makeParams() {
		Map<String, String> params = new LinkedHashMap<>(4);
		if (page != null) {
			params.put("page", page.toString());
		}
		if (pageSize != null) {
			params.put("page_size", pageSize.toString());
		}
		if (order != null) {
			params.put("order", order.toString());
		}
		return params;
	}

	@SuppressWarnings("unchecked")
	protected abstract static class Builder<F extends HalFilterRequest, B extends Builder<? extends F, ? extends B>> {
		protected Integer page, pageSize;
		protected SortOrder order;

		protected B page(int page) {
			this.page = page;
			return (B) this;
		}

		protected B pageSize(int pageSize) {
			this.pageSize = pageSize;
			return (B) this;
		}

		protected B order(SortOrder order) {
			this.order = order;
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
