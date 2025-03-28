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
import com.vonage.client.VonageResponseParseException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.math.BigDecimal;

public class StandardInsightResponseTest {

    @Test
    public void testFromJson() {
        StandardInsightResponse response = Jsonable.fromJson(
                "{\n" +
                "    \"status\": 43,\n" +
                "    \"status_message\": \"Lookup not returned\",\n" +
                "    \"request_id\": \"34564b7d-df8b-47fd-aa07-b722602dd974\",\n" +
                "    \"international_format_number\": \"441632960960\",\n" +
                "    \"national_format_number\": \"01632 960960\",\n" +
                "    \"country_code\": \"GB\",\n" +
                "    \"country_code_iso3\": \"GBR\",\n" +
                "    \"country_name\": \"United Kingdom\",\n" +
                "    \"country_prefix\": \"44\",\n" +
                "    \"request_price\": \"0.00500000\",\n" +
                "    \"remaining_balance\": \"18.34408949\",\n" +
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
                "    \"ported\": \"assumed_not_ported\",\n" +
                "    \"caller_identity\": {\n" +
                "        \"first_name\": \"Bob\",\n" +
                "        \"last_name\": \"Atkey\",\n" +
                "        \"caller_name\": \"Monads, Incorporated\",\n" +
                "        \"caller_type\": \"business\"\n" +
                "    }\n" +
                "}");

        assertEquals(InsightStatus.LOOKUP_NOT_RETURNED, response.getStatus());
        assertEquals(43, response.getStatus().getInsightStatus());
        assertEquals("Lookup not returned", response.getStatusMessage());
        assertEquals("34564b7d-df8b-47fd-aa07-b722602dd974", response.getRequestId());
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
        assertEquals(NetworkType.LANDLINE, response.getCurrentCarrier().getNetworkType());
        assertEquals("landline", response.getCurrentCarrier().getNetworkType().toString());
        assertEquals(PortedStatus.ASSUMED_NOT_PORTED, response.getPorted());
        assertEquals("assumed_not_ported", response.getPorted().toString());
        assertEquals("GB-HAPPY-RESERVED", response.getOriginalCarrier().getNetworkCode());
        assertEquals("United Kingdom Mobile Reserved", response.getOriginalCarrier().getName());
        assertEquals("GB", response.getOriginalCarrier().getCountry());
        assertEquals(NetworkType.MOBILE, response.getOriginalCarrier().getNetworkType());
        assertEquals("mobile", response.getOriginalCarrier().getNetworkType().toString());
        assertEquals(new BigDecimal("18.34408949"), response.getRemainingBalance());
        assertEquals(new BigDecimal("0.00500000"), response.getRequestPrice());
        assertEquals("Bob", response.getCallerIdentity().getFirstName());
        assertEquals("Atkey", response.getCallerIdentity().getLastName());
        assertEquals("Monads, Incorporated", response.getCallerIdentity().getName());
        assertEquals(CallerType.BUSINESS, response.getCallerIdentity().getType());
    }

    @Test
    public void testFromJsonUnknownInsightStatus() {
        StandardInsightResponse response = Jsonable.fromJson("{\n" +
                "    \"status\": 2147,\n" +
                "    \"status_message\": \"Lookup not returned\"\n" +
                "}");

        assertEquals(InsightStatus.UNKNOWN, response.getStatus());
        assertEquals(2147, response.getStatus().getInsightStatus());
    }

    @Test
    public void testFromJsonNullNetworkType() {
        StandardInsightResponse response = Jsonable.fromJson("{\n" +
                "    \"status\": 45,\n" +
                "    \"status_message\": \"Lookup not returned\",\n" +
                "    \"request_id\": \"d79c3d82-e2ee-46ff-972a-97b76be419cb\",\n" +
                "    \"current_carrier\": {\n" +
                "        \"network_code\": \"NONE\",\n" +
                "        \"name\": \"Unknown\",\n" +
                "        \"country\": \"GB\",\n" +
                "        \"network_type\": \"null\"\n" +
                "    }\n" +
                "}");

        assertNull(response.getCurrentCarrier().getNetworkType());
        assertEquals(InsightStatus.LOOKUP_NOT_RETURNED, response.getStatus());
        int status = response.getStatus().getInsightStatus();
        assertTrue(status >= 43 && status <= 45);
    }

    @Test
    public void testFromBusyJson() {
        StandardInsightResponse response = Jsonable.fromJson(
                "{\n" + "    \"status\": 1,\n" + "    \"status_message\": \"Back off\",\n"
                        + "    \"request_id\": \"d79c3d82-e2ee-46ff-972a-97b76be419cb\"\n" + "}");

        assertEquals(InsightStatus.THROTTLED, response.getStatus());
        assertEquals(1, response.getStatus().getInsightStatus());
        assertEquals("Back off", response.getStatusMessage());
        assertEquals("d79c3d82-e2ee-46ff-972a-97b76be419cb", response.getRequestId());
    }

    @Test
    public void testJsonError() {
        assertThrows(VonageResponseParseException.class, () ->
                Jsonable.fromJson("blarg", StandardInsightResponse.class)
        );
    }
}
