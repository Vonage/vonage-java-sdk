/*
 * Copyright (c) 2020 Vonage
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
package com.vonage.client.insight;

import com.vonage.client.VonageUnexpectedException;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class StandardInsightResponseTest {
    @Test
    public void testFromJson() throws Exception {
        StandardInsightResponse response = StandardInsightResponse.fromJson(
                "{\n" + "    \"status\": 0,\n" + "    \"status_message\": \"Success\",\n"
                        + "    \"request_id\": \"34564b7d-df8b-47fd-aa07-b722602dd974\",\n"
                        + "    \"international_format_number\": \"441632960960\",\n"
                        + "    \"national_format_number\": \"01632 960960\",\n" + "    \"country_code\": \"GB\",\n"
                        + "    \"country_code_iso3\": \"GBR\",\n" + "    \"country_name\": \"United Kingdom\",\n"
                        + "    \"country_prefix\": \"44\",\n" + "    \"request_price\": \"0.00500000\",\n"
                        + "    \"remaining_balance\": \"18.34408949\",\n" + "    \"current_carrier\": {\n"
                        + "        \"network_code\": \"GB-FIXED-RESERVED\",\n"
                        + "        \"name\": \"United Kingdom Landline Reserved\",\n" + "        \"country\": \"GB\",\n"
                        + "        \"network_type\": \"landline\"\n" + "    },\n" + "    \"original_carrier\": {\n"
                        + "        \"network_code\": \"GB-HAPPY-RESERVED\",\n"
                        + "        \"name\": \"United Kingdom Mobile Reserved\",\n" + "        \"country\": \"GB\",\n"
                        + "        \"network_type\": \"mobile\"\n" + "    },\n"
                        + "    \"ported\": \"assumed_not_ported\",\n" + "    \"caller_identity\": {\n"
                        + "        \"first_name\": \"Bob\",\n" + "        \"last_name\": \"Atkey\",\n"
                        + "        \"caller_name\": \"Monads, Incorporated\",\n"
                        + "        \"caller_type\": \"business\"\n" + "    }\n" + "}");

        assertEquals(InsightStatus.SUCCESS, response.getStatus());
        assertEquals("Success", response.getStatusMessage());
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
        assertEquals(CarrierDetails.NetworkType.LANDLINE, response.getCurrentCarrier().getNetworkType());

        assertEquals("GB-HAPPY-RESERVED", response.getOriginalCarrier().getNetworkCode());
        assertEquals("United Kingdom Mobile Reserved", response.getOriginalCarrier().getName());
        assertEquals("GB", response.getOriginalCarrier().getCountry());
        assertEquals(CarrierDetails.NetworkType.MOBILE, response.getOriginalCarrier().getNetworkType());

        assertEquals(new BigDecimal("18.34408949"), response.getRemainingBalance());
        assertEquals(new BigDecimal("0.00500000"), response.getRequestPrice());

        assertEquals("Bob", response.getCallerIdentity().getFirstName());
        assertEquals("Atkey", response.getCallerIdentity().getLastName());
        assertEquals("Monads, Incorporated", response.getCallerIdentity().getName());
        assertEquals(CallerType.BUSINESS, response.getCallerIdentity().getType());
    }

    @Test
    public void fromBusyJson() throws Exception {
        StandardInsightResponse response = StandardInsightResponse.fromJson(
                "{\n" + "    \"status\": 1,\n" + "    \"status_message\": \"Back off\",\n"
                        + "    \"request_id\": \"d79c3d82-e2ee-46ff-972a-97b76be419cb\"\n" + "}");

        assertEquals(InsightStatus.THROTTLED, response.getStatus());
        assertEquals("Back off", response.getStatusMessage());
        assertEquals("d79c3d82-e2ee-46ff-972a-97b76be419cb", response.getRequestId());
    }

    @Test
    public void fromErrorJson() throws Exception {
        StandardInsightResponse response = StandardInsightResponse.fromJson(
                "{\n" + "    \"status\": 3,\n" + "    \"error_text\": \"I'm not sure what you mean\",\n"
                        + "    \"request_id\": \"d79c3d82-e2ee-46ff-972a-97b76be419cb\"\n" + "}");

        assertEquals(InsightStatus.INVALID_PARAMS, response.getStatus());
        assertEquals("I'm not sure what you mean", response.getErrorText());
        assertEquals("d79c3d82-e2ee-46ff-972a-97b76be419cb", response.getRequestId());
    }

    @Test
    public void testJsonError() throws Exception {
        try {
            StandardInsightResponse.fromJson("blarg");
            fail("Deserializing nonsense JSON should result in a VonageUnexpectedException");
        } catch (VonageUnexpectedException nue) {
            // This is expected
        }
    }
}
