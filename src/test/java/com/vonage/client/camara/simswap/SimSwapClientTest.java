/*
 *   Copyright 2024 Vonage
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
package com.vonage.client.camara.simswap;

import com.vonage.client.AbstractClientTest;
import com.vonage.client.RestEndpoint;
import com.vonage.client.TestUtils;
import com.vonage.client.auth.camara.FraudPreventionDetectionScope;
import static com.vonage.client.auth.camara.FraudPreventionDetectionScope.CHECK_SIM_SWAP;
import static com.vonage.client.auth.camara.FraudPreventionDetectionScope.RETRIEVE_SIM_SWAP_DATE;
import com.vonage.client.camara.CamaraResponseException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.Executable;
import java.time.Instant;

public class SimSwapClientTest extends AbstractClientTest<SimSwapClient> {
    final String phoneNumber = "491512 3456789", invalidNumber = TestUtils.API_SECRET;

    public SimSwapClientTest() {
        client = new SimSwapClient(wrapper);
    }

    void assert403ResponseException(Executable invocation) throws Exception {
        final int status = 403;
        String message = "",
                code = "PERMISSION_DENIED", responseJson = STR."""
            {
               "status": \{status},
               "code": "\{code}",
               "message": "Client does not have sufficient permissions to perform this action"
            }
        """;

        var parsed = assertApiResponseException(status, responseJson, CamaraResponseException.class, invocation);
        assertEquals(code, parsed.getCode());
    }

    @Test
    public void testCheckSimSwap() throws Exception {
        stubResponse("{\"swapped\":true}");
        assertTrue(client.checkSimSwap(phoneNumber, 2400));

        stubResponse("{\"swapped\":false}");
        assertFalse(client.checkSimSwap(phoneNumber, 1));

        stubResponse("{}");
        assertFalse(client.checkSimSwap(phoneNumber, 240));

        stubResponse("{\"swapped\":\"true\"}");
        assertTrue(client.checkSimSwap(phoneNumber));

        stubResponse("{\"swapped\":\"false\"}");
        assertFalse(client.checkSimSwap(phoneNumber));

        assertThrows(IllegalArgumentException.class, () -> client.checkSimSwap(phoneNumber, 0));
        assertThrows(IllegalArgumentException.class, () -> client.checkSimSwap(phoneNumber, 2401));
        stubResponseAndAssertThrows(200, () -> client.checkSimSwap(null), NullPointerException.class);
        stubResponseAndAssertThrows(200,
                () -> client.checkSimSwap(invalidNumber, 350),
                IllegalArgumentException.class
        );

        assert403ResponseException(() -> client.checkSimSwap(phoneNumber));
    }

    @Test
    public void testRetrieveSimSwapDate() throws Exception {
        var timestampStr = "2019-08-24T14:15:22Z";
        var timestamp = Instant.parse(timestampStr);
        var response = "{\"latestSimChange\": \""+timestampStr+"\"}";

        stubResponse(200, timestampStr);
        assertEquals(timestamp, client.retrieveSimSwapDate(phoneNumber));

        stubResponseAndAssertThrows(timestampStr,
                () -> client.retrieveSimSwapDate(null),
                NullPointerException.class
        );

        stubResponseAndAssertThrows(timestampStr,
                () -> client.retrieveSimSwapDate(invalidNumber),
                IllegalArgumentException.class
        );

        assert403ResponseException(() -> client.retrieveSimSwapDate(phoneNumber));
    }

    @Test
    public void testCheckEndpoint() throws Exception {
        new SimSwapEndpointTestSpec<CheckSimSwapResponse>() {
            final int maxAge = 560;

            @Override
            protected RestEndpoint<SimSwapRequest, CheckSimSwapResponse> endpoint() {
                return client.check;
            }

            @Override
            protected FraudPreventionDetectionScope getScope() {
                return CHECK_SIM_SWAP;
            }

            @Override
            protected SimSwapRequest sampleRequest() {
                return new SimSwapRequest(msisdn, 560);
            }

            @Override
            protected String sampleRequestBodyString() {
                return super.sampleRequestBodyString().replace("}", ",\"maxAge\":"+maxAge+"}");
            }
        }
        .runTests();
    }

    @Test
    public void testRetrieveDateEndpoint() throws Exception {
        new SimSwapEndpointTestSpec<SimSwapDateResponse>() {

            @Override
            protected RestEndpoint<SimSwapRequest, SimSwapDateResponse> endpoint() {
                return client.retrieveDate;
            }

            @Override
            protected FraudPreventionDetectionScope getScope() {
                return RETRIEVE_SIM_SWAP_DATE;
            }
        }
        .runTests();
    }
}
