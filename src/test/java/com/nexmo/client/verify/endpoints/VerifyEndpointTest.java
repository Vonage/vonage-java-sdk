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
import com.nexmo.client.verify.VerifyRequest;
import com.nexmo.client.verify.VerifyResult;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Before;
import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;
import java.util.List;
import java.util.Locale;

import static com.nexmo.client.TestUtils.assertContainsParam;
import static com.nexmo.client.TestUtils.assertParamMissing;
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
                VerifyRequest.LineType.MOBILE
        );
        VerifyEndpoint endpoint = new VerifyEndpoint(null);
        RequestBuilder request = endpoint.makeRequest(verifyRequest);

        assertEquals("POST", request.build().getMethod());
        assertEquals("https://api.nexmo.com/verify/xml", request.build().getURI().toString());
        List<NameValuePair> params = request.getParameters();

        assertContainsParam(params, "number", "4477990090090");
        assertContainsParam(params, "brand", "Brand.com");
        assertContainsParam(params, "sender_id", "Your friend");
        assertContainsParam(params, "code_length", "4");
        assertContainsParam(params, "lg", "en-gb");
        assertContainsParam(params, "require_type", "MOBILE");
    }

    @Test
    public void testCustomBaseUrl() throws Exception {
        VerifyRequest verifyRequest = new VerifyRequest(
                "4477990090090",
                "Brand.com",
                "Your friend",
                4,
                new Locale("en", "GB"),
                VerifyRequest.LineType.MOBILE
        );
        VerifyEndpoint endpoint = new VerifyEndpoint(null);
        endpoint.setBaseUrl("https://api.example.com/");
        RequestBuilder request = endpoint.makeRequest(verifyRequest);

        assertEquals("POST", request.build().getMethod());
        assertEquals("https://api.example.com/verify/xml", request.build().getURI().toString());
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
        assertContainsParam(params, "number", "4477990090090");
        assertContainsParam(params, "brand", "Brand.com");
        assertParamMissing(params, "code_length");
        assertParamMissing(params, "lg");
        assertParamMissing(params, "sender_id");
        assertParamMissing(params, "require_type");
        assertParamMissing(params, "country");
        assertParamMissing(params, "pin_expiry");
        assertParamMissing(params, "next_event_wait");
    }

    @Test
    public void testConstructVerifyParamsWithOptionalValues() throws Exception {
        VerifyRequest verifyRequest = new VerifyRequest(
                "4477990090090",
                "Brand.com"
        );
        verifyRequest.setFrom("VERIFICATION");
        verifyRequest.setLength(6);
        verifyRequest.setLocale(new Locale("en", "gb"));
        verifyRequest.setType(VerifyRequest.LineType.LANDLINE);
        verifyRequest.setCountry("ZZ");
        verifyRequest.setPinExpiry(60);
        verifyRequest.setNextEventWait(90);

        VerifyEndpoint endpoint = new VerifyEndpoint(null);
        RequestBuilder request = endpoint.makeRequest(verifyRequest);
        List<NameValuePair> params = request.getParameters();
        assertContainsParam(params, "number", "4477990090090");
        assertContainsParam(params, "brand", "Brand.com");

        assertContainsParam(params, "code_length", "6");
        assertContainsParam(params, "sender_id", "VERIFICATION");
        assertContainsParam(params, "lg", "en-gb");
        assertContainsParam(params, "require_type", "LANDLINE");
        assertContainsParam(params, "country", "ZZ");
        assertContainsParam(params, "pin_expiry", "60");
        assertContainsParam(params, "next_event_wait", "90");

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
                    VerifyRequest.LineType.MOBILE
            );
            fail("A null 'number' argument should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // this is expected
        }
    }

    @Test
    public void testConstructVerifyParamsNullBrand() throws Exception {
        try {
            new VerifyRequest(
                    "4477990090090",
                    null,
                    "Your friend",
                    4,
                    new Locale("en", "GB"),
                    VerifyRequest.LineType.MOBILE
            );
            fail("A null 'brand' argument should throw IllegalArgumentException");
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
                    VerifyRequest.LineType.MOBILE
            );
            fail("A VerifyRequest with length 3 should raise IllegalArgumentException.");
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
}
