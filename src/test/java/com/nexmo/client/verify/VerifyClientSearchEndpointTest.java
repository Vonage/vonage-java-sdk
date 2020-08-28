/*
 * Copyright (c) 2011-2018 Vonage Inc
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
package com.nexmo.client.verify;

import com.nexmo.client.ClientTest;
import com.nexmo.client.VonageResponseParseException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class VerifyClientSearchEndpointTest extends ClientTest<VerifyClient> {
    @Before
    public void setUp() {
        client = new VerifyClient(wrapper);
    }

    @Test
    public void testSearchSuccessWithSingleResult() throws Exception {
        //language=JSON
        String json = "{\n" + "  \"request_id\": \"not-a-search-request-id\",\n"
                + "  \"account_id\": \"not-an-account-id\",\n" + "  \"number\": \"447700900999\",\n"
                + "  \"sender_id\": \"447700900991\",\n" + "  \"date_submitted\": \"2014-03-04 10:11:12\",\n"
                + "  \"date_finalized\": \"2015-02-03 07:08:09\",\n" + "  \"checks\": [\n" + "    {\n"
                + "      \"date_received\": \"2012-01-02 03:04:05\",\n" + "      \"code\": \"code\",\n"
                + "      \"status\": \"VALID\",\n" + "      \"ip_address\": \"\"\n" + "    },\n" + "    {\n"
                + "      \"date_received\": \"2012-01-02 03:04:05\",\n" + "      \"code\": \"code\",\n"
                + "      \"status\": \"VALID\",\n" + "      \"ip_address\": \"\"\n" + "    }\n" + "  ],\n"
                + "  \"first_event_date\": \"2012-01-02 03:04:05\",\n"
                + "  \"last_event_date\": \"2012-01-02 03:04:06\",\n" + "  \"price\": \"0.03000000\",\n"
                + "  \"currency\": \"EUR\",\n" + "  \"status\": \"SUCCESS\"\n" + "}";
        wrapper.setHttpClient(this.stubHttpClient(200, json));

        SearchVerifyResponse response = client.search("not-a-search-request-id");
        assertEquals(VerifyStatus.OK, response.getStatus());
        assertNull(response.getErrorText());
        List<VerifyDetails> requests = response.getVerificationRequests();

        assertEquals(1, requests.size());

        VerifyDetails details = requests.get(0);

        assertEquals("not-an-account-id", details.getAccountId());
        assertEquals("EUR", details.getCurrency());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2015-02-03 07:08:09"),
                details.getDateFinalized()
        );
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-03-04 10:11:12"),
                details.getDateSubmitted()
        );
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2012-01-02 03:04:05"),
                details.getFirstEventDate()
        );
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2012-01-02 03:04:06"),
                details.getLastEventDate()
        );
        assertEquals("447700900999", details.getNumber());
        assertEquals(new BigDecimal("0.03000000"), details.getPrice());
        assertEquals("not-a-search-request-id", details.getRequestId());
        assertEquals("447700900991", details.getSenderId());
        assertEquals(VerifyDetails.Status.SUCCESS, details.getStatus());

        List<VerifyCheck> checks = details.getChecks();

        assertEquals(2, checks.size());

        for (VerifyCheck check : checks) {
            assertEquals("code", check.getCode());
            assertEquals("", check.getIpAddress());
            assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2012-01-02 03:04:05"), check.getDate());
            assertEquals(VerifyCheck.Status.VALID, check.getStatus());
        }
    }

    @Test
    public void testSearchError() throws Exception {
        //language=JSON
        String json = "{\n" + "  \"request_id\": \"\",\n" + "  \"status\": \"101\",\n"
                + "  \"error_text\": \"No response found.\"\n" + "}";
        wrapper.setHttpClient(this.stubHttpClient(200, json));

        SearchVerifyResponse response = client.search("AAAAA");
        assertEquals(VerifyStatus.NO_RESPONSE, response.getStatus());
    }

    @Test
    public void testSearchFailed() throws Exception {
        //language=JSON
        String json = "{\n" + "  \"request_id\": \"a-random-request-id\",\n" + "  \"account_id\": \"account-id\",\n"
                + "  \"number\": \"not-a-number\",\n" + "  \"sender_id\": \"verify\",\n"
                + "  \"date_submitted\": \"2016-10-19 11:18:56\",\n"
                + "  \"date_finalized\": \"2016-10-19 11:20:00\",\n" + "  \"checks\": [],\n"
                + "  \"first_event_date\": \"2016-10-19 11:18:56\",\n"
                + "  \"last_event_date\": \"2016-10-19 11:18:56\",\n" + "  \"price\": \"0.03000000\",\n"
                + "  \"currency\": \"EUR\",\n" + "  \"status\": \"FAILED\"\n" + "}";
        wrapper.setHttpClient(this.stubHttpClient(200, json));

        SearchVerifyResponse response = client.search("AAAAA");
        assertEquals(VerifyStatus.OK, response.getStatus());

        VerifyDetails details = response.getVerificationRequests().get(0);
        assertEquals(VerifyDetails.Status.FAILED, details.getStatus());
    }

    @Test
    public void testSearchExpired() throws Exception {
        String json = "{\n" + "  \"request_id\": \"a-random-request-id\",\n" + "  \"account_id\": \"account-id\",\n"
                + "  \"number\": \"not-a-number\",\n" + "  \"sender_id\": \"verify\",\n"
                + "  \"date_submitted\": \"2016-10-19 11:25:19\",\n"
                + "  \"date_finalized\": \"2016-10-19 11:35:27\",\n" + "  \"checks\": [],\n"
                + "  \"first_event_date\": \"2016-10-19 11:25:19\",\n"
                + "  \"last_event_date\": \"2016-10-19 11:30:26\",\n" + "  \"price\": \"0\",\n"
                + "  \"currency\": \"EUR\",\n" + "  \"status\": \"EXPIRED\"\n" + "}\n";
        wrapper.setHttpClient(this.stubHttpClient(200, json));

        SearchVerifyResponse response = client.search("AAAAA");
        assertEquals(VerifyStatus.OK, response.getStatus());

        VerifyDetails details = response.getVerificationRequests().get(0);
        assertEquals(VerifyDetails.Status.EXPIRED, details.getStatus());
    }

    @Test
    public void testSearchInProgress() throws Exception {
        String json = "{\n" + "  \"request_id\": \"a-random-request-id\",\n" + "  \"account_id\": \"account-id\",\n"
                + "  \"number\": \"not-a-number\",\n" + "  \"sender_id\": \"verify\",\n"
                + "  \"date_submitted\": \"2016-10-19 11:25:19\",\n" + "  \"date_finalized\": \"\",\n"
                + "  \"checks\": [],\n" + "  \"first_event_date\": \"2016-10-19 11:25:19\",\n"
                + "  \"last_event_date\": \"2016-10-19 11:30:26\",\n" + "  \"price\": \"0.10000000\",\n"
                + "  \"currency\": \"EUR\",\n" + "  \"status\": \"IN PROGRESS\"\n" + "}\n";
        wrapper.setHttpClient(this.stubHttpClient(200, json));

        SearchVerifyResponse response = client.search("AAAAA");
        assertEquals(VerifyStatus.OK, response.getStatus());

        VerifyDetails details = response.getVerificationRequests().get(0);
        assertEquals(VerifyDetails.Status.IN_PROGRESS, details.getStatus());
    }

    @Test
    public void testSearchWithWrongParams() throws Exception {
        String json = "{\n" + "  \"request_id\": \"\",\n" + "  \"status\": 6,\n"
                + "  \"error_text\": \"The Vonage platform was unable to process this message for the following reason: Wrong parameters.\"\n"
                + "}";
        wrapper.setHttpClient(this.stubHttpClient(200, json));

        SearchVerifyResponse response = client.search("AAAAA");
        assertEquals(VerifyStatus.INVALID_REQUEST, response.getStatus());
    }

    @Test
    public void testSearchWithMultipleResults() throws Exception {
        String json = "{\n" + "  \"verification_requests\": [\n" + "    { \n"
                + "      \"request_id\": \"a-random-request-id\",\n" + "      \"account_id\": \"abcde\",\n"
                + "      \"number\": \"447700900999\",\n" + "      \"sender_id\": \"verify\",\n"
                + "      \"date_submitted\": \"2016-10-21 15:41:02\",\n" + "      \"date_finalized\": \"\",\n"
                + "      \"checks\": [],\n" + "      \"first_event_date\": \"2016-10-21 15:41:02\",\n"
                + "      \"last_event_date\": \"2016-10-21 15:43:07\",\n" + "      \"price\": \"0.10000000\",\n"
                + "      \"currency\": \"EUR\",\n" + "      \"status\": \"IN PROGRESS\"\n" + "    },\n" + "    { \n"
                + "      \"request_id\": \"another-random-request-id\",\n" + "      \"account_id\": \"fghij\",\n"
                + "      \"number\": \"447700900991\",\n" + "      \"sender_id\": \"verify2\",\n"
                + "      \"date_submitted\": \"2016-10-21 15:41:58\",\n" + "      \"date_finalized\": \"\",\n"
                + "      \"checks\": [],\n" + "      \"first_event_date\": \"2016-10-21 15:41:58\",\n"
                + "      \"last_event_date\": \"2016-10-21 15:41:58\",\n" + "      \"price\": \"0.10000000\",\n"
                + "      \"currency\": \"EUR\",\n" + "      \"status\": \"EXPIRED\"\n" + "    }\n" + "  ]\n" + "}";
        wrapper.setHttpClient(this.stubHttpClient(200, json));

        SearchVerifyResponse response = client.search("not-a-search-request-id");
        assertEquals(VerifyStatus.OK, response.getStatus());
        assertNull(response.getErrorText());
        List<VerifyDetails> requests = response.getVerificationRequests();

        assertEquals(2, requests.size());

        VerifyDetails first = requests.get(0);
        VerifyDetails second = requests.get(1);

        assertEquals("abcde", first.getAccountId());
        assertEquals("EUR", first.getCurrency());
        assertNull(first.getDateFinalized());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-10-21 15:41:02"),
                first.getDateSubmitted()
        );
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-10-21 15:41:02"),
                first.getFirstEventDate()
        );
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-10-21 15:43:07"),
                first.getLastEventDate()
        );
        assertEquals("447700900999", first.getNumber());
        assertEquals(new BigDecimal("0.10000000"), first.getPrice());
        assertEquals("a-random-request-id", first.getRequestId());
        assertEquals("verify", first.getSenderId());
        assertEquals(VerifyDetails.Status.IN_PROGRESS, first.getStatus());

        assertEquals("fghij", second.getAccountId());
        assertEquals("EUR", second.getCurrency());
        assertNull(second.getDateFinalized());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-10-21 15:41:58"),
                second.getDateSubmitted()
        );
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-10-21 15:41:58"),
                second.getFirstEventDate()
        );
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-10-21 15:41:58"),
                second.getLastEventDate()
        );
        assertEquals("447700900991", second.getNumber());
        assertEquals(new BigDecimal("0.10000000"), second.getPrice());
        assertEquals("another-random-request-id", second.getRequestId());
        assertEquals("verify2", second.getSenderId());
        assertEquals(VerifyDetails.Status.EXPIRED, second.getStatus());
    }

    @Test(expected = VonageResponseParseException.class)
    public void testSearchInvalidDates() throws Exception {
        String json = "    { \n" + "      \"request_id\": \"a-random-request-id\",\n"
                + "      \"account_id\": \"account-id\",\n" + "      \"number\": \"not-a-number\",\n"
                + "      \"sender_id\": \"verify\",\n" + "      \"date_submitted\": \"aaa\",\n"
                + "      \"date_finalized\": \"ddd\",\n" + "      \"checks\": [],\n"
                + "      \"first_event_date\": \"bbb\",\n" + "      \"last_event_date\": \"ccc\",\n"
                + "      \"price\": \"0.10000000\",\n" + "      \"currency\": \"EUR\",\n"
                + "      \"status\": \"SUCCESS\"\n" + "    }";

        wrapper.setHttpClient(this.stubHttpClient(200, json));
        client.search("a-random-request-id");
    }
}
