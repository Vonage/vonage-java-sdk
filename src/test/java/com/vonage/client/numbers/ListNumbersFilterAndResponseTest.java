/*
 *   Copyright 2022 Vonage
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

import com.vonage.client.VonageUnexpectedException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListNumbersFilterAndResponseTest {
    @Test
    public void testDeserialization() {
        ListNumbersResponse response = ListNumbersResponse.fromJson("{\n" +
                "  \"count\": 1,\n" +
                "  \"numbers\": [\n" +
                "    {\n" +
                "      \"country\": \"GB\",\n" +
                "      \"msisdn\": \"447700900000\",\n" +
                "      \"moHttpUrl\": \"https://example.com/mo\",\n" +
                "      \"type\": \"mobile-lvn\",\n" +
                "      \"features\": [\n" +
                "        \"VOICE\",\n" +
                "        \"SMS\"\n" +
                "      ],\n" +
                "      \"voiceCallbackType\": \"app\",\n" +
                "      \"voiceCallbackValue\": \"aaaaaaaa-bbbb-cccc-dddd-0123456789ab\"\n" +
                "    }\n" +
                "  ]\n" +
                "}");
        assertEquals(1, response.getCount());
        OwnedNumber number = response.getNumbers()[0];
        assertEquals("GB", number.getCountry());
        assertEquals("447700900000", number.getMsisdn());
        assertEquals("https://example.com/mo", number.getMoHttpUrl());
        assertEquals("mobile-lvn", number.getType());
        assertArrayEquals(new String[]{"VOICE", "SMS"}, number.getFeatures());
        assertEquals("app", number.getVoiceCallbackType());
        assertEquals("aaaaaaaa-bbbb-cccc-dddd-0123456789ab", number.getVoiceCallbackValue());
    }

    @Test
    public void testBadJson() throws Exception {
        try {
            ListNumbersResponse.fromJson("this-is-nonsense");
            fail("VonageUnexpectedException should be raised for bad JSON data.");
        } catch (VonageUnexpectedException nue) {
            // Expected
        }
    }

    @Test
    public void testFilterValues() throws Exception {
        ListNumbersFilter filter = new ListNumbersFilter(1, 50, "456", SearchPattern.ANYWHERE);
        assertEquals(1, (long)filter.getIndex());
        assertEquals(50, (long)filter.getSize());
        assertEquals("456", filter.getPattern());
        assertEquals(1, filter.getSearchPattern().getValue());
    }

    @Test
    public void testSearchPatternValues() {
        assertEquals(0, SearchPattern.STARTS_WITH.getValue());
        assertEquals(1, SearchPattern.ANYWHERE.getValue());
        assertEquals(2, SearchPattern.ENDS_WITH.getValue());

        assertEquals(SearchPattern.STARTS_WITH, SearchPattern.valueOf("STARTS_WITH"));
    }
}
