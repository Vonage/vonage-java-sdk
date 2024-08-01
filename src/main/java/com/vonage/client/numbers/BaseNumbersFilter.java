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
 * Base class for {@linkplain ListNumbersFilter} and {@linkplain SearchNumbersFilter}.
 *
 * @since 8.10.0
 */
abstract class BaseNumbersFilter implements QueryParamsRequest {
    private final String country;
    private Integer index, size;
    private String pattern;
    private SearchPattern searchPattern;

    BaseNumbersFilter(Builder<?, ?> builder) {
        if ((index = builder.index) != null && index < 1) {
            throw new IllegalArgumentException("'index' must be positive.");
        }
        if ((size = builder.size) != null && (size < 1 || size > 100)) {
            throw new IllegalArgumentException("'size' must be between 1 and 100.");
        }
        country = BaseNumberRequest.validateCountry(builder.country);
        pattern = builder.pattern;
        searchPattern = builder.searchPattern;
    }

    public Integer getIndex() {
        return index;
    }

    public Integer getSize() {
        return size;
    }

    /**
     * Two character country code in ISO 3166-1 alpha-2 format.
     *
     * @return The number's country code.
     */
    public String getCountry() {
        return country;
    }

    public String getPattern() {
        return pattern;
    }

    public SearchPattern getSearchPattern() {
        return searchPattern;
    }

    @Deprecated
    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * Set the maximum number of matching results to be returned.
     *
     * @param size An Integer between 10 and 100 (inclusive) or null, to indicate that the default value should be
     *             used.
     */
    @Deprecated
    public void setSize(Integer size) {
        this.size = size;
    }

    @Deprecated
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * @param searchPattern The pattern you want to search for. Use the * wildcard to match the start or end of the number.
     *                      For example, *123* matches all numbers that contain the pattern 123.
     */
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
        if (country != null) {
            params.put("country", country);
        }
        return params;
    }

    @SuppressWarnings("unchecked")
    public static abstract class Builder<F extends BaseNumbersFilter, B extends Builder<F, B>> {
        private Integer index, size;
        private String pattern, country;
        private SearchPattern searchPattern;

        Builder() {}


        /**
         * TODO description
         *
         * @param country The TODO.
         * @return This builder.
         */
        public B country(String country) {
            this.country = country;
            return (B) this;
        }

        /**
         * TODO description
         *
         * @param index The TODO.
         * @return This builder.
         */
        public B index(int index) {
            this.index = index;
            return (B) this;
        }

        /**
         * TODO description
         *
         * @param size The TODO.
         * @return This builder.
         */
        public B size(int size) {
            this.size = size;
            return (B) this;
        }

        /**
         * TODO description
         *
         * @param strategy
         * @param pattern The TODO.
         * @return This builder.
         */
        public B pattern(SearchPattern strategy, String pattern) {
            this.searchPattern = Objects.requireNonNull(strategy, "Matching strategy is required.");
            this.pattern = Objects.requireNonNull(pattern, "Pattern is required");
            return (B) this;
        }

        /**
         * Builds the filter request parameters.
         *
         * @return A new number filter with this builder's properties.
         */
        public abstract F build();
    }
}
