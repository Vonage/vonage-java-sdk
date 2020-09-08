/*
 * Copyright (c) 2020 Vonage
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
package com.vonage.client.redact;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RedactRequestTest {

    @Test
    public void testRequiredParams() {
        String json = "{\"id\":\"testId\",\"product\":\"voice\"}";
        assertEquals(new RedactRequest("testId", RedactRequest.Product.VOICE).toJson(), json);
    }

    @Test
    public void testTypeParamOutbound() {
        String json = "{\"id\":\"testId\",\"product\":\"sms\",\"type\":\"outbound\"}";
        RedactRequest request = new RedactRequest("testId", RedactRequest.Product.SMS);
        request.setType(RedactRequest.Type.OUTBOUND);

        assertEquals(request.toJson(), json);
    }

    @Test
    public void testTypeParamInbound() {
        String json = "{\"id\":\"testId\",\"product\":\"sms\",\"type\":\"inbound\"}";
        RedactRequest request = new RedactRequest("testId", RedactRequest.Product.SMS);
        request.setType(RedactRequest.Type.INBOUND);

        assertEquals(request.toJson(), json);
    }

    @Test
    public void testProductVoice() {
        String json = "{\"id\":\"testId\",\"product\":\"voice\"}";
        assertEquals(new RedactRequest("testId", RedactRequest.Product.VOICE).toJson(), json);
    }

    @Test
    public void testProductNumberInsight() {
        String json = "{\"id\":\"testId\",\"product\":\"number-insight\"}";
        assertEquals(new RedactRequest("testId", RedactRequest.Product.NUMBER_INSIGHTS).toJson(), json);
    }

    @Test
    public void testProductVerify() {
        String json = "{\"id\":\"testId\",\"product\":\"verify\"}";
        assertEquals(new RedactRequest("testId", RedactRequest.Product.VERIFY).toJson(), json);
    }

    @Test
    public void testProductVerifySdk() {
        String json = "{\"id\":\"testId\",\"product\":\"verify-sdk\"}";
        assertEquals(new RedactRequest("testId", RedactRequest.Product.VERIFY_SDK).toJson(), json);
    }

    @Test
    public void testProductMessage() {
        String json = "{\"id\":\"testId\",\"product\":\"messages\"}";
        assertEquals(new RedactRequest("testId", RedactRequest.Product.MESSAGE).toJson(), json);
    }

    @Test
    public void testProductMessages() {
        String json = "{\"id\":\"testId\",\"product\":\"messages\"}";
        assertEquals(new RedactRequest("testId", RedactRequest.Product.MESSAGES).toJson(), json);
    }

    @Test
    public void testProductWorkflow() {
        String json = "{\"id\":\"testId\",\"product\":\"workflow\"}";
        assertEquals(new RedactRequest("testId", RedactRequest.Product.WORKFLOW).toJson(), json);
    }
}
