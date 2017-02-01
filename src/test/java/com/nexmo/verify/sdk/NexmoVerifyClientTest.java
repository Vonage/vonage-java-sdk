/*
 * Copyright (c) 2011-2017 Nexmo Inc
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
package com.nexmo.verify.sdk;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Before;
import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class NexmoVerifyClientTest {

    private NexmoVerifyClient client;

    @Before
    public void setUp() throws ParserConfigurationException {
        client = new NexmoVerifyClient("not-an-api-key", "secret");
    }

    private HttpClient stubHttpClient(int statusCode, String content) throws Exception {
        HttpClient result = mock(HttpClient.class);

        HttpResponse response = mock(HttpResponse.class);
        StatusLine sl = mock(StatusLine.class);
        HttpEntity entity = mock(HttpEntity.class);

        when(result.execute(any(HttpUriRequest.class))).thenReturn(response);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(content.getBytes("UTF-8")));
        when(sl.getStatusCode()).thenReturn(statusCode);
        when(response.getStatusLine()).thenReturn(sl);
        when(response.getEntity()).thenReturn(entity);

        return result;
    }

    @Test
    public void testConstructorNullBaseUrl() throws Exception {
        try {
            new NexmoVerifyClient(null, "api-key", "api-secret", 5000, 5000);
            fail("null baseUrl should have raised IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // This is expected
        }
    }

    @Test
    public void testConstructorHttpBaseUrl() throws Exception {
        try {
            new NexmoVerifyClient("http://not.a.real.domain/api", "api-key", "api-secret", 5000, 5000);
            fail("null baseUrl should have raised IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // This is expected
        }
    }

    @Test
    public void testVerify() throws Exception {
        client.setHttpClient(this.stubHttpClient(200, "<?xml version='1.0' encoding='UTF-8' ?>\n" +
                "    <verify_response>\n" +
                "        <request_id>not-really-a-request-id</request_id>\n" +
                "        <status>0</status>\n" +
                "        <error_text>error</error_text>\n" +
                "    </verify_response>"));
        VerifyResult r = client.verify(
                "447700900999",
                "TestBrand",
                "15555215554", 6, Locale.US);
        assertEquals(VerifyResult.STATUS_OK, r.getStatus());
    }

    @Test
    public void testVerifyHttpError() throws Exception {
        client.setHttpClient(this.stubHttpClient(500, "<?xml version='1.0' encoding='UTF-8' ?>\n" +
                "    <verify_response>\n" +
                "        <request_id>not-really-a-request-id</request_id>\n" +
                "        <status>0</status>\n" +
                "        <error_text>error</error_text>\n" +
                "    </verify_response>"));
        VerifyResult r = client.verify(
                "447700900999",
                "TestBrand",
                "15555215554", 6, Locale.US);
        assertEquals(VerifyResult.STATUS_COMMS_FAILURE, r.getStatus());
    }

    @Test
    public void testCheck() throws Exception {
        client.setHttpClient(this.stubHttpClient(200, "<?xml version='1.0' encoding='UTF-8' ?>\n" +
                "    <verify_response>\n" +
                "        <event_id>verify-check-id</event_id>\n" +
                "        <status>0</status>\n" +
                "        <price>100.00</price>\n" +
                "        <currency>EUR</currency>\n" +
                "        <error_text>error</error_text>\n" +
                "    </verify_response>"));

        CheckResult c = client.check("verify-check-id", "1234", "my-ip-address");
        assertEquals(CheckResult.STATUS_OK, c.getStatus());
    }

    @Test
    public void testCheckMissingParams() throws Exception {
        client.setHttpClient(this.stubHttpClient(200, "<?xml version='1.0' encoding='UTF-8' ?>\n" +
                "    <verify_response>\n" +
                "        <event_id>verify-check-id</event_id>\n" +
                "        <status>0</status>\n" +
                "        <price>100.00</price>\n" +
                "        <currency>EUR</currency>\n" +
                "        <error_text>error</error_text>\n" +
                "    </verify_response>"));

        try {
            client.check(null, null);
            fail("Calling check with null destination should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // This is expected
        }
    }

    @Test
    public void testCheckError() throws Exception {
        client.setHttpClient(this.stubHttpClient(200, "<?xml version='1.0' encoding='UTF-8' ?>\n" +
                "    <verify_response>\n" +
                "        <status>6</status>\n" +
                "        <error_text>Something has gone terribly wrong!</error_text>\n" +
                "    </verify_response>"));

        CheckResult c = client.check("verify-check-id", "1234", "my-ip-address");
        assertEquals(CheckResult.STATUS_INVALID_REQUEST, c.getStatus());
    }

    @Test
    public void testCheckInvalidResponse() throws Exception {
        client.setHttpClient(this.stubHttpClient(
                500,
                "<?xml version='1.0' encoding='UTF-8' ?>\n" +
                "<not_valid_response />"));

        CheckResult c = client.check("verify-check-id", "1234", "my-ip-address");
        assertEquals(CheckResult.STATUS_COMMS_FAILURE, c.getStatus());
    }

    @Test
    public void testSearchSuccess() throws Exception {
        client.setHttpClient(this.stubHttpClient(200, "<?xml version='1.0' encoding='UTF-8' ?>\n" +
                "    <verify_request>\n" +
                "        <request_id>not-a-search-request-id</request_id>\n" +
                "        <account_id>not-an-account-id</account_id>\n" +
                "        <number>447700900999</number>\n" +
                "        <sender_id>447700900991</sender_id>\n" +
                "        <date_submitted>2014-03-04 10:11:12</date_submitted>\n" +
                "        <date_finalized>2015-02-03 07:08:09</date_finalized>\n" +
                "        <first_event_date>2012-01-02 03:04:05</first_event_date>\n" +
                "        <last_event_date>2012-01-02 03:04:06</last_event_date>\n" +
                "        <checks>\n" +
                "           <check>\n" +
                "           <date_received>2012-01-02 03:04:05</date_received>\n" +
                "           <status>VALID</status>\n" +
                "           <code>code</code>\n" +
                "        </check>\n" +
                "        <check>\n" +
                "           <date_received>2012-01-02 03:04:06</date_received>\n" +
                "           <status>VALID</status>\n" +
                "           <code>code</code>\n" +
                "           </check>\n" +
                "        </checks>\n" +
                "        <price>price</price>\n" +
                "        <currency> currency</currency>\n" +
                "        <error_text>error</error_text>\n" +
                "        <status>SUCCESS</status>\n" +
                "    </verify_request>"));


        SearchResult c = client.search("not-a-search-request-id");
        assertEquals(SearchResult.STATUS_OK, c.getStatus());
        assertEquals("not-a-search-request-id", c.getRequestId());
        assertEquals(SearchResult.VerificationStatus.SUCCESS, c.getVerificationStatus());
        assertEquals("447700900999", c.getNumber());
        assertEquals("447700900991", c.getSenderId());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-03-04 10:11:12"),
                c.getDateSubmitted());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2015-02-03 07:08:09"),
                c.getDateFinalized());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2012-01-02 03:04:05"),
                c.getFirstEventDate());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2012-01-02 03:04:06"),
                c.getLastEventDate());
    }

    @Test
    public void testSearchError() throws Exception {
        client.setHttpClient(this.stubHttpClient(200, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<verify_response>\n" +
                "  <request_id />\n" +
                "  <status>101</status>\n" +
                "  <error_text>No response found</error_text>\n" +
                "</verify_response>"));

        SearchResult c = client.search("AAAAA");
        assertEquals(SearchResult.STATUS_NO_RESPONSE, c.getStatus());
    }

    @Test
    public void testSearchFailed() throws Exception {
        client.setHttpClient(this.stubHttpClient(200, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
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
                "    </check>\n" +
                "    <check>\n" +
                "      <date_received>2016-10-19 11:19:54</date_received>\n" +
                "      <code>1234</code>\n" +
                "      <status>INVALID</status>\n" +
                "      <ip_address />\n" +
                "    </check>\n" +
                "    <check>\n" +
                "      <date_received>2016-10-19 11:19:58</date_received>\n" +
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
                "</verify_request>"));

            SearchResult c = client.search("a-random-request-id");
            assertEquals(SearchResult.STATUS_OK, c.getStatus());
            assertEquals("a-random-request-id", c.getRequestId());
            assertEquals(SearchResult.VerificationStatus.FAILED, c.getVerificationStatus());
    }

    @Test
    public void testSearchExpired() throws Exception {
        client.setHttpClient(this.stubHttpClient(200, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<verify_request>\n" +
                "  <request_id>a-random-request-id</request_id>\n" +
                "  <account_id>account-id</account_id>\n" +
                "  <number>not-a-number</number>\n" +
                "  <sender_id>verify</sender_id>\n" +
                "  <date_submitted>2016-10-19 11:25:19</date_submitted>\n" +
                "  <date_finalized>2016-10-19 11:35:27</date_finalized>\n" +
                "  <checks />\n" +
                "  <first_event_date>2016-10-19 11:25:19</first_event_date>\n" +
                "  <last_event_date>2016-10-19 11:30:26</last_event_date>\n" +
                "  <price>0</price>\n" +
                "  <currency>EUR</currency>\n" +
                "  <status>EXPIRED</status>\n" +
                "</verify_request>"));

        SearchResult c = client.search("a-random-request-id");
        assertEquals(SearchResult.STATUS_OK, c.getStatus());
        assertEquals("a-random-request-id", c.getRequestId());
        assertEquals(SearchResult.VerificationStatus.EXPIRED, c.getVerificationStatus());
    }

    @Test
    public void testSearchInProgress() throws Exception {
        client.setHttpClient(this.stubHttpClient(200, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<verify_request>\n" +
                "  <request_id>a-random-request-id</request_id>\n" +
                "  <account_id>account-id</account_id>\n" +
                "  <number>not-a-number</number>\n" +
                "  <sender_id>verify</sender_id>\n" +
                "  <date_submitted>2016-10-19 11:25:19</date_submitted>\n" +
                "  <date_finalized />\n" +
                "  <checks />\n" +
                "  <first_event_date>2016-10-19 11:25:19</first_event_date>\n" +
                "  <last_event_date>2016-10-19 11:30:26</last_event_date>\n" +
                "  <price>0.10000000</price>\n" +
                "  <currency>EUR</currency>\n" +
                "  <status>IN PROGRESS</status>\n" +
                "</verify_request>"));
        SearchResult c = client.search("a-random-request-id");
        assertEquals(SearchResult.STATUS_OK, c.getStatus());
        assertEquals("a-random-request-id", c.getRequestId());
        assertEquals(SearchResult.VerificationStatus.IN_PROGRESS, c.getVerificationStatus());
    }

    @Test
    public void testSearchWithWrongParams() throws Exception {
        client.setHttpClient(this.stubHttpClient(200, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<verify_response>\n" +
                "  <request_id />\n" +
                "  <status>6</status>\n" +
                "  <error_text>The Nexmo platform was unable to process this message for the following reason: Wrong parameters</error_text>\n" +
                "</verify_response>"));
        SearchResult c = client.search("a-random-request-id");
        assertEquals(SearchResult.STATUS_INVALID_REQUEST, c.getStatus());
    }

    @Test
    public void testSearchWithMultipleResults() throws Exception {
        client.setHttpClient(this.stubHttpClient(200, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<verification_requests>\n" +
                "  <verify_request>\n" +
                "    <request_id>a-random-request-id</request_id>\n" +
                "    <account_id>abcde</account_id>\n" +
                "    <number>447700900999</number>\n" +
                "    <sender_id>verify</sender_id>\n" +
                "    <date_submitted>2016-10-21 15:41:02</date_submitted>\n" +
                "    <date_finalized />\n" +
                "    <checks />\n" +
                "    <first_event_date>2016-10-21 15:41:02</first_event_date>\n" +
                "    <last_event_date>2016-10-21 15:43:07</last_event_date>\n" +
                "    <price>0.10000000</price>\n" +
                "    <currency>EUR</currency>\n" +
                "    <status>IN PROGRESS</status>\n" +
                "  </verify_request>\n" +
                "  <verify_request>\n" +
                "    <request_id>another-random-request-id</request_id>\n" +
                "    <account_id>abcde</account_id>\n" +
                "    <number>447700900991</number>\n" +
                "    <sender_id>verify</sender_id>\n" +
                "    <date_submitted>2016-10-21 15:41:58</date_submitted>\n" +
                "    <date_finalized />\n" +
                "    <checks />\n" +
                "    <first_event_date>2016-10-21 15:41:58</first_event_date>\n" +
                "    <last_event_date>2016-10-21 15:41:58</last_event_date>\n" +
                "    <price>0.10000000</price>\n" +
                "    <currency>EUR</currency>\n" +
                "    <status>IN PROGRESS</status>\n" +
                "  </verify_request>\n" +
                "</verification_requests>"));
        SearchResult[] c = client.search("a-random-request-id", "another-random-request-id");
        assertEquals(SearchResult.STATUS_OK, c[0].getStatus());
    }

    @Test
    public void testSearchDodgyDates() throws Exception {
        client.setHttpClient(this.stubHttpClient(200, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
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
                "</verify_request>"));
        SearchResult c = client.search("a-random-request-id");
        assertEquals(SearchResult.STATUS_OK, c.getStatus());
        assertEquals("a-random-request-id", c.getRequestId());
        assertEquals(SearchResult.VerificationStatus.SUCCESS, c.getVerificationStatus());
    }

    @Test
    public void testSearchHttpError() throws Exception {
        client.setHttpClient(this.stubHttpClient(500, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<verify_request />"));

        SearchResult c = client.search("a-random-request-id");
        assertEquals(SearchResult.STATUS_COMMS_FAILURE, c.getStatus());

    }

    @Test
    public void testLineType() {
        NexmoVerifyClient.LineType all = NexmoVerifyClient.LineType.ALL;
        assertEquals("ALL", all.toString());
    }
}