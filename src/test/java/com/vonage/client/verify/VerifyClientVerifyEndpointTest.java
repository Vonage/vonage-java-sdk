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
import com.vonage.client.VonageApiResponseException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class VerifyClientVerifyEndpointTest extends ClientTest<VerifyClient> {

    public VerifyClientVerifyEndpointTest() {
        client = new VerifyClient(wrapper);
    }

    @Test
    public void testVerifyWithNumberBrandFromLengthLocaleLineType() throws Exception {
        stubResponse(200,
                "{\n" + "  \"request_id\": \"not-really-a-request-id\",\n" + "  \"status\": 0,\n"
                        + "  \"error_text\": \"error\"\n" + "}"
        );

        VerifyResponse response = client.verify("447700900999",
                "TestBrand",
                "15555215554",
                6,
                Locale.US,
                VerifyRequest.LineType.MOBILE
        );

        assertEquals(VerifyStatus.OK, response.getStatus());
        assertEquals("error", response.getErrorText());
        assertEquals("not-really-a-request-id", response.getRequestId());
    }

    @Test
    public void testVerifyWithNumberBrandFromLengthLocale() throws Exception {
        stubResponse(200,
                "{\n" + "  \"request_id\": \"not-really-a-request-id\",\n" + "  \"status\": 0,\n"
                        + "  \"error_text\": \"error\"\n" + "}"
        );

        VerifyResponse response = client.verify("447700900999", "TestBrand", "15555215554", 6, Locale.US);

        assertEquals(VerifyStatus.OK, response.getStatus());
        assertEquals("error", response.getErrorText());
        assertEquals("not-really-a-request-id", response.getRequestId());
    }

    @Test
    public void testVerifyWithNumberBrandFrom() throws Exception {
        stubResponse(200,
                "{\n" + "  \"request_id\": \"not-really-a-request-id\",\n" + "  \"status\": 0,\n"
                        + "  \"error_text\": \"error\"\n" + "}"
        );

        VerifyResponse response = client.verify("447700900999", "TestBrand", "15555215554");

        assertEquals(VerifyStatus.OK, response.getStatus());
        assertEquals("error", response.getErrorText());
        assertEquals("not-really-a-request-id", response.getRequestId());
    }

    @Test
    public void testVerifyWithNumberBrand() throws Exception {
        stubResponse(200,
                "{\n" + "  \"request_id\": \"not-really-a-request-id\",\n" + "  \"status\": 0,\n"
                        + "  \"error_text\": \"error\"\n" + "}"
        );

        VerifyResponse response = client.verify("447700900999", "TestBrand");

        assertEquals(VerifyStatus.OK, response.getStatus());
        assertEquals("error", response.getErrorText());
        assertEquals("not-really-a-request-id", response.getRequestId());
    }

    @Test
    public void testVerifyWithRequestObject() throws Exception {
        stubResponse(200,
                "{\n" + "  \"request_id\": \"not-really-a-request-id\",\n" + "  \"status\": 0,\n"
                        + "  \"error_text\": \"error\"\n" + "}"
        );

        VerifyResponse response = client.verify(VerifyRequest.builder("447700900999","TestBrand")
                .senderId("15555215554")
                .length(6)
                .locale(Locale.US)
        .build());

        assertEquals(VerifyStatus.OK, response.getStatus());
        assertEquals("error", response.getErrorText());
        assertEquals("not-really-a-request-id", response.getRequestId());
    }

    @Test
    public void testVerifyHttpError() throws Exception {
        stubResponse(500,
                "{\n" + "  \"request_id\": \"not-really-a-request-id\",\n" + "  \"status\": 0,\n"
                        + "  \"error_text\": \"error\"\n" + "}"
        );
        assertThrows(VonageApiResponseException.class, () ->
                client.verify("447700900999", "TestBrand", "15555215554", 6, Locale.US)
        );
    }

    @Test
    public void testVerifyWithNonNumericStatus() throws Exception {
        stubResponse(200,
                "{\n" + "  \"request_id\": \"not-really-a-request-id\",\n" + "  \"status\": \"test\",\n"
                        + "  \"error_text\": \"error\"\n" + "}"
        );

        VerifyResponse response = client.verify(VerifyRequest.builder("447700900999","TestBrand")
                .senderId("15555215554")
                .length(6)
                .locale(Locale.US)
                .build());

        assertEquals(VerifyStatus.INTERNAL_ERROR, response.getStatus());
        assertEquals("error", response.getErrorText());
        assertEquals("not-really-a-request-id", response.getRequestId());
    }

    @Test
    public void testVerifyWithWorkflow() throws Exception {
        stubResponse(200,
                "{\n" + "  \"request_id\": \"not-really-a-request-id\",\n" + "  \"status\": \"test\",\n"
                        + "  \"error_text\": \"error\"\n" + "}"
        );

        VerifyResponse response = client.verify("447900000000", "testBrand", VerifyRequest.Workflow.SMS);
        assertEquals(VerifyStatus.INTERNAL_ERROR, response.getStatus());
        assertEquals("error", response.getErrorText());
        assertEquals("not-really-a-request-id", response.getRequestId());
    }

    @Test
    public void testAdvanceVerification() throws Exception {
        String json = "{\n" + "  \"status\":\"0\",\n" + "  \"command\":\"trigger_next_event\"\n" + "}";
        stubResponse(200, json);

        ControlResponse response = client.advanceVerification("a-request-id");
        assertEquals("0", response.getStatus());
        assertEquals(VerifyControlCommand.TRIGGER_NEXT_EVENT, response.getCommand());
    }

    @Test
    public void testCancelVerification() throws Exception {
        String json = "{\n" + "  \"status\":\"0\",\n" + "  \"command\":\"cancel\"\n" + "}";
        stubResponse(200, json);

        ControlResponse response = client.cancelVerification("a-request-id");
        assertEquals("0", response.getStatus());
        assertEquals(VerifyControlCommand.CANCEL, response.getCommand());
    }

    @Test
    public void testEndpoint() throws Exception {
        new VerifyEndpointTestSpec<VerifyRequest, VerifyResponse>() {

            @Override
            protected RestEndpoint<VerifyRequest, VerifyResponse> endpoint() {
                return client.verify;
            }

            @Override
            protected String expectedEndpointUri(VerifyRequest request) {
                return "/verify/json";
            }

            @Override
            protected VerifyRequest sampleRequest() {
                return VerifyRequest.builder("4477990090090", "Brand.com")
                        .senderId("VERIFICATION").length(6)
                        .locale(new Locale("en", "gb"))
                        .type(VerifyRequest.LineType.LANDLINE).country("GB")
                        .pinExpiry(60).pinCode("a1b2C3").nextEventWait(90)
                        .workflow(VerifyRequest.Workflow.TTS_TTS).build();
            }

            @Override
            protected Map<String, ?> sampleQueryParams() {
                Map<String, String> params = new LinkedHashMap<>();
                params.put("number", "4477990090090");
                params.put("brand", "Brand.com");
                params.put("pin_code", "a1b2C3");
                params.put("code_length", "6");
                params.put("sender_id", "VERIFICATION");
                params.put("lg", "en-gb");
                params.put("country", "GB");
                params.put("pin_expiry", "60");
                params.put("next_event_wait", "90");
                params.put("workflow_id", "3");

                VerifyRequest request = sampleRequest();
                assertNotNull(request.toString());
                assertNotNull(request.getType());
                assertEquals(request.getNumber(), params.get("number"));
                assertEquals(request.getBrand(), params.get("brand"));
                assertEquals(request.getPinCode(), params.get("pin_code"));
                assertEquals(request.getLength().toString(), params.get("code_length"));
                assertEquals(request.getFrom(), params.get("sender_id"));
                assertEquals(request.getDashedLocale(), params.get("lg"));
                assertEquals(request.getCountry(), params.get("country"));
                assertEquals(request.getPinExpiry().toString(), params.get("pin_expiry"));
                assertEquals(request.getNextEventWait().toString(), params.get("next_event_wait"));
                assertEquals(String.valueOf(request.getWorkflow().getId()), params.get("workflow_id"));
                
                return params;
            }
        }
        .runTests();
    }
}
