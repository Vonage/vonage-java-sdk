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

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.http.client.methods.RequestBuilder;

public class ListNumbersFilter {
    private Integer index;
    private Integer size;
    private String pattern;
    private SearchPattern searchPattern;

    public ListNumbersFilter() {
        this(null, null, null, null);
    }

    public ListNumbersFilter(
            @JsonProperty Integer index,
            @JsonProperty Integer size,
            @JsonProperty String pattern,
            @JsonProperty SearchPattern searchPattern) {
        this.index = index;
        this.size = size;
        this.pattern = pattern;
        this.searchPattern = searchPattern;
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

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public SearchPattern getSearchPattern() {
        return searchPattern;
    }

    public void setSearchPattern(SearchPattern searchPattern) {
        this.searchPattern = searchPattern;
    }

    public void addParams(RequestBuilder request) {
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
