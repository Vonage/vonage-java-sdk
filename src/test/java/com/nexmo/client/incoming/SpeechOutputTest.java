package com.nexmo.client.incoming;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class SpeechOutputTest {

    ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();

    }

    @Test
    public void TimeoutReasonDeserializationTest() throws IOException {
        SpeechOutput.TimeoutReason timeoutReason = mapper.readValue("\"start_timeout\"", SpeechOutput.TimeoutReason.class);
        assertEquals("Value should be START_TIMEOUT", SpeechOutput.TimeoutReason.START_TIMEOUT, timeoutReason);
    }

    @Test
    public void TimeoutReasonSerializationTest() throws JsonProcessingException {
        String expectedJsonStr = "\"start_timeout\"";
        String actualJsonStr = mapper.writeValueAsString(SpeechOutput.TimeoutReason.START_TIMEOUT);

        assertEquals("Value should be start_timeout", expectedJsonStr, actualJsonStr);

    }

    @Test
    public void ErrorPropertyTest() throws IOException {
        String expectedMessage = "Speech not enabled";
        SpeechOutput speechOutput = mapper.readValue("{\"error\": \"Speech not enabled\"}", SpeechOutput.class);
        assertEquals("Should contain speech message",expectedMessage, speechOutput.getError());
        assertNull(speechOutput.getResults());
        assertNull(speechOutput.getTimeoutReason());
    }
}