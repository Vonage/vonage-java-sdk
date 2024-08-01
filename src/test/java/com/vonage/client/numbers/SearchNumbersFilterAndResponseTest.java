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

import com.vonage.client.TestUtils;
import com.vonage.client.VonageUnexpectedException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class SearchNumbersFilterAndResponseTest {

    @Test
    public void testDeserialization() {
        SearchNumbersResponse response = SearchNumbersResponse.fromJson("{\n" +
                "  \"count\": 4,\n" +
                "  \"numbers\": [\n" +
                "    {\n" +
                "      \"country\": \"GB\",\n" +
                "      \"msisdn\": \"447700900000\",\n" +
                "      \"cost\": \"0.50\",\n" +
                "      \"type\": \"mobile-lvn\",\n" +
                "      \"features\": [\n" +
                "        \"VOICE\",\n" +
                "        \"SMS\"\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}");

        TestUtils.testJsonableBaseObject(response);
        assertEquals(4, response.getCount());
        AvailableNumber number = response.getNumbers()[0];
        assertEquals("GB", number.getCountry());
        assertEquals("447700900000", number.getMsisdn());
        assertEquals("0.50", number.getCost());
        assertEquals("mobile-lvn", number.getType());
        assertArrayEquals(new String[]{"VOICE", "SMS"}, number.getFeatures());
    }

    @Test
    public void testBadJson() throws Exception {
        try {
            SearchNumbersResponse.fromJson("this-is-nonsense");
            fail("VonageUnexpectedException should be raised for bad JSON data.");
        } catch (VonageUnexpectedException nue) {
            // Expected
        }
    }

    @Test
    public void testFilterValues() throws Exception {
        SearchNumbersFilter filter = new SearchNumbersFilter("GG");
        filter.setIndex(1);
        filter.setSize(50);
        filter.setPattern("456");
        filter.setSearchPattern(SearchPattern.STARTS_WITH);
        filter.setFeatures(new String[] {"SMS"});

        assertEquals("GG", filter.getCountry());
        assertEquals(1, (long)filter.getIndex());
        assertEquals(50, (long)filter.getSize());
        assertEquals("456", filter.getPattern());
        assertEquals(0, filter.getSearchPattern().getValue());
        assertArrayEquals(new String[] {"SMS"}, filter.getFeatures());
    }

    @Test
    public void testSearchPatternValues() {
        assertEquals(0, SearchPattern.STARTS_WITH.getValue());
        assertEquals(1, SearchPattern.ANYWHERE.getValue());
        assertEquals(2, SearchPattern.ENDS_WITH.getValue());

        assertEquals(SearchPattern.STARTS_WITH, SearchPattern.valueOf("STARTS_WITH"));
    }
}
