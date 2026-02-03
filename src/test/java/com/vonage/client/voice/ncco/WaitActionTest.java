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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import org.junit.jupiter.api.*;

public class WaitActionTest {

    @Test
    public void testBuilderMultipleInstances() {
        WaitAction.Builder builder = WaitAction.builder();
        assertNotSame(builder.build(), builder.build());
        
        assertNotSame(
                WaitAction.builder(0.5),
                WaitAction.builder(0.5)
        );
    }

    @Test
    public void testGetAction() {
        WaitAction wait = WaitAction.builder().build();
        assertEquals("wait", wait.getAction());
    }

    @Test
    public void testDefault() {
        WaitAction wait = WaitAction.builder().build();
        assertEquals("[{\"action\":\"wait\"}]", new Ncco(wait).toJson());
    }

    @Test
    public void testWithTimeout() {
        WaitAction wait = WaitAction.builder().timeout(0.5).build();
        assertEquals("[{\"timeout\":0.5,\"action\":\"wait\"}]", new Ncco(wait).toJson());
    }

    @Test
    public void testBuilderWithTimeout() {
        WaitAction wait = WaitAction.builder(0.5).build();
        assertEquals("[{\"timeout\":0.5,\"action\":\"wait\"}]", new Ncco(wait).toJson());
    }

    @Test
    public void testMinimumTimeout() {
        WaitAction wait = WaitAction.builder().timeout(0.1).build();
        assertEquals("[{\"timeout\":0.1,\"action\":\"wait\"}]", new Ncco(wait).toJson());
    }

    @Test
    public void testMaximumTimeout() {
        WaitAction wait = WaitAction.builder().timeout(7200.0).build();
        assertEquals("[{\"timeout\":7200.0,\"action\":\"wait\"}]", new Ncco(wait).toJson());
    }

    @Test
    public void testIntegerTimeout() {
        WaitAction wait = WaitAction.builder().timeout(10).build();
        assertEquals("[{\"timeout\":10.0,\"action\":\"wait\"}]", new Ncco(wait).toJson());
    }

    @Test
    public void testDecimalTimeout() {
        WaitAction wait = WaitAction.builder().timeout(2.5).build();
        assertEquals("[{\"timeout\":2.5,\"action\":\"wait\"}]", new Ncco(wait).toJson());
    }

    @Test
    public void testExampleFromDocumentation() {
        // From the documentation example
        TalkAction talk1 = TalkAction.builder("Welcome to a Vonage moderated conference").build();
        WaitAction wait = WaitAction.builder(0.5).build();
        TalkAction talk2 = TalkAction.builder("We will connect you when an agent is available").build();
        
        Ncco ncco = new Ncco(talk1, wait, talk2);
        String json = ncco.toJson();
        
        // Verify wait action is properly serialized in the NCCO
        assertEquals(true, json.contains("\"action\":\"wait\""));
        assertEquals(true, json.contains("\"timeout\":0.5"));
    }
}
