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
package com.vonage.client.users;

import com.vonage.client.QueryParamsRequest;
import com.vonage.client.common.HalLinks;
import com.vonage.client.common.SortOrder;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Query parameters for {@link UsersClient#listUsers(ListUsersRequest)}.
 */
public final class ListUsersRequest implements QueryParamsRequest {
    private final int pageSize;
    private final SortOrder order;
    private final String name, cursor;

    private ListUsersRequest(Builder builder) {
        if ((pageSize = builder.pageSize) < 1 || pageSize > 100) {
            throw new IllegalArgumentException("Page size must be between 1 and 100.");
        }
        order = builder.order;
        name = builder.name;
        cursor = parseCursor(builder.cursor);
    }

    static String parseCursor(URI cursor) {
        if (cursor == null) return null;
        String query = cursor.getRawQuery();
        return query.substring(query.indexOf("cursor=") + 7);
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
        if (cursor != null) {
            params.put("cursor", cursor);
        }
        return params;
    }

    /**
     * Number of records to return in the response. Default is 10.
     *
     * @return The number of results to return.
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * The time order to return results in. Default is ascending.
     *
     * @return The order to return results in as an enum.
     */
    public SortOrder getOrder() {
        return order;
    }

    /**
     * Unique name for a user to filter by.
     *
     * @return The username to search for, or {@code null} if not specified.
     */
    public String getName() {
        return name;
    }

    /**
     * The cursor to start returning results from, as derived from the {@code _links} section of a response.
     *
     * @return The cursor key to search from, or {@code null} if not specified.
     */
    public String getCursor() {
        return cursor;
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
        private int pageSize = 10;
        private SortOrder order = SortOrder.ASCENDING;
        private String name;
        private URI cursor;

        Builder() {}

        /**
         * Number of records to return in the response. Maximum is 100, default is 10.
         *
         * @param pageSize Number of results to return.
         *
         * @return This builder.
         */
        public Builder pageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        /**
         * The order to return the results in. Users are sorted by their creation time.
         * Default is ascending - i.e. newest to oldest. Use {@linkplain SortOrder#DESC} to sort
         * the results from oldest to newest.
         *
         * @param order The sort order for results as an enum.
         *
         * @return This builder.
         */
        public Builder order(SortOrder order) {
            this.order = order;
            return this;
        }

        /**
         * Find user by their name.
         *
         * @param name Unique user name.
         *
         * @return This builder.
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * The cursor to start returning results from.<br>
         *
         * You are not expected to provide this manually. Instead, this will be parsed from the response
         * page's links. You need to provide the URL of the page. This can be obtained from
         * {@link ListUsersResponse#getLinks()}, on which you would then call either
         * {@linkplain HalLinks#getNextUrl()} or {@linkplain HalLinks#getPrevUrl()} and pass it to this method.
         *
         * @param cursor Page link URL containing the "cursor" query parameter.
         *
         * @return This builder.
         */
        public Builder cursor(URI cursor) {
            this.cursor = cursor;
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
