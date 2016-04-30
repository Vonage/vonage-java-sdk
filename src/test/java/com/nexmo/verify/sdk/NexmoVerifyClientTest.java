package com.nexmo.verify.sdk;
/*
 * Copyright (c) 2011-2013 Nexmo Inc
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

import com.nexmo.NexmoBaseTest;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

import static org.junit.Assert.*;


public class NexmoVerifyClientTest extends NexmoBaseTest {

    private NexmoVerifyClient client;

    public NexmoVerifyClientTest() throws IOException {
        super();
    }

    @Before
    public void setUp() throws ParserConfigurationException {
        client = new NexmoVerifyClient(getApiKey(), getApiSecret());
    }

    @Test
    public void testVerify() throws IOException, SAXException {
        VerifyResult r = client.verify(getProperty("verify.number"),
                getProperty("verify.brand"),
                getProperty("verify.sender"), 6, Locale.US);
        assertEquals(VerifyResult.STATUS_OK, r.getStatus());
    }

    @Test
    public void testCheck() throws IOException, SAXException {
        String id = getProperty("verify.check.id", false);
        String code = getProperty("verify.check.code", false);
        if (id != null && code != null) {
            CheckResult c = client.check(id, code);
            assertEquals(CheckResult.STATUS_OK, c.getStatus());
        }
    }

    @Test
    public void testSearchSuccess() throws IOException, SAXException, ParseException {
        String id = getProperty("verify.search.success.id", false);
        if (id != null) {
            SearchResult c = client.search(id);
            assertEquals(SearchResult.STATUS_OK, c.getStatus());
            assertEquals(id, c.getRequestId());
            assertEquals(SearchResult.VerificationStatus.SUCCESS, c.getVerificationStatus());
            assertEquals(getProperty("verify.number"), c.getNumber());
            assertEquals(getProperty("verify.sender"), c.getSenderId());
            assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(getProperty("verify.search.success.dateSubmitted")),
                    c.getDateSubmitted());
            assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(getProperty("verify.search.success.dateFinalized")),
                    c.getDateFinalized());
            assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(getProperty("verify.search.success.firstEventDate")),
                    c.getFirstEventDate());
            assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(getProperty("verify.search.success.lastEventDate")),
                    c.getLastEventDate());
        }
    }

    @Test
    public void testSearchError() throws IOException, SAXException, ParseException {
        SearchResult c = client.search("AAAAA");
        assertEquals(SearchResult.STATUS_NO_RESPONSE, c.getStatus());
    }

    @Test
    public void testSearchFailed() throws IOException, SAXException {
        String id = getProperty("verify.search.failed.id", false);
        if (id != null) {
            SearchResult c = client.search(id);
            assertEquals(SearchResult.STATUS_OK, c.getStatus());
            assertEquals(id, c.getRequestId());
            assertEquals(SearchResult.VerificationStatus.FAILED, c.getVerificationStatus());
        }
    }

    @Test
    public void testSearchExpired() throws IOException, SAXException {
        String id = getProperty("verify.search.expired.id", false);
        if (id != null) {
            SearchResult c = client.search(id);
            assertEquals(SearchResult.STATUS_OK, c.getStatus());
            assertEquals(id, c.getRequestId());
            assertEquals(SearchResult.VerificationStatus.EXPIRED, c.getVerificationStatus());
        }
    }

    @Test
    public void testSearchInProgress() throws IOException, SAXException {
        String id = getProperty("verify.search.inProgress.id", false);
        if (id != null) {
            SearchResult c = client.search(id);
            assertEquals(SearchResult.STATUS_OK, c.getStatus());
            assertEquals(id, c.getRequestId());
            assertEquals(SearchResult.VerificationStatus.IN_PROGRESS, c.getVerificationStatus());
        }
    }

}
