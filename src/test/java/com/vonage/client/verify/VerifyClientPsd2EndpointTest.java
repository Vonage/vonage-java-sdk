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

import com.vonage.client.ClientTest;
import com.vonage.client.RestEndpoint;
import org.junit.Test;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class VerifyClientPsd2EndpointTest extends ClientTest<VerifyClient> {

    public VerifyClientPsd2EndpointTest() {
        client = new VerifyClient(wrapper);
    }

    @Test
    public void testPsd2Verify() throws Exception {
        stubResponse(200,
                "{" + "\"request_id\": \"abcdef0123456789abcdef0123456789\"," + " \"status\": 0" + "}"
        );

        VerifyResponse response = client.psd2Verify("447700900999", 10.31, "Ebony");

        assertEquals(VerifyStatus.OK, response.getStatus());
        assertEquals("abcdef0123456789abcdef0123456789", response.getRequestId());
    }

    @Test
    public void testPsd2VerifyWithWorkflow() throws Exception {
        stubResponse(200,
                "{" + "\"request_id\": \"abcdef0123456789abcdef0123456789\"," + " \"status\": 0" + "}"
        );

        VerifyResponse response = client.psd2Verify("447700900999", 10.31, "Ebony", Psd2Request.Workflow.SMS);

        assertEquals(VerifyStatus.OK, response.getStatus());
        assertEquals("abcdef0123456789abcdef0123456789", response.getRequestId());
    }

    @Test
    public void testPsd2VerifyWithRequestObject() throws Exception {
        stubResponse(200,
                "{" + "\"request_id\": \"abcdef0123456789abcdef0123456789\"," + " \"status\": 0" + "}"
        );
        Psd2Request request = new Psd2Request.Builder("447700900999", 10.31, "Ebony").build();
        VerifyResponse response = client.psd2Verify(request);
        assertEquals(VerifyStatus.OK, response.getStatus());
        assertEquals("abcdef0123456789abcdef0123456789", response.getRequestId());
    }

    @Test
    public void testEndpoint() throws Exception {
        new VerifyEndpointTestSpec<Psd2Request, VerifyResponse>() {

            @Override
            protected RestEndpoint<Psd2Request, VerifyResponse> endpoint() {
                return client.psd2;
            }

            @Override
            protected String expectedEndpointUri(Psd2Request request) {
                return "/verify/psd2/json";
            }

            @Override
            protected Psd2Request sampleRequest() {
                return Psd2Request.builder("4477990090090", 43.21, "Ebony")
                        .workflow(Psd2Request.Workflow.SMS).length(4).locale(Locale.UK)
                        .country("GB").pinExpiry(60).nextEventWait(90).build();
            }

            @Override
            protected Map<String, String> sampleQueryParams() {
                Psd2Request request = sampleRequest();
                Map<String, String> params = new HashMap<>();

                params.put("number", request.getNumber());
                params.put("amount", request.getAmount().toString());
                params.put("payee", request.getPayee());
                params.put("code_length", request.getLength().toString());
                params.put("lg", request.getDashedLocale());
                params.put("country", request.getCountry());
                params.put("pin_expiry", request.getPinExpiry().toString());
                params.put("next_event_wait", request.getNextEventWait().toString());
                params.put("workflow_id", String.valueOf(request.getWorkflow().getId()));

                assertEquals("4477990090090", params.get("number"));
                assertEquals("43.21", params.get("amount"));
                assertEquals("Ebony", params.get("payee"));
                assertEquals("4", params.get("code_length"));
                assertEquals("en-gb", params.get("lg"));
                assertEquals("GB", params.get("country"));
                assertEquals("60", params.get("pin_expiry"));
                assertEquals("90", params.get("next_event_wait"));
                assertEquals("6", params.get("workflow_id"));

                return params;
            }
        }
        .runTests();
    }
}
