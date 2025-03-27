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

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class encapsulates a request to search for available Vonage Virtual Numbers.
 */
public class SearchNumbersFilter extends BaseNumbersFilter {
    private final Feature[] features;
    private final Type type;

    private SearchNumbersFilter(Builder builder) {
        super(builder);
        features = builder.features;
        type = builder.type;
    }

    /**
     * Desired capabilities as an array of enums.
     *
     * @return The capabilities to search for as an enum array, or {@code null} if unspecified.
     */
    public Feature[] getFeatures() {
        return features;
    }

    /**
     * Type of number to search for.
     *
     * @return The number type as an enum, or {@code null} if unspecified.
     */
    public Type getType() {
        return type;
    }

    @Override
    public Map<String, String> makeParams() {
        Map<String, String> params = super.makeParams();
        if (features != null && features.length > 0) {
            params.put("features", Arrays.stream(features).map(Feature::toString).collect(Collectors.joining(",")));
        }
        conditionalAdd("type", type.toString());
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
     * Builder for setting the parameters of SearchNumbersFilter.
     *
     * @since 8.10.0
     */
    public static final class Builder extends BaseNumbersFilter.Builder<SearchNumbersFilter, Builder> {
        private Feature[] features;
        private Type type;

        private Builder() {}

        /**
         * Set this parameter to find numbers that have one or more features.
         *
         * @param features The desired features of the number as enums.
         * @return This builder.
         */
        public Builder features(Feature... features) {
            if (features != null) {
                this.features = Arrays.stream(features).distinct().toArray(Feature[]::new);
            }
            return this;
        }

        /**
         * Set this parameter to filter the type of number, such as mobile or landline.
         *
         * @param type The type of number as an enum.
         * @return This builder.
         */
        public Builder type(Type type) {
            this.type = type;
            return this;
        }

        @Override
        public SearchNumbersFilter build() {
            return new SearchNumbersFilter(this);
        }
    }
}
