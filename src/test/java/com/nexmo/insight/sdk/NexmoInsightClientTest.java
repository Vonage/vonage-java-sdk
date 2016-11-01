package com.nexmo.insight.sdk;
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
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.*;

import javax.xml.parsers.ParserConfigurationException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class NexmoInsightClientTest {
    private NexmoInsightClient client;

    @Before
    public void setUp() throws ParserConfigurationException {
        client = new NexmoInsightClient("not-an-api-key", "secret");
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
    public void testInsight() throws Exception {
        this.client.setHttpClient(this.stubHttpClient(
                200,
                "<?xml version='1.0' encoding='UTF-8' ?>\n" +
                        "  <lookup>\n" +
                        "      <requestId>a-fictitious-request-id</requestId>\n" +
                        "      <number>447700900999</number>\n" +
                        "      <status>0</status>\n" +
                        "      <errorText>error</errorText>\n" +
                        "      <requestPrice>0.03</requestPrice>\n" +
                        "      <remainingBalance>1.97</remainingBalance>\n" +
                        "  </lookup>"));


        String[] features = new String[] { "type", "reachable"};

        InsightResult r = client.request(
                "447700900999",
                "http://some.callback.url/script.php",
                features,
                30000,
                "POST",
                "CLIENT-DATA",
                "127.0.0.1");
        assertEquals(InsightResult.STATUS_OK, r.getStatus());
        assertEquals("447700900999", r.getNumber());
        assertEquals(0.03f, r.getRequestPrice(), 0);
    }

    @Test
    public void testParseInsightResult() throws Exception {
        InsightResult r = this.client.parseInsightResult("<?xml version='1.0' encoding='UTF-8' ?>\n" +
                "  <lookup>\n" +
                "      <requestId>a-fictitious-request-id</requestId>\n" +
                "      <number>447700900999</number>\n" +
                "      <status>0</status>\n" +
                "      <errorText>error</errorText>\n" +
                "      <requestPrice>0.03</requestPrice>\n" +
                "      <remainingBalance>1.97</remainingBalance>\n" +
                "  </lookup>");
        assertEquals(0, r.getStatus());
        assertEquals("447700900999", r.getNumber());
    }

    @Test
    public void testParseInsightResultBadXml() throws Exception {
        try {
            this.client.parseInsightResult("NOT-XML");
            fail("A non-XML response should raise NexmoResponseParseException");
        } catch (NexmoResponseParseException e) {
            // this is expected
        }
    }

    @Test
    public void testParseInsightResultIncorrectRoot() throws Exception {
        try {
            this.client.parseInsightResult("<?xml version='1.0' encoding='UTF-8' ?><not-lookup/>");
            fail("A non-XML response should raise NexmoResponseParseException");
        } catch (NexmoResponseParseException e) {
            // this is expected
        }
    }

    @Test
    public void testParseInsightResultEmptyValues() throws Exception {
        InsightResult r = this.client.parseInsightResult("<?xml version='1.0' encoding='UTF-8' ?>\n" +
                "  <lookup>\n" +
                "      <requestId></requestId>\n" +
                "      <number></number>\n" +
                "      <status>0</status>\n" +
                "      <errorText></errorText>\n" +
                "      <requestPrice></requestPrice>\n" +
                "      <remainingBalance></remainingBalance>\n" +
                "  </lookup>");
        assertEquals(0, r.getStatus());
        assertEquals(null, r.getNumber());
        assertEquals(null, r.getErrorText());
        assertEquals(null, r.getRequestId());
        assertEquals(0.0, r.getRequestPrice(), 0.0001);
        assertEquals(0.0, r.getRemainingBalance(), 0.0001);
    }

    @Test
    public void testParseInsightResultStatusBad() throws Exception {
        InsightResult r = this.client.parseInsightResult("<?xml version='1.0' encoding='UTF-8' ?>\n" +
                "  <lookup>\n" +
                "      <requestId>a-fictitious-request-id</requestId>\n" +
                "      <number>447700900999</number>\n" +
                "      <status>SHOULDBEANUMBER</status>\n" +
                "      <errorText>error</errorText>\n" +
                "      <requestPrice>0.03</requestPrice>\n" +
                "      <remainingBalance>1.97</remainingBalance>\n" +
                "  </lookup>");
        assertEquals(InsightResult.STATUS_INTERNAL_ERROR, r.getStatus());
    }

    @Test
    public void testParseInsightResultStatusEmpty() throws Exception {
        try {
            this.client.parseInsightResult("<?xml version='1.0' encoding='UTF-8' ?>\n" +
                    "  <lookup>\n" +
                    "      <requestId>a-fictitious-request-id</requestId>\n" +
                    "      <number>447700900999</number>\n" +
                    "      <status></status>\n" +
                    "      <errorText>error</errorText>\n" +
                    "      <requestPrice>0.03</requestPrice>\n" +
                    "      <remainingBalance>1.97</remainingBalance>\n" +
                    "  </lookup>");
        } catch (NexmoResponseParseException e) {
            // this is expected
        }
    }

    @Test
    public void testParseInsightResultStatusMissing() throws Exception {
        try {
            this.client.parseInsightResult("<?xml version='1.0' encoding='UTF-8' ?>\n" +
                    "  <lookup>\n" +
                    "      <requestId>a-fictitious-request-id</requestId>\n" +
                    "      <number>447700900999</number>\n" +
                    "      <errorText>error</errorText>\n" +
                    "      <requestPrice>0.03</requestPrice>\n" +
                    "      <remainingBalance>1.97</remainingBalance>\n" +
                    "  </lookup>");
            fail("Missing <status> should result in NexmoResponseParseException");
        } catch (NexmoResponseParseException e) {
            // this is expected
        }
    }

    @Test
    public void testParseInsightResultBadFloats() throws Exception {
        InsightResult r = this.client.parseInsightResult("<?xml version='1.0' encoding='UTF-8' ?>\n" +
                "  <lookup>\n" +
                "      <requestId></requestId>\n" +
                "      <number></number>\n" +
                "      <status>0</status>\n" +
                "      <errorText></errorText>\n" +
                "      <requestPrice>SHOULDBEAFLOAT</requestPrice>\n" +
                "      <remainingBalance>SHOULDBEAFLOAT</remainingBalance>\n" +
                "  </lookup>");
        assertEquals(0.0f, r.getRequestPrice(), 0.0001f);
        assertEquals(0.0f, r.getRemainingBalance(), 0.0001f);
    }

    @Test
    public void testParseInsightResultUnrecognizedTag() throws Exception {
        InsightResult r = this.client.parseInsightResult("<?xml version='1.0' encoding='UTF-8' ?>\n" +
                "  <lookup>\n" +
                "      <requestId>a-fictitious-request-id</requestId>\n" +
                "      <number>447700900999</number>\n" +
                "      <status>0</status>\n" +
                "      <errorText>error</errorText>\n" +
                "      <requestPrice>0.03</requestPrice>\n" +
                "      <remainingBalance>1.97</remainingBalance>\n" +
                "      <WHATISTHIS/>\n" +
                "  </lookup>");
        assertEquals(0, r.getStatus());
        assertEquals("447700900999", r.getNumber());
    }
}
