/*
 *   Copyright 2025 Vonage
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

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class RedactRequestTest {

    @Test
    public void testRequiredParams() {
        String json = "{\"id\":\"testId\",\"product\":\"voice\"}";
        assertEquals(json, new RedactRequest("testId", Product.VOICE).toJson());
    }

    @Test
    public void testTypeParamOutbound() {
        String json = "{\"id\":\"testId\",\"product\":\"sms\",\"type\":\"outbound\"}";
        RedactRequest request = new RedactRequest("testId", Product.SMS);
        request.setType(Type.OUTBOUND);

        assertEquals(json, request.toJson());
    }

    @Test
    public void testTypeParamInbound() {
        String json = "{\"id\":\"testId\",\"product\":\"sms\",\"type\":\"inbound\"}";
        RedactRequest request = new RedactRequest("testId", Product.SMS);
        request.setType(Type.INBOUND);

        assertEquals(json, request.toJson());
    }

    @Test
    public void testProductVoice() {
        String json = "{\"id\":\"testId\",\"product\":\"voice\"}";
        assertEquals(json, new RedactRequest("testId", Product.VOICE).toJson());
    }

    @Test
    public void testProductNumberInsight() {
        String json = "{\"id\":\"testId\",\"product\":\"number-insight\"}";
        assertEquals(json, new RedactRequest("testId", Product.NUMBER_INSIGHTS).toJson());
    }

    @Test
    public void testProductVerify() {
        String json = "{\"id\":\"testId\",\"product\":\"verify\"}";
        assertEquals(json, new RedactRequest("testId", Product.VERIFY).toJson());
    }

    @Test
    public void testProductVerifySdk() {
        String json = "{\"id\":\"testId\",\"product\":\"verify-sdk\"}";
        assertEquals(json, new RedactRequest("testId", Product.VERIFY_SDK).toJson());
    }
    

    @Test
    public void testProductMessages() {
        String json = "{\"id\":\"testId\",\"product\":\"messages\"}";
        assertEquals(json, new RedactRequest("testId", Product.MESSAGES).toJson());
    }

    @Test
    public void testProductWorkflow() {
        String json = "{\"id\":\"testId\",\"product\":\"workflow\"}";
        assertEquals(json, new RedactRequest("testId", Product.WORKFLOW).toJson());
    }
}
