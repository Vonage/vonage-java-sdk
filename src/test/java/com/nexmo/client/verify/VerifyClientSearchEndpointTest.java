/*
 * Copyright (c) 2011-2018 Nexmo Inc
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

import com.nexmo.client.NexmoResponseParseException;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;

import static org.junit.Assert.assertEquals;

public class VerifyClientSearchEndpointTest extends ClientTest<VerifyClient> {
    @Before
    public void setUp() {
        client = new VerifyClient(wrapper);
    }

    @Test
    public void testSearchSuccess() throws Exception {
        //language=JSON
        String json = "{\n" + "  \"request_id\": \"not-a-search-request-id\",\n" + "  \"account_id\": \"not-an-account-id\",\n" + "  \"number\": \"447700900999\",\n" + "  \"sender_id\": \"447700900991\",\n" + "  \"date_submitted\": \"2014-03-04 10:11:12\",\n" + "  \"date_finalized\": \"2015-02-03 07:08:09\",\n" + "  \"checks\": [\n" + "    {\n" + "      \"date_received\": \"2012-01-02 03:04:05\",\n" + "      \"code\": \"code\",\n" + "      \"status\": \"VALID\",\n" + "      \"ip_address\": \"\"\n" + "    },\n" + "    {\n" + "      \"date_received\": \"2012-01-02 03:04:05\",\n" + "      \"code\": \"code\",\n" + "      \"status\": \"VALID\",\n" + "      \"ip_address\": \"\"\n" + "    }\n" + "  ],\n" + "  \"first_event_date\": \"2012-01-02 03:04:05\",\n" + "  \"last_event_date\": \"2012-01-02 03:04:06\",\n" + "  \"price\": \"0.03000000\",\n" + "  \"currency\": \"EUR\",\n" + "  \"status\": \"SUCCESS\"\n" + "}";
        wrapper.setHttpClient(this.stubHttpClient(200, json));

        SearchResult c = client.search("not-a-search-request-id");
        assertEquals(SearchResult.STATUS_OK, c.getStatus());
        assertEquals("not-a-search-request-id", c.getRequestId());
        assertEquals(SearchResult.VerificationStatus.SUCCESS, c.getVerificationStatus());
        assertEquals("447700900999", c.getNumber());
        assertEquals("447700900991", c.getSenderId());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-03-04 10:11:12"), c.getDateSubmitted());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2015-02-03 07:08:09"), c.getDateFinalized());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2012-01-02 03:04:05"), c.getFirstEventDate());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2012-01-02 03:04:06"), c.getLastEventDate());
    }

    @Test
    public void testSearchError() throws Exception {
        //language=JSON
        String json = "{\n" + "  \"request_id\": \"\",\n" + "  \"status\": \"101\",\n" + "  \"error_text\": \"No response found.\"\n" + "}";
        wrapper.setHttpClient(this.stubHttpClient(200, json));

        SearchResult c = client.search("AAAAA");
        assertEquals(SearchResult.STATUS_NO_RESPONSE, c.getStatus());
    }

    @Test
    public void testSearchFailed() throws Exception {
        //language=JSON
        String json = "{\n" + "  \"request_id\": \"a-random-request-id\",\n" + "  \"account_id\": \"account-id\",\n" + "  \"number\": \"not-a-number\",\n" + "  \"sender_id\": \"verify\",\n" + "  \"date_submitted\": \"2016-10-19 11:18:56\",\n" + "  \"date_finalized\": \"2016-10-19 11:20:00\",\n" + "  \"checks\": [\n" + "    {\n" + "      \"date_received\": \"2016-10-19 11:19:54\",\n" + "      \"code\": \"1234\",\n" + "      \"status\": \"INVALID\",\n" + "      \"ip_address\": \"\"\n" + "    },\n" + "    {\n" + "      \"date_received\": \"2016-10-19 11:19:58\",\n" + "      \"code\": \"1234\",\n" + "      \"status\": \"INVALID\",\n" + "      \"ip_address\": \"\"\n" + "    },\n" + "    {\n" + "      \"date_received\": \"2016-10-19 11:20:00\",\n" + "      \"code\": \"1234\",\n" + "      \"status\": \"INVALID\",\n" + "      \"ip_address\": \"\"\n" + "    }\n" + "  ],\n" + "  \"first_event_date\": \"2016-10-19 11:18:56\",\n" + "  \"last_event_date\": \"2016-10-19 11:18:56\",\n" + "  \"price\": \"0.03000000\",\n" + "  \"currency\": \"EUR\",\n" + "  \"status\": \"FAILED\"\n" + "}";
        wrapper.setHttpClient(this.stubHttpClient(200, json));

        SearchResult c = client.search("a-random-request-id");
        assertEquals(SearchResult.STATUS_OK, c.getStatus());
        assertEquals("a-random-request-id", c.getRequestId());
        assertEquals(SearchResult.VerificationStatus.FAILED, c.getVerificationStatus());
    }

    @Test
    public void testSearchExpired() throws Exception {
        String json = "{\n" + "  \"request_id\": \"a-random-request-id\",\n" + "  \"account_id\": \"account-id\",\n" + "  \"number\": \"not-a-number\",\n" + "  \"sender_id\": \"verify\",\n" + "  \"date_submitted\": \"2016-10-19 11:25:19\",\n" + "  \"date_finalized\": \"2016-10-19 11:35:27\",\n" + "  \"checks\": [],\n" + "  \"first_event_date\": \"2016-10-19 11:25:19\",\n" + "  \"last_event_date\": \"2016-10-19 11:30:26\",\n" + "  \"price\": \"0\",\n" + "  \"currency\": \"EUR\",\n" + "  \"status\": \"EXPIRED\"\n" + "}\n";
        wrapper.setHttpClient(this.stubHttpClient(200, json));

        SearchResult c = client.search("a-random-request-id");
        assertEquals(SearchResult.STATUS_OK, c.getStatus());
        assertEquals("a-random-request-id", c.getRequestId());
        assertEquals(SearchResult.VerificationStatus.EXPIRED, c.getVerificationStatus());
    }

    @Test
    public void testSearchInProgress() throws Exception {
        String json = "{\n" + "  \"request_id\": \"a-random-request-id\",\n" + "  \"account_id\": \"account-id\",\n" + "  \"number\": \"not-a-number\",\n" + "  \"sender_id\": \"verify\",\n" + "  \"date_submitted\": \"2016-10-19 11:25:19\",\n" + "  \"date_finalized\": \"\",\n" + "  \"checks\": [],\n" + "  \"first_event_date\": \"2016-10-19 11:25:19\",\n" + "  \"last_event_date\": \"2016-10-19 11:30:26\",\n" + "  \"price\": \"0.10000000\",\n" + "  \"currency\": \"EUR\",\n" + "  \"status\": \"IN PROGRESS\"\n" + "}\n";
        wrapper.setHttpClient(this.stubHttpClient(200, json));

        SearchResult c = client.search("a-random-request-id");
        assertEquals(SearchResult.STATUS_OK, c.getStatus());
        assertEquals("a-random-request-id", c.getRequestId());
        assertEquals(SearchResult.VerificationStatus.IN_PROGRESS, c.getVerificationStatus());
    }

    @Test
    public void testSearchWithWrongParams() throws Exception {
        String json = "{\n" + "  \"request_id\": \"\",\n" + "  \"status\": 6,\n" + "  \"error_text\": \"The Nexmo platform was unable to process this message for the following reason: Wrong parameters.\"\n" + "}";
        wrapper.setHttpClient(this.stubHttpClient(200, json));

        SearchResult c = client.search("a-random-request-id");
        assertEquals(SearchResult.STATUS_INVALID_REQUEST, c.getStatus());
    }

    @Test
    public void testSearchWithMultipleResults() throws Exception {
        String json = "{\n" + "  \"verification_requests\": [\n" + "    { \n" + "      \"request_id\": \"a-random-request-id\",\n" + "      \"account_id\": \"abcde\",\n" + "      \"number\": \"447700900999\",\n" + "      \"sender_id\": \"verify\",\n" + "      \"date_submitted\": \"2016-10-21 15:41:02\",\n" + "      \"date_finalized\": \"\",\n" + "      \"checks\": [],\n" + "      \"first_event_date\": \"2016-10-21 15:41:02\",\n" + "      \"last_event_date\": \"2016-10-21 15:43:07\",\n" + "      \"price\": \"0.10000000\",\n" + "      \"currency\": \"EUR\",\n" + "      \"status\": \"IN PROGRESS\"\n" + "    },\n" + "    { \n" + "      \"request_id\": \"another-random-request-id\",\n" + "      \"account_id\": \"fghij\",\n" + "      \"number\": \"447700900991\",\n" + "      \"sender_id\": \"verify2\",\n" + "      \"date_submitted\": \"2016-10-21 15:41:58\",\n" + "      \"date_finalized\": \"\",\n" + "      \"checks\": [],\n" + "      \"first_event_date\": \"2016-10-21 15:41:58\",\n" + "      \"last_event_date\": \"2016-10-21 15:41:58\",\n" + "      \"price\": \"0.10000000\",\n" + "      \"currency\": \"EUR\",\n" + "      \"status\": \"EXPIRED\"\n" + "    }\n" + "  ]\n" + "}";
        wrapper.setHttpClient(this.stubHttpClient(200, json));

        SearchResult[] c = client.search("a-random-request-id", "another-random-request-id");
        SearchResult firstResult = c[0];
        SearchResult secondResult = c[1];

        assertEquals("a-random-request-id", firstResult.getRequestId());
        assertEquals("abcde", firstResult.getAccountId());
        assertEquals("447700900999", firstResult.getNumber());
        assertEquals("verify", firstResult.getSenderId());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-10-21 15:41:02"),
                firstResult.getDateSubmitted());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-10-21 15:41:02"),
                firstResult.getFirstEventDate());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-10-21 15:43:07"),
                firstResult.getLastEventDate());
        assertEquals(SearchResult.VerificationStatus.IN_PROGRESS, firstResult.getVerificationStatus());

        assertEquals("another-random-request-id", secondResult.getRequestId());
        assertEquals("fghij", secondResult.getAccountId());
        assertEquals("447700900991", secondResult.getNumber());
        assertEquals("verify2", secondResult.getSenderId());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-10-21 15:41:58"),
                secondResult.getDateSubmitted());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-10-21 15:41:58"),
                secondResult.getFirstEventDate());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-10-21 15:41:58"),
                secondResult.getLastEventDate());
        assertEquals(SearchResult.VerificationStatus.EXPIRED, secondResult.getVerificationStatus());
    }

    @Test(expected = NexmoResponseParseException.class)
    public void testSearchInvalidDates() throws Exception {
        String json = "    { \n" + "      \"request_id\": \"a-random-request-id\",\n" + "      \"account_id\": \"account-id\",\n" + "      \"number\": \"not-a-number\",\n" + "      \"sender_id\": \"verify\",\n" + "      \"date_submitted\": \"aaa\",\n" + "      \"date_finalized\": \"ddd\",\n" + "      \"checks\": [],\n" + "      \"first_event_date\": \"bbb\",\n" + "      \"last_event_date\": \"ccc\",\n" + "      \"price\": \"0.10000000\",\n" + "      \"currency\": \"EUR\",\n" + "      \"status\": \"SUCCESS\"\n" + "    }";

        wrapper.setHttpClient(this.stubHttpClient(200, json));
        SearchResult c = client.search("a-random-request-id");
    }
}
