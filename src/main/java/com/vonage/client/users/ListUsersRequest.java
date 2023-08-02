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
package com.vonage.client.users;

import com.vonage.client.QueryParamsRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Query parameters for {@link UsersClient#listUsers(ListUsersRequest)}.
 */
public final class ListUsersRequest implements QueryParamsRequest {
    private final int pageSize;
    private final SortOrder order;
    private final String name;

    private ListUsersRequest(Builder builder) {
        if ((pageSize = builder.pageSize) < 1 || pageSize > 100) {
            throw new IllegalArgumentException("Page size must be between 1 and 100.");
        }
        order = builder.order;
        name = builder.name;
    }

    @Override
    public Map<String, String> makeParams() {
        LinkedHashMap<String, String> params = new LinkedHashMap<>(4);
        params.put("page_size", String.valueOf(pageSize));
        if (order != null) {
            params.put("order", order.toString());
        }
        if (name != null) {
            params.put("name", name);
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
        private int pageSize = 100;
        private SortOrder order;
        private String name;

        Builder() {}

        public Builder pageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder order(SortOrder order) {
            this.order = order;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return A new ListUsersRequest with this builder's properties.
         */
        public ListUsersRequest build() {
            return new ListUsersRequest(this);
        }
    }
}
