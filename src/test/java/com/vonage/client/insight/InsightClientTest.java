/*
 *   Copyright 2020 Vonage
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
package com.vonage.client.insight;

import com.vonage.client.ClientTest;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class InsightClientTest extends ClientTest<InsightClient> {

    private static final String BASIC_RESPOSE_JSON =
            "{\n" + "  \"status\": 0,\n" + "  \"status_message\": \"Success\",\n"
                    + "  \"request_id\": \"aaaaaaaa-bbbb-cccc-dddd-0123456789ab\",\n"
                    + "  \"international_format_number\": \"447700900000\",\n"
                    + "  \"national_format_number\": \"07700 900000\",\n" + "  \"country_code\": \"GB\",\n"
                    + "  \"country_code_iso3\": \"GBR\",\n" + "  \"country_name\": \"United Kingdom\",\n"
                    + "  \"country_prefix\": \"44\"\n" + "}";

    private static final String STANDARD_RESPONSE_JSON =
            "{\n" + "  \"status\": 0,\n" + "  \"status_message\": \"Success\",\n"
                    + "  \"request_id\": \"aaaaaaaa-bbbb-cccc-dddd-0123456789ab\",\n"
                    + "  \"international_format_number\": \"447700900000\",\n"
                    + "  \"national_format_number\": \"07700 900000\",\n" + "  \"country_code\": \"GB\",\n"
                    + "  \"country_code_iso3\": \"GBR\",\n" + "  \"country_name\": \"United Kingdom\",\n"
                    + "  \"country_prefix\": \"44\",\n" + "  \"request_price\": \"0.04000000\",\n"
                    + "  \"refund_price\": \"0.01500000\",\n" + "  \"remaining_balance\": \"1.23456789\",\n"
                    + "  \"current_carrier\": {\n" + "    \"network_code\": \"12345\",\n"
                    + "    \"name\": \"Acme Inc\",\n" + "    \"country\": \"GB\",\n"
                    + "    \"network_type\": \"mobile\"\n" + "  },\n" + "  \"original_carrier\": {\n"
                    + "    \"network_code\": \"98765\",\n" + "    \"name\": \"Foo Bar\",\n"
                    + "    \"country\": \"US\",\n" + "    \"network_type\": \"mobile\"\n" + "  },\n"
                    + "  \"ported\": \"not_ported\",\n" + "  \"roaming\": {\n" + "    \"status\": \"roaming\",\n"
                    + "    \"roaming_country_code\": \"US\",\n" + "    \"roaming_network_code\": 12345,\n"
                    + "    \"roaming_network_name\": \"Acme Inc\"\n" + "  },\n" + "  \"caller_identity\": {\n"
                    + "    \"caller_type\": \"consumer\",\n" + "    \"caller_name\": \"John Smith\",\n"
                    + "    \"first_name\": \"John\",\n" + "    \"last_name\": \"Smith\",\n"
                    + "    \"subscription_type\": \"unknown\"\n" + "  },\n" + "  \"caller_name\": \"John Smith\",\n"
                    + "  \"last_name\": \"Smith\",\n" + "  \"first_name\": \"John\",\n"
                    + "  \"caller_type\": \"consumer\"\n" + "}";

    private static final String ADVANCED_RESPONSE_JSON =
            "{\n" + "  \"status\": 0,\n" + "  \"status_message\": \"Success\",\n"
                    + "  \"request_id\": \"aaaaaaaa-bbbb-cccc-dddd-0123456789ab\",\n"
                    + "  \"international_format_number\": \"447700900000\",\n"
                    + "  \"national_format_number\": \"07700 900000\",\n" + "  \"country_code\": \"GB\",\n"
                    + "  \"country_code_iso3\": \"GBR\",\n" + "  \"country_name\": \"United Kingdom\",\n"
                    + "  \"country_prefix\": \"44\",\n" + "  \"request_price\": \"0.04000000\",\n"
                    + "  \"refund_price\": \"0.01500000\",\n" + "  \"remaining_balance\": \"1.23456789\",\n"
                    + "  \"current_carrier\": {\n" + "    \"network_code\": \"12345\",\n"
                    + "    \"name\": \"Acme Inc\",\n" + "    \"country\": \"GB\",\n"
                    + "    \"network_type\": \"mobile\"\n" + "  },\n" + "  \"original_carrier\": {\n"
                    + "    \"network_code\": \"98765\",\n" + "    \"name\": \"Foo Bar\",\n"
                    + "    \"country\": \"US\",\n" + "    \"network_type\": \"mobile\"\n" + "  },\n"
                    + "  \"ported\": \"not_ported\",\n" + "  \"roaming\": {\n" + "    \"status\": \"roaming\",\n"
                    + "    \"roaming_country_code\": \"US\",\n" + "    \"roaming_network_code\": 12345,\n"
                    + "    \"roaming_network_name\": \"Acme Inc\"\n" + "  },\n" + "  \"caller_identity\": {\n"
                    + "    \"caller_type\": \"consumer\",\n" + "    \"caller_name\": \"John Smith\",\n"
                    + "    \"first_name\": \"John\",\n" + "    \"last_name\": \"Smith\",\n"
                    + "    \"subscription_type\": \"unknown\"\n" + "  },\n" + "  \"lookup_outcome\": \"0\",\n"
                    + "  \"lookup_outcome_message\": \"Success\",\n" + "  \"valid_number\": \"valid\",\n"
                    + "  \"reachable\": \"reachable\",\n" + "  \"ip\": {\n" + "    \"address\": \"123.0.0.255\",\n"
                    + "    \"ip_match_level\": \"country\",\n" + "    \"ip_country\": \"GB\",\n"
                    + "    \"ip_city\": \"London\"\n" + "  },\n" + "  \"ip_warnings\": \"no_warning\"\n" + ",\n"
                    + "  \"caller_name\": \"John Smith\",\n" + "  \"last_name\": \"Smith\",\n"
                    + "  \"first_name\": \"John\",\n" + "  \"caller_type\": \"consumer\"}";

    private static final String ASYNC_ADVANCED_RESPONSE_JSON = "{\n" +
            "  \"request_id\": \"aaaaaaaa-bbbb-cccc-dddd-0123456789ab\",\n" +
            "  \"number\": \"447700900000\",\n" +
            "  \"remaining_balance\": \"1.23456789\",\n" +
            "  \"request_price\": \"0.01500000\",\n" +
            "  \"status\": 0\n" +
            "}";

    @Before
    public void setUp() {
        client = new InsightClient(wrapper);
    }

    @Test
    public void testBasicInsightWithNumber() throws Exception {
        wrapper.setHttpClient(stubHttpClient(200, BASIC_RESPOSE_JSON));

        BasicInsightResponse response = this.client.getBasicNumberInsight("1234");

        assertBasicResponse(response);
    }

    @Test
    public void testBasicInsightWithNumberAndCountry() throws Exception {
        wrapper.setHttpClient(stubHttpClient(200, BASIC_RESPOSE_JSON));

        BasicInsightResponse response = this.client.getBasicNumberInsight("1234", "GB");

        assertBasicResponse(response);
    }

    @Test
    public void testStandardInsightWithNumber() throws Exception {
        wrapper.setHttpClient(stubHttpClient(200, STANDARD_RESPONSE_JSON));

        StandardInsightResponse response = this.client.getStandardNumberInsight("1234");

        assertBasicResponse(response);
    }

    @Test
    public void testStandardInsightWithNumberAndCountry() throws Exception {
        wrapper.setHttpClient(stubHttpClient(200, STANDARD_RESPONSE_JSON));

        StandardInsightResponse response = this.client.getStandardNumberInsight("1234", "GB");

        assertStandardResponse(response);
    }

    @Test
    public void testStandardInsightWithNumberAndCountryAndCnam() throws Exception {
        wrapper.setHttpClient(stubHttpClient(200, STANDARD_RESPONSE_JSON));

        StandardInsightResponse response = this.client.getStandardNumberInsight("1234", "GB", true);

        assertStandardResponse(response);
    }

    @Test
    public void testAdvancedInsightWithNumber() throws Exception {
        wrapper.setHttpClient(stubHttpClient(200, ADVANCED_RESPONSE_JSON));

        AdvancedInsightResponse response = this.client.getAdvancedNumberInsight("1234");

        assertAdvancedInsightResponse(response);
    }

    @Test
    public void testAdvancedInsightWithNumberAndCountry() throws Exception {
        wrapper.setHttpClient(stubHttpClient(200, ADVANCED_RESPONSE_JSON));

        AdvancedInsightResponse response = this.client.getAdvancedNumberInsight("1234", "GB");

        assertAdvancedInsightResponse(response);
    }

    @Test
    public void testAdvancedInsightWithNumberAndCountryAndIp() throws Exception {
        wrapper.setHttpClient(stubHttpClient(200, ADVANCED_RESPONSE_JSON));

        AdvancedInsightResponse response = this.client.getAdvancedNumberInsight("1234", "GB", "127.0.0.1");

        assertAdvancedInsightResponse(response);
    }

    @Test
    public void testAdvancedInsightWithNumberAndCountryAndIpAndCnam() throws Exception {
        wrapper.setHttpClient(stubHttpClient(200, ADVANCED_RESPONSE_JSON));

        AdvancedInsightResponse response = this.client.getAdvancedNumberInsight("1234", "GB", "127.0.0.1", true);

        assertAdvancedInsightResponse(response);
    }

    @Test
    public void testAsyncAdvancedInsight() throws Exception {
        wrapper.setHttpClient(stubHttpClient(200, ASYNC_ADVANCED_RESPONSE_JSON));

        AdvancedInsightResponse response = this.client.getAdvancedNumberInsight(AdvancedInsightRequest.builder("1234")
                .async(true)
                .callback("https://example.com")
                .build());

        assertAsyncInsightResponse(response);
    }

    private void assertAsyncInsightResponse(AdvancedInsightResponse response) {
        assertEquals(InsightStatus.SUCCESS, response.getStatus());
        assertEquals(new BigDecimal("1.23456789"), response.getRemainingBalance());
        assertEquals(new BigDecimal("0.01500000"), response.getRequestPrice());
    }

    private void assertBasicResponse(BasicInsightResponse response) {
        assertEquals(InsightStatus.SUCCESS, response.getStatus());
        assertEquals("Success", response.getStatusMessage());
        assertEquals("aaaaaaaa-bbbb-cccc-dddd-0123456789ab", response.getRequestId());
        assertEquals("447700900000", response.getInternationalFormatNumber());
        assertEquals("07700 900000", response.getNationalFormatNumber());
        assertEquals("GB", response.getCountryCode());
        assertEquals("GBR", response.getCountryCodeIso3());
        assertEquals("United Kingdom", response.getCountryName());
        assertEquals("44", response.getCountryPrefix());
    }

    private void assertStandardResponse(StandardInsightResponse response) {
        assertBasicResponse(response);
        assertEquals(new BigDecimal("0.04000000"), response.getRequestPrice());
        assertEquals(new BigDecimal("0.01500000"), response.getRefundPrice());
        assertEquals(new BigDecimal("1.23456789"), response.getRemainingBalance());

        assertEquals("12345", response.getCurrentCarrier().getNetworkCode());
        assertEquals("Acme Inc", response.getCurrentCarrier().getName());
        assertEquals("GB", response.getCurrentCarrier().getCountry());
        assertEquals(CarrierDetails.NetworkType.MOBILE, response.getCurrentCarrier().getNetworkType());

        assertEquals("98765", response.getOriginalCarrier().getNetworkCode());
        assertEquals("Foo Bar", response.getOriginalCarrier().getName());
        assertEquals("US", response.getOriginalCarrier().getCountry());
        assertEquals(CarrierDetails.NetworkType.MOBILE, response.getOriginalCarrier().getNetworkType());

        assertEquals(CallerType.CONSUMER, response.getCallerIdentity().getType());
        assertEquals("John Smith", response.getCallerIdentity().getName());
        assertEquals("John", response.getCallerIdentity().getFirstName());
        assertEquals("Smith", response.getCallerIdentity().getLastName());
    }

    private void assertAdvancedInsightResponse(AdvancedInsightResponse response) {
        assertBasicResponse(response);
        assertStandardResponse(response);

        assertEquals(AdvancedInsightResponse.PortedStatus.NOT_PORTED, response.getPorted());

        assertEquals(RoamingDetails.RoamingStatus.ROAMING, response.getRoaming().getStatus());
        assertEquals("US", response.getRoaming().getRoamingCountryCode());
        assertEquals("12345", response.getRoaming().getRoamingNetworkCode());
        assertEquals("Acme Inc", response.getRoaming().getRoamingNetworkName());
    }
}
