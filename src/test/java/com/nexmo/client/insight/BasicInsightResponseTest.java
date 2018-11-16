/*
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
package com.nexmo.client.insight;

import com.nexmo.client.NexmoUnexpectedException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class BasicInsightResponseTest {
    @Test
    public void fromJson() throws Exception {
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
    public void fromBusyJson() throws Exception {
        BasicInsightResponse response = BasicInsightResponse.fromJson(
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
    public void fromErrorJson() throws Exception {
        BasicInsightResponse response = BasicInsightResponse.fromJson(
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
    public void testJsonError() throws Exception {
        try {
            BasicInsightResponse.fromJson("blarg");
            fail("Deserializing nonsense JSON should result in a NexmoUnexpectedException");
        } catch (NexmoUnexpectedException nue) {
            // This is expected
        }
    }
}
