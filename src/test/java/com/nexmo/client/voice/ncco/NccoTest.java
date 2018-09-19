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
package com.nexmo.client.voice.ncco;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nexmo.client.NexmoUnexpectedException;
import com.nexmo.client.voice.VoiceName;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NccoTest {
    @Test(expected = NexmoUnexpectedException.class)
    public void testUnableToSerializeJson() throws Exception {
        ObjectWriter writer = mock(ObjectWriter.class);
        when(writer.writeValueAsString(any())).thenThrow(JsonProcessingException.class);

        Ncco ncco = new Ncco(writer);
        ncco.toJson();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testItemListImmutableWhenEmpty() {
        Ncco ncco = new Ncco();
        ncco.getActions().add(new TalkAction.Builder("Test Message").build());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testItemListImmutableWhenNotEmpty() {
        TalkAction.Builder builder = new TalkAction.Builder("Hello!");
        TalkAction intro = builder.build();
        TalkAction outro = builder.text("Thanks for calling!").build();

        Ncco ncco = new Ncco(intro);
        ncco.getActions().add(outro);
    }

    @Test
    public void testGetActions() {
        TalkAction.Builder builder = new TalkAction.Builder("Test Message");

        TalkAction actionOne = builder.build();
        TalkAction actionTwo = builder.text("Another message").build();

        Collection<Action> actions = Arrays.<Action>asList(actionOne, actionTwo);

        Ncco ncco = new Ncco(actions);

        assertSame(actions, ncco.getActions());
    }

    @Test
    public void testSerializeMultipleActions() {
        TalkAction talk = new TalkAction.Builder("Test message").voiceName(VoiceName.JOEY).build();
        InputAction input = new InputAction.Builder().maxDigits(5).build();
        RecordAction record = new RecordAction.Builder().beepStart(true).build();
        ConnectAction connect = new ConnectAction.Builder(new PhoneEndpoint.Builder("15554441234").build()).build();

        String expectedJson = "[{\"text\":\"Test message\",\"voiceName\":\"Joey\",\"action\":\"talk\"},{\"maxDigits\":5,\"action\":\"input\"},{\"beepStart\":true,\"action\":\"record\"},{\"endpoint\":[{\"number\":\"15554441234\",\"type\":\"phone\"}],\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(talk, input, record, connect).toJson());

        Ncco ncco = new Ncco(talk, input, record, connect);
        assertEquals(expectedJson, ncco.toJson());
    }

    @Test
    public void testObjectWriterAndActionConstruction() {
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        TalkAction talk = new TalkAction.Builder("Test message").build();

        Ncco ncco = new Ncco(writer, talk);

        // Json w/ pretty print
        String expectedJson = "[ {\n" + "  \"text\" : \"Test message\",\n" + "  \"action\" : \"talk\"\n" + "} ]";
        assertEquals(expectedJson, ncco.toJson());
    }
}
