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
import com.vonage.client.VonageResponseParseException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.math.BigDecimal;

public class VerifyClientCheckEndpointTest extends ClientTest<VerifyClient> {

    @Before
    public void setUp() {
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

        wrapper.setHttpClient(stubHttpClient(200, json));
        responses[0] = client.check("a-request-id", "1234", "127.0.0.1");

        wrapper.setHttpClient(stubHttpClient(200, json));
        responses[1] = client.check("a-request-id", "1234");

        for (CheckResponse response : responses) {
            assertEquals("a-request-id", response.getRequestId());
            assertEquals(VerifyStatus.OK, response.getStatus());
            assertFalse(response.getStatus().isTemporaryError());
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
        wrapper.setHttpClient(stubHttpClient(200, json));
        CheckResponse response = client.check("a-request-id", "1234", "127.0.0.1");

        assertNull(response.getRequestId());
    }

    @Test(expected = VonageResponseParseException.class)
    public void testCheckWithoutStatusThrowsException() throws Exception {
        String json = "{\n" + "  \"request_id\": \"a-request-id\",\n" + "  \"event_id\": \"an-event-id\",\n"
                + "  \"price\": \"0.10000000\",\n" + "  \"currency\": \"EUR\"\n" + "}\n";
        wrapper.setHttpClient(stubHttpClient(200, json));
        client.check("a-request-id", "1234", "127.0.0.1");
    }

    @Test
    public void testCheckWithNonNumericStatus() throws Exception {
        String json = "{\n" + "  \"request_id\": \"a-request-id\",\n" + "  \"status\": \"test\",\n"
                + "  \"event_id\": \"an-event-id\",\n" + "  \"price\": \"0.10000000\",\n" +
                "  \"currency\": \"EUR\",\n" + "  \"estimated_price_messages_sent\": \"0.33001\""
                + "}\n";
        wrapper.setHttpClient(stubHttpClient(200, json));
        CheckResponse response = client.check("a-request-id", "1234");
        assertEquals(VerifyStatus.INTERNAL_ERROR, response.getStatus());
        assertEquals(BigDecimal.valueOf(0.33001), response.getEstimatedPriceMessagesSent());
    }

    @Test
    public void testCheckWithoutEventId() throws Exception {
        String json = "{\n" + "  \"request_id\": \"a-request-id\",\n" + "  \"status\": \"0\",\n"
                + "  \"price\": \"0.10000000\",\n" + "  \"currency\": \"EUR\"\n" + "}\n";
        wrapper.setHttpClient(stubHttpClient(200, json));
        CheckResponse response = client.check("a-request-id", "1234", "127.0.0.1");

        assertNull(response.getEventId());
    }

    @Test
    public void testCheckWithoutPrice() throws Exception {
        String json = "{\n" + "  \"request_id\": \"a-request-id\",\n" + "  \"status\": \"0\",\n"
                + "  \"event_id\": \"an-event-id\",\n" + "  \"currency\": \"EUR\"\n" + "}\n";
        wrapper.setHttpClient(stubHttpClient(200, json));
        CheckResponse response = client.check("a-request-id", "1234", "127.0.0.1");

        assertNull(response.getPrice());
    }

    @Test(expected = VonageResponseParseException.class)
    public void testCheckWithNonNumericPrice() throws Exception {
        String json = "{\n" + "  \"request_id\": \"a-request-id\",\n" + "  \"status\": \"0\",\n"
                + "  \"event_id\": \"an-event-id\",\n" + "  \"price\": \"test\",\n" + "  \"currency\": \"EUR\"\n"
                + "}\n";
        wrapper.setHttpClient(stubHttpClient(200, json));
        client.check("a-request-id", "1234");
    }

    @Test
    public void testCheckWithoutCurrency() throws Exception {
        String json = "{\n" + "  \"request_id\": \"a-request-id\",\n" + "  \"status\": \"0\",\n"
                + "  \"event_id\": \"an-event-id\",\n" + "  \"price\": \"0.10000000\"\n" + "}\n";
        wrapper.setHttpClient(stubHttpClient(200, json));
        CheckResponse response = client.check("a-request-id", "1234", "127.0.0.1");

        assertNull(response.getCurrency());
    }

    @Test
    public void testCheckWithError() throws Exception {
        String json = "{\n" + "  \"status\": \"2\",\n" + "  \"error_text\": \"There was an error.\"\n" + "}";
        wrapper.setHttpClient(stubHttpClient(200, json));
        CheckResponse response = client.check("a-request-id", "1234", "127.0.0.1");

        assertEquals(VerifyStatus.MISSING_PARAMS, response.getStatus());
        assertEquals("There was an error.", response.getErrorText());
    }

    @Test
    public void testWithInvalidNumericStatus() throws Exception {
        String json = "{\n" + "  \"status\": \"5958\"\n" + "}";
        wrapper.setHttpClient(stubHttpClient(200, json));
        CheckResponse response = client.check("a-request-id", "1234", "127.0.0.1");

        assertEquals(VerifyStatus.UNKNOWN, response.getStatus());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullRequestId() throws Exception {
        client.check(null, "1234");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLongRequestId() throws Exception {
        StringBuilder requestId = new StringBuilder(33);
        for (; requestId.length() < 32; requestId.append("req-"));
        client.check(requestId.append('0').toString(), "1234");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCode() throws Exception {
        client.check("a-request-id", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCodeLessThan4Characters() throws Exception {
        client.check("a-request-id", "012");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCodeMoreThan6Characters() throws Exception {
        client.check("a-request-id", "1234567");
    }
}
