package com.vonage.client.voice;/*
 * Copyright (c) 2020 Vonage
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

import com.vonage.client.voice.ncco.Ncco;
import com.vonage.client.voice.ncco.RecordAction;
import com.vonage.client.voice.ncco.TalkAction;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TransferCallPayloadTest {
    @Test
    public void transferJson() throws Exception {
        String expected = "{\"action\":\"transfer\",\"destination\":{\"type\":\"ncco\",\"url\":[\"https://example"
                + ".com/ncco\"]}}";
        String actual = CallModifier.transferCall("not-a-uuid", "https://example.com/ncco").toJson();
        assertEquals(expected, actual);
    }

    @Test
    public void testTransferWithInlineNcco() {
        String expected = "{\"action\":\"transfer\",\"destination\":{\"type\":\"ncco\",\"ncco\":[{\"text\":\"Hello from Vonage\",\"action\":\"talk\"},{\"action\":\"record\"},{\"text\":\"Thank you!\",\"action\":\"talk\"}]}}";
        String actual = CallModifier.transferCall("not-a-uuid", new Ncco(TalkAction.builder("Hello from Vonage").build(),
                RecordAction.builder().build(),
                TalkAction.builder("Thank you!").build())
        ).toJson();

        assertEquals(expected, actual);
    }

    @Test
    public void testTypeValueOf() throws Exception {
        assertEquals(TransferDestination.Type.NCCO, TransferDestination.Type.valueOf("NCCO"));
    }
}
