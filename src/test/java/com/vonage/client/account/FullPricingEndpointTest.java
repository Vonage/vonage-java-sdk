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
package com.vonage.client.account;

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FullPricingEndpointTest {

    @Test
    public void testDefaultUriVoice() {
        FullPricingRequest request = new FullPricingRequest(ServiceType.VOICE);

        RequestBuilder builder = new FullPricingEndpoint(new HttpWrapper()).makeRequest(request);
        assertEquals("GET", builder.getMethod());
        assertEquals(
                "https://rest.nexmo.com/account/get-full-pricing/outbound/voice",
                builder.build().getURI().toString()
        );
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
    }

    @Test
    public void testCustomUriSms() {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("http://example.com").build());
        FullPricingRequest request = new FullPricingRequest(ServiceType.SMS);

        RequestBuilder builder = new FullPricingEndpoint(wrapper).makeRequest(request);
        assertEquals("GET", builder.getMethod());
        assertEquals(
                "http://example.com/account/get-full-pricing/outbound/sms",
                builder.build().getURI().toString()
        );
    }

    @Test
    public void testCustomUriSmsTransit() {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://vonage.com").build());
        FullPricingRequest request = new FullPricingRequest(ServiceType.SMS_TRANSIT);

        RequestBuilder builder = new FullPricingEndpoint(wrapper).makeRequest(request);
        assertEquals("GET", builder.getMethod());
        assertEquals(
                "https://vonage.com/account/get-full-pricing/outbound/sms-transit",
                builder.build().getURI().toString()
        );
    }
}
