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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.vonage.client.Jsonable;
import com.vonage.client.VonageUnexpectedException;
import com.vonage.client.voice.TextToSpeechLanguage;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class NccoTest {

    @Test
    public void testUnableToSerializeJson() throws Exception {
        ObjectWriter writer = mock(ObjectWriter.class);
        when(writer.writeValueAsString(any())).thenThrow(JsonProcessingException.class);
        Ncco ncco = new Ncco(writer);
        assertThrows(VonageUnexpectedException.class, ncco::toJson);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testItemListImmutableWhenEmpty() {
        Ncco ncco = new Ncco();
        assertThrows(UnsupportedOperationException.class, () ->
                ((Collection<Action>) ncco.getActions()).add(TalkAction.builder("Test Message").build())
        );
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testItemListImmutableWhenNotEmpty() {
        TalkAction.Builder builder = TalkAction.builder("Hello!");
        TalkAction intro = builder.build();
        TalkAction outro = builder.text("Thanks for calling!").build();
        Ncco ncco = new Ncco(intro);
        assertThrows(UnsupportedOperationException.class, () ->
                ((Collection<Action>) ncco.getActions()).add(outro)
        );
    }

    @Test
    public void testGetActions() {
        TalkAction.Builder builder = TalkAction.builder("Test Message");
        TalkAction actionOne = builder.build();
        TalkAction actionTwo = builder.text("Another message").build();
        Collection<Action> actions = Arrays.asList(actionOne, actionTwo);
        Ncco ncco = new Ncco(actions);
        assertSame(actions, ncco.getActions());
    }

    @Test
    public void testSerializeMultipleActions() {
        TalkAction talk = TalkAction.builder("Test message").language(TextToSpeechLanguage.BASQUE).build();
        var dtmfSettings = DtmfSettings.builder().maxDigits(5).build();
        InputAction input = InputAction.builder().dtmf(dtmfSettings).type(Collections.singletonList("dtmf")).build();
        RecordAction record = RecordAction.builder().beepStart(true).build();
        ConnectAction connect = ConnectAction.builder(PhoneEndpoint.builder("15554441234").build()).build();

        String expectedJson = "[{\"text\":\"Test message\",\"language\":\"eu-ES\",\"action\":\"talk\"}," +
                "{\"type\":[\"dtmf\"],\"dtmf\":{\"maxDigits\":5},\"action\":\"input\"}," +
                "{\"beepStart\":true,\"action\":\"record\"}," +
                "{\"endpoint\":[{\"number\":\"15554441234\",\"type\":\"phone\"}],\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(talk, input, record, connect).toJson());

        Ncco ncco = new Ncco(talk, input, record, connect);
        assertEquals(expectedJson, ncco.toJson());
    }

    @Test
    public void testObjectWriterAndActionConstruction() {
        ObjectWriter writer = Jsonable.createDefaultObjectMapper().writer().withDefaultPrettyPrinter();
        TalkAction talk = TalkAction.builder("Test message").build();

        Ncco ncco = new Ncco(writer, talk);

        // Json w/ pretty print
        String expectedJson = "[ {" + System.lineSeparator() + "  \"text\" : \"Test message\"," + System.lineSeparator()
            + "  \"action\" : \"talk\"" + System.lineSeparator() + "} ]";
        assertEquals(expectedJson, ncco.toJson());
    }
}
