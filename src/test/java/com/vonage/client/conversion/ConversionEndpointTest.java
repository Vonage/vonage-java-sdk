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
package com.vonage.client.conversion;

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConversionEndpointTest {
    private ConversionEndpoint method;

    @Before
    public void setUp() throws Exception {
        method = new ConversionEndpoint(new HttpWrapper());
    }

    @Test
    public void testConstructParametersWithSms() throws Exception {
        ConversionRequest request = new ConversionRequest(ConversionRequest.Type.SMS,
                "MESSAGE-ID",
                true,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-03-04 10:11:12")
        );
        RequestBuilder requestBuilder = method.makeRequest(request);
        List<NameValuePair> params = requestBuilder.getParameters();

        assertContainsParam(params, "message-id", "MESSAGE-ID");
        assertContainsParam(params, "delivered", "true");
        assertContainsParam(params, "timestamp", "2014-03-04 10:11:12");
        assertEquals(method.getBaseUri() + ConversionRequest.Type.SMS.name().toLowerCase(),
                requestBuilder.getUri().toString()
        );
    }

    @Test
    public void testConstructParametersWithVoice() throws Exception {
        ConversionRequest request = new ConversionRequest(ConversionRequest.Type.VOICE,
                "MESSAGE-ID",
                true,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-03-04 10:11:12")
        );
        RequestBuilder requestBuilder = method.makeRequest(request);
        List<NameValuePair> params = requestBuilder.getParameters();

        assertContainsParam(params, "message-id", "MESSAGE-ID");
        assertContainsParam(params, "delivered", "true");
        assertContainsParam(params, "timestamp", "2014-03-04 10:11:12");
        assertEquals(method.getBaseUri() + ConversionRequest.Type.VOICE.name().toLowerCase(),
                requestBuilder.getUri().toString()
        );
    }

    private void assertContainsParam(List<NameValuePair> params, String key, String value) {
        NameValuePair item = new BasicNameValuePair(key, value);
        assertTrue("" + params + " should contain " + item, params.contains(item));
    }

    @Test
    public void testDefaultUri() throws Exception {
        ConversionRequest request = new ConversionRequest(ConversionRequest.Type.VOICE,
                "MESSAGE-ID",
                true,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-03-04 10:11:12")
        );

        RequestBuilder builder = method.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://api.nexmo.com/conversions/voice",
                builder.build().getURI().toString()
        );
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        ConversionEndpoint method = new ConversionEndpoint(wrapper);
        ConversionRequest request = new ConversionRequest(ConversionRequest.Type.VOICE,
                "MESSAGE-ID",
                true,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-03-04 10:11:12")
        );

        RequestBuilder builder = method.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://example.com/conversions/voice", builder.build().getURI().toString());
    }
}
