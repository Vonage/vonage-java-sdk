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
package com.vonage.client.insight;

import com.vonage.client.VonageUnexpectedException;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BasicInsightResponseTest {

    @Test
    public void fromJson() {
        BasicInsightResponse response = BasicInsightResponse.fromJson("{\n" +
                "    \"status\": 0,\n" +
                "    \"status_message\": \"Success\",\n" +
                "    \"request_id\": \"d79c3d82-e2ee-46ff-972a-97b76be419cb\",\n" +
                "    \"international_format_number\": \"441632960960\",\n" +
                "    \"national_format_number\": \"01632 960960\",\n" +
                "    \"country_code\": \"GB\",\n" +
                "    \"country_code_iso3\": \"GBR\",\n" +
                "    \"country_name\": \"United Kingdom\",\n" +
                "    \"country_prefix\": \"44\"\n" +
                "}");

        assertEquals(InsightStatus.SUCCESS, response.getStatus());
        assertEquals("Success", response.getStatusMessage());
        assertEquals("d79c3d82-e2ee-46ff-972a-97b76be419cb", response.getRequestId());
        assertEquals("441632960960", response.getInternationalFormatNumber());
        assertEquals("01632 960960", response.getNationalFormatNumber());
        assertEquals("GB", response.getCountryCode());
        assertEquals("GBR", response.getCountryCodeIso3());
        assertEquals("United Kingdom", response.getCountryName());
        assertEquals("44", response.getCountryPrefix());
    }

    @Test
    public void fromBusyJson() {
        BasicInsightResponse response = BasicInsightResponse.fromJson("{\n" +
            "    \"status\": 1,\n" +
            "    \"status_message\": \"Back off\",\n" +
            "    \"request_id\": \"d79c3d82-e2ee-46ff-972a-97b76be419cb\"\n" +
            "}");

        assertEquals(InsightStatus.THROTTLED, response.getStatus());
        assertEquals("Back off", response.getStatusMessage());
        assertEquals("d79c3d82-e2ee-46ff-972a-97b76be419cb", response.getRequestId());
    }

    @Test
    public void testFromUnknownFieldJson() {
        StandardInsightResponse response = StandardInsightResponse.fromJson(
                "{\n" + "    \"status\": 3,\n" + "    \"error_text\": \"I'm not sure what you mean\",\n"
                    + "    \"request_id\": \"d79c3d82-e2ee-46ff-972a-97b76be419cb\"\n" + "}");

        assertEquals(InsightStatus.INVALID_PARAMS, response.getStatus());
        assertEquals("d79c3d82-e2ee-46ff-972a-97b76be419cb", response.getRequestId());
    }

    @Test(expected = VonageUnexpectedException.class)
    public void testJsonError() {
        BasicInsightResponse.fromJson("blarg");
    }
}
