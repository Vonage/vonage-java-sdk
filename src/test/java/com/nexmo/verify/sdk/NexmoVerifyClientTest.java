package com.nexmo.verify.sdk;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class NexmoVerifyClientTest {

    private NexmoVerifyClient client;

    @Before
    public void setUp() throws ParserConfigurationException {
        client = new NexmoVerifyClient("not-an-api-key", "secret");
    }

    private HttpClient stubHttpClient(int statusCode, String content) {
        HttpClient result = mock(HttpClient.class);
        try {
            HttpResponse response = mock(HttpResponse.class);
            StatusLine sl = mock(StatusLine.class);
            HttpEntity entity = mock(HttpEntity.class);

            when(result.execute(any(HttpUriRequest.class))).thenReturn(response);
            when(entity.getContent()).thenReturn(new ByteArrayInputStream(content.getBytes("UTF-8")));
            when(sl.getStatusCode()).thenReturn(statusCode);
            when(response.getStatusLine()).thenReturn(sl);
            when(response.getEntity()).thenReturn(entity);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        return result;
    }

    @Test
    public void testVerify() throws IOException, SAXException {
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
    public void testCheck() throws IOException, SAXException {
        client.setHttpClient(this.stubHttpClient(200, "<?xml version='1.0' encoding='UTF-8' ?>\n" +
                "    <verify_response>\n" +
                "        <event_id>verify-check-id</event_id>\n" +
                "        <status>0</status>\n" +
                "        <price>100.00</price>\n" +
                "        <currency>EUR</currency>\n" +
                "        <error_text>error</error_text>\n" +
                "    </verify_response>"));

        String id = "verify-check-id";
        String code = "1234";
        if (id != null && code != null) {
            CheckResult c = client.check(id, code);
            assertEquals(CheckResult.STATUS_OK, c.getStatus());
        }
    }

    @Test
    public void testSearchSuccess() throws IOException, SAXException, ParseException {
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
    public void testSearchError() throws IOException, SAXException, ParseException {
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
    public void testSearchFailed() throws IOException, SAXException {
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
    public void testSearchExpired() throws IOException, SAXException {
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
    public void testSearchInProgress() throws IOException, SAXException {
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
}
