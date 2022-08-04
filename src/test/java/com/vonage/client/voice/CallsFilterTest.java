/*
 *   Copyright 2022 Vonage
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

import org.apache.http.NameValuePair;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class CallsFilterTest {
    @Test
    public void testAllParams() {
        Calendar startCalendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        startCalendar.set(2016, Calendar.JANUARY, 1, 7, 8, 20);
        startCalendar.set(Calendar.MILLISECOND, 0);
        Date startDate = startCalendar.getTime();
        Calendar endCalendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        endCalendar.set(2016, Calendar.JANUARY, 1, 7, 8, 55);
        endCalendar.set(Calendar.MILLISECOND, 0);
        Date endDate = endCalendar.getTime();

        CallsFilter filter = CallsFilter.builder()
                .status(CallStatus.COMPLETED)
                .dateStart(startDate)
                .dateEnd(endDate)
                .recordIndex(12)
                .order(CallOrder.ASCENDING)
                .pageSize(10)
                .conversationUuid("this-is-not-a-uuid")
                .build();

        assertEquals(CallStatus.COMPLETED, filter.getStatus());
        assertEquals(startDate, filter.getDateStart());
        assertEquals(endDate, filter.getDateEnd());
        assertEquals(12, (int) filter.getRecordIndex());
        assertEquals(CallOrder.ASCENDING, filter.getOrder());
        assertEquals(10, (int) filter.getPageSize());
        assertEquals("this-is-not-a-uuid", filter.getConversationUuid());


        List<NameValuePair> params = filter.toUrlParams();
        Map<String, String> paramLookup = new HashMap<>();
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
        CallsFilter filter = CallsFilter.builder().build();
        List<NameValuePair> params = filter.toUrlParams();
        assertEquals(0, params.size());
    }

    @Test
    public void testStaticBuilderProducesNewBuilder() {
        assertNotSame(CallsFilter.builder(), CallsFilter.builder());
    }
}