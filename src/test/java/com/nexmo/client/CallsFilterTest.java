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

package com.nexmo.client;

import com.nexmo.client.voice.CallStatus;
import com.nexmo.client.voice.CallsFilter;
import org.apache.http.NameValuePair;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class CallsFilterTest {
    @Test
    public void testAllParams() {
        /*
    private Integer recordIndex;
    private String order;
         */

        CallsFilter filter = new CallsFilter();
        filter.setStatus(CallStatus.COMPLETED);
        filter.setDateStart(new GregorianCalendar(2016, Calendar.JANUARY, 1, 7, 8, 20).getTime());
        filter.setDateEnd(new GregorianCalendar(2016, Calendar.JANUARY, 1, 7, 8, 55).getTime());
        filter.setRecordIndex(12);
        filter.setOrder("asc");
        filter.setPageSize(10);
        filter.setConversationUuid("this-is-not-a-uuid");

        List<NameValuePair> params = filter.toUrlParams();
        Map<String, String> paramLookup = new HashMap<String, String>();
        for (NameValuePair pair : params) {
            paramLookup.put(pair.getName(), pair.getValue());
        }

        assertEquals("completed", paramLookup.get("status"));
        assertEquals("2016-01-01T07:08:55Z", paramLookup.get("date_end"));
        assertEquals("2016-01-01T07:08:20Z", paramLookup.get("date_start"));
        assertEquals("12", paramLookup.get("record_index"));
        assertEquals("asc", paramLookup.get("order"));
        assertEquals("10", paramLookup.get("page_size"));
        assertEquals("this-is-not-a-uuid", paramLookup.get("conversation_uuid"));
    }

    @Test
    public void testNoParams() throws Exception {
        CallsFilter filter = new CallsFilter();
        List<NameValuePair> params = filter.toUrlParams();
        assertEquals(0, params.size());
    }
}
