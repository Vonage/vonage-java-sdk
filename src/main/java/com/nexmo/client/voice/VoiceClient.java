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
import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.voice.endpoints.CallsEndpoint;
import com.nexmo.client.voice.endpoints.DtmfEndpoint;
import com.nexmo.client.voice.endpoints.StreamsEndpoint;
import com.nexmo.client.voice.endpoints.TalkEndpoint;

import java.io.IOException;

/**
 * A client for talking to the Nexmo Voice API. The standard way to obtain an instance of this class is to use
 * {@link NexmoClient#getVoiceClient()}.
 */
public class VoiceClient extends AbstractClient {
    protected final CallsEndpoint calls;
    protected final StreamsEndpoint streams;
    protected final TalkEndpoint talk;
    protected final DtmfEndpoint dtmf;
    private String baseUri;

    /**
     * Constructor.
     *
     * @param httpWrapper (required) shared HTTP wrapper object used for making REST calls.
     */
    public VoiceClient(HttpWrapper httpWrapper) {
        super(httpWrapper);

        calls = new CallsEndpoint(httpWrapper);
        streams = new StreamsEndpoint(httpWrapper);
        talk = new TalkEndpoint(httpWrapper);
        dtmf = new DtmfEndpoint(httpWrapper);
    }

    public VoiceClient(HttpWrapper httpWrapper, String baseUri) {
        super(httpWrapper);
        this.baseUri = baseUri;

        calls = new CallsEndpoint(httpWrapper, baseUri);
        streams = new StreamsEndpoint(httpWrapper, baseUri);
        talk = new TalkEndpoint(httpWrapper, baseUri);
        dtmf = new DtmfEndpoint(httpWrapper, baseUri);
    }

    /**
     * Begin a call to a phone number.
     *
     * @param callRequest Describing the call to be made.
     * @return A CallEvent describing the initial state of the call, containing the <tt>uuid</tt> required to
     * interact with the ongoing phone call.
     * @throws IOException          if a network error occurred contacting the Nexmo Voice API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public CallEvent createCall(Call callRequest) throws IOException, NexmoClientException {
        return calls.post(callRequest);
    }

    /**
     * Obtain the first page of CallInfo objects, representing the most recent calls initiated by
     * {@link #createCall(Call)}.
     *
     * @return A CallInfoPage representing the response from the Nexmo Voice API.
     * @throws IOException          if a network error occurred contacting the Nexmo Voice API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public CallInfoPage listCalls() throws IOException, NexmoClientException {
        return this.listCalls(null);
    }

    /**
     * Obtain the first page of CallInfo objects matching the query described by <tt>filter</tt>, representing the most
     * recent calls initiated by {@link #createCall(Call)}.
     *
     * @param filter (optional) A filter describing which calls to be listed.
     * @return A CallInfoPage representing the response from the Nexmo Voice API.
     * @throws IOException          if a network error occurred contacting the Nexmo Voice API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public CallInfoPage listCalls(CallsFilter filter) throws IOException, NexmoClientException {
        return calls.get(filter);
    }

    /**
     * Look up the status of a single call initiated by {@link #createCall(Call)}.
     *
     * @param uuid (required) The UUID of the call, obtained from the object returned by {@link #createCall(Call)}.
     *             This value can be obtained with {@link CallEvent#getUuid()}
     * @return A CallInfo object, representing the response from the Nexmo Voice API.
     * @throws IOException          if a network error occurred contacting the Nexmo Voice API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public CallInfo getCallDetails(String uuid) throws IOException, NexmoClientException {
        return calls.get(uuid);
    }

    /**
     * Send DTMF codes to an ongoing call.
     *
     * @param uuid   (required) The UUID of the call, obtained from the object returned by {@link #createCall(Call)}.
     *               This value can be obtained with {@link CallEvent#getUuid()}
     * @param digits (required) A string specifying the digits to be sent to the call. Valid characters are the digits
     *               <tt>1-9</tt>, <tt>#</tt>, <tt>*</tt>, with the special character <tt>p</tt> indicating a short
     *               pause between tones.
     * @return A CallInfo object, representing the response from the Nexmo Voice API.
     * @throws IOException          if a network error occurred contacting the Nexmo Voice API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public DtmfResponse sendDtmf(String uuid, String digits) throws IOException, NexmoClientException {
        return dtmf.put(uuid, digits);
    }

    /**
     * Hang up a call.
     * <p>
     * In future, further operations will be possible. At the moment, the only valid value for <tt>action</tt> is
     * hangup.
     *
     * @param uuid   (required) The UUID of the call, obtained from the object returned by {@link #createCall(Call)}.
     *               This value can be obtained with {@link CallEvent#getUuid()}
     * @param action The word "hangup"
     * @return A CallInfo object, representing the response from the Nexmo Voice API.
     * @throws IOException          if a network error occurred contacting the Nexmo Voice API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public ModifyCallResponse modifyCall(String uuid, String action) throws IOException, NexmoClientException {
        return calls.put(uuid, action);
    }

    /**
     * Stream audio to an ongoing call.
     *
     * @param uuid      The UUID of the call, obtained from the object returned by {@link #createCall(Call)}. This value can
     *                  be obtained with {@link CallEvent#getUuid()}
     * @param streamUrl A URL of an audio file in MP3 or 16-bit WAV format, to be streamed to the call.
     * @param loop      The number of times to repeat the audio. The default value is <tt>1</tt>, or you can use
     *                  <tt>0</tt> to indicate that the audio should be repeated indefinitely.
     * @return The data returned from the Voice API
     * @throws IOException          if a network error occurred contacting the Nexmo Voice API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public StreamResponse startStream(String uuid, String streamUrl, int loop) throws IOException, NexmoClientException {
        return streams.put(new StreamRequest(uuid, streamUrl, loop));
    }

    /**
     * Stream audio to an ongoing call.
     *
     * @param uuid      The UUID of the call, obtained from the object returned by {@link #createCall(Call)}. This value can
     *                  be obtained with {@link CallEvent#getUuid()}
     * @param streamUrl A URL of an audio file in MP3 or 16-bit WAV format, to be streamed to the call.
     * @return The data returned from the Voice API.
     * @throws IOException          if a network error occurred contacting the Nexmo Voice API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public StreamResponse startStream(String uuid, String streamUrl) throws IOException, NexmoClientException {
        return streams.put(new StreamRequest(uuid, streamUrl, 1));
    }

    /**
     * Stop the audio being streamed into a call.
     *
     * @param uuid The UUID of the call, obtained from the object returned by {@link #createCall(Call)}. This value can
     *             be obtained with {@link CallEvent#getUuid()}
     * @return The data returned from the Voice API
     * @throws IOException          if a network error occurred contacting the Nexmo Voice API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public StreamResponse stopStream(String uuid) throws IOException, NexmoClientException {
        return streams.delete(uuid);
    }

    /**
     * Send a synthesized speech message to an ongoing call.
     * <p>
     * The message will only play once, spoken with the default voice of Kimberly.
     *
     * @param uuid The UUID of the call, obtained from the object returned by {@link #createCall(Call)}. This value can
     *             be obtained with {@link CallEvent#getUuid()}
     * @param text The message to be spoken to the call participants.
     * @return The data returned from the Voice API.
     * @throws IOException          if a network error occurred contacting the Nexmo Voice API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public TalkResponse startTalk(String uuid, String text) throws IOException, NexmoClientException {
        return talk.put(new TalkRequest(uuid, text));
    }

    /**
     * Send a synthesized speech message to an ongoing call.
     *
     * @param uuid      The UUID of the call, obtained from the object returned by {@link #createCall(Call)}. This value can
     *                  be obtained with {@link CallEvent#getUuid()}
     * @param text      The message to be spoken to the call participants.
     * @param voiceName The voice to be used to speak the message.
     * @return The data returned from the Voice API.
     * @throws IOException          if a network error occurred contacting the Nexmo Voice API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public TalkResponse startTalk(String uuid, String text, VoiceName voiceName) throws IOException, NexmoClientException {
        return talk.put(new TalkRequest(uuid, text, voiceName));
    }

    /**
     * Send a synthesized speech message to an ongoing call.
     * <p>
     * The message will be spoken with the default voice of Kimberly.
     *
     * @param uuid The UUID of the call, obtained from the object returned by {@link #createCall(Call)}. This value can
     *             be obtained with {@link CallEvent#getUuid()}
     * @param text The message to be spoken to the call participants.
     * @param loop The number of times to repeat the message. The default value is <tt>1</tt>, or you can use
     *             <tt>0</tt> to indicate that the message should be repeated indefinitely.
     * @return The data returned from the Voice API.
     * @throws IOException          if a network error occurred contacting the Nexmo Voice API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public TalkResponse startTalk(String uuid, String text, int loop) throws IOException, NexmoClientException {
        return talk.put(new TalkRequest(uuid, text, loop));
    }

    /**
     * Send a synthesized speech message to an ongoing call.
     *
     * @param uuid      The UUID of the call, obtained from the object returned by {@link #createCall(Call)}. This value can
     *                  be obtained with {@link CallEvent#getUuid()}
     * @param text      The message to be spoken to the call participants.
     * @param voiceName The voice to be used to speak the message.
     * @param loop      The number of times to repeat the message. The default value is <tt>1</tt>, or you can use
     *                  <tt>0</tt> to indicate that the message should be repeated indefinitely.
     * @return The data returned from the Voice API.
     * @throws IOException          if a network error occurred contacting the Nexmo Voice API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public TalkResponse startTalk(String uuid, String text, VoiceName voiceName, int loop) throws IOException, NexmoClientException {
        return talk.put(new TalkRequest(uuid, text, voiceName, loop));
    }

    /**
     * Stop the message being spoken into a call.
     *
     * @param uuid The UUID of the call, obtained from the object returned by {@link #createCall(Call)}. This value can
     *             be obtained with {@link CallEvent#getUuid()}
     * @return The data returned from the Voice API
     * @throws IOException          if a network error occurred contacting the Nexmo Voice API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public TalkResponse stopTalk(String uuid) throws IOException, NexmoClientException {
        return talk.delete(uuid);
    }

    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;

        this.calls.setBaseUri(baseUri);
        this.streams.setBaseUri(baseUri);
        this.talk.setBaseUri(baseUri);
        this.dtmf.setBaseUri(baseUri);
    }
}
