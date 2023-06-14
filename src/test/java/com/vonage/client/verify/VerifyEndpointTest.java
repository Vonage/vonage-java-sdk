/*
 *   Copyright 2023 Vonage
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
import static org.junit.Assert.*;
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
    public void testPinLengthBoundaries() {
        VerifyRequest.Builder builder = VerifyRequest.builder(verifyRequest.getNumber(), verifyRequest.getBrand())
                .senderId(verifyRequest.getFrom()).locale(verifyRequest.getLocale());

        assertEquals(4, builder.length(4).build().getLength().intValue());
        assertEquals(6, builder.length(6).build().getLength().intValue());
        assertThrows(IllegalArgumentException.class, () -> builder.length(5).build());
        builder.length(null);
        assertThrows(IllegalArgumentException.class, () -> builder.pinCode("123").build());
        assertThrows(IllegalArgumentException.class, () -> builder.pinCode("A1234567890").build());
        assertEquals(4, builder.pinCode("abcd").build().getPinCode().length());
        assertEquals(10, builder.pinCode("0123456789").build().getPinCode().length());
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
        assertParamMissing(params, "pin_code");
        assertParamMissing(params, "next_event_wait");
        assertParamMissing(params, "workflow_id");
        assertNull(verifyRequest.getLength());
        assertNull(verifyRequest.getLocale());
        assertNull(verifyRequest.getFrom());
        assertNull(verifyRequest.getType());
        assertNull(verifyRequest.getCountry());
        assertNull(verifyRequest.getPinExpiry());
        assertNull(verifyRequest.getPinCode());
        assertNull(verifyRequest.getNextEventWait());
        assertNull(verifyRequest.getWorkflow());
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
                .pinCode("a1b2C3")
                .nextEventWait(90)
                .workflow(VerifyRequest.Workflow.TTS_TTS)
                .build();

        assertNotNull(verifyRequest.toString());
        assertNotNull(verifyRequest.getType());
        RequestBuilder request = method.makeRequest(verifyRequest);
        List<NameValuePair> params = request.getParameters();
        assertParamMissing(params, "require_type");
        assertParamMissing(params, "type");
        assertContainsParam(params, "number", "4477990090090");
        assertContainsParam(params, "brand", "Brand.com");
        assertContainsParam(params, "pin_code", "a1b2C3");
        assertContainsParam(params, "code_length", "6");
        assertContainsParam(params, "sender_id", "VERIFICATION");
        assertContainsParam(params, "lg", "en-gb");
        assertContainsParam(params, "country", "GB");
        assertContainsParam(params, "pin_expiry", "60");
        assertContainsParam(params, "next_event_wait", "90");
        assertContainsParam(params, "workflow_id", "3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidBrand() {
        VerifyRequest.builder("4477990090090", "A very looooooooooooooong brand name").build();
    }

    @Test
    public void testDefaultUri() throws Exception {
        RequestBuilder builder = method.makeRequest(verifyRequest);
        assertEquals("POST", builder.getMethod());
        assertEquals(ContentType.APPLICATION_FORM_URLENCODED.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
        assertEquals("https://api.nexmo.com/verify/json", builder.build().getURI().toString());
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
