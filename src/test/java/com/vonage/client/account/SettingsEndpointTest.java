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
import com.vonage.client.TestUtils;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SettingsEndpointTest {
    private SettingsEndpoint method;

    @Before
    public void setUp() throws Exception {
        method = new SettingsEndpoint(new HttpWrapper());
    }

    @Test
    public void testDefaultUri() throws Exception {
        SettingsRequest request = new SettingsRequest("https://example.com/inbound-sms", "https://example.com/delivery-receipt");

        RequestBuilder builder = method.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://rest.nexmo.com/account/settings",
                builder.build().getURI().toString()
        );
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        SettingsEndpoint method = new SettingsEndpoint(wrapper);
        SettingsRequest request = new SettingsRequest("https://example.com/inbound-sms", "https://example.com/delivery-receipt");

        RequestBuilder builder = method.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://example.com/account/settings", builder.build().getURI().toString());
    }

    @Test
    public void testUpdatingIncomingSmsUrlOnly() throws Exception {
        SettingsRequest request = SettingsRequest.withIncomingSmsUrl("https://example.com/inbound-sms");

        RequestBuilder builder = method.makeRequest(request);

        Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
        assertEquals("POST", builder.getMethod());
        assertEquals("https://rest.nexmo.com/account/settings",
                builder.build().getURI().toString()
        );

        assertEquals("https://example.com/inbound-sms", params.get("moCallBackUrl"));
        assertNull(params.get("drCallBackUrl"));
    }

    @Test
    public void testUpdatingDeliveryReceiptUrlOnly() throws Exception {
        SettingsRequest request = SettingsRequest.withDeliveryReceiptUrl("https://example.com/delivery-receipt");

        RequestBuilder builder = method.makeRequest(request);

        Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
        assertEquals("POST", builder.getMethod());
        assertEquals("https://rest.nexmo.com/account/settings",
                builder.build().getURI().toString()
        );

        assertEquals("https://example.com/delivery-receipt", params.get("drCallBackUrl"));
        assertNull(params.get("moCallBackUrl"));
    }

    @Test
    public void testUpdatingDBothUrls() throws Exception {
        SettingsRequest request = new SettingsRequest("https://example.com/inbound-sms", "https://example.com/delivery-receipt");

        RequestBuilder builder = method.makeRequest(request);

        Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
        assertEquals("POST", builder.getMethod());
        assertEquals("https://rest.nexmo.com/account/settings",
                builder.build().getURI().toString()
        );

        assertEquals("https://example.com/delivery-receipt", params.get("drCallBackUrl"));
        assertEquals("https://example.com/inbound-sms", params.get("moCallBackUrl"));
    }
}
