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

import com.vonage.client.common.SortOrder;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Instant;
import java.util.*;

public class CallsFilterTest {

    @Test
    public void testAllDeprecatedParams() {
        Calendar startCalendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        startCalendar.set(2016, Calendar.JANUARY, 1, 7, 8, 20);
        startCalendar.set(Calendar.MILLISECOND, 0);
        Date startDate = startCalendar.getTime();
        Calendar endCalendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        endCalendar.set(2016, Calendar.JANUARY, 1, 7, 8, 55);
        endCalendar.set(Calendar.MILLISECOND, 0);
        Date endDate = endCalendar.getTime();
        Integer pageSize = 10, recordIndex = 12;
        String conversationUuid = "this-is-not-a-uuid";
        CallStatus status = CallStatus.COMPLETED;
        CallOrder order = CallOrder.ASCENDING;

        CallsFilter filter = CallsFilter.builder()
                .order((CallOrder) null).order(order)
                .dateStart(null).dateStart(startDate)
                .dateEnd(null).dateEnd(endDate)
                .recordIndex(null).recordIndex(recordIndex)
                .pageSize(null).pageSize(pageSize)
                .conversationUuid(conversationUuid)
                .status(status).build();

        assertEquals(startDate, filter.getDateStart());
        assertEquals(endDate, filter.getDateEnd());

        Map<String, String> paramLookup = filter.makeParams();
        assertEquals(7, paramLookup.size());
        assertEquals(status.name().toLowerCase(), paramLookup.get("status"));
        assertEquals("2016-01-01T07:08:55Z", paramLookup.get("date_end"));
        assertEquals("2016-01-01T07:08:20Z", paramLookup.get("date_start"));
        assertEquals(recordIndex.toString(), paramLookup.get("record_index"));
        assertEquals(order.getCallOrder(), paramLookup.get("order"));
        assertEquals(pageSize.toString(), paramLookup.get("page_size"));
        assertEquals(conversationUuid, paramLookup.get("conversation_uuid"));
    }

    @Test
    public void testNoParams() {
        assertEquals(0, CallsFilter.builder().build().makeParams().size());
    }

    @Test
    public void testStaticBuilderProducesNewBuilder() {
        assertNotSame(CallsFilter.builder(), CallsFilter.builder());
    }

    @Test
    public void testAllParams() {
        Instant startDate = Instant.parse("2020-01-30T07:08:24Z"), endDate = Instant.now();
        String conversationId = "CON-aaaaaaaa-bbbb-4ccc-8ddd-0123456789ab";
        int recordIndex = 6, pageSize = 31;
        CallStatus status = CallStatus.RINGING;
        SortOrder order = SortOrder.DESCENDING;

        CallsFilter filter = CallsFilter.builder()
                .startDate(startDate).endDate(endDate)
                .conversationUuid(conversationId).recordIndex(recordIndex)
                .pageSize(pageSize).status(status).order(order).build();

        assertEquals(startDate, filter.getStartDate());
        assertEquals(endDate, filter.getEndDate());
        assertEquals(conversationId, filter.getConversationUuid());
        assertEquals(recordIndex, filter.getRecordIndex());
        assertEquals(pageSize, filter.getPageSize());
        assertEquals(status, filter.getStatus());
        assertEquals(order, filter.getOrder());

        var params = filter.makeParams();
        assertNotNull(params);
        assertEquals(7, params.size());
        assertEquals(startDate, Instant.parse(params.get("date_start")));
        assertEquals(endDate, Instant.parse(params.get("date_end")));
        assertEquals(conversationId, params.get("conversation_uuid"));
        assertEquals(Integer.toString(recordIndex), params.get("record_index"));
        assertEquals(Integer.toString(pageSize), params.get("page_size"));
        assertEquals(status.name().toLowerCase(), params.get("status"));
        assertEquals("desc", params.get("order"));
    }
}