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
import org.apache.http.client.methods.RequestBuilder;
import java.util.LinkedHashMap;
import java.util.Map;

public class ListNumbersFilter implements QueryParamsRequest {
    private Integer index, size;
    private String pattern;
    private SearchPattern searchPattern;

    public ListNumbersFilter() {
        this(null, null, null, null);
    }

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

    @Deprecated
    public void addParams(RequestBuilder request) {
        makeParams().forEach(request::addParameter);
    }

    @Override
    public Map<String, String> makeParams() {
        LinkedHashMap<String, String> params = new LinkedHashMap<>(4);
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
}
