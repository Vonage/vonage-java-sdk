package com.nexmo.verify.sdk.endpoints;
/*
 * Copyright (c) 2011-2016 Nexmo Inc
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

import com.nexmo.client.NexmoResponseParseException;
import com.nexmo.verify.sdk.SearchResult;
import org.junit.Before;
import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

public class SearchClientTest {

    private SearchClient client;

    @Before
    public void setUp() throws ParserConfigurationException {
        client = new SearchClient("https://base-url.example.com/verify", "api-key", "api-secret", 1000, 1000);
    }

    @Test
    public void testNoRequestId() throws Exception {
        try {
            client.search();
            fail("search should fail if no arguments are provided.");
        } catch (IllegalArgumentException e) {
            // this is expected
        }
    }

    @Test
    public void testTooManyRequestIds() throws Exception {
        try {
            client.search("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11");
            fail("search should fail if too many arguments are provided.");
        } catch (IllegalArgumentException e) {
            // this is expected
        }
    }

    @Test
    public void testParseSearchResponse() throws Exception {
        SearchResult[] rs = client.parseSearchResponse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<verify_request>\n" +
                "  <request_id>a-random-request-id</request_id>\n" +
                "  <account_id>account-id</account_id>\n" +
                "  <number>not-a-number</number>\n" +
                "  <sender_id>verify</sender_id>\n" +
                "  <date_submitted>aaa</date_submitted>\n" +
                "  <date_finalized>ddd</date_finalized>\n" +
                "  <checks />\n" +
                "  <first_event_date>bbb</first_event_date>\n" +
                "  <last_event_date>ccc</last_event_date>\n" +
                "  <price>0.10000000</price>\n" +
                "  <currency>EUR</currency>\n" +
                "  <status>SUCCESS</status>\n" +
                "</verify_request>");
        assertEquals(SearchResult.STATUS_OK, rs[0].getStatus());
        assertEquals("a-random-request-id", rs[0].getRequestId());
        assertEquals(SearchResult.VerificationStatus.SUCCESS, rs[0].getVerificationStatus());
    }

    @Test
    public void testParseSearchResponseInvalidRoot() throws Exception {
        try {
            client.parseSearchResponse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<INCORRECT>\n" +
                    "  <request_id>a-random-request-id</request_id>\n" +
                    "  <account_id>account-id</account_id>\n" +
                    "  <number>not-a-number</number>\n" +
                    "  <sender_id>verify</sender_id>\n" +
                    "  <date_submitted>aaa</date_submitted>\n" +
                    "  <date_finalized>ddd</date_finalized>\n" +
                    "  <checks />\n" +
                    "  <first_event_date>bbb</first_event_date>\n" +
                    "  <last_event_date>ccc</last_event_date>\n" +
                    "  <price>0.10000000</price>\n" +
                    "  <currency>EUR</currency>\n" +
                    "  <status>SUCCESS</status>\n" +
                    "</INCORRECT>");
            fail("If the root node is unrecognised, an NexmoResponseParseException should be thrown.");
        } catch (NexmoResponseParseException e) {
            // this is expected
        }
    }

    @Test
    public void testParseVerifyRequestXmlNodeBadStatus() throws Exception {
        try {
            client.parseSearchResponse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<verify_request>\n" +
                    "  <request_id>a-random-request-id</request_id>\n" +
                    "  <account_id>account-id</account_id>\n" +
                    "  <number>not-a-number</number>\n" +
                    "  <sender_id>verify</sender_id>\n" +
                    "  <date_submitted>aaa</date_submitted>\n" +
                    "  <date_finalized>ddd</date_finalized>\n" +
                    "  <checks />\n" +
                    "  <first_event_date>bbb</first_event_date>\n" +
                    "  <last_event_date>ccc</last_event_date>\n" +
                    "  <price>0.10000000</price>\n" +
                    "  <currency>EUR</currency>\n" +
                    "  <status>WHATISTHIS</status>\n" +
                    "</verify_request>");
            fail("Invalid status value should throw NexmoResponseParseException");
        } catch (NexmoResponseParseException e) {
            // this is expected
        }
    }

    @Test
    public void testCheckInvalidStatus() throws Exception {
        try {
            client.parseSearchResponse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<verify_request>\n" +
                    "  <request_id>a-random-request-id</request_id>\n" +
                    "  <account_id>account-id</account_id>\n" +
                    "  <number>not-a-number</number>\n" +
                    "  <sender_id>verify</sender_id>\n" +
                    "  <date_submitted>2016-10-19 11:18:56</date_submitted>\n" +
                    "  <date_finalized>2016-10-19 11:20:00</date_finalized>\n" +
                    "  <checks>\n" +
                    "    <check>\n" +
                    "      <date_received>2016-10-19 11:20:00</date_received>\n" +
                    "      <code>1234</code>\n" +
                    "      <status>WHATISTHIS</status>\n" +
                    "      <ip_address />\n" +
                    "    </check>\n" +
                    "  </checks>\n" +
                    "  <first_event_date>2016-10-19 11:18:56</first_event_date>\n" +
                    "  <last_event_date>2016-10-19 11:18:56</last_event_date>\n" +
                    "  <price>0</price>\n" +
                    "  <currency>EUR</currency>\n" +
                    "  <status>FAILED</status>\n" +
                    "</verify_request>");
                    fail("Invalid status value should throw NexmoResponseParseException");

        } catch (NexmoResponseParseException e) {
            // this is expected
        }
    }

    @Test
    public void testBadDateInCheck() throws Exception {
        SearchResult[] rs = client.parseSearchResponse(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<verify_request>\n" +
                "  <request_id>a-random-request-id</request_id>\n" +
                "  <account_id>account-id</account_id>\n" +
                "  <number>not-a-number</number>\n" +
                "  <sender_id>verify</sender_id>\n" +
                "  <date_submitted>2016-10-19 11:18:56</date_submitted>\n" +
                "  <date_finalized>2016-10-19 11:20:00</date_finalized>\n" +
                "  <checks>\n" +
                "    <check>\n" +
                "      <date_received>THIS IS NOT A DATE</date_received>\n" +
                "      <code>1234</code>\n" +
                "      <status>INVALID</status>\n" +
                "      <ip_address />\n" +
                "    </check>\n" +
                "  </checks>\n" +
                "  <first_event_date>2016-10-19 11:18:56</first_event_date>\n" +
                "  <last_event_date>2016-10-19 11:18:56</last_event_date>\n" +
                "  <price>0</price>\n" +
                "  <currency>EUR</currency>\n" +
                "  <status>FAILED</status>\n" +
                "</verify_request>");
        assertNull(rs[0].getChecks().get(0).getDate());
    }

    @Test
    public void testNullCheckStatus() throws Exception {
        try {
            client.parseSearchResponse(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                            "<verify_request>\n" +
                            "  <request_id>a-random-request-id</request_id>\n" +
                            "  <account_id>account-id</account_id>\n" +
                            "  <number>not-a-number</number>\n" +
                            "  <sender_id>verify</sender_id>\n" +
                            "  <date_submitted>2016-10-19 11:18:56</date_submitted>\n" +
                            "  <date_finalized>2016-10-19 11:20:00</date_finalized>\n" +
                            "  <checks>\n" +
                            "    <check>\n" +
                            "      <date_received>2016-10-19 11:20:00</date_received>\n" +
                            "      <code>1234</code>\n" +
                            "      <status/>\n" +
                            "      <ip_address />\n" +
                            "    </check>\n" +
                            "  </checks>\n" +
                            "  <first_event_date>2016-10-19 11:18:56</first_event_date>\n" +
                            "  <last_event_date>2016-10-19 11:18:56</last_event_date>\n" +
                            "  <price>0</price>\n" +
                            "  <currency>EUR</currency>\n" +
                            "  <status>FAILED</status>\n" +
                            "</verify_request>");
            fail("Null status should throw NexmoResponseParseException");
        } catch (NexmoResponseParseException e) {
            // this is expected
        }
    }

    @Test
    public void testNullCheckDateReceived() throws Exception {
        SearchResult[] rs = client.parseSearchResponse(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<verify_request>\n" +
                        "  <request_id>a-random-request-id</request_id>\n" +
                        "  <account_id>account-id</account_id>\n" +
                        "  <number>not-a-number</number>\n" +
                        "  <sender_id>verify</sender_id>\n" +
                        "  <date_submitted>2016-10-19 11:18:56</date_submitted>\n" +
                        "  <date_finalized>2016-10-19 11:20:00</date_finalized>\n" +
                        "  <checks>\n" +
                        "    <check>\n" +
                        "      <date_received/>\n" +
                        "      <code>1234</code>\n" +
                        "      <status>INVALID</status>\n" +
                        "      <ip_address />\n" +
                        "    </check>\n" +
                        "  </checks>\n" +
                        "  <first_event_date>2016-10-19 11:18:56</first_event_date>\n" +
                        "  <last_event_date>2016-10-19 11:18:56</last_event_date>\n" +
                        "  <price>0</price>\n" +
                        "  <currency>EUR</currency>\n" +
                        "  <status>FAILED</status>\n" +
                        "</verify_request>");
        assertNull(rs[0].getChecks().get(0).getDate());
    }

    @Test
    public void testParseCheckXmlNodeUnrecognizedElement() throws Exception {
        SearchResult[] rs = client.parseSearchResponse(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<verify_request>\n" +
                        "  <request_id>a-random-request-id</request_id>\n" +
                        "  <account_id>account-id</account_id>\n" +
                        "  <number>not-a-number</number>\n" +
                        "  <sender_id>verify</sender_id>\n" +
                        "  <date_submitted>2016-10-19 11:18:56</date_submitted>\n" +
                        "  <date_finalized>2016-10-19 11:20:00</date_finalized>\n" +
                        "  <checks>\n" +
                        "    <check>\n" +
                        "      <date_received>2016-10-19 11:20:00</date_received>\n" +
                        "      <code>1234</code>\n" +
                        "      <status>INVALID</status>\n" +
                        "      <ip_address />\n" +
                        "      <THISISEXTRA/>\n" +
                        "    </check>\n" +
                        "  </checks>\n" +
                        "  <first_event_date>2016-10-19 11:18:56</first_event_date>\n" +
                        "  <last_event_date>2016-10-19 11:18:56</last_event_date>\n" +
                        "  <price>0</price>\n" +
                        "  <currency>EUR</currency>\n" +
                        "  <status>FAILED</status>\n" +
                        "</verify_request>");
        assertEquals(new GregorianCalendar(2016, 9, 19, 11, 20, 0).getTime(), rs[0].getChecks().get(0).getDate());
    }
}
