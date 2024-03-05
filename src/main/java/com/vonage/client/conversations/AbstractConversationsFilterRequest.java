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
package com.vonage.client.conversations;

import com.vonage.client.common.HalFilterRequest;
import com.vonage.client.common.SortOrder;
import java.time.Instant;
import java.util.Map;

abstract class AbstractConversationsFilterRequest extends HalFilterRequest {

    protected AbstractConversationsFilterRequest(Builder<
            ? extends AbstractConversationsFilterRequest,
            ? extends Builder<?, ?>> builder) {
        super(builder);
    }

    @Override
    protected Integer validatePageSize(Integer pageSize) {
        if (pageSize != null && (pageSize < 1 || pageSize > 100)) {
            throw new IllegalArgumentException("Page size must be between 1 and 100.");
        }
        return pageSize;
    }

    protected String formatTimestamp(Instant time) {
        return time.toString()
                .replace('T', ' ')
                .replace("Z", "");
    }

    @Override
    public Map<String, String> makeParams() {
        Map<String, String> params = super.makeParams();
        if (startDate != null) {
            params.put("date_start", formatTimestamp(startDate));
        }
        if (endDate != null) {
            params.put("date_end", formatTimestamp(endDate));
        }
        return params;
    }

    @Override
    public Integer getPageSize() {
        return super.getPageSize();
    }

    @Override
    public SortOrder getOrder() {
        return super.getOrder();
    }

    protected abstract static class Builder<
                F extends AbstractConversationsFilterRequest, B extends Builder<? extends F, ? extends B>>
            extends HalFilterRequest.Builder<F, B> {

        Builder() {}

        @Override
        public B pageSize(int pageSize) {
            return super.pageSize(pageSize);
        }

        @Override
        public B order(SortOrder order) {
            return super.order(order);
        }
    }
}
