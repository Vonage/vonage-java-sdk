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

import com.nexmo.client.ClientTest;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class VerifyClientVerifyEndpointTest extends ClientTest<VerifyClient> {

    @Before
    public void setUp() {
        client = new VerifyClient(wrapper);
    }

    @Test
    public void testVerifyWithNumberBrandFromLengthLocaleLineType() throws Exception {
        wrapper.setHttpClient(this.stubHttpClient(200,
                                                  "{\n" + "  \"request_id\": \"not-really-a-request-id\",\n" + "  \"status\": 0,\n" + "  \"error_text\": \"error\"\n" + "}"));
        VerifyResult r = client.verify("447700900999", "TestBrand", "15555215554", 6, Locale.US, VerifyRequest.LineType.MOBILE);
        assertEquals(VerifyResult.STATUS_OK, r.getStatus());
    }

    @Test
    public void testVerifyWithNumberBrandFromLengthLocale() throws Exception {
        wrapper.setHttpClient(this.stubHttpClient(200,
                "{\n" + "  \"request_id\": \"not-really-a-request-id\",\n" + "  \"status\": 0,\n" + "  \"error_text\": \"error\"\n" + "}"));
        VerifyResult r = client.verify("447700900999", "TestBrand", "15555215554", 6, Locale.US);
        assertEquals(VerifyResult.STATUS_OK, r.getStatus());
    }

    @Test
    public void testVerifyWithNumberBrandFrom() throws Exception {
        wrapper.setHttpClient(this.stubHttpClient(200,
                                                  "{\n" + "  \"request_id\": \"not-really-a-request-id\",\n" + "  \"status\": 0,\n" + "  \"error_text\": \"error\"\n" + "}"));
        VerifyResult r = client.verify("447700900999", "TestBrand", "15555215554");
        assertEquals(VerifyResult.STATUS_OK, r.getStatus());
    }

    @Test
    public void testVerifyWithNumberBrand() throws Exception {
        wrapper.setHttpClient(this.stubHttpClient(200,
                                                  "{\n" + "  \"request_id\": \"not-really-a-request-id\",\n" + "  \"status\": 0,\n" + "  \"error_text\": \"error\"\n" + "}"));
        VerifyResult r = client.verify("447700900999", "TestBrand");
        assertEquals(VerifyResult.STATUS_OK, r.getStatus());
    }

    @Test
    public void testVerifyWithRequestObject() throws Exception {
        wrapper.setHttpClient(this.stubHttpClient(200,
                "{\n" + "  \"request_id\": \"not-really-a-request-id\",\n" + "  \"status\": 0,\n" + "  \"error_text\": \"error\"\n" + "}"));
        VerifyResult r = client.verify(new VerifyRequest("447700900999", "TestBrand", "15555215554", 6, Locale.US));
        assertEquals(VerifyResult.STATUS_OK, r.getStatus());
    }

    @Test
    public void testVerifyHttpError() throws Exception {
        wrapper.setHttpClient(this.stubHttpClient(500,
                "{\n" + "  \"request_id\": \"not-really-a-request-id\",\n" + "  \"status\": 0,\n" + "  \"error_text\": \"error\"\n" + "}"));
        try {
            client.verify("447700900999", "TestBrand", "15555215554", 6, Locale.US);
            fail("An IOException should be thrown if an HTTP 500 response is received.");
        } catch (IOException ioe) {
            // This is expected
        }
    }

    @Test
    public void testVerifyWithNonNumericStatus() throws Exception {
        wrapper.setHttpClient(this.stubHttpClient(200,
                "{\n" + "  \"request_id\": \"not-really-a-request-id\",\n" + "  \"status\": \"test\",\n" + "  \"error_text\": \"error\"\n" + "}"));
        VerifyResult r = client.verify(new VerifyRequest("447700900999", "TestBrand", "15555215554", 6, Locale.US));
        assertEquals(VerifyResult.STATUS_INTERNAL_ERROR, r.getStatus());
    }
}
