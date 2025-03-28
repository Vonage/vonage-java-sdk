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
package com.vonage.client.insight;

import com.vonage.client.Jsonable;
import static com.vonage.client.TestUtils.testJsonableBaseObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AdvancedAsyncInsightResponseTest {

    @Test
    public void testFromJsonAllFields() {
        AdvancedAsyncInsightResponse response = Jsonable.fromJson(
            """
                {
                   "request_id": "aaaaaaaa-bbbb-cccc-dddd-0123456789ab",
                   "number": "447700900000",
                   "remaining_balance": "1.23456789",
                   "request_price": "0.01500000",
                   "status": 0,
                   "error_text": "Success"
                }
            """
        );
        testJsonableBaseObject(response);
        assertEquals("aaaaaaaa-bbbb-cccc-dddd-0123456789ab", response.getRequestId());
        assertEquals("447700900000", response.getNumber());
        assertEquals("1.23456789", response.getRemainingBalance().toString());
        assertEquals("0.01500000", response.getRequestPrice().toString());
        assertEquals(InsightStatus.SUCCESS, response.getStatus());
        assertEquals("Success", response.getErrorText());
    }

    @Test
    public void testFromJsonEmpty() {
        AdvancedAsyncInsightResponse response = Jsonable.fromJson("{}");
        testJsonableBaseObject(response);
        assertNull(response.getRequestId());
        assertNull(response.getNumber());
        assertNull(response.getRemainingBalance());
        assertNull(response.getRequestPrice());
        assertNull(response.getStatus());
        assertNull(response.getErrorText());
    }

}
