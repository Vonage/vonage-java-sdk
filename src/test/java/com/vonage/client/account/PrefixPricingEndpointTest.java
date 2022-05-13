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

public class PrefixPricingEndpointTest {

    @Test
    public void testDefaultUriVoice() {
        PrefixPricingRequest request = new PrefixPricingRequest(ServiceType.VOICE, "44");

        RequestBuilder builder = new PrefixPricingEndpoint(new HttpWrapper()).makeRequest(request);
        assertEquals("GET", builder.getMethod());
        assertEquals(
                "https://rest.nexmo.com/account/get-prefix-pricing/outbound/voice?prefix=44",
                builder.build().getURI().toString()
        );
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
    }

    @Test
    public void testCustomUriSms() {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        PrefixPricingRequest request = new PrefixPricingRequest(ServiceType.SMS, "1");

        RequestBuilder builder = new PrefixPricingEndpoint(wrapper).makeRequest(request);
        assertEquals("GET", builder.getMethod());
        assertEquals(
                "https://example.com/account/get-prefix-pricing/outbound/sms?prefix=1",
                builder.build().getURI().toString()
        );
    }

    @Test
    public void testDefaultUriSmsTransit() {
        PrefixPricingRequest request = new PrefixPricingRequest(ServiceType.SMS_TRANSIT, "16");

        RequestBuilder builder = new PrefixPricingEndpoint(new HttpWrapper()).makeRequest(request);
        assertEquals("GET", builder.getMethod());
        assertEquals(
                "https://rest.nexmo.com/account/get-prefix-pricing/outbound/sms-transit?prefix=16",
                builder.build().getURI().toString()
        );
    }
}
