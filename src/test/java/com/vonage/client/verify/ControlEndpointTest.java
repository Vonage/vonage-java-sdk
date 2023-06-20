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
import com.vonage.client.TestUtils;
import com.vonage.client.auth.TokenAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import static org.junit.Assert.*;

public class ControlEndpointTest {
    private ControlEndpoint endpoint;

    @Before
    public void setUp() throws Exception {
        endpoint = new ControlEndpoint(new HttpWrapper());
    }

    @Test
    public void testGetAcceptableAuthMethods() throws Exception {
        Class<?>[] auths = endpoint.getAcceptableAuthMethods();
        assertArrayEquals(new Class<?>[]{TokenAuthMethod.class}, auths);
    }

    @Test
    public void testMakeRequest() throws Exception {
        RequestBuilder builder = endpoint.makeRequest(new ControlRequest("request-id",
                VerifyControlCommand.CANCEL
        ));
        assertEquals("POST", builder.getMethod());
        assertEquals("https://api.nexmo.com/verify/control/json", builder.build().getURI().toString());

        Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
        assertEquals("request-id", params.get("request_id"));
        assertEquals("cancel", params.get("cmd"));
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
    }

    @Test
    public void testParseResponse() throws Exception {
        HttpResponse stub = TestUtils.makeJsonHttpResponse(200,
                "{\n" + "  \"status\":\"0\",\n" + "  \"command\":\"cancel\"\n" + "}"
        );
        ControlResponse response = endpoint.parseResponse(stub);
        assertEquals("0", response.getStatus());
        assertEquals(VerifyControlCommand.CANCEL, response.getCommand());
    }

    @Test
    public void testParseErrorResponse() throws Exception {
        HttpResponse stub = TestUtils.makeJsonHttpResponse(200,
                "{\n" + "    \"error_text\": \"Missing username\",\n" + "    \"status\": \"2\"\n" + "}"
        );
        try {
            ControlResponse response = endpoint.parseResponse(stub);
            fail("Parsing an error response should throw an exception.");
        } catch (VerifyException exc) {
            assertEquals("2", exc.getStatus());
            assertEquals("Missing username", exc.getErrorText());
        }

    }

    @Test
    public void testDefaultUri() throws Exception {
        ControlRequest request = new ControlRequest("request-id", VerifyControlCommand.CANCEL);

        RequestBuilder builder = endpoint.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://api.nexmo.com/verify/control/json", builder.build().getURI().toString());
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        ControlEndpoint endpoint = new ControlEndpoint(wrapper);
        ControlRequest request = new ControlRequest("request-id", VerifyControlCommand.CANCEL);

        RequestBuilder builder = endpoint.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://example.com/verify/control/json", builder.build().getURI().toString());
    }
}
