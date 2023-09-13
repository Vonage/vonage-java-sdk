/*
 *   Copyright 2023 Vonage
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

import com.vonage.client.VonageUnexpectedException;
import static org.junit.Assert.*;
import org.junit.Test;

public class ControlResponseTest {

    @Test
    public void testConstructor() {
        ControlResponse response = new ControlResponse("1", VerifyControlCommand.CANCEL);
        assertEquals("1", response.getStatus());
        assertEquals(VerifyControlCommand.CANCEL, response.getCommand());
    }

    @Test
    public void testParseError() {
        ControlResponse response = ControlResponse.fromJson("{\n" +
                "    \"error_text\": \"Missing username\",\n" +
                "    \"status\": \"2\"\n" +
                "}");
        assertEquals("2", response.getStatus());
        assertEquals("Missing username", response.getErrorText());
    }

    @Test
    public void testBadJson() {
        assertThrows(VonageUnexpectedException.class, () -> ControlResponse.fromJson("blarg"));
    }
}
