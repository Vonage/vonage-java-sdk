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
package com.vonage.client.voice;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class CallModifierTest {
    private CallModifier callModifier;

    @Before
    public void setUp() throws Exception {
        callModifier = new CallModifier("abc-123", ModifyCallAction.HANGUP);
    }

    @Test
    public void getUuid() throws Exception {
        assertEquals("abc-123", callModifier.getUuid());
    }

    @Test
    public void getAction() throws Exception {
        assertEquals(ModifyCallAction.HANGUP, callModifier.getAction());
    }

    @Test
    public void toJson() throws Exception {
        String jsonString = "{\"action\":\"hangup\"}";
        assertEquals(jsonString, callModifier.toJson());
    }
}
