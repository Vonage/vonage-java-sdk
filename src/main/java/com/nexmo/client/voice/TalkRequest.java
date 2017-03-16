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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.NexmoUnexpectedException;

/**
 * The request object to send synthesized audio.
 * <p>
 * Contains the {@code uuid} of the {@link Call} and the {@link TalkPayload} to be sent in the request.
 */

public class TalkRequest {
    private TalkPayload talkPayload;
    private String uuid;

    public TalkRequest(String uuid, String text, VoiceName voiceName, int loop) {
        this.talkPayload = new TalkPayload(text, voiceName, loop);
        this.uuid = uuid;
    }

    public TalkRequest(String uuid, String text, VoiceName voiceName) {
        this(uuid, text, voiceName, 1);
    }

    public TalkRequest(String uuid, String text, int loop) {
        this(uuid, text, VoiceName.KIMBERLY, loop);
    }

    public TalkRequest(String uuid, String text) {
        this(uuid, text, VoiceName.KIMBERLY, 1);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this.talkPayload);
        } catch (JsonProcessingException jpe) {
            throw new NexmoUnexpectedException("Failed to produce json from TalkRequest object.", jpe);
        }
    }
}
