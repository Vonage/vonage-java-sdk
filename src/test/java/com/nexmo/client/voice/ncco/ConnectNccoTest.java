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

package com.nexmo.client.voice.ncco;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.voice.PhoneEndpoint;
import com.nexmo.client.voice.MachineDetection;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConnectNccoTest {

    @Test
    public void getAction() throws Exception {
        ConnectNcco ncco = new ConnectNcco(new PhoneEndpoint("447700900637"));
        assertEquals("connect", ncco.getAction());
    }

    @Test
    public void testToJson() throws Exception {
        assertEquals(
                "{\"endpoint\":[{\"type\":\"phone\",\"number\":\"447700900637\"}],\"action\":\"connect\"}",
                new ConnectNcco(new PhoneEndpoint("447700900637")).toJson());
    }

    @Test
    public void testJson() throws Exception {
        String json;
        {
            ConnectNcco ncco = new ConnectNcco("447700900637");
            ncco.setEndpoint(new PhoneEndpoint("447700900642"));
            ncco.setMachineDetection(MachineDetection.HANGUP);
            ncco.setEventMethod("GET");
            ncco.setEventUrl("https://api.example.com/event");
            ncco.setFrom("447700900723");
            ncco.setLimit(12);
            ncco.setTimeout(1000);

            json = ncco.toJson();
        }

        ConnectNcco ncco2 = new ObjectMapper().readValue(json, ConnectNcco.class);
        assertArrayEquals(new PhoneEndpoint[]{new PhoneEndpoint("447700900642")}, ncco2.getEndpoint());
        assertEquals(MachineDetection.HANGUP, ncco2.getMachineDetection());
        assertEquals("GET", ncco2.getEventMethod());
        assertArrayEquals(new String[]{"https://api.example.com/event"}, ncco2.getEventUrl());
        assertEquals("447700900723", ncco2.getFrom());
        assertEquals(12, (int) ncco2.getLimit());
        assertEquals(1000, (int) ncco2.getTimeout());
    }
}
