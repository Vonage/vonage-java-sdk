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
package com.vonage.client.voice;

import com.vonage.client.common.HalFilterRequest;
import com.vonage.client.common.SortOrder;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * Filter options for {@link VoiceClient#listCalls(CallsFilter)}.
 */
public class CallsFilter extends HalFilterRequest {
    private final CallStatus status;
    private final Integer recordIndex;
    private final String conversationUuid;

    private CallsFilter(Builder builder) {
        super(builder);
        this.status = builder.status;
        this.recordIndex = builder.recordIndex;
        this.conversationUuid = builder.conversationUuid;
    }

    @Override
    public Integer getPageSize() {
        return super.getPageSize();
    }

    @Override
    public Instant getStartDate() {
        return super.getStartDate();
    }

    @Override
    public Instant getEndDate() {
        return super.getEndDate();
    }

    @Override
    public SortOrder getOrder() {
        return super.getOrder();
    }

    /**
     * Minimum in the date range of the calls to lookup.
     *
     * @return The start date, or {@code null} if unspecified.
     *
     * @deprecated Use {@link #getStartDate()} instead. This will be removed in a future release.
     */
    @Deprecated
    public Date getDateStart() {
        return Date.from(getStartDate());
    }

    /**
     * Maximum in the date range of calls to lookup.
     *
     * @return The end date, or {@code null} if unspecified.
     *
     * @deprecated Use {@link #getEndDate()} instead. This will be removed in a future release.
     */
    @Deprecated
    public Date getDateEnd() {
        return Date.from(getEndDate());
    }

    /**
     * Status of the calls to lookup.
     *
     * @return The call status as an enum, or {@code null} if unspecified.
     */
    public CallStatus getStatus() {
        return status;
    }

    /**
     * Start index for the results.
     *
     * @return The starting index as an integer, or {@code null} if unspecified.
     */
    public Integer getRecordIndex() {
        return recordIndex;
    }

    /**
     * ID of the conversation to filter by.
     *
     * @return The conversation ID, or {@code null} if unspecified.
     */
    public String getConversationUuid() {
        return conversationUuid;
    }

    @Override
    public Map<String, String> makeParams() {
        Map<String, String> params = super.makeParams();
        conditionalAdd(params, "status", status);
        conditionalAdd(params, "date_start", startDate);
        conditionalAdd(params, "date_end", endDate);
        conditionalAdd(params, "record_index", recordIndex);
        conditionalAdd(params, "conversation_uuid", conversationUuid);
        return params;
    }

    /**
     * Entrypoint for constructing an instance of this class. All fields are optional.
     *
     * @return A new Builder instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for specifying the call filter query parameters.
     */
    public static final class Builder extends HalFilterRequest.Builder<CallsFilter, Builder> {
        private CallStatus status;
        private Integer recordIndex;
        private String conversationUuid;

        Builder() {
        }

        /**
         * Status of the calls to lookup.
         *
         * @param status The call status as an enum.
         *
         * @return This builder.
         */
        public Builder status(CallStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Start index for the results.
         *
         * @param recordIndex The starting index as an integer.
         *
         * @return This builder.
         */
        public Builder recordIndex(int recordIndex) {
            this.recordIndex = recordIndex;
            return this;
        }

        /**
         * Specific conversation to return calls for.
         *
         * @param conversationUuid The conversation ID to filter by.
         *
         * @return This builder.
         */
        public Builder conversationUuid(String conversationUuid) {
            this.conversationUuid = conversationUuid;
            return this;
        }

        @Override
        public Builder pageSize(int pageSize) {
            return super.pageSize(pageSize);
        }

        @Override
        public Builder order(SortOrder order) {
            return super.order(order);
        }

        @Override
        public Builder startDate(Instant startDate) {
            return super.startDate(startDate);
        }

        @Override
        public Builder endDate(Instant endDate) {
            return super.endDate(endDate);
        }

        /**
         * Minimum in the date range of the calls to lookup.
         *
         * @param dateStart The start date.
         *
         * @return This builder.
         *
         * @deprecated Use {@link #startDate(Instant)} instead. This will be removed in a future release.
         */
        @Deprecated
        public Builder dateStart(Date dateStart) {
            return dateStart != null ? super.startDate(dateStart.toInstant()) : this;
        }

        /**
         * Maximum in the date range of calls to lookup.
         *
         * @param dateEnd The end date.
         *
         * @return This builder.
         *
         * @deprecated Use {@link #endDate(Instant)} instead. This will be removed in a future release.
         */
        @Deprecated
        public Builder dateEnd(Date dateEnd) {
            return dateEnd != null ? super.endDate(dateEnd.toInstant()) : this;
        }

        /**
         * Number of results to include in the response.
         *
         * @param pageSize The number of calls in the response.
         *
         * @return This builder.
         *
         * @deprecated Use {@link #pageSize(int)} instead. This will be removed in a future release.
         */
        @Deprecated
        public Builder pageSize(Integer pageSize) {
            return pageSize != null ? super.pageSize(pageSize) : this;
        }

        /**
         * Start index for the results.
         *
         * @param recordIndex The starting index as an integer.
         *
         * @return This builder.
         *
         * @deprecated Use {@link #recordIndex(int)} instead. This will be removed in a future release.
         */
        @Deprecated
        public Builder recordIndex(Integer recordIndex) {
            this.recordIndex = recordIndex;
            return this;
        }

        /**
         * Order of the returned calls.
         *
         * @param order The sort order as an enum.
         *
         * @return This builder.
         *
         * @deprecated Use {@link #order(SortOrder)} instead. This will be removed in a future release.
         */
        @Deprecated
        public Builder order(CallOrder order) {
            return order != null ? order(SortOrder.fromString(order.name())) : this;
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