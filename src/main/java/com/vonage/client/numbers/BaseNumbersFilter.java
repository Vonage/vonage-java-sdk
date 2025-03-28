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
package com.vonage.client.numbers;

import com.vonage.client.AbstractQueryParamsRequest;
import java.util.Map;
import java.util.Objects;

/**
 * Base class for {@linkplain ListNumbersFilter} and {@linkplain SearchNumbersFilter}.
 *
 * @since 8.10.0
 */
abstract class BaseNumbersFilter extends AbstractQueryParamsRequest {
    private final Integer index, size;
    private final String pattern, country;
    private final SearchPattern searchPattern;

    BaseNumbersFilter(Builder<?, ?> builder) {
        if ((index = builder.index) != null && index < 1) {
            throw new IllegalArgumentException("'index' must be positive.");
        }
        if ((size = builder.size) != null && (size < 1 || size > 100)) {
            throw new IllegalArgumentException("'size' must be between 1 and 100.");
        }
        country = builder.country != null ? BaseNumberRequest.validateCountry(builder.country) : null;
        pattern = builder.pattern;
        searchPattern = builder.searchPattern;
    }

    /**
     * Page index to start return results from. Default is 1.
     *
     * @return The cursor index, or {@code null} if unspecified.
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * Page size. Default is 10, max is 100.
     *
     * @return The number of results to return, or {@code null} if unspecified.
     */
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

    /**
     * Number pattern to narrow down the results by.
     *
     * @return The number pattern, or {@code null} if unspecified.
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * Strategy to use for matching.
     *
     * @return The pattern matching strategy as an enum, or {@code null} if unspecified.
     */
    public SearchPattern getSearchPattern() {
        return searchPattern;
    }

    @Override
    public Map<String, String> makeParams() {
        Map<String, String> params = super.makeParams();
        conditionalAdd("index", index);
        conditionalAdd("size", size);
        conditionalAdd("pattern", pattern);
        if (searchPattern != null) {
            conditionalAdd("search_pattern", searchPattern.getValue());
        }
        conditionalAdd("country", country);
        return params;
    }

    @SuppressWarnings("unchecked")
    public static abstract class Builder<F extends BaseNumbersFilter, B extends Builder<F, B>> {
        private Integer index, size;
        private String pattern, country;
        private SearchPattern searchPattern;

        Builder() {}


        /**
         * Two character country code in ISO 3166-1 alpha-2 format.
         *
         * @param country The country code.
         * @return This builder.
         */
        public B country(String country) {
            this.country = country;
            return (B) this;
        }

        /**
         * Page index to start return results from. Default is 1.
         *
         * @param index The page index, starting from 1.
         * @return This builder.
         */
        public B index(int index) {
            this.index = index;
            return (B) this;
        }

        /**
         * Page size. Default is 10, max is 100.
         *
         * @param size The number of results to return in the response.
         * @return This builder.
         */
        public B size(int size) {
            this.size = size;
            return (B) this;
        }

        /**
         * Use this method to filter numbers matching the specified pattern.
         *
         * @param strategy Strategy to use for matching.
         * @param pattern Number pattern to narrow down the results by. Use the * wildcard to match the start or
         *                end of the number. For example, *123* matches all numbers that contain the pattern 123.
         *
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
