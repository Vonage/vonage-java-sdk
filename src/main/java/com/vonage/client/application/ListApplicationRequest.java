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
package com.vonage.client.application;

import com.vonage.client.QueryParamsRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Query parameters for {@link ApplicationClient#listApplications(ListApplicationRequest)}.
 */
public class ListApplicationRequest implements QueryParamsRequest {
    private final int pageSize, page;

    private ListApplicationRequest(Builder builder) {
        pageSize = builder.pageSize;
        page = builder.page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPage() {
        return page;
    }

    @Override
    public Map<String, String> makeParams() {
        LinkedHashMap<String, String> params = new LinkedHashMap<>(4);
        if (page > 0) {
            params.put("page", String.valueOf(page));
        }
        if (pageSize > 0) {
            params.put("page_size", String.valueOf(pageSize));
        }
        return params;
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @return A new Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int pageSize, page;

        /**
         * @param pageSize The number of applications per page.
         *
         * @return This builder.
         */
        public Builder pageSize(long pageSize) {
            this.pageSize = (int) pageSize;
            return this;
        }

        /**
         * @param page The current page number, starts at 1.
         *
         * @return This builder.
         */
        public Builder page(long page) {
            this.page = (int) page;
            return this;
        }

        /**
         * @return A new {@link ListApplicationRequest} from the stored configuration.
         */
        public ListApplicationRequest build() {
            return new ListApplicationRequest(this);
        }
    }
}
