package com.nexmo.client.voice;
/*
 * Copyright (c) 2011-2016 Nexmo Inc
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

import com.nexmo.client.AbstractClient;
import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.voice.endpoints.CallsEndpoint;
import com.nexmo.client.voice.endpoints.StreamsEndpoint;

import java.io.IOException;

public class NexmoVoiceClient extends AbstractClient {
    public final CallsEndpoint calls;
    public final StreamsEndpoint streams;
    public final TalkEndpoint talk;

    public NexmoVoiceClient(HttpWrapper httpWrapper) {
        super(httpWrapper);

        calls = new CallsEndpoint(httpWrapper);
        streams = new StreamsEndpoint(httpWrapper);
        talk = new TalkEndpoint(httpWrapper);
    }

    public CallEvent createCall(Call callRequest) throws IOException, NexmoClientException {
        return calls.post(callRequest);
    }

    public CallRecordPage listCalls() throws IOException, NexmoClientException {
        return this.listCalls(null);
    }

    public CallRecordPage listCalls(CallsFilter filter) throws IOException, NexmoClientException {
        return calls.get(filter);
    }

    public CallRecord getCallDetails(String uuid) throws IOException, NexmoClientException {
        return calls.get(uuid);
    }

    public NexmoResponse startStream(String streamUrl, boolean loop) throws IOException, NexmoClientException {
        return streams.put(new StreamRequest(streamUrl, loop));
    }

    //Send a stream that will not loop.
    public NexmoResponse startStream(String streamUrl) throws IOException, NexmoClientException {
        return streams.put(new StreamRequest(streamUrl, false));
    }

    public NexmoResponse stopStream() throws IOException, NexmoClientException {
        return streams.delete(null);
    }

    public NexmoResponse startTalk(String text, VoiceName voiceName, boolean loop) throws IOException, NexmoClientException {
        return talk.put(new TalkRequest(text, voiceName, loop));
    }
}
