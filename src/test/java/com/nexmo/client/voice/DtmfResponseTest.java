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
package com.nexmo.client.voice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;



public class DtmfResponseTest {
    private DtmfResponse response;

    @Before
    public void setUp() {
        response = DtmfResponse.fromJson("{\n" +
                "  \"message\": \"DTMF sent\",\n" +
                "  \"uuid\": \"ssf61863-4a51-ef6b-11e1-w6edebcf93bb\"\n" +
                "}");
    }

    @Test
    public void testBasics() {
        assertEquals("DTMF sent", response.getMessage());
        assertEquals("ssf61863-4a51-ef6b-11e1-w6edebcf93bb", response.getUuid());
    }

    @Test
    public void testUnknownJson() throws IOException {
        String json = "{\n" +
                "    \"unknownProperty\": \"unknown\"\n" +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        DtmfResponse dr = mapper.readValue(json, DtmfResponse.class);
        assertNull(dr.getUuid());
    }
}