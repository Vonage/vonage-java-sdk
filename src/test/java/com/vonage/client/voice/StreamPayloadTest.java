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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class StreamPayloadTest {
    private StreamPayload payload;

    @BeforeEach
    public void setUp() throws Exception {
        payload = new StreamPayload("https://nexmo-community.github.io/ncco-examples/assets/voice_api_audio_streaming.mp3", 2, 0.4);
    }

    @Test
    public void getStreamUrl() throws Exception {
        assertArrayEquals(new String[]{"https://nexmo-community.github.io/ncco-examples/assets/voice_api_audio_streaming.mp3"}, payload.getStreamUrl());
    }

    @Test
    public void getLoop() throws Exception {
        assertEquals(2, payload.getLoop().intValue());
    }

    @Test
    public void getLevel() throws Exception {
        assertEquals(0.4, payload.getLevel(), 0.01);
    }

    @Test
    public void toJson() throws Exception {
        String jsonString = "{\"stream_url\":[" +
                "\"https://nexmo-community.github.io/ncco-examples/assets/voice_api_audio_streaming.mp3\"" +
                "],\"loop\":2,\"level\":0.4}";
        assertEquals(jsonString, payload.toJson());
    }

}
