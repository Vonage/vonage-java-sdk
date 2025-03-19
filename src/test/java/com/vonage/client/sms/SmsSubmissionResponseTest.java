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
package com.vonage.client.sms;

import com.vonage.client.Jsonable;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class SmsSubmissionResponseTest {

    @Test
    public void testSingleEmptyMessageResponse() {
        SmsSubmissionResponse response = Jsonable.fromJson("{\"message-count\":1, \"messages\":[{}]}");
        assertEquals(1, response.getMessageCount());
        assertNotNull(response.getMessages());
        assertEquals(1, response.getMessages().size());
        assertNotNull(response.getMessages().getFirst());
    }

    @Test
    public void testEmptyMessageResponses() {
        SmsSubmissionResponse response = Jsonable.fromJson("{\"message-count\":0, \"messages\":[]}");
        assertEquals(0, response.getMessageCount());
        assertNotNull(response.getMessages());
        assertEquals(0, response.getMessages().size());
    }

    @Test
    public void testNoMessageResponses() {
        SmsSubmissionResponse response = Jsonable.fromJson("{\"message-count\":-2}");
        assertEquals(-2, response.getMessageCount());
        assertNull(response.getMessages());
    }

    @Test
    public void testEmptyJson() {
        SmsSubmissionResponse response = Jsonable.fromJson("{}");
        assertNull(response.getMessageCount());
        assertNull(response.getMessages());
    }
}
