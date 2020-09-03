package com.nexmo.client.verify;

import com.nexmo.client.ClientTest;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class VerifyClientPsd2EndpointTest extends ClientTest<VerifyClient> {

    @Before
    public void setUp() {
        client = new VerifyClient(wrapper);
    }

    @Test
    public void testPsd2Verify() throws Exception {
        wrapper.setHttpClient(this.stubHttpClient(200,
                "{" + "\"request_id\": \"abcdef0123456789abcdef0123456789\"," + " \"status\": 0" + "}"
        ));

        VerifyResponse response = client.psd2Verify("447700900999", 10.31, "Ebony");

        assertEquals(VerifyStatus.OK, response.getStatus());
        assertEquals("abcdef0123456789abcdef0123456789", response.getRequestId());
    }

    @Test
    public void testPsd2VerifyWithWorkflow() throws Exception {
        wrapper.setHttpClient(this.stubHttpClient(200,
                "{" + "\"request_id\": \"abcdef0123456789abcdef0123456789\"," + " \"status\": 0" + "}"
        ));

        VerifyResponse response = client.psd2Verify("447700900999", 10.31, "Ebony", Psd2Request.Workflow.SMS);

        assertEquals(VerifyStatus.OK, response.getStatus());
        assertEquals("abcdef0123456789abcdef0123456789", response.getRequestId());
    }

    @Test
    public void testPsd2VerifyWithOptionalParameters() throws Exception {
        wrapper.setHttpClient(this.stubHttpClient(200,
                "{" + "\"request_id\": \"abcdef0123456789abcdef0123456789\"," + " \"status\": 0" + "}"
        ));

        Psd2Request request = new Psd2Request.Builder("447700900999", 10.31, "Ebony")
                .workflow(Psd2Request.Workflow.SMS)
                .length(4)
                .locale(Locale.UK)
                .country("GB")
                .nextEventWait(30)
                .pinExpiry(70)
                .build();

        VerifyResponse response = client.psd2Verify(request);

        assertEquals(VerifyStatus.OK, response.getStatus());
        assertEquals("abcdef0123456789abcdef0123456789", response.getRequestId());
    }
}
