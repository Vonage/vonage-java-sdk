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
package com.vonage.client.verify;

import com.vonage.client.QueryParamsRequest;
import java.util.LinkedHashMap;
import java.util.Map;

public class SearchRequest implements QueryParamsRequest {
    /**
     * Number of maximum request IDs that can be searched for.
     */
    private static final int MAX_SEARCH_REQUESTS = 10;

    private final String[] requestIds;

    public SearchRequest(final String... requestIds) {
        if (requestIds.length == 0) {
            throw new IllegalArgumentException("At least one request ID must be provided in a SearchRequest");
        }
        else if (requestIds.length > MAX_SEARCH_REQUESTS) {
            throw new IllegalArgumentException("Too many request IDs. Max is " + MAX_SEARCH_REQUESTS);
        }
        this.requestIds = requestIds;
    }

    public String[] getRequestIds() {
        return requestIds;
    }

    @Override
    public Map<String, ?> makeParams() {
        Map<String, Object> params = new LinkedHashMap<>(requestIds.length);
        if (requestIds.length == 1) {
            params.put("request_id", requestIds[0]);
        }
        else {
            params.put("request_ids", requestIds);
        }
        return params;
    }
}
