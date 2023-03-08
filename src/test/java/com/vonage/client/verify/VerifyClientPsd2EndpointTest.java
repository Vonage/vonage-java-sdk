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
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class VerifyClientPsd2EndpointTest extends ClientTest<VerifyClient> {

    @Before
    public void setUp() {
        client = new VerifyClient(wrapper);
    }

    @Test
    public void testPsd2Verify() throws Exception {
        wrapper.setHttpClient(stubHttpClient(200,
                "{" + "\"request_id\": \"abcdef0123456789abcdef0123456789\"," + " \"status\": 0" + "}"
        ));

        VerifyResponse response = client.psd2Verify("447700900999", 10.31, "Ebony");

        assertEquals(VerifyStatus.OK, response.getStatus());
        assertEquals("abcdef0123456789abcdef0123456789", response.getRequestId());
    }

    @Test
    public void testPsd2VerifyWithWorkflow() throws Exception {
        wrapper.setHttpClient(stubHttpClient(200,
                "{" + "\"request_id\": \"abcdef0123456789abcdef0123456789\"," + " \"status\": 0" + "}"
        ));

        VerifyResponse response = client.psd2Verify("447700900999", 10.31, "Ebony", Psd2Request.Workflow.SMS);

        assertEquals(VerifyStatus.OK, response.getStatus());
        assertEquals("abcdef0123456789abcdef0123456789", response.getRequestId());
    }

    @Test
    public void testPsd2VerifyWithRequestObject() throws Exception {
        wrapper.setHttpClient(stubHttpClient(200,
                "{" + "\"request_id\": \"abcdef0123456789abcdef0123456789\"," + " \"status\": 0" + "}"
        ));

        Psd2Request request = new Psd2Request.Builder("447700900999", 10.31, "Ebony").build();

        VerifyResponse response = client.psd2Verify(request);

        assertEquals(VerifyStatus.OK, response.getStatus());
        assertEquals("abcdef0123456789abcdef0123456789", response.getRequestId());
    }
}
