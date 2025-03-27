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

import com.vonage.client.Jsonable;
import com.vonage.client.TestUtils;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.*;

public class DtmfResponseTest {
    private DtmfResponse response;

    @BeforeEach
    public void setUp() {
        response = Jsonable.fromJson("{\n" +
                "  \"message\": \"DTMF sent\",\n" +
                "  \"uuid\": \"ssf61863-4a51-ef6b-11e1-w6edebcf93bb\"\n" +
                "}");
    }

    @Test
    public void testBasics() {
        TestUtils.testJsonableBaseObject(response);
        assertEquals("DTMF sent", response.getMessage());
        assertEquals("ssf61863-4a51-ef6b-11e1-w6edebcf93bb", response.getUuid());
    }

    @Test
    public void testUnknownJson() {
        String json = "{\n" +
                "    \"unknownProperty\": \"unknown\"\n" +
                "}";
        DtmfResponse dr = Jsonable.fromJson(json);
        assertNull(dr.getUuid());
    }
}