package com.nexmo.client.voice;

/*
 * Copyright (c) 2011-2016 Nexmo Inc
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
    private String order;
    private String conversationUuid;

    public CallStatus getStatus() {
        return status;
    }

    public void setStatus(CallStatus status) {
        this.status = status;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getRecordIndex() {
        return recordIndex;
    }

    public void setRecordIndex(Integer recordIndex) {
        this.recordIndex = recordIndex;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getConversationUuid() {
        return conversationUuid;
    }

    public void setConversationUuid(String conversationUuid) {
        this.conversationUuid = conversationUuid;
    }

    public List<NameValuePair> toUrlParams() {
        List<NameValuePair> result = new ArrayList<NameValuePair>(10);
        conditionalAdd(result, "status", this.status);
        conditionalAdd(result, "date_start", this.dateStart);
        conditionalAdd(result, "date_end", this.dateEnd);
        conditionalAdd(result, "page_size", this.pageSize);
        conditionalAdd(result, "record_index", this.recordIndex);
        conditionalAdd(result, "order", this.order);
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
}
