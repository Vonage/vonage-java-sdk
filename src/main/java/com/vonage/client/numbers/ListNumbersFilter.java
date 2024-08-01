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
package com.vonage.client.numbers;

import com.vonage.client.QueryParamsRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Filter criteria used in {@link NumbersClient#listNumbers(ListNumbersFilter)}.
 */
public class ListNumbersFilter implements QueryParamsRequest {
    private Integer index, size;
    private String pattern;
    private SearchPattern searchPattern;
    private Type type;

    private ListNumbersFilter(Builder builder) {
        if ((index = builder.index) != null && index < 1) {
            throw new IllegalArgumentException("'index' must be positive.");
        }
        if ((size = builder.size) != null && (size < 1 || size > 100)) {
            throw new IllegalArgumentException("'size' must be between 1 and 100.");
        }
        pattern = builder.pattern;
        searchPattern = builder.searchPattern;
        type = builder.type;
    }

    @Deprecated
    public ListNumbersFilter() {
        this(null, null, null, null);
    }

    @Deprecated
    public ListNumbersFilter(
            Integer index,
            Integer size,
            String pattern,
            SearchPattern searchPattern) {
        this.index = index;
        this.size = size;
        this.pattern = pattern;
        this.searchPattern = searchPattern;
    }

    public Integer getIndex() {
        return index;
    }

    public Integer getSize() {
        return size;
    }

    public String getPattern() {
        return pattern;
    }

    public SearchPattern getSearchPattern() {
        return searchPattern;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setSearchPattern(SearchPattern searchPattern) {
        this.searchPattern = searchPattern;
    }

    @Override
    public Map<String, String> makeParams() {
        LinkedHashMap<String, String> params = new LinkedHashMap<>(8);
        if (index != null) {
            params.put("index", index.toString());
        }
        if (size != null) {
            params.put("size", size.toString());
        }
        if (pattern != null) {
            params.put("pattern", pattern);
        }
        if (searchPattern != null) {
            params.put("search_pattern", Integer.toString(searchPattern.getValue()));
        }
        return params;
    }

    /**
     * Entrypoint for constructing an instance of this class.
     *
     * @return A new Builder.
     * @since 8.10.0
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for setting the parameters of ListNumbersFilter.
     *
     * @since 8.10.0
     */
    public static final class Builder {
        private Integer index, size;
        private String pattern;
        private SearchPattern searchPattern;
        private Type type;

        private Builder() {}

        /**
         * TODO description
         *
         * @param index The TODO.
         * @return This builder.
         */
        public Builder index(Integer index) {
            this.index = index;
            return this;
        }

        /**
         * TODO description
         *
         * @param size The TODO.
         * @return This builder.
         */
        public Builder size(Integer size) {
            this.size = size;
            return this;
        }

        /**
         * TODO description
         *
         * @param strategy
         * @param pattern The TODO.
         * @return This builder.
         */
        public Builder pattern(SearchPattern strategy, String pattern) {
            this.searchPattern = Objects.requireNonNull(strategy, "Matching strategy is required.");
            this.pattern = Objects.requireNonNull(pattern, "Pattern is required");
            return this;
        }

        /**
         * TODO description
         *
         * @param type The TODO.
         * @return This builder.
         */
        public Builder type(Type type) {
            this.type = type;
            return this;
        }

        /**
         * Builds the ListNumbersFilter request parameters.
         *
         * @return A new ListNumbersFilter instance with this builder's properties.
         */
        public ListNumbersFilter build() {
            return new ListNumbersFilter(this);
        }
    }
}
