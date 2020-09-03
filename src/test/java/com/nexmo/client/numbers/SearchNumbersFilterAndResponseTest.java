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
package com.nexmo.client.numbers;

import com.nexmo.client.VonageUnexpectedException;
import org.junit.Test;

import static org.junit.Assert.*;

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
                "      \"type\": \"mobile\",\n" +
                "      \"features\": [\n" +
                "        \"VOICE\",\n" +
                "        \"SMS\"\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}");
        assertEquals(4, response.getCount());
        AvailableNumber number = response.getNumbers()[0];
        assertEquals("GB", number.getCountry());
        assertEquals("447700900000", number.getMsisdn());
        assertEquals("0.50", number.getCost());
        assertEquals("mobile", number.getType());
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
