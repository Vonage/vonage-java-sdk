/*
 *   Copyright 2025 Vonage
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
package com.vonage.client.identityinsights;

import com.vonage.client.AbstractClientTest;
import com.vonage.client.RestEndpoint;
import com.vonage.client.TestUtils;
import com.vonage.client.auth.JWTAuthMethod;
import com.vonage.client.camara.CamaraResponseException;
import com.vonage.client.common.HttpMethod;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class IdentityInsightsClientTest extends AbstractClientTest<IdentityInsightsClient> {
    final String phoneNumber = "+14155552671";
    
    public IdentityInsightsClientTest() {
        client = new IdentityInsightsClient(wrapper);
    }
    
    private String loadJsonResource(String filename, String requestId) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(filename)) {
            if (is == null) {
                throw new IOException("Could not find resource: " + filename);
            }
            byte[] buffer = new byte[1024];
            StringBuilder sb = new StringBuilder();
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
            }
            return sb.toString().replace("REQUEST_ID_PLACEHOLDER", requestId);
        }
    }
    
    @Test
    public void testGetInsightsFormat() throws Exception {
        String requestId = UUID.randomUUID().toString();
        String responseJson = loadJsonResource("format-response.json", requestId);
        
        stubResponse(responseJson);
        
        IdentityInsightsRequest request = new IdentityInsightsRequest(phoneNumber)
                .format();
        
        IdentityInsightsResponse response = client.getInsights(request);
        
        assertNotNull(response);
        assertEquals(requestId, response.getRequestId());
        assertNotNull(response.getInsights());
        assertNotNull(response.getInsights().getFormat());
        
        FormatInsightResponse format = response.getInsights().getFormat();
        assertEquals("US", format.getCountryCode());
        assertEquals("United States", format.getCountryName());
        assertEquals("1", format.getCountryPrefix());
        assertEquals("California", format.getOfflineLocation());
        assertEquals(1, format.getTimeZones().size());
        assertEquals("America/Los_Angeles", format.getTimeZones().get(0));
        assertEquals("+14155552671", format.getNumberInternational());
        assertEquals("(415) 555-2671", format.getNumberNational());
        assertTrue(format.getIsFormatValid());
        assertEquals(InsightStatusCode.OK, format.getStatus().getCode());
        assertEquals("Success", format.getStatus().getMessage());
    }
    
    @Test
    public void testGetInsightsSimSwap() throws Exception {
        String requestId = UUID.randomUUID().toString();
        String responseJson = loadJsonResource("sim-swap-response.json", requestId);
        
        stubResponse(responseJson);
        
        IdentityInsightsRequest request = new IdentityInsightsRequest(phoneNumber)
                .simSwap(240);
        
        IdentityInsightsResponse response = client.getInsights(request);
        
        assertNotNull(response);
        assertNotNull(response.getInsights().getSimSwap());
        
        SimSwapInsightResponse simSwap = response.getInsights().getSimSwap();
        assertNotNull(simSwap.getLatestSimSwapAt());
        assertTrue(simSwap.getIsSwapped());
        assertEquals(InsightStatusCode.OK, simSwap.getStatus().getCode());
    }
    
    @Test
    public void testGetInsightsCurrentCarrier() throws Exception {
        String requestId = UUID.randomUUID().toString();
        String responseJson = loadJsonResource("current-carrier-response.json", requestId);
        
        stubResponse(responseJson);
        
        IdentityInsightsRequest request = new IdentityInsightsRequest(phoneNumber)
                .currentCarrier();
        
        IdentityInsightsResponse response = client.getInsights(request);
        
        assertNotNull(response);
        assertNotNull(response.getInsights().getCurrentCarrier());
        
        CurrentCarrierInsightResponse carrier = response.getInsights().getCurrentCarrier();
        assertEquals("Orange Espana, S.A. Unipersonal", carrier.getName());
        assertEquals(NetworkType.MOBILE, carrier.getNetworkType());
        assertEquals("ES", carrier.getCountryCode());
        assertEquals("21403", carrier.getNetworkCode());
        assertEquals(InsightStatusCode.OK, carrier.getStatus().getCode());
    }
    
    @Test
    public void testGetInsightsMultiple() throws Exception {
        String requestId = UUID.randomUUID().toString();
        String responseJson = loadJsonResource("multiple-insights-response.json", requestId);
        
        stubResponse(responseJson);
        
        IdentityInsightsRequest request = new IdentityInsightsRequest(phoneNumber)
                .purpose("FraudPreventionAndDetection")
                .format()
                .simSwap()
                .originalCarrier();
        
        IdentityInsightsResponse response = client.getInsights(request);
        
        assertNotNull(response);
        assertEquals(requestId, response.getRequestId());
        assertNotNull(response.getInsights().getFormat());
        assertNotNull(response.getInsights().getSimSwap());
        assertNotNull(response.getInsights().getOriginalCarrier());
        
        assertTrue(response.getInsights().getFormat().getIsFormatValid());
        assertFalse(response.getInsights().getSimSwap().getIsSwapped());
        assertEquals("Verizon Wireless", response.getInsights().getOriginalCarrier().getName());
        assertEquals(NetworkType.MOBILE, response.getInsights().getOriginalCarrier().getNetworkType());
    }
    
    @Test
    public void testRequestValidation() {
        assertThrows(NullPointerException.class, () -> new IdentityInsightsRequest(null));
        
        IdentityInsightsRequest request = new IdentityInsightsRequest(phoneNumber);
        assertNotNull(request.getPhoneNumber());
        
        assertThrows(IllegalArgumentException.class, 
            () -> new SimSwapInsightRequest(0));
        assertThrows(IllegalArgumentException.class, 
            () -> new SimSwapInsightRequest(2401));
        
        assertDoesNotThrow(() -> new SimSwapInsightRequest(1));
        assertDoesNotThrow(() -> new SimSwapInsightRequest(2400));
    }
}
