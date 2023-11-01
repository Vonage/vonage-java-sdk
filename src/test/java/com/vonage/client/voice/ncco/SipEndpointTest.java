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
package com.vonage.client.voice.ncco;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Collections;

public class SipEndpointTest {

    @Test
    public void testAllFields() {
        SipEndpoint endpoint = SipEndpoint.builder("")
                .uri("sip:test@example.com")
                .headers(Collections.singletonMap("k1", "v1"))
                .build();

        ConnectAction connect = ConnectAction.builder(endpoint).build();

        String expectedJson = "[{\"endpoint\":[{\"uri\":\"sip:test@example.com\"," +
                "\"headers\":{\"k1\":\"v1\"},\"type\":\"sip\"}],\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }
}
