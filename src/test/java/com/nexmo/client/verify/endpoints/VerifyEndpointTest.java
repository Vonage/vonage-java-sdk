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
package com.nexmo.client.verify.endpoints;


import com.nexmo.client.NexmoResponseParseException;
import com.nexmo.client.verify.VerifyClient;
import com.nexmo.client.verify.VerifyRequest;
import com.nexmo.client.verify.VerifyResult;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Before;
import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.junit.Assert.*;

public class VerifyEndpointTest {

    private VerifyEndpoint client;

    @Before
    public void setUp() throws ParserConfigurationException {
        client = new VerifyEndpoint(null);
    }

    @Test
    public void testConstructVerifyParams() throws Exception {
        VerifyRequest verifyRequest = new VerifyRequest(
                "4477990090090",
                "Brand.com",
                "Your friend",
                4,
                new Locale("en", "GB"),
                com.nexmo.client.verify.VerifyClient.LineType.MOBILE
        );
        VerifyEndpoint endpoint = new VerifyEndpoint(null);
        RequestBuilder request = endpoint.makeRequest(verifyRequest);
        List<NameValuePair> params = request.getParameters();

        assertContainsParam(params, "number", "4477990090090");
        assertContainsParam(params, "brand", "Brand.com");
        assertContainsParam(params, "sender_id", "Your friend");
        assertContainsParam(params, "code_length", "4");
        assertContainsParam(params, "lg", "en-gb");
        assertContainsParam(params, "require_type", "MOBILE");
    }

    @Test
    public void testConstructVerifyParamsMissingValues() throws Exception {
        VerifyRequest verifyRequest = new VerifyRequest(
                "4477990090090",
                "Brand.com",
                null,
                0,
                null,
                null
        );
        VerifyEndpoint endpoint = new VerifyEndpoint(null);
        RequestBuilder request = endpoint.makeRequest(verifyRequest);
        List<NameValuePair> params = request.getParameters();
        assertParamMissing(params, "code_length");
        assertParamMissing(params, "lg");
        assertParamMissing(params, "from");
        assertParamMissing(params, "require_type");
    }

    @Test
    public void testConstructVerifyParamsNullNumber() throws Exception {
        try {
            new VerifyRequest(
                    null,
                    "Brand.com",
                    "Your friend",
                    4,
                    new Locale("en", "GB"),
                    VerifyClient.LineType.MOBILE
            );
            fail();
        } catch (IllegalArgumentException e) {
            // this is expected
        }
    }

    @Test
    public void testConstructVerifyParamsNullBrand() throws Exception {
        try {
            new VerifyRequest(
                    null,
                    "Brand.com",
                    "Your friend",
                    4,
                    new Locale("en", "GB"),
                    VerifyClient.LineType.MOBILE
            );
            fail();
        } catch (IllegalArgumentException e) {
            // this is expected
        }
    }

    @Test
    public void testConstructVerifyParamsInvalidCode() throws Exception {
        try {
            new VerifyRequest(
                    "4477990090090",
                    "Brand.com",
                    "Your friend",
                    3,
                    new Locale("en", "GB"),
                    com.nexmo.client.verify.VerifyClient.LineType.MOBILE
            );
            fail();
        } catch (IllegalArgumentException e) {
            // this is expected
        }
    }

    @Test
    public void testParseVerifyResponse() throws Exception {
        VerifyResult r = client.parseVerifyResponse("<?xml version='1.0' encoding='UTF-8' ?>\n" +
                "    <verify_response>\n" +
                "        <request_id>not-really-a-request-id</request_id>\n" +
                "        <status>0</status>\n" +
                "        <error_text>error</error_text>\n" +
                "    </verify_response>");
        assertEquals("not-really-a-request-id", r.getRequestId());
        assertEquals(VerifyResult.STATUS_OK, r.getStatus());
        assertEquals("error", r.getErrorText());
    }

    @Test
    public void testParseVerifyResponseBadXml() throws Exception {
        try {
            client.parseVerifyResponse("NOT XML");
            fail("Invalid XML should cause NexmoResponseParseException");
        } catch (NexmoResponseParseException e) {
            // this is expected
        }
    }

    @Test
    public void testParseVerifyResponseIncorrectXmlRoot() throws Exception {
        try {
            client.parseVerifyResponse("<?xml version=\"1.0\" encoding=\"UTF-8\"?><INCORRECTROOT/>");
            fail("Incorrect XML root should cause NexmoResponseParseException");
        } catch (NexmoResponseParseException e) {
            // this is expected
        }
    }

    @Test
    public void testParseVerifyResponseInvalidStatus() throws Exception {
        VerifyResult r = client.parseVerifyResponse("<?xml version='1.0' encoding='UTF-8' ?>\n" +
                "    <verify_response>\n" +
                "        <request_id>not-really-a-request-id</request_id>\n" +
                "        <status>NOTANUMBER</status>\n" +
                "        <error_text>error</error_text>\n" +
                "    </verify_response>");
        assertEquals(VerifyResult.STATUS_INTERNAL_ERROR, r.getStatus());
    }

    @Test
    public void testParseVerifyResponseMissingStatus() throws Exception {
        try {
            client.parseVerifyResponse("<?xml version='1.0' encoding='UTF-8' ?>\n" +
                    "    <verify_response>\n" +
                    "        <request_id>not-really-a-request-id</request_id>\n" +
                    "        <error_text>error</error_text>\n" +
                    "    </verify_response>");
            fail("Missing <status> should result in NexmoResponseParseException");
        } catch (NexmoResponseParseException e) {
            // this is expected
        }
    }

    @Test
    public void testParseVerifyResponseEmptyStatus() throws Exception {
        try {
            client.parseVerifyResponse("<?xml version='1.0' encoding='UTF-8' ?>\n" +
                    "    <verify_response>\n" +
                    "        <request_id>not-really-a-request-id</request_id>\n" +
                    "        <status/>\n" +
                    "        <error_text>error</error_text>\n" +
                    "    </verify_response>");
            fail("Missing <status> should result in NexmoResponseParseException");
        } catch (NexmoResponseParseException e) {
            // this is expected
        }
    }

    @Test
    public void testParseVerifyResponseStatusThrottled() throws Exception {
        VerifyResult r = client.parseVerifyResponse("<?xml version='1.0' encoding='UTF-8' ?>\n" +
                "    <verify_response>\n" +
                "        <request_id>not-really-a-request-id</request_id>\n" +
                "        <status>1</status>\n" +
                "        <error_text>error</error_text>\n" +
                "    </verify_response>");

        assertEquals(VerifyResult.STATUS_THROTTLED, r.getStatus());
        assertTrue(r.isTemporaryError());
    }

    @Test
    public void testParseVerifyResponseStatusInternalError() throws Exception {
        VerifyResult r = client.parseVerifyResponse("<?xml version='1.0' encoding='UTF-8' ?>\n" +
                "    <verify_response>\n" +
                "        <request_id>not-really-a-request-id</request_id>\n" +
                "        <status>5</status>\n" +
                "        <error_text>error</error_text>\n" +
                "    </verify_response>");

        assertEquals(VerifyResult.STATUS_INTERNAL_ERROR, r.getStatus());
        assertTrue(r.isTemporaryError());
    }

    @Test
    public void testParseVerifyResponseUnexpectedTag() throws Exception {
        try {
            client.parseVerifyResponse("<?xml version='1.0' encoding='UTF-8' ?>\n" +
                    "    <verify_response>\n" +
                    "        <request_id>not-really-a-request-id</request_id>\n" +
                    "        <status/>\n" +
                    "        <error_text>error</error_text>\n" +
                    "        <WHATISTHIS/>\n" +
                    "    </verify_response>");
            fail("Missing <status> should result in NexmoResponseParseException");
        } catch (NexmoResponseParseException e) {
            // this is expected
        }
    }

    private static void assertContainsParam(List<NameValuePair> params, String key, String value) {
        NameValuePair item = new BasicNameValuePair(key, value);
        assertTrue(
                "" + params + " should contain " + item,
                params.contains(item)
        );
    }

    private static void assertParamMissing(List<NameValuePair> params, String key) {
        Set<String> keys = new HashSet<>();
        for (NameValuePair pair : params) {
            keys.add(pair.getName());
        }
        assertFalse(
                "" + params + " should not contain " + key,
                keys.contains(key)
        );
    }
}
