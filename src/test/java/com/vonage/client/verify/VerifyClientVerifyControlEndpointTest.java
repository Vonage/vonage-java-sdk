/*
 *   Copyright 2024 Vonage
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
import com.vonage.client.RestEndpoint;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class VerifyClientVerifyControlEndpointTest extends ClientTest<VerifyClient> {

    @BeforeEach
    public void setUp() {
        client = new VerifyClient(wrapper);
    }

    @Test
    public void testAdvanceVerification() throws Exception {
        String json = "{\n" + "  \"status\":\"0\",\n" + "  \"command\":\"trigger_next_event\"\n" + "}";
        stubResponse(200, json);

        ControlResponse response = client.advanceVerification("a-request-id");
        assertEquals("0", response.getStatus());
        assertEquals(VerifyControlCommand.TRIGGER_NEXT_EVENT, response.getCommand());
    }

    @Test
    public void testCancelVerification() throws Exception {
        String json = "{\n" + "  \"status\":\"0\",\n" + "  \"command\":\"cancel\"\n" + "}";
        stubResponse(200, json);

        ControlResponse response = client.cancelVerification("a-request-id");
        assertEquals("0", response.getStatus());
        assertEquals(VerifyControlCommand.CANCEL, response.getCommand());
    }

    @Test
    public void testParseErrorResponse() throws Exception {
        stubResponse(200, "{\n" +
                "    \"error_text\": \"Missing username\",\n" + "    \"status\": \"2\"\n" + "}"
        );
        try {
            ControlResponse response = client.control.execute(new ControlRequest(
                    "req", VerifyControlCommand.TRIGGER_NEXT_EVENT
            ));
            fail("Parsing an error response should throw an exception.");
        }
        catch (VerifyException exc) {
            assertEquals("2", exc.getStatus());
            assertEquals("Missing username", exc.getErrorText());
        }
    }

    @Test
    public void testEndpoint() throws Exception {
        new VerifyEndpointTestSpec<ControlRequest, ControlResponse>() {
            final String requestId = UUID.randomUUID().toString().replace("-", "");

            @Override
            protected RestEndpoint<ControlRequest, ControlResponse> endpoint() {
                return client.control;
            }

            @Override
            protected String expectedEndpointUri(ControlRequest request) {
                return "/verify/control/json";
            }

            @Override
            protected ControlRequest sampleRequest() {
                return new ControlRequest(requestId, VerifyControlCommand.CANCEL);
            }

            @Override
            protected Map<String, ?> sampleQueryParams() {
                Map<String, String> params = new LinkedHashMap<>(2);
                params.put("request_id", requestId);
                params.put("cmd", "cancel");

                ControlRequest request = sampleRequest();
                assertEquals(params.get("request_id"), request.getRequestId());
                assertEquals(params.get("cmd"), request.getCommand().toString());

                return params;
            }
        }
        .runTests();
    }
}
