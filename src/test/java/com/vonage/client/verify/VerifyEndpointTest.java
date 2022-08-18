/*
 *   Copyright 2022 Vonage
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
package com.vonage.client.verify;

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.Locale;

public class VerifyEndpointTest extends MethodTest<VerifyEndpoint> {
    private VerifyRequest verifyRequest;

    @Before
    public void setUp() {
        method = new VerifyEndpoint(new HttpWrapper());
        verifyRequest = VerifyRequest.builder("4477990090090",
                "Brand.com")
                .senderId("Your friend")
                .length(4)
                .locale(new Locale("en", "gb"))
                .build();
    }

    @Test
    public void testConstructVerifyParams() throws Exception {
        RequestBuilder request = method.makeRequest(verifyRequest);
        List<NameValuePair> params = request.getParameters();

        assertContainsParam(params, "number", "4477990090090");
        assertContainsParam(params, "brand", "Brand.com");
        assertContainsParam(params, "sender_id", "Your friend");
        assertContainsParam(params, "code_length", "4");
        assertContainsParam(params, "lg", "en-gb");
    }

    @Test
    public void testConstructVerifyParamsMissingValues() throws Exception {
        VerifyRequest verifyRequest = VerifyRequest.builder("4477990090090","Brand.com").build();

        RequestBuilder request = method.makeRequest(verifyRequest);
        List<NameValuePair> params = request.getParameters();
        assertContainsParam(params, "number", "4477990090090");
        assertContainsParam(params, "brand", "Brand.com");
        assertParamMissing(params, "code_length");
        assertParamMissing(params, "lg");
        assertParamMissing(params, "sender_id");
        assertParamMissing(params, "require_type");
        assertParamMissing(params, "country");
        assertParamMissing(params, "pin_expiry");
        assertParamMissing(params, "next_event_wait");
        assertParamMissing(params, "workflow_id");
    }

    @Test
    public void testConstructVerifyParamsWithOptionalValues() throws Exception {
        VerifyRequest verifyRequest = VerifyRequest.builder("4477990090090",
                "Brand.com")
                .senderId("VERIFICATION")
                .length(6)
                .locale(new Locale("en", "gb"))
                .type(VerifyRequest.LineType.LANDLINE)
                .country("GB")
                .pinExpiry(60)
                .nextEventWait(90)
                .workflow(VerifyRequest.Workflow.TTS_TTS)
                .build();

        RequestBuilder request = method.makeRequest(verifyRequest);
        List<NameValuePair> params = request.getParameters();
        assertContainsParam(params, "number", "4477990090090");
        assertContainsParam(params, "brand", "Brand.com");

        assertContainsParam(params, "code_length", "6");
        assertContainsParam(params, "sender_id", "VERIFICATION");
        assertContainsParam(params, "lg", "en-gb");
        assertParamMissing(params, "require_type");
        assertContainsParam(params, "country", "GB");
        assertContainsParam(params, "pin_expiry", "60");
        assertContainsParam(params, "next_event_wait", "90");
        assertContainsParam(params, "workflow_id", "3");
    }

    @Test
    public void testDefaultUri() throws Exception {
        RequestBuilder builder = method.makeRequest(verifyRequest);
        assertEquals("POST", builder.getMethod());
        assertEquals(ContentType.APPLICATION_FORM_URLENCODED.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
        assertEquals("https://api.nexmo.com/verify/json",
                builder.build().getURI().toString()
        );
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        VerifyEndpoint method = new VerifyEndpoint(wrapper);

        RequestBuilder builder = method.makeRequest(verifyRequest);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://example.com/verify/json", builder.build().getURI().toString());
    }
}
