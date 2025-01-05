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
package com.vonage.client.sms;

import com.vonage.client.DynamicEndpointTestSpec;
import com.vonage.client.RestEndpoint;
import com.vonage.client.VonageApiResponseException;
import com.vonage.client.auth.ApiKeyHeaderAuthMethod;
import com.vonage.client.auth.AuthMethod;
import com.vonage.client.auth.SignatureAuthMethod;
import com.vonage.client.common.HttpMethod;
import com.vonage.client.sms.messages.Message;
import com.vonage.client.sms.messages.TextMessage;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

abstract class SmsEndpointTestSpec extends DynamicEndpointTestSpec<Message, SmsSubmissionResponse> {
    private final SmsClient smsClient;

    public SmsEndpointTestSpec(SmsClient smsClient) {
        this.smsClient = smsClient;
    }

    @Override
    protected RestEndpoint<Message, SmsSubmissionResponse> endpoint() {
        return smsClient.sendMessage;
    }

    @Override
    protected HttpMethod expectedHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected Collection<Class<? extends AuthMethod>> expectedAuthMethods() {
        return Arrays.asList(SignatureAuthMethod.class, ApiKeyHeaderAuthMethod.class);
    }

    @Override
    protected Class<? extends Exception> expectedResponseExceptionType() {
        return VonageApiResponseException.class;
    }

    @Override
    protected String expectedContentTypeHeader(Message request) {
        return "application/x-www-form-urlencoded";
    }

    @Override
    protected String expectedDefaultBaseUri() {
        return "https://rest.nexmo.com";
    }

    @Override
    protected String expectedEndpointUri(Message request) {
        return "/sms/json";
    }

    @Override
    protected Message sampleRequest() {
        return new TextMessage("TestSender", "447900000001", "Test msg");
    }

    @Override
    protected Map<String, String> sampleQueryParams() {
        return Map.of(
                "type", "text",
                "text", "Test msg",
                "from", "TestSender",
                "to", "447900000001"
        );
    }
}
