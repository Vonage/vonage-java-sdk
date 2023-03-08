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

import com.vonage.client.ClientTest;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class VerifyClientVerifyControlEndpointTest extends ClientTest<VerifyClient> {

    @Before
    public void setUp() {
        client = new VerifyClient(wrapper);
    }

    @Test
    public void testAdvanceVerification() throws Exception {
        String json = "{\n" + "  \"status\":\"0\",\n" + "  \"command\":\"trigger_next_event\"\n" + "}";
        wrapper.setHttpClient(stubHttpClient(200, json));

        ControlResponse response = client.advanceVerification("a-request-id");
        assertEquals("0", response.getStatus());
        assertEquals(VerifyControlCommand.TRIGGER_NEXT_EVENT, response.getCommand());
    }

    @Test
    public void testCancelVerification() throws Exception {
        String json = "{\n" + "  \"status\":\"0\",\n" + "  \"command\":\"cancel\"\n" + "}";
        wrapper.setHttpClient(stubHttpClient(200, json));

        ControlResponse response = client.cancelVerification("a-request-id");
        assertEquals("0", response.getStatus());
        assertEquals(VerifyControlCommand.CANCEL, response.getCommand());
    }
}
