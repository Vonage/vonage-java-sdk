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
package com.vonage.client.incoming;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InputEventTest {

    private static final Log LOG = LogFactory.getLog(InputEventTest.class);
    @Test
    public void testDeserializeInputEvent() {
        String inputJsonStr = getInputEventJsonString().toString();
        LOG.debug(inputJsonStr);

        InputEvent<DtmfResult> inputEvent = InputEvent.fromJson(inputJsonStr);
        assertEquals("aaaaaaaa-bbbb-cccc-dddd-0123456789ab", inputEvent.getUuid());
        assertEquals("bbbbbbbb-cccc-dddd-eeee-0123456789ab", inputEvent.getConversationUuid());
        assertTrue(inputEvent.getDtmf().isTimedOut());
        assertEquals("1234", inputEvent.getDtmf().getDigits());

        Calendar calendar = new GregorianCalendar(2020, Calendar.JUNE, 11, 10, 0, 0);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        assertEquals(calendar.getTime(), inputEvent.getTimestamp());

        assertEquals("447700900000", inputEvent.getTo());
        assertEquals("447700779000", inputEvent.getFrom());

        assertEquals(SpeechResults.TimeoutReason.END_ON_SILENCE_TIMEOUT, inputEvent.getSpeech().getTimeoutReason());
        assertTrue(inputEvent.getSpeech().getResults().stream().anyMatch(result -> result.getConfidence().equals("0.9405097")));
        assertTrue(inputEvent.getSpeech().getResults().stream().anyMatch(result -> result.getText().equals("sales")));
        assertTrue(inputEvent.getSpeech().getResults().stream().anyMatch(result -> result.getConfidence().equals("0.70543784")));
        assertTrue(inputEvent.getSpeech().getResults().stream().anyMatch(result -> result.getText().equals("sails")));
    }

    @Test
    public void testDeserializeInputEventSpeechError() {
        String expectedMessage = "ERR1: Failed to analyze audio";
        ObjectNode jsonNode = getInputEventJsonString();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode speechNode = mapper.createObjectNode();
        speechNode.put("error", expectedMessage);
        jsonNode.set("speech", speechNode);

        InputEvent<DtmfResult> inputEvent = InputEvent.fromJson(jsonNode.toString());
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
        resultNode1.put("text", "sales");

        ObjectNode resultNode2 = mapper.createObjectNode();
        resultNode2.put("confidence", "0.70543784");
        resultNode2.put("text", "sails");
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
        inputEventNode.put("to", "447700900000");
        inputEventNode.put("from", "447700779000");

        return inputEventNode;
    }
}
