/*
 *   Copyright 2025 Vonage
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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class VbcEndpointTest {

    @Test
    public void testVbcEndpoint() {
        String ext = "123";
        VbcEndpoint endpoint = VbcEndpoint.builder("abcd").extension(ext).build();
        ConnectAction connect = ConnectAction.builder(endpoint).build();
        String expectedJson = "[{\"endpoint\":[{\"extension\":\"%s\",\"type\":\"vbc\"}],\"action\":\"connect\"}]";
        assertEquals(String.format(expectedJson, ext), new Ncco(connect).toJson());
    }
}
