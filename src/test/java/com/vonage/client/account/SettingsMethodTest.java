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
package com.vonage.client.account;

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import com.vonage.client.TestUtils;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SettingsMethodTest {
    private SettingsMethod method;

    @Before
    public void setUp() throws Exception {
        this.method = new SettingsMethod(new HttpWrapper());
    }

    @Test
    public void testDefaultUri() throws Exception {
        SettingsRequest request = new SettingsRequest("https://example.com/inbound-sms", "https://example.com/delivery-receipt");

        RequestBuilder builder = method.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://rest.nexmo.com/account/settings",
                builder.build().getURI().toString()
        );
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        SettingsMethod method = new SettingsMethod(wrapper);
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
