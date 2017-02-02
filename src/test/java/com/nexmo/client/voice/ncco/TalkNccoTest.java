/*
 * Copyright (c) 2011-2017 Nexmo Inc
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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TalkNccoTest {
    @Test
    public void testToJson() throws Exception {
        assertEquals("{\"text\":\"Talk to me\",\"action\":\"talk\"}", new TalkNcco("Talk to me").toJson());
    }

    @Test
    public void testJson() throws Exception {
        String json;
        {
            TalkNcco ncco = new TalkNcco("Talk to me");
            ncco.setText("Don't talk to me");
            ncco.setBargeIn(true);
            ncco.setLoop(3);
            ncco.setVoiceName("Larry");

            json = ncco.toJson();
        }

        TalkNcco ncco = new ObjectMapper().readValue(json, TalkNcco.class);
        assertEquals("Don't talk to me", ncco.getText());
        assertEquals(true, ncco.getBargeIn());
        assertEquals(3, (int) ncco.getLoop());
        assertEquals("Larry", ncco.getVoiceName());

    }
}