/*
 * Copyright (c) 2011-2018 Nexmo Inc
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

import com.nexmo.client.voice.MachineDetection;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ConnectWebSocketNccoTest {

    @Test
    public void getAction() throws Exception {
        ConnectWebSocketNcco ncco = new ConnectWebSocketNcco(new WebSocketEndpoint("wss://example.com",
                "content-type"
        ));
        assertEquals("connect", ncco.getAction());
    }

    @Test
    public void testToJsonWebSocketConstructor() throws Exception {
        assertEquals(
                "{\"endpoint\":[{\"uri\":\"wss://example.com\",\"type\":\"websocket\",\"content-type\":\"content-type\"}],\"action\":\"connect\"}",
                new ConnectWebSocketNcco(new WebSocketEndpoint("wss://example.com", "content-type")).toJson()
        );

        Map<String, String> headers = new HashMap<>();
        headers.put("key", "value");
        headers.put("key2", "value2");

        assertEquals(
                "{\"endpoint\":[{\"uri\":\"wss://example.com\",\"headers\":{\"key2\":\"value2\",\"key\":\"value\"},\"type\":\"websocket\",\"content-type\":\"content-type\"}],\"action\":\"connect\"}",
                new ConnectWebSocketNcco(new WebSocketEndpoint("wss://example.com", "content-type", headers)).toJson()
        );
    }

    @Test
    public void testToJsonValueConstructors() throws Exception {
        assertEquals(
                "{\"endpoint\":[{\"uri\":\"wss://example.com\",\"type\":\"websocket\",\"content-type\":\"content-type\"}],\"action\":\"connect\"}",
                new ConnectWebSocketNcco("wss://example.com", "content-type").toJson()
        );

        Map<String, String> headers = new HashMap<>();
        headers.put("key", "value");
        headers.put("key2", "value2");

        assertEquals(
                "{\"endpoint\":[{\"uri\":\"wss://example.com\",\"headers\":{\"key2\":\"value2\",\"key\":\"value\"},\"type\":\"websocket\",\"content-type\":\"content-type\"}],\"action\":\"connect\"}",
                new ConnectWebSocketNcco("wss://example.com", "content-type", headers).toJson()
        );
    }

    @Test
    public void testToJsonSettingDifferentWebSocketEndpoint() {
        ConnectWebSocketNcco ncco = new ConnectWebSocketNcco("wss://example.com", "content-type");
        WebSocketEndpoint newEndpoint = new WebSocketEndpoint("wss://two.example.net", "content-type-two");

        ncco.setEndpoint(newEndpoint);
        ncco.setFrom("test from");
        ncco.setEventMethod("GET");
        ncco.setEventUrl("test event url");
        ncco.setLimit(100);
        ncco.setMachineDetection(MachineDetection.CONTINUE);
        ncco.setTimeout(10);

        assertEquals(
                "{\"endpoint\":[{\"uri\":\"wss://two.example.net\",\"type\":\"websocket\",\"content-type\":\"content-type-two\"}],\"from\":\"test from\",\"timeout\":10,\"limit\":100,\"machineDetection\":\"continue\",\"eventMethod\":\"GET\",\"action\":\"connect\",\"eventUrl\":[\"test event url\"]}",
                ncco.toJson()
        );
    }
}

