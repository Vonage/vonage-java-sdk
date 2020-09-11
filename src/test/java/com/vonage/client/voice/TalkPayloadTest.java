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

package com.vonage.client.voice;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TalkPayloadTest {
    private TalkPayload payload;

    @Before
    public void setUp() throws Exception {
        payload = new TalkPayload("Hola Mundo!", VoiceName.CONCHITA, 0);
    }

    @Test
    public void getLoop() throws Exception {
        assertEquals(0, payload.getLoop());
    }

    @Test
    public void getText() throws Exception {
        assertEquals("Hola Mundo!", payload.getText());
    }

    @Test
    public void getVoiceName() throws Exception {
        assertEquals(VoiceName.CONCHITA, payload.getVoiceName());
    }

    @Test
    public void toJson() throws Exception {
        String jsonString = "{\"text\":\"Hola Mundo!\",\"loop\":0,\"voice_name\":\"Conchita\"}";
        assertEquals(jsonString, payload.toJson());
    }
}
