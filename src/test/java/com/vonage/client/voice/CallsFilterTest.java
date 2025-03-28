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
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class CallsFilterTest {

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
        Instant startDate = Instant.parse("2020-01-30T07:08:24Z"),
                endDate = Instant.now().truncatedTo(ChronoUnit.SECONDS);
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