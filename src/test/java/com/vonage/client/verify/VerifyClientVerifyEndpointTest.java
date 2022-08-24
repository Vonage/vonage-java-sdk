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

import com.vonage.client.ClientTest;
import com.vonage.client.VonageResponseParseException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Locale;

public class VerifyClientVerifyEndpointTest extends ClientTest<VerifyClient> {

    @Before
    public void setUp() {
        client = new VerifyClient(wrapper);
    }

    @Test
    public void testVerifyWithNumberBrandFromLengthLocaleLineType() throws Exception {
        wrapper.setHttpClient(stubHttpClient(200,
                "{\n" + "  \"request_id\": \"not-really-a-request-id\",\n" + "  \"status\": 0,\n"
                        + "  \"error_text\": \"error\"\n" + "}"
        ));

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
        wrapper.setHttpClient(stubHttpClient(200,
                "{\n" + "  \"request_id\": \"not-really-a-request-id\",\n" + "  \"status\": 0,\n"
                        + "  \"error_text\": \"error\"\n" + "}"
        ));

        VerifyResponse response = client.verify("447700900999", "TestBrand", "15555215554", 6, Locale.US);

        assertEquals(VerifyStatus.OK, response.getStatus());
        assertEquals("error", response.getErrorText());
        assertEquals("not-really-a-request-id", response.getRequestId());
    }

    @Test
    public void testVerifyWithNumberBrandFrom() throws Exception {
        wrapper.setHttpClient(stubHttpClient(200,
                "{\n" + "  \"request_id\": \"not-really-a-request-id\",\n" + "  \"status\": 0,\n"
                        + "  \"error_text\": \"error\"\n" + "}"
        ));

        VerifyResponse response = client.verify("447700900999", "TestBrand", "15555215554");

        assertEquals(VerifyStatus.OK, response.getStatus());
        assertEquals("error", response.getErrorText());
        assertEquals("not-really-a-request-id", response.getRequestId());
    }

    @Test
    public void testVerifyWithNumberBrand() throws Exception {
        wrapper.setHttpClient(stubHttpClient(200,
                "{\n" + "  \"request_id\": \"not-really-a-request-id\",\n" + "  \"status\": 0,\n"
                        + "  \"error_text\": \"error\"\n" + "}"
        ));

        VerifyResponse response = client.verify("447700900999", "TestBrand");

        assertEquals(VerifyStatus.OK, response.getStatus());
        assertEquals("error", response.getErrorText());
        assertEquals("not-really-a-request-id", response.getRequestId());
    }

    @Test
    public void testVerifyWithRequestObject() throws Exception {
        wrapper.setHttpClient(stubHttpClient(200,
                "{\n" + "  \"request_id\": \"not-really-a-request-id\",\n" + "  \"status\": 0,\n"
                        + "  \"error_text\": \"error\"\n" + "}"
        ));

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
        wrapper.setHttpClient(stubHttpClient(500,
                "{\n" + "  \"request_id\": \"not-really-a-request-id\",\n" + "  \"status\": 0,\n"
                        + "  \"error_text\": \"error\"\n" + "}"
        ));
        assertThrows(VonageResponseParseException.class, () ->
                client.verify("447700900999", "TestBrand", "15555215554", 6, Locale.US)
        );
    }

    @Test
    public void testVerifyWithNonNumericStatus() throws Exception {
        wrapper.setHttpClient(stubHttpClient(200,
                "{\n" + "  \"request_id\": \"not-really-a-request-id\",\n" + "  \"status\": \"test\",\n"
                        + "  \"error_text\": \"error\"\n" + "}"
        ));

        VerifyResponse response = client.verify(VerifyRequest.builder("447700900999","TestBrand")
                .senderId("15555215554")
                .length(6)
                .locale(Locale.US)
                .build());

        assertEquals(VerifyStatus.INTERNAL_ERROR, response.getStatus());
        assertEquals("error", response.getErrorText());
        assertEquals("not-really-a-request-id", response.getRequestId());
    }
}
