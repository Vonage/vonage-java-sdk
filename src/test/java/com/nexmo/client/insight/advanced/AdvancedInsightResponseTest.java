package com.nexmo.client.insight.advanced;/*
 * Copyright (c) 2011-2017 Nexmo Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import com.nexmo.client.NexmoUnexpectedException;
import com.nexmo.client.insight.CarrierDetails;
import com.nexmo.client.insight.RoamingDetails;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AdvancedInsightResponseTest {
    @Test
    public void testFromJson() throws Exception {
        AdvancedInsightResponse response = AdvancedInsightResponse.fromJson("{\n" +
                "    \"status\": 0,\n" +
                "    \"status_message\": \"Success\",\n" +
                "    \"lookup_outcome\": 1,\n" +
                "    \"lookup_outcome_message\": \"Partial success - some fields populated\",\n" +
                "    \"request_id\": \"0c082a69-85df-4bbc-aae6-ee998e17e5a4\",\n" +
                "    \"international_format_number\": \"441632960960\",\n" +
                "    \"national_format_number\": \"01632 960960\",\n" +
                "    \"country_code\": \"GB\",\n" +
                "    \"country_code_iso3\": \"GBR\",\n" +
                "    \"country_name\": \"United Kingdom\",\n" +
                "    \"country_prefix\": \"44\",\n" +
                "    \"request_price\": \"0.03000000\",\n" +
                "    \"remaining_balance\": \"18.30908949\",\n" +
                "    \"current_carrier\": {\n" +
                "        \"network_code\": \"GB-FIXED-RESERVED\",\n" +
                "        \"name\": \"United Kingdom Landline Reserved\",\n" +
                "        \"country\": \"GB\",\n" +
                "        \"network_type\": \"landline\"\n" +
                "    },\n" +
                "    \"original_carrier\": {\n" +
                "        \"network_code\": \"GB-HAPPY-RESERVED\",\n" +
                "        \"name\": \"United Kingdom Mobile Reserved\",\n" +
                "        \"country\": \"GB\",\n" +
                "        \"network_type\": \"mobile\"\n" +
                "    },\n" +
                "    \"valid_number\": \"valid\",\n" +
                "    \"reachable\": \"unknown\",\n" +
                "    \"ported\": \"assumed_not_ported\",\n" +
                "    \"roaming\": {\"status\": \"not_roaming\"}\n" +
                "}");

        assertEquals(0, response.getStatus());
        assertEquals("Success", response.getStatusMessage());
        assertEquals("0c082a69-85df-4bbc-aae6-ee998e17e5a4", response.getRequestId());
        assertEquals("441632960960", response.getInternationalFormatNumber());
        assertEquals("01632 960960", response.getNationalFormatNumber());
        assertEquals("GB", response.getCountryCode());
        assertEquals("GBR", response.getCountryCodeIso3());
        assertEquals("United Kingdom", response.getCountryName());
        assertEquals("44", response.getCountryPrefix());
        assertEquals("GB-FIXED-RESERVED", response.getCurrentCarrier().getNetworkCode());
        assertEquals("United Kingdom Landline Reserved", response.getCurrentCarrier().getName());
        assertEquals("GB", response.getCurrentCarrier().getCountry());
        assertEquals("GB-FIXED-RESERVED", response.getCurrentCarrier().getNetworkCode());
        assertEquals("United Kingdom Landline Reserved", response.getCurrentCarrier().getName());
        assertEquals("GB", response.getCurrentCarrier().getCountry());
        assertEquals(CarrierDetails.NetworkType.LANDLINE, response.getCurrentCarrier().getNetworkType());

        assertEquals("GB-HAPPY-RESERVED", response.getOriginalCarrier().getNetworkCode());
        assertEquals("United Kingdom Mobile Reserved", response.getOriginalCarrier().getName());
        assertEquals("GB", response.getOriginalCarrier().getCountry());
        assertEquals(CarrierDetails.NetworkType.MOBILE, response.getOriginalCarrier().getNetworkType());

        assertEquals("18.30908949", response.getRemainingBalance());
        assertEquals("0.03000000", response.getRequestPrice());

        assertEquals(AdvancedInsightResponse.Validity.VALID, response.getValidNumber());
        assertEquals(AdvancedInsightResponse.Reachability.UNKNOWN, response.getReachability());
        assertEquals(AdvancedInsightResponse.PortedStatus.ASSUMED_NOT_PORTED, response.getPorted());
        assertEquals(RoamingDetails.RoamingStatus.NOT_ROAMING, response.getRoaming().getStatus());
        assertEquals(new Integer(1), response.getLookupOutcome());
        assertEquals("Partial success - some fields populated", response.getLookupOutcomeMessage());

    }

    @Test
    public void testRoamingDeserialization() throws Exception {
        AdvancedInsightResponse response = AdvancedInsightResponse.fromJson("{\n" +
                "  \"status\": 0,\n" +
                "  \"status_message\": \"Success\",\n" +
                "  \"lookup_outcome\": 1,\n" +
                "  \"lookup_outcome_message\": \"Partial success - some fields populated\",\n" +
                "  \"request_id\": \"0c082a69-85df-4bbc-aae6-ee998e17e5a4\",\n" +
                "  \"international_format_number\": \"441632960960\",\n" +
                "  \"national_format_number\": \"01632 960960\",\n" +
                "  \"country_code\": \"GB\",\n" +
                "  \"country_code_iso3\": \"GBR\",\n" +
                "  \"country_name\": \"United Kingdom\",\n" +
                "  \"country_prefix\": \"44\",\n" +
                "  \"request_price\": \"0.03000000\",\n" +
                "  \"remaining_balance\": \"18.30908949\",\n" +
                "  \"current_carrier\": {\n" +
                "    \"network_code\": \"GB-FIXED-RESERVED\",\n" +
                "    \"name\": \"United Kingdom Landline Reserved\",\n" +
                "    \"country\": \"GB\",\n" +
                "    \"network_type\": \"landline\"\n" +
                "  },\n" +
                "  \"original_carrier\": {\n" +
                "    \"network_code\": \"GB-FIXED-RESERVED\",\n" +
                "    \"name\": \"United Kingdom Landline Reserved\",\n" +
                "    \"country\": \"GB\",\n" +
                "    \"network_type\": \"landline\"\n" +
                "  },\n" +
                "  \"valid_number\": \"valid\",\n" +
                "  \"reachable\": \"unknown\",\n" +
                "  \"ported\": \"assumed_not_ported\",\n" +
                "  \"roaming\": {\n" +
                "    \"status\": \"roaming\",\n" +
                "    \"roaming_country_code\": \"GB\",\n" +
                "    \"roaming_network_code\": \"gong\",\n" +
                "    \"roaming_network_name\": \"Gong Telecommunications\"\n" +
                "  }\n" +
                "}");

        assertEquals(RoamingDetails.RoamingStatus.ROAMING, response.getRoaming().getStatus());
        assertEquals("GB", response.getRoaming().getRoamingCountryCode());
        assertEquals("gong", response.getRoaming().getRoamingNetworkCode());
        assertEquals("Gong Telecommunications", response.getRoaming().getRoamingNetworkName());
    }

    @Test
    public void fromBusyJson() throws Exception {
        AdvancedInsightResponse response = AdvancedInsightResponse.fromJson(
                "{\n" +
                        "    \"status\": 1,\n" +
                        "    \"status_message\": \"Back off\",\n" +
                        "    \"request_id\": \"d79c3d82-e2ee-46ff-972a-97b76be419cb\"\n" +
                        "}"
        );

        assertEquals(1, response.getStatus());
        assertEquals("Back off", response.getStatusMessage());
        assertEquals("d79c3d82-e2ee-46ff-972a-97b76be419cb", response.getRequestId());
    }

    @Test
    public void fromErrorJson() throws Exception {
        AdvancedInsightResponse response = AdvancedInsightResponse.fromJson(
                "{\n" +
                        "    \"status\": 3,\n" +
                        "    \"error_text\": \"I'm not sure what you mean\",\n" +
                        "    \"request_id\": \"d79c3d82-e2ee-46ff-972a-97b76be419cb\"\n" +
                        "}"
        );

        assertEquals(3, response.getStatus());
        assertEquals("I'm not sure what you mean", response.getErrorText());
        assertEquals("d79c3d82-e2ee-46ff-972a-97b76be419cb", response.getRequestId());
    }

    @Test
    public void testJsonError() throws Exception {
        try {
            AdvancedInsightResponse.fromJson("blarg");
            fail("Deserializing nonsense JSON should result in a NexmoUnexpectedException");
        } catch (NexmoUnexpectedException nue) {
            // This is expected
        }
    }

}
