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

import com.vonage.client.AbstractClientTest;
import com.vonage.client.RestEndpoint;
import com.vonage.client.VonageResponseParseException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VerifyClientCheckEndpointTest extends AbstractClientTest<VerifyClient> {
    
    public VerifyClientCheckEndpointTest() {
        client = new VerifyClient(wrapper);
    }

    @Test
    public void testCheckWithValidResponseAndIp() throws Exception {
        String json = "{\n" + "  \"request_id\": \"a-request-id\",\n" + "  \"status\": \"0\",\n"
                + "  \"event_id\": \"an-event-id\",\n" + "  \"price\": \"0.10000000\",\n" + "  \"currency\": \"EUR\"\n"
                + "}\n";
        // For proper coverage we will check both with and without IP.  However, the logic remains the same.
        // Note: We have to stub the client each time because it won't allow for sequential requests.

        CheckResponse[] responses = new CheckResponse[2];

        stubResponse(200, json);
        responses[0] = client.check("a-request-id", "1234");

        stubResponse(200, json);
        responses[1] = client.check("a-request-id", "1234");

        for (CheckResponse response : responses) {
            assertEquals("a-request-id", response.getRequestId());
            assertEquals(VerifyStatus.OK, response.getStatus());
            assertEquals("an-event-id", response.getEventId());
            assertEquals(new BigDecimal("0.10000000"), response.getPrice());
            assertEquals("EUR", response.getCurrency());
            assertNull(response.getErrorText());
        }
    }

    @Test
    public void testCheckWithoutRequestId() throws Exception {
        String json = "{\n" + "  \"status\": \"0\",\n" + "  \"event_id\": \"an-event-id\",\n"
                + "  \"price\": \"0.10000000\",\n" + "  \"currency\": \"EUR\"\n" + "}\n";
        stubResponse(200, json);
        CheckResponse response = client.check("a-request-id", "1234");

        assertNull(response.getRequestId());
    }

    @Test
    public void testCheckWithoutStatusThrowsException() throws Exception {
        String json = "{\n" + "  \"request_id\": \"a-request-id\",\n" + "  \"event_id\": \"an-event-id\",\n"
                + "  \"price\": \"0.10000000\",\n" + "  \"currency\": \"EUR\"\n" + "}\n";
        stubResponse(200, json);
        assertThrows(VonageResponseParseException.class, () ->
                client.check("a-request-id", "1234")
        );
    }

    @Test
    public void testCheckWithNonNumericStatus() throws Exception {
        String json = "{\n" + "  \"request_id\": \"a-request-id\",\n" + "  \"status\": \"test\",\n"
                + "  \"event_id\": \"an-event-id\",\n" + "  \"price\": \"0.10000000\",\n" +
                "  \"currency\": \"EUR\",\n" + "  \"estimated_price_messages_sent\": \"0.33001\""
                + "}\n";
        stubResponse(200, json);
        CheckResponse response = client.check("a-request-id", "1234");
        assertEquals(VerifyStatus.INTERNAL_ERROR, response.getStatus());
        assertEquals(BigDecimal.valueOf(0.33001), response.getEstimatedPriceMessagesSent());
    }

    @Test
    public void testCheckWithoutEventId() throws Exception {
        String json = "{\n" + "  \"request_id\": \"a-request-id\",\n" + "  \"status\": \"0\",\n"
                + "  \"price\": \"0.10000000\",\n" + "  \"currency\": \"EUR\"\n" + "}\n";
        stubResponse(200, json);
        CheckResponse response = client.check("a-request-id", "1234");

        assertNull(response.getEventId());
    }

    @Test
    public void testCheckWithoutPrice() throws Exception {
        String json = "{\n" + "  \"request_id\": \"a-request-id\",\n" + "  \"status\": \"0\",\n"
                + "  \"event_id\": \"an-event-id\",\n" + "  \"currency\": \"EUR\"\n" + "}\n";
        stubResponse(200, json);
        CheckResponse response = client.check("a-request-id", "1234");

        assertNull(response.getPrice());
    }

    @Test
    public void testCheckWithNonNumericPrice() throws Exception {
        String json = "{\n" + "  \"request_id\": \"a-request-id\",\n" + "  \"status\": \"0\",\n"
                + "  \"event_id\": \"an-event-id\",\n" + "  \"price\": \"test\",\n" + "  \"currency\": \"EUR\"\n"
                + "}\n";
        stubResponse(200, json);
        assertThrows(VonageResponseParseException.class, () -> client.check("a-request-id", "1234"));
    }

    @Test
    public void testCheckWithoutCurrency() throws Exception {
        String json = "{\n" + "  \"request_id\": \"a-request-id\",\n" + "  \"status\": \"0\",\n"
                + "  \"event_id\": \"an-event-id\",\n" + "  \"price\": \"0.10000000\"\n" + "}\n";
        stubResponse(200, json);
        CheckResponse response = client.check("a-request-id", "1234");

        assertNull(response.getCurrency());
    }

    @Test
    public void testCheckWithError() throws Exception {
        String json = "{\n" + "  \"status\": \"2\",\n" + "  \"error_text\": \"There was an error.\"\n" + "}";
        stubResponse(200, json);
        CheckResponse response = client.check("a-request-id", "1234");

        assertEquals(VerifyStatus.MISSING_PARAMS, response.getStatus());
        assertEquals("There was an error.", response.getErrorText());
    }

    @Test
    public void testWithInvalidNumericStatus() throws Exception {
        String json = "{\n" + "  \"status\": \"5958\"\n" + "}";
        stubResponse(200, json);
        CheckResponse response = client.check("a-request-id", "1234");

        assertEquals(VerifyStatus.UNKNOWN, response.getStatus());
    }

    @Test
    public void testNullRequestId() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> client.check(null, "1234"));
    }

    @Test
    public void testLongRequestId() throws Exception {
        StringBuilder requestId = new StringBuilder(33);
        for (; requestId.length() < 32; requestId.append("req-"));
        assertThrows(IllegalArgumentException.class, () ->
                client.check(requestId.append('0').toString(), "1234")
        );
    }

    @Test
    public void testNullCode() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> client.check("a-request-id", null));
    }

    @Test
    public void testCodeLessThan4Characters() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> client.check("a-request-id", "012"));
    }

    @Test
    public void testCodeMoreThan6Characters() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> client.check("a-request-id", "1234567"));
    }

    @Test
    public void testEndpoint() throws Exception {
        new VerifyEndpointTestSpec<CheckRequest, CheckResponse>() {
            final String requestId = UUID.randomUUID().toString().replace("-", "");

            @Override
            protected RestEndpoint<CheckRequest, CheckResponse> endpoint() {
                return client.check;
            }

            @Override
            protected String expectedEndpointUri(CheckRequest request) {
                return "/verify/check/json";
            }

            @Override
            protected CheckRequest sampleRequest() {
                return new CheckRequest(requestId, "567321");
            }

            @Override
            protected Map<String, String> sampleQueryParams() {
                CheckRequest request = sampleRequest();
                Map<String, String> params = new HashMap<>(4);
                params.put("request_id", request.getRequestId());
                params.put("code", request.getCode());
                assertEquals(requestId, params.get("request_id"));
                assertEquals("567321", params.get("code"));
                return params;
            }
        }
        .runTests();
    }
}
