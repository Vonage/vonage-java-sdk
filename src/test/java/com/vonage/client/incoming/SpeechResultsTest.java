/*
 * Copyright 2023 Vonage
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vonage.client.incoming;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.*;
import java.io.IOException;

public class SpeechResultsTest {

    ObjectMapper mapper;

    @BeforeEach
    public void setUp() throws Exception {
        mapper = new ObjectMapper();

    }

    @Test
    public void TimeoutReasonDeserializationTest() throws IOException {
        SpeechResults.TimeoutReason timeoutReason = mapper.readValue("\"start_timeout\"", SpeechResults.TimeoutReason.class);
        assertEquals(SpeechResults.TimeoutReason.START_TIMEOUT, timeoutReason, "Value should be START_TIMEOUT");
    }

    @Test
    public void TimeoutReasonSerializationTest() throws JsonProcessingException {
        String expectedJsonStr = "\"start_timeout\"";
        String actualJsonStr = mapper.writeValueAsString(SpeechResults.TimeoutReason.START_TIMEOUT);

        assertEquals(expectedJsonStr, actualJsonStr, "Value should be start_timeout");

    }

    @Test
    public void ErrorPropertyTest() throws IOException {
        String expectedMessage = "ERR1: Failed to analyze audio";
        SpeechResults speechResults = mapper.readValue("{\"error\": \"ERR1: Failed to analyze audio\"}", SpeechResults.class);
        assertEquals(expectedMessage, speechResults.getError(), "Should contain speech error message");
        assertNull(speechResults.getResults());
        assertNull(speechResults.getTimeoutReason());
    }

}