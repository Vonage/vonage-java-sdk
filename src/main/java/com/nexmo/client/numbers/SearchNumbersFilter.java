/*
 * Copyright (c) 2011-2017 Nexmo Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nexmo.client.numbers;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.RequestBuilder;

public class SearchNumbersFilter {
    private final String country;

    private String pattern;
    private ListNumbersFilter.SearchPattern searchPattern;
    private String[] features;
    private Integer index;
    private Integer size;

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

    public void setSize(Integer size) {
        this.size = size;
    }

    public ListNumbersFilter.SearchPattern getSearchPattern() {
        return searchPattern;
    }

    public void setSearchPattern(ListNumbersFilter.SearchPattern searchPattern) {
        this.searchPattern = searchPattern;
    }

    public void addParams(RequestBuilder request) {
        request.addParameter("country", country);
        if (features != null && features.length > 0) {
            request.addParameter("features", StringUtils.join(",", features));
        }
        if (index != null) {
            request.addParameter("index", index.toString());
        }
        if (size != null) {
            request.addParameter("size", size.toString());
        }
        if (pattern != null) {
            request.addParameter("pattern", pattern);
        }
        if (searchPattern != null) {
            request.addParameter("search_pattern", Integer.toString(searchPattern.getValue()));
        }
    }
}
