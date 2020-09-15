/*
 *   Copyright 2020 Vonage
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
package com.vonage.client.dtmf;

import com.vonage.client.voice.DtmfRequest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;



public class DtmfRequestTest {
    private DtmfRequest request;

    @Before
    public void setUp() throws Exception {
        request = new DtmfRequest("123-abc", "8675309");
    }

    @Test
    public void getUuid() throws Exception {
        assertEquals("123-abc", request.getUuid());
    }

    @Test
    public void getDigits() throws Exception {
        assertEquals("8675309", request.getDigits());
    }

    @Test
    public void setUuid() throws Exception {
        request.setUuid("000-xyz");
        assertEquals("000-xyz", request.getUuid());
    }

    @Test
    public void setDigits() throws Exception {
        request.setDigits("555");
        assertEquals("555", request.getDigits());
    }

    @Test
    public void toJson() throws Exception {
        String jsonString = "{" + "\"digits\":\"8675309\"" + "}";
        assertEquals(jsonString, request.toJson());
    }

}