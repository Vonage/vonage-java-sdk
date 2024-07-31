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
package com.vonage.client.numbers;

import static org.junit.jupiter.api.Assertions.*;

abstract class BuyCancelNumberRequestEndpointTestSpec extends BaseNumberRequestEndpointTestSpec<BuyCancelNumberRequest> {
    final String targetApiKey = "1a2345b7";

    @Override
    protected BuyCancelNumberRequest sampleRequest() {
        return new BuyCancelNumberRequest(country, msisdn, null);
    }

    @Override
    public void runTests() throws Exception {
        super.runTests();
        testGetters();
        testValidation();
        testWithTargetApiKey();
    }

    private void testGetters() {
        var sample = sampleRequest();
        assertNull(sample.getTargetApiKey());
        assertEquals(msisdn, sample.getMsisdn());
        assertEquals(country, sample.getCountry());
    }

    private void testValidation() {
        assertThrows(IllegalArgumentException.class, () -> new BuyCancelNumberRequest("GBR", msisdn, targetApiKey));
        assertThrows(IllegalArgumentException.class, () -> new BuyCancelNumberRequest(null, msisdn, targetApiKey));
        assertThrows(NullPointerException.class, () -> new BuyCancelNumberRequest(country, null, targetApiKey));
        assertThrows(IllegalArgumentException.class, () -> new BuyCancelNumberRequest(country, targetApiKey, null));
    }

    private void testWithTargetApiKey() throws Exception {
        var request = new BuyCancelNumberRequest(country, msisdn, targetApiKey);
        params.put("target_api_key", targetApiKey);
        assertRequestUriAndBody(request, params);
    }
}
