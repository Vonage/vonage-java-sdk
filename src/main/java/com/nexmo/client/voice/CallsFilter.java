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
package com.nexmo.client.voice;


import com.fasterxml.jackson.databind.util.ISO8601Utils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CallsFilter {
    private CallStatus status;
    private Date dateStart;
    private Date dateEnd;
    private Integer pageSize;
    private Integer recordIndex;
    private CallOrder order;
    private String conversationUuid;

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

    List<NameValuePair> toUrlParams() {
        List<NameValuePair> result = new ArrayList<>(10);
        conditionalAdd(result, "status", this.status);
        conditionalAdd(result, "date_start", this.dateStart);
        conditionalAdd(result, "date_end", this.dateEnd);
        conditionalAdd(result, "page_size", this.pageSize);
        conditionalAdd(result, "record_index", this.recordIndex);
        conditionalAdd(result, "order", (this.order != null) ? this.order.getCallOrder() : null);
        conditionalAdd(result, "conversation_uuid", this.conversationUuid);

        return result;
    }

    private void conditionalAdd(List<NameValuePair> params, String name, String value) {
        if (value != null) {
            params.add(new BasicNameValuePair(name, value));
        }
    }

    private void conditionalAdd(List<NameValuePair> params, String name, Date value) {
        if (value != null) {
            params.add(new BasicNameValuePair(name, ISO8601Utils.format(value)));
        }
    }

    private void conditionalAdd(List<NameValuePair> params, String name, Object value) {
        if (value != null) {
            params.add(new BasicNameValuePair(name, value.toString()));
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private CallStatus status;
        private Date dateStart;
        private Date dateEnd;
        private Integer pageSize;
        private Integer recordIndex;
        private CallOrder order;
        private String conversationUuid;

        /**
         * @param status The status of the calls to lookup.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder status(CallStatus status) {
            this.status = status;
            return this;
        }

        /**
         * @param dateStart The minimum in the date range of the calls to lookup.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder dateStart(Date dateStart) {
            this.dateStart = dateStart;
            return this;
        }

        /**
         * @param dateEnd The maximum in the date range of calls to lookup.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder dateEnd(Date dateEnd) {
            this.dateEnd = dateEnd;
            return this;
        }

        /**
         * @param pageSize The number of calls in the response.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder pageSize(Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        /**
         * @param recordIndex The starting index.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder recordIndex(Integer recordIndex) {
            this.recordIndex = recordIndex;
            return this;
        }

        /**
         * @param order The order of the calls.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder order(CallOrder order) {
            this.order = order;
            return this;
        }

        /**
         * @param conversationUuid The specific conversation to return calls for.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder conversationUuid(String conversationUuid) {
            this.conversationUuid = conversationUuid;
            return this;
        }

        /**
         * @return A new {@link CallsFilter} object with the stored builder options.
         */
        public CallsFilter build() {
            return new CallsFilter(this);
        }
    }
}