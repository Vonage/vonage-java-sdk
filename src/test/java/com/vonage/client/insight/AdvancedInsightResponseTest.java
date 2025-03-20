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
package com.vonage.client.insight;

import com.vonage.client.Jsonable;
import com.vonage.client.TestUtils;
import com.vonage.client.VonageResponseParseException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

public class AdvancedInsightResponseTest {

    @Test
    public void testFromJson() {
        AdvancedInsightResponse response = Jsonable.fromJson("{\n" +
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
                "    \"reachable\": \"reachable\",\n" +
                "    \"ported\": \"assumed_not_ported\",\n" +
                "    \"roaming\": {\"status\": \"not_roaming\"},\n" +
                "    \"first_name\": \"Bob\",\n" +
                "    \"last_name\": \"Atkey\",\n" +
                "    \"caller_name\": \"Monads, Incorporated\",\n" +
                "    \"caller_type\": \"unknown\",\n" +
                "    \"real_time_data\": {\n" +
                "       \"active_status\": \"active\",\n" +
                "       \"handset_status\": \"On\"\n" +
                "    }," +
                "    \"error_text\": \"Success\"\n" +
                "}");

        TestUtils.testJsonableBaseObject(response);
        assertEquals(InsightStatus.SUCCESS, response.getStatus());
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
        assertEquals(new BigDecimal("18.30908949"), response.getRemainingBalance());
        assertEquals(new BigDecimal("0.03000000"), response.getRequestPrice());
        assertEquals(Validity.VALID, response.getValidNumber());
        assertEquals("valid", response.getValidNumber().toString());
        assertEquals(Reachability.REACHABLE, response.getReachability());
        assertEquals("reachable", response.getReachability().toString());
        assertEquals(PortedStatus.ASSUMED_NOT_PORTED, response.getPorted());
        assertEquals(RoamingDetails.RoamingStatus.NOT_ROAMING, response.getRoaming().getStatus());
        assertEquals("not_roaming", response.getRoaming().getStatus().toString());
        assertEquals(LookupOutcome.PARTIAL_SUCCESS, response.getLookupOutcome());
        assertEquals(1, response.getLookupOutcome().getCode());
        assertEquals("Partial success - some fields populated", response.getLookupOutcomeMessage());
        assertEquals("Bob", response.getFirstName());
        assertEquals("Atkey", response.getLastName());
        assertEquals("Monads, Incorporated", response.getCallerName());
        assertEquals(CallerType.UNKNOWN, response.getCallerType());
        assertEquals("Success", response.getErrorText());
    }

    @Test
    public void testFromJsonUnknownRoaming() {
        AdvancedInsightResponse response = Jsonable.fromJson("{\n" +
                "    \"roaming\": \"unknown\",\n" +
                "    \"caller_type\": \"dunno\",\n" +
                "    \"real_time_data\": {\n" +
                "       \"active_status\": \"inactive\"" +
                "    }\n" +
                "}");

        assertEquals(RoamingDetails.RoamingStatus.UNKNOWN, response.getRoaming().getStatus());
        assertEquals("unknown", response.getRoaming().getStatus().toString());
        assertEquals(CallerType.UNKNOWN, response.getCallerType());
        assertEquals("unknown", response.getCallerType().toString());
    }

    @Test
    public void testInvalidRoamingShape() {
        AdvancedInsightResponse response = Jsonable.fromJson("{\"roaming\": []}");
        assertNotNull(response);
        assertEquals(RoamingDetails.RoamingStatus.UNKNOWN, response.getRoaming().getStatus());
    }

    @Test
    public void testFromJsonWithNullableEnums() {
        AdvancedInsightResponse response = Jsonable.fromJson("{\n" +
                "    \"lookup_outcome\": 2,\n" +
                "    \"lookup_outcome_message\": \"Failure\",\n" +
                "    \"caller_type\": \"unknown\",\n" +
                "    \"ported\": \"null\"\n," +
                "    \"reachable\": \"null\",\n" +
                "    \"real_time_data\": {\n" +
                "       \"handset_status\": \"Off\"\n" +
                "    }\n" +
                "}");

        assertEquals(LookupOutcome.FAILED, response.getLookupOutcome());
        assertEquals(2, response.getLookupOutcome().getCode());
        assertEquals(CallerType.UNKNOWN, response.getCallerType());
        assertNull(response.getPorted());
        assertNull(response.getRoaming());
        assertNull(response.getReachability());
    }

    @Test
    public void testDeserializeUnknownEnumsFallbackToUnknown() {
        AdvancedInsightResponse response = Jsonable.fromJson(
                "{\n" +
                "    \"valid_number\": \"failed_validity\",\n" +
                "    \"reachable\": \"failed_reachibility\",\n" +
                "    \"ported\": \"failure_ported_status\",\n" +
                "  \"roaming\": {\n" +
                "    \"status\": \"failure_roaming_status\"\n" +
                "  },\n" +
                "    \"original_carrier\": {\n" +
                "        \"network_code\": \"GB-HAPPY-RESERVED\",\n" +
                "        \"name\": \"United Kingdom Mobile Reserved\",\n" +
                "        \"country\": \"GB\",\n" +
                "        \"network_type\": \"dunno\"\n" +
                "    }\n," +
                "    \"real_time_data\": {\n" +
                "       \"active_status\": \"null\",\n" +
                "       \"handset_status\": \"unknown\"\n" +
                "    }\n" +
                "}"
        );

        assertEquals(Validity.UNKNOWN, response.getValidNumber());
        assertEquals(Reachability.UNKNOWN, response.getReachability());
        assertEquals(PortedStatus.UNKNOWN, response.getPorted());
        assertEquals(RoamingDetails.RoamingStatus.UNKNOWN, response.getRoaming().getStatus());
        assertNull(response.getCurrentCarrier());
        assertEquals(CarrierDetails.NetworkType.UNKNOWN, response.getOriginalCarrier().getNetworkType());
        assertNull(response.getLookupOutcome());
    }

    @Test
    public void testRoamingDeserialization() {
        AdvancedInsightResponse response = Jsonable.fromJson("{\n" +
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
    public void testUnknownLookupOutcome() {
        AdvancedInsightResponse response = Jsonable.fromJson("{\n" +
                "  \"lookup_outcome\": 3,\n" +
                "  \"lookup_outcome_message\": \"??\"\n" +
                "}");

        assertEquals(LookupOutcome.UNKNOWN, response.getLookupOutcome());
        assertEquals(3, response.getLookupOutcome().getCode());
    }

    @Test
    public void testFromBusyJson() {
        AdvancedInsightResponse response = Jsonable.fromJson(
                "{\n" +
                        "    \"status\": 1,\n" +
                        "    \"status_message\": \"Back off\",\n" +
                        "    \"request_id\": \"d79c3d82-e2ee-46ff-972a-97b76be419cb\"\n" +
                        "}"
        );

        assertEquals(InsightStatus.THROTTLED, response.getStatus());
        assertEquals("Back off", response.getStatusMessage());
        assertEquals("d79c3d82-e2ee-46ff-972a-97b76be419cb", response.getRequestId());
    }

    @Test
    public void testFromErrorJson() {
        AdvancedInsightResponse response = Jsonable.fromJson(
                "{\n" +
                        "    \"status\": 3,\n" +
                        "    \"error_text\": \"I'm not sure what you mean\",\n" +
                        "    \"request_id\": \"d79c3d82-e2ee-46ff-972a-97b76be419cb\"\n" +
                        "}"
        );

        assertEquals(InsightStatus.INVALID_PARAMS, response.getStatus());
        assertEquals("I'm not sure what you mean", response.getErrorText());
        assertEquals("d79c3d82-e2ee-46ff-972a-97b76be419cb", response.getRequestId());
    }

    @Test
    public void testJsonError() {
        assertThrows(VonageResponseParseException.class, () ->
                Jsonable.fromJson("blarg", AdvancedInsightResponse.class)
        );
    }

    @Test
    public void testLookupOutcome() {
        String json = "{\n" +
                "    \"lookup_outcome\": 0,\n" +
                "    \"lookup_outcome_message\": \"Success\"\n" +
                "}";
        AdvancedInsightResponse response = Jsonable.fromJson(json);

        assertEquals(LookupOutcome.SUCCESS, response.getLookupOutcome());
        assertEquals(0, response.getLookupOutcome().getCode());
        assertEquals("Success", response.getLookupOutcomeMessage());

        response = Jsonable.fromJson(json.replace("0", "1"));
        assertEquals(LookupOutcome.PARTIAL_SUCCESS, response.getLookupOutcome());

        response = Jsonable.fromJson(json.replace("0", "2"));
        assertEquals(LookupOutcome.FAILED, response.getLookupOutcome());

        response = Jsonable.fromJson(json.replace("0", "null"));
        assertNull(response.getLookupOutcome());

        response = Jsonable.fromJson(json.replace("    \"lookup_outcome\": 0,\n", ""));
        assertNull(response.getLookupOutcome());

        assertNull(LookupOutcome.fromInt(null));
    }
}
