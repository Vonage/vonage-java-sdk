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
package com.vonage.client.voice;

import com.vonage.client.QueryParamsRequest;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Filter options for {@link VoiceClient#listCalls(CallsFilter)}.
 */
public class CallsFilter implements QueryParamsRequest {
    private final CallStatus status;
    private final Date dateStart, dateEnd;
    private final Integer pageSize, recordIndex;
    private final CallOrder order;
    private final String conversationUuid;

    private CallsFilter(Builder builder) {
        this.status = builder.status;
        this.dateStart = builder.dateStart;
        this.dateEnd = builder.dateEnd;
        this.pageSize = builder.pageSize;
        this.recordIndex = builder.recordIndex;
        this.order = builder.order;
        this.conversationUuid = builder.conversationUuid;
    }

    public CallStatus getStatus() {
        return status;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getRecordIndex() {
        return recordIndex;
    }

    public CallOrder getOrder() {
        return order;
    }

    public String getConversationUuid() {
        return conversationUuid;
    }

    @Override
    public Map<String, String> makeParams() {
        Map<String, String> params = new LinkedHashMap<>();
        conditionalAdd(params, "status", this.status);
        conditionalAdd(params, "date_start", this.dateStart);
        conditionalAdd(params, "date_end", this.dateEnd);
        conditionalAdd(params, "page_size", this.pageSize);
        conditionalAdd(params, "record_index", this.recordIndex);
        conditionalAdd(params, "order", (this.order != null) ? this.order.getCallOrder() : null);
        conditionalAdd(params, "conversation_uuid", this.conversationUuid);
        return params;
    }

    private void conditionalAdd(Map<String, String> params, String name, String value) {
        if (value != null) {
            params.put(name, value);
        }
    }

    private void conditionalAdd(Map<String, String> params, String name, Date value) {
        if (value != null) {
            params.put(name, DateTimeFormatter.ISO_INSTANT.format(value.toInstant()));
        }
    }

    private void conditionalAdd(Map<String, String> params, String name, Object value) {
        if (value != null) {
            params.put(name, value.toString());
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private CallStatus status;
        private Date dateStart, dateEnd;
        private Integer pageSize, recordIndex;
        private CallOrder order;
        private String conversationUuid;

        Builder() {}

        /**
         * @param status The status of the calls to lookup.
         *
         * @return This Builder to keep building.
         */
        public Builder status(CallStatus status) {
            this.status = status;
            return this;
        }

        /**
         * @param dateStart The minimum in the date range of the calls to lookup.
         *
         * @return This Builder to keep building.
         */
        public Builder dateStart(Date dateStart) {
            this.dateStart = dateStart;
            return this;
        }

        /**
         * @param dateEnd The maximum in the date range of calls to lookup.
         *
         * @return This Builder to keep building.
         */
        public Builder dateEnd(Date dateEnd) {
            this.dateEnd = dateEnd;
            return this;
        }

        /**
         * @param pageSize The number of calls in the response.
         *
         * @return This Builder to keep building.
         */
        public Builder pageSize(Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        /**
         * @param recordIndex The starting index.
         *
         * @return This Builder to keep building.
         */
        public Builder recordIndex(Integer recordIndex) {
            this.recordIndex = recordIndex;
            return this;
        }

        /**
         * @param order The order of the calls.
         *
         * @return This Builder to keep building.
         */
        public Builder order(CallOrder order) {
            this.order = order;
            return this;
        }

        /**
         * @param conversationUuid The specific conversation to return calls for.
         *
         * @return This Builder to keep building.
         */
        public Builder conversationUuid(String conversationUuid) {
            this.conversationUuid = conversationUuid;
            return this;
        }

        /**
         * Constructs an instance of {@linkplain CallsFilter}.
         *
         * @return A new {@link CallsFilter} object with the stored builder options.
         */
        public CallsFilter build() {
            return new CallsFilter(this);
        }
    }
}