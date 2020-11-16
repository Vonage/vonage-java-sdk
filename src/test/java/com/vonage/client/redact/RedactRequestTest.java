/*
 *   Copyright 2020 Vonage
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
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
