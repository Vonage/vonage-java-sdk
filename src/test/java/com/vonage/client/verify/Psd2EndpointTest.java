/*
 * Copyright 2011-2017 Nexmo Inc
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
package com.vonage.client.verify;

import com.vonage.client.HttpWrapper;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import static org.junit.Assert.assertEquals;

public class Psd2EndpointTest extends MethodTest<Psd2Endpoint> {


    @Before
    public void setUp() throws Exception {
        method = new Psd2Endpoint(new HttpWrapper());
    }

    @Test
    public void makeRequest() throws IOException {
       RequestBuilder builder = method.makeRequest(new Psd2Request.Builder("15555555555",10.31, "Ebony").build());

        List<NameValuePair> parameters = builder.getParameters();

        assertContainsParam(parameters,"number","15555555555");
        assertContainsParam(parameters,"amount","10.31");
        assertContainsParam(parameters,"payee","Ebony");
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
    }

    @Test
    public void makeRequestWithWorkflow() throws IOException {
        RequestBuilder builder = method.makeRequest(
                new Psd2Request.Builder("15555555555", 10.31, "Ebony")
                        .workflow(Psd2Request.Workflow.SMS)
                        .build());

        List<NameValuePair> parameters = builder.getParameters();

        assertContainsParam(parameters,"number","15555555555");
        assertContainsParam(parameters,"amount","10.31");
        assertContainsParam(parameters,"payee","Ebony");
        assertContainsParam(parameters,"workflow_id","6");
    }

    @Test
    public void testConstructVerifyParamsWithOptionalValues() throws Exception {
        Psd2Request psd2Request = new Psd2Request.Builder("4477990090090",10.31, "Ebony")
                .workflow(Psd2Request.Workflow.SMS)
                .length(4)
                .locale(Locale.UK)
                .country("GB")
                .pinExpiry(60)
                .nextEventWait(90)
                .build();

        RequestBuilder request = method.makeRequest(psd2Request);
        List<NameValuePair> params = request.getParameters();
        assertContainsParam(params, "number", "4477990090090");
        assertContainsParam(params, "payee", "Ebony");

        assertContainsParam(params, "code_length", "4");
        assertContainsParam(params, "lg", "en-gb");
        assertContainsParam(params, "country", "GB");
        assertContainsParam(params, "pin_expiry", "60");
        assertContainsParam(params, "next_event_wait", "90");
        assertContainsParam(params, "workflow_id", "6");
    }

    @Test
    public void parseResponse() throws IOException {
        String expectedResponse = "{"
                + "\"request_id\":\"abcdef0123456789abcdef0123456789\","
                + "\"status\": 0"
                + "}";

        HttpResponse stubResponse = new BasicHttpResponse(
                new BasicStatusLine(new ProtocolVersion("1.1", 1, 1), 200, "OK")
        );
        stubResponse.setEntity(new StringEntity(expectedResponse, ContentType.APPLICATION_JSON));

        VerifyResponse verifyResponse = method.parseResponse(stubResponse);

        assertEquals("abcdef0123456789abcdef0123456789", verifyResponse.getRequestId());
        assertEquals(VerifyStatus.OK, verifyResponse.getStatus());
    }
}