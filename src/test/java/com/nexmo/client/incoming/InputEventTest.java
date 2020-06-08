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
package com.nexmo.client.incoming;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InputEventTest {

    @Test
    public void testDeserializeInputEvent() {

        String inputJsonStr = getInputEventJsonString().toString();

        InputEvent<DtmfOutput> inputEvent = InputEvent.fromJson(inputJsonStr);
        assertTrue(inputEvent.getDtmf().getTimedOut());
        assertEquals("1234", inputEvent.getDtmf().getDigits());

        assertEquals(SpeechOutput.TimeoutReason.END_ON_SILENCE_TIMEOUT, inputEvent.getSpeech().getTimeoutReason());
        assertTrue(inputEvent.getSpeech().getResults().stream().anyMatch(result -> result.getConfidence().equals("0.9405097")));
        assertTrue(inputEvent.getSpeech().getResults().stream().anyMatch(result -> result.getText().equals("Sales")));
        assertTrue(inputEvent.getSpeech().getResults().stream().anyMatch(result -> result.getConfidence().equals("0.70543784")));
        assertTrue(inputEvent.getSpeech().getResults().stream().anyMatch(result -> result.getText().equals("Sails")));

        assertEquals("aaaaaaaa-bbbb-cccc-dddd-0123456789ab", inputEvent.getUuid());
        assertEquals("bbbbbbbb-cccc-dddd-eeee-0123456789ab", inputEvent.getConversationUuid());

        Calendar calendar = new GregorianCalendar(2020, Calendar.JUNE, 11, 10, 0, 0);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));

        assertEquals(calendar.getTime(), inputEvent.getTimestamp());
    }

    //At the time of this test, to use dtmf without speech, you will get a response that contains the speech object
    //with an error property, even though it is technically not an error, this should be changed when ASR goes to GA
    @Test
    public void testDeserializeInputEventSpeechError() {
        String expectedMessage = "Speech was not enabled";
        ObjectNode jsonNode = getInputEventJsonString();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode speechNode = mapper.createObjectNode();
        speechNode.put("error", expectedMessage);
        jsonNode.set("speech", speechNode);

        InputEvent<DtmfOutput> inputEvent = InputEvent.fromJson(jsonNode.toString());
        String actualMessage = inputEvent.getSpeech().getError();

        assertEquals(expectedMessage, actualMessage);

    }

    @Test
    public void testDeserializeInputEvent_Deprecated() {
        String json = "{\n" + "  \"uuid\": \"aaaaaaaa-bbbb-cccc-dddd-0123456789ab\",\n"
                + "  \"conversation_uuid\": \"bbbbbbbb-cccc-dddd-eeee-0123456789ab\",\n" + "  \"timed_out\": true,\n"
                + "  \"dtmf\": \"1234\",\n" + "  \"timestamp\": \"2020-01-01T14:00:00.000Z\"\n" + "}";

        InputEvent inputEvent = InputEvent.fromJson(json);
        assertEquals("aaaaaaaa-bbbb-cccc-dddd-0123456789ab", inputEvent.getUuid());
        assertEquals("bbbbbbbb-cccc-dddd-eeee-0123456789ab", inputEvent.getConversationUuid());
        assertTrue(inputEvent.isTimedOut());
        assertEquals("1234", inputEvent.getDtmf());

        Calendar calendar = new GregorianCalendar(2020, Calendar.JANUARY, 1, 14, 0, 0);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));

        assertEquals(calendar.getTime(), inputEvent.getTimestamp());
    }

    private ObjectNode getInputEventJsonString() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode inputEventNode = mapper.createObjectNode();


        ArrayNode resultsNode = mapper.createArrayNode();

        ObjectNode resultNode1 = mapper.createObjectNode();
        resultNode1.put("confidence", "0.9405097");
        resultNode1.put("text", "Sales");

        ObjectNode resultNode2 = mapper.createObjectNode();
        resultNode2.put("confidence", "0.70543784");
        resultNode2.put("text", "Sails");
        resultsNode.add(resultNode1);
        resultsNode.add(resultNode2);

        ObjectNode speechNode = mapper.createObjectNode();
        speechNode.put("timeout_reason", "end_on_silence_timeout");
        speechNode.set("results", resultsNode);
        inputEventNode.set("speech", speechNode);

        ObjectNode dtmfNode = mapper.createObjectNode();
        dtmfNode.put("digits", "1234");
        dtmfNode.put("timed_out", true);
        inputEventNode.set("dtmf", dtmfNode);

        inputEventNode.put("uuid", "aaaaaaaa-bbbb-cccc-dddd-0123456789ab");
        inputEventNode.put("conversation_uuid", "bbbbbbbb-cccc-dddd-eeee-0123456789ab");
        inputEventNode.put("timestamp", "2020-06-11T10:00:00.000Z");

        return inputEventNode;
    }
}
