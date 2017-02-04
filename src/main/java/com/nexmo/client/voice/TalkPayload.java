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
package com.nexmo.client.voice;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The JSON payload that will be sent in a {@link TalkRequest}.
 * <p>
 * {@code text}: A string of up to 1500 characters containing the message to be synthesized
 * in the Call or Conversation. Each comma in text adds a short pause to the synthesized speech.
 * {@link VoiceName}: The name of the voice used to deliver {@code text}.
 * {@code loop}: The number of times the audio file at stream_url is repeated before the stream ends. Set to 0 to loop infinitely.
 */

public class TalkPayload {
    private String text;
    private VoiceName voiceName;
    private int loop;

    public TalkPayload(String text, VoiceName voiceName, int loop) {
        this.text = text;
        this.voiceName = voiceName;
        this.loop = loop;
    }

    public String getText() {
        return text;
    }

    @JsonProperty("voice_name")
    public VoiceName getVoiceName() {
        return voiceName;
    }

    public int getLoop() {
        return loop;
    }
}
