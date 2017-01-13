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

import com.nexmo.client.voice.CallsFilter;
import org.apache.http.NameValuePair;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CallsFilterTest {
    @Test
    public void testToUrlParams() {
        CallsFilter filter = new CallsFilter();
        filter.setStatus("completed");
        filter.setDateStart(new GregorianCalendar(2016, Calendar.JANUARY, 1, 7, 8, 20).getTime());
        filter.setPageSize(10);

        List<NameValuePair> params = filter.toUrlParams();
        Map<String, String> paramLookup = new HashMap<String, String>();
        for (NameValuePair pair : params) {
            paramLookup.put(pair.getName(), pair.getValue());
        }

        assertEquals("completed", paramLookup.get("status"));
        assertNull(paramLookup.get("date_end"));
        assertEquals("2016-01-01T07:08:20Z", paramLookup.get("date_start"));
        assertEquals("10", paramLookup.get("page_size"));
    }
}
