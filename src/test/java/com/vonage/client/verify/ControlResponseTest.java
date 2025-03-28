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
package com.vonage.client.verify;

import com.vonage.client.Jsonable;
import com.vonage.client.TestUtils;
import com.vonage.client.VonageUnexpectedException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class ControlResponseTest {

    @Test
    public void testConstructor() {
        ControlResponse response = new ControlResponse(VerifyStatus.THROTTLED, VerifyControlCommand.CANCEL);
        TestUtils.testJsonableBaseObject(response);
        assertEquals(VerifyStatus.THROTTLED, response.getStatus());
        assertEquals(VerifyControlCommand.CANCEL, response.getCommand());
    }

    @Test
    public void testParseError() {
        ControlResponse response = Jsonable.fromJson("{\n" +
                "    \"error_text\": \"Missing username\",\n" +
                "    \"status\": \"2\"\n" +
                "}");
        assertEquals(VerifyStatus.MISSING_PARAMS, response.getStatus());
        assertEquals("Missing username", response.getErrorText());
    }

    @Test
    public void testBadJson() {
        assertThrows(VonageUnexpectedException.class, () ->
                Jsonable.fromJson("blarg", ControlResponse.class)
        );
    }
}
