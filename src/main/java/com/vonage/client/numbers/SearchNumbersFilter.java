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
package com.vonage.client.numbers;

import com.vonage.client.QueryParamsRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class encapsulates a request to search for available Vonage Virtual Numbers.
 */
public class SearchNumbersFilter implements QueryParamsRequest {
    private final String country;

    private String pattern;
    private SearchPattern searchPattern;
    private String[] features;
    private Integer index, size;
    private Type type;

    /**
     * Construct a request with the only required parameter, the country code.
     *
     * @param country A String containing a two-character country code.
     */
    public SearchNumbersFilter(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String[] getFeatures() {
        return features;
    }

    public void setFeatures(String[] features) {
        this.features = features;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getSize() {
        return size;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    /**
     * Set the maximum number of matching results to be returned.
     *
     * @param size An Integer between 10 and 100 (inclusive) or null, to indicate that the default value should be
     *             used.
     */
    public void setSize(Integer size) {
        this.size = size;
    }

    public SearchPattern getSearchPattern() {
        return searchPattern;
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
        params.put("country", country);
        if (features != null && features.length > 0) {
            params.put("features", String.join(",", features));
        }
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
        if (type != null) {
            params.put("type", type.getType());
        }
        return params;
    }
}
