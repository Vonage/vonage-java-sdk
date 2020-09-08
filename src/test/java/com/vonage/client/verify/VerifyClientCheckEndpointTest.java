/*
 * Copyright (c) 2020 Vonage
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.vonage.client.verify;

import com.vonage.client.ClientTest;
import com.vonage.client.VonageResponseParseException;
import org.junit.Assert;
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
            Assert.assertEquals("a-request-id", response.getRequestId());
            Assert.assertEquals(VerifyStatus.OK, response.getStatus());
            Assert.assertEquals("an-event-id", response.getEventId());
            Assert.assertEquals(new BigDecimal("0.10000000"), response.getPrice());
            Assert.assertEquals("EUR", response.getCurrency());
            Assert.assertNull(response.getErrorText());
        }
    }

    @Test
    public void testCheckWithoutRequestId() throws Exception {
        String json = "{\n" + "  \"status\": \"0\",\n" + "  \"event_id\": \"an-event-id\",\n"
                + "  \"price\": \"0.10000000\",\n" + "  \"currency\": \"EUR\"\n" + "}\n";
        wrapper.setHttpClient(stubHttpClient(200, json));
        CheckResponse response = client.check("a-request-id", "1234", "127.0.0.1");

        Assert.assertNull(response.getRequestId());
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
                + "  \"event_id\": \"an-event-id\",\n" + "  \"price\": \"0.10000000\",\n" + "  \"currency\": \"EUR\"\n"
                + "}\n";
        wrapper.setHttpClient(stubHttpClient(200, json));
        CheckResponse response = client.check("a-request-id", "1234");
        Assert.assertEquals(VerifyStatus.INTERNAL_ERROR, response.getStatus());
    }

    @Test
    public void testCheckWithoutEventId() throws Exception {
        String json = "{\n" + "  \"request_id\": \"a-request-id\",\n" + "  \"status\": \"0\",\n"
                + "  \"price\": \"0.10000000\",\n" + "  \"currency\": \"EUR\"\n" + "}\n";
        wrapper.setHttpClient(stubHttpClient(200, json));
        CheckResponse response = client.check("a-request-id", "1234", "127.0.0.1");

        Assert.assertNull(response.getEventId());
    }

    @Test
    public void testCheckWithoutPrice() throws Exception {
        String json = "{\n" + "  \"request_id\": \"a-request-id\",\n" + "  \"status\": \"0\",\n"
                + "  \"event_id\": \"an-event-id\",\n" + "  \"currency\": \"EUR\"\n" + "}\n";
        wrapper.setHttpClient(stubHttpClient(200, json));
        CheckResponse response = client.check("a-request-id", "1234", "127.0.0.1");

        Assert.assertNull(response.getPrice());
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

        Assert.assertNull(response.getCurrency());
    }

    @Test
    public void testCheckWithError() throws Exception {
        String json = "{\n" + "  \"status\": \"2\",\n" + "  \"error_text\": \"There was an error.\"\n" + "}";
        wrapper.setHttpClient(stubHttpClient(200, json));
        CheckResponse response = client.check("a-request-id", "1234", "127.0.0.1");

        Assert.assertEquals(VerifyStatus.MISSING_PARAMS, response.getStatus());
        Assert.assertEquals("There was an error.", response.getErrorText());
    }

    @Test
    public void testWithInvalidNumericStatus() throws Exception {
        String json = "{\n" + "  \"status\": \"5958\"\n" + "}";
        wrapper.setHttpClient(stubHttpClient(200, json));
        CheckResponse response = client.check("a-request-id", "1234", "127.0.0.1");

        Assert.assertEquals(VerifyStatus.UNKNOWN, response.getStatus());
    }
}
