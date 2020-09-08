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

public class ConversionMethodTest {
    private ConversionMethod method;

    @Before
    public void setUp() throws Exception {
        this.method = new ConversionMethod(new HttpWrapper());
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
        ConversionMethod method = new ConversionMethod(wrapper);
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
