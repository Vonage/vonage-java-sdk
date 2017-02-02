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


import com.nexmo.client.AbstractClient;
import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.voice.endpoints.CallsEndpoint;
import com.nexmo.client.voice.endpoints.DtmfEndpoint;
import com.nexmo.client.voice.endpoints.StreamsEndpoint;
import com.nexmo.client.voice.endpoints.TalkEndpoint;

import java.io.IOException;

public class VoiceClient extends AbstractClient {
    public final CallsEndpoint calls;
    public final StreamsEndpoint streams;
    public final TalkEndpoint talk;
    public final DtmfEndpoint dtmf;

    public VoiceClient(HttpWrapper httpWrapper) {
        super(httpWrapper);

        calls = new CallsEndpoint(httpWrapper);
        streams = new StreamsEndpoint(httpWrapper);
        talk = new TalkEndpoint(httpWrapper);
        dtmf = new DtmfEndpoint(httpWrapper);
    }

    public CallEvent createCall(Call callRequest) throws IOException, NexmoClientException {
        return calls.post(callRequest);
    }

    public CallInfoPage listCalls() throws IOException, NexmoClientException {
        return this.listCalls(null);
    }

    public CallInfoPage listCalls(CallsFilter filter) throws IOException, NexmoClientException {
        return calls.get(filter);
    }

    public CallInfo getCallDetails(String uuid) throws IOException, NexmoClientException {
        return calls.get(uuid);
    }

    public DtmfResponse sendDtmf(String uuid, String digits) throws IOException, NexmoClientException {
        return dtmf.put(uuid, digits);
    }

    public CallInfo modifyCall(String uuid, String action) throws IOException, NexmoClientException {
        return calls.put(uuid, action);
    }

    public StreamResponse startStream(String uuid, String streamUrl, int loop) throws IOException, NexmoClientException {
        return streams.put(new StreamRequest(uuid, streamUrl, loop));
    }

    /**
     * Start a stream that only plays once.
     */
    public StreamResponse startStream(String uuid, String streamUrl) throws IOException, NexmoClientException {
        return streams.put(new StreamRequest(uuid, streamUrl, 1));
    }

    public StreamResponse stopStream(String uuid) throws IOException, NexmoClientException {
        return streams.delete(uuid);
    }

    public TalkResponse startTalk(String uuid, String text, VoiceName voiceName, int loop) throws IOException, NexmoClientException {
        return talk.put(new TalkRequest(uuid, text, voiceName, loop));
    }

    /**
     * Send a synthesized speech message that only plays once
     */
    public TalkResponse startTalk(String uuid, String text, VoiceName voiceName) throws IOException, NexmoClientException {
        return talk.put(new TalkRequest(uuid, text, voiceName));
    }

    /**
     * Send a synthesized speech message with the default voice of Kimberly
     */
    public TalkResponse startTalk(String uuid, String text, int loop) throws IOException, NexmoClientException {
        return talk.put(new TalkRequest(uuid, text, loop));
    }

    /**
     * Send a synthesized speech message that only plays once with the default voice of Kimberly
     */
    public TalkResponse startTalk(String uuid, String text) throws IOException, NexmoClientException {
        return talk.put(new TalkRequest(uuid, text));
    }

    public TalkResponse stopTalk(String uuid) throws IOException, NexmoClientException {
        return talk.delete(uuid);
    }
}