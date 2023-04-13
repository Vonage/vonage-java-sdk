/*
 *   Copyright 2023 Vonage
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
package com.vonage.client.voice;

import com.vonage.client.HttpWrapper;
import com.vonage.client.VonageClient;
import com.vonage.client.VonageClientException;
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.voice.ncco.Ncco;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.UUID;

/**
 * A client for talking to the Vonage Voice API. The standard way to obtain an instance of this class is to use {@link
 * VonageClient#getVoiceClient()}.
 */
public class VoiceClient {
    final CreateCallEndpoint createCall;
    final ReadCallEndpoint readCall;
    final ListCallsEndpoint listCalls;
    final ModifyCallEndpoint modifyCall;
    final StartStreamEndpoint startStream;
    final StopStreamEndpoint stopStream;
    final StartTalkEndpoint startTalk;
    final StopTalkEndpoint stopTalk;
    final SendDtmfEndpoint sendDtmf;
    final DownloadRecordingEndpoint downloadRecording;

    /**
     * Constructor.
     *
     * @param httpWrapper (required) shared HTTP wrapper object used for making REST calls.
     */
    public VoiceClient(HttpWrapper httpWrapper) {
        createCall = new CreateCallEndpoint(httpWrapper);
        readCall = new ReadCallEndpoint(httpWrapper);
        listCalls = new ListCallsEndpoint(httpWrapper);
        modifyCall = new ModifyCallEndpoint(httpWrapper);
        startStream = new StartStreamEndpoint(httpWrapper);
        stopStream = new StopStreamEndpoint(httpWrapper);
        startTalk = new StartTalkEndpoint(httpWrapper);
        stopTalk = new StopTalkEndpoint(httpWrapper);
        sendDtmf = new SendDtmfEndpoint(httpWrapper);
        downloadRecording = new DownloadRecordingEndpoint(httpWrapper);
    }

    private String validateUuid(String uuid) {
        return UUID.fromString(Objects.requireNonNull(uuid, "UUID is required.")).toString();
    }

    private String validateUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("URL is required.");
        }
        try {
            new URI(url);
        }
        catch (URISyntaxException ex) {
            throw new IllegalArgumentException("Invalid URL provided.", ex);
        }
        return url;
    }

    /**
     * Begin a call to a phone number.
     *
     * @param callRequest Describing the call to be made.
     *
     * @return A CallEvent describing the initial state of the call, containing the {@code uuid} required to interact
     * with the ongoing phone call.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public CallEvent createCall(Call callRequest) throws VonageResponseParseException, VonageClientException {
        return createCall.execute(callRequest);
    }

    /**
     * Obtain the first page of CallInfo objects, representing the most recent calls initiated by {@link
     * #createCall(Call)}.
     *
     * @return A CallInfoPage representing the response from the Vonage Voice API.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public CallInfoPage listCalls() throws VonageResponseParseException, VonageClientException {
        return listCalls(null);
    }

    /**
     * Obtain the first page of CallInfo objects matching the query described by {@code filter}, representing the most
     * recent calls initiated by {@link #createCall(Call)}.
     *
     * @param filter (optional) A filter describing which calls to be listed.
     *
     * @return A CallInfoPage representing the response from the Vonage Voice API.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public CallInfoPage listCalls(CallsFilter filter) throws VonageResponseParseException, VonageClientException {
        return listCalls.execute(filter);
    }

    /**
     * Look up the status of a single call initiated by {@link #createCall(Call)}.
     *
     * @param uuid (required) The UUID of the call, obtained from the object returned by {@link #createCall(Call)}. This
     *             value can be obtained with {@link CallEvent#getUuid()}.
     *
     * @return A CallInfo object, representing the response from the Vonage Voice API.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public CallInfo getCallDetails(String uuid) throws VonageResponseParseException, VonageClientException {
        return readCall.execute(validateUuid(uuid));
    }

    /**
     * Send DTMF codes to an ongoing call.
     *
     * @param uuid   (REQUIRED) The UUID of the call, obtained from the object returned by {@link #createCall(Call)}.
     *               This value can be obtained with {@link CallEvent#getUuid()}
     * @param digits (REQUIRED) A string specifying the digits to be sent to the call. Valid characters are the digits
     *               {@code 1-9</tt>, <tt>#</tt>, <tt>*</tt>, with the special character <tt>p} indicating a short pause
     *               between tones.
     *
     * @return A CallInfo object, representing the response from the Vonage Voice API.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public DtmfResponse sendDtmf(String uuid, String digits) throws VonageResponseParseException, VonageClientException {
        if (digits == null || digits.trim().isEmpty()) {
            throw new IllegalArgumentException("Must include at least one digit to send.");
        }
        return sendDtmf.execute(new DtmfRequestWrapper(validateUuid(uuid), new DtmfPayload(digits)));
    }

    /**
     * Modify an ongoing call.
     * <p>
     * This method modifies an ongoing call, identified by "uuid". Modifications to the call can be one of:
     * <ul>
     * <li>Terminate the call (hangup)
     * <li>Mute a call leg (mute)
     * <li>Unmute a call leg (unmute)
     * <li>Earmuff a call leg (earmuff)
     * <li>Unearmuff a call leg (unearmuff)
     * </ul>
     *
     * @param uuid   The UUID of the call, obtained from the object returned by {@link #createCall(Call)}. This value
     *               can be obtained with {@link CallEvent#getUuid()}.
     * @param action The Action to take.
     *
     * @return A ModifyCallResponse object, representing the response from the Vonage Voice API.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public ModifyCallResponse modifyCall(String uuid, ModifyCallAction action) throws VonageResponseParseException, VonageClientException {
        return modifyCall(uuid, new ModifyCallPayload(Objects.requireNonNull(action, "Action is required.")));
    }

    /**
     * Internal implementation of {@code updateCall}.
     *
     * @param uuid The call ID.
     * @param payload The request body.
     *
     * @return The server response.
     *
     * @since 7.3.0
     */
    private ModifyCallResponse modifyCall(String uuid, ModifyCallPayload payload) {
        return modifyCall.execute(new ModifyCallRequestWrapper(validateUuid(uuid), payload));
    }

    /**
     * Modify an ongoing call using a CallModifier object.
     * <p>
     * In most cases, you will want to use {@link #modifyCall(String, ModifyCallAction)} or {@link #transferCall(String,
     * String)} instead of this method.
     *
     * @param modifier A CallModifier describing the modification to be made.
     *
     * @return A ModifyCallResponse object, representing the response from the Vonage Voice API.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     *
     * @deprecated Use {@link #modifyCall(String, ModifyCallAction)}. This will be removed in the next major release.
     */
    @Deprecated
    public ModifyCallResponse modifyCall(CallModifier modifier) throws VonageResponseParseException, VonageClientException {
        return modifyCall(modifier.getUuid(), modifier.getAction());
    }

    /**
     * Transfer a call to a different NCCO endpoint.
     *
     * @param uuid    The UUID of the call, obtained from the object returned by {@link #createCall(Call)}. This value
     *                can be obtained with {@link CallEvent#getUuid()}.
     * @param nccoUrl The URL of the NCCO endpoint the call should be transferred to.
     *
     * @return A ModifyCallResponse object, representing the response from the Vonage Voice API.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public ModifyCallResponse transferCall(String uuid, String nccoUrl) throws VonageResponseParseException, VonageClientException {
        return modifyCall(uuid, new TransferCallPayload(validateUrl(nccoUrl)));
    }

    /**
     * Transfer a call to a different NCCO.
     *
     * @param uuid The UUID of the call, obtained from the object returned by {@link #createCall(Call)}. This value can
     *             be obtained with {@link CallEvent#getUuid()}.
     * @param ncco The new NCCO that will be used in the call.
     *
     * @return A ModifyCallResponse object, representing the response from the Vonage Voice API.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public ModifyCallResponse transferCall(String uuid, Ncco ncco) throws VonageResponseParseException, VonageClientException {
        return modifyCall(uuid, new TransferCallPayload(Objects.requireNonNull(ncco, "NCCO is required.")));
    }

    /**
     * Stream audio to an ongoing call.
     *
     * @param uuid      The UUID of the call, obtained from the object returned by {@link #createCall(Call)}. This value
     *                  can be obtained with {@link CallEvent#getUuid()}.
     * @param streamUrl A URL of an audio file in MP3 or 16-bit WAV format, to be streamed to the call.
     * @param loop      The number of times to repeat the audio. The default value is {@code 1}, or you can use {@code
     *                  0} to indicate that the audio should be repeated indefinitely.
     *
     * @return The data returned from the Voice API.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public StreamResponse startStream(String uuid, String streamUrl, int loop) throws VonageResponseParseException, VonageClientException {
        return startStream(uuid, streamUrl, loop, 0d);
    }

    /**
     * Stream audio to an ongoing call.
     *
     * @param uuid      The UUID of the call, obtained from the object returned by {@link #createCall(Call)}. This value
     *                  can be obtained with {@link CallEvent#getUuid()}.
     * @param streamUrl A URL of an audio file in MP3 or 16-bit WAV format, to be streamed to the call.
     * @param loop      The number of times to repeat the audio. The default value is {@code 1}, or you can use {@code
     *                  0} to indicate that the audio should be repeated indefinitely.
     * @param level The audio level of the stream, between -1 and 1 with a precision of 0.1. The default value is 0.
     *
     * @return The data returned from the Voice API.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     *
     * @since 7.3.0
     */
    public StreamResponse startStream(String uuid, String streamUrl, int loop, double level) throws VonageResponseParseException, VonageClientException {
        return startStream.execute(new StreamRequestWrapper(
                validateUuid(uuid),
                new StreamPayload(validateUrl(streamUrl), loop, level)
        ));
    }

    /**
     * Stream audio to an ongoing call.
     *
     * @param uuid      The UUID of the call, obtained from the object returned by {@link #createCall(Call)}. This value
     *                  can be obtained with {@link CallEvent#getUuid()}.
     * @param streamUrl A URL of an audio file in MP3 or 16-bit WAV format, to be streamed to the call.
     *
     * @return The data returned from the Voice API.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public StreamResponse startStream(String uuid, String streamUrl) throws VonageResponseParseException, VonageClientException {
        return startStream(uuid, streamUrl, 1);
    }

    /**
     * Stop the audio being streamed into a call.
     *
     * @param uuid The UUID of the call, obtained from the object returned by {@link #createCall(Call)}. This value can
     *             be obtained with {@link CallEvent#getUuid()}.
     *
     * @return The data returned from the Voice API.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public StreamResponse stopStream(String uuid) throws VonageResponseParseException, VonageClientException {
        return stopStream.execute(validateUuid(uuid));
    }

    /**
     * Send a synthesized speech message to an ongoing call.
     * @param uuid The UUID of the call, obtained from the object returned by {@link #createCall(Call)}.
     * This value can be obtained with {@link CallEvent#getUuid()}.
     *
     * @param properties Properties of the text-to-speech request.
     *
     * @return Metadata from the Voice API if successful.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     *
     * @since 7.3.0
     */
    public TalkResponse startTalk(String uuid, TalkPayload properties) {
        return startTalk.execute(new TalkRequest(
                validateUuid(uuid),
                Objects.requireNonNull(properties, "TalkPayload is required)")
        ));
    }

    /**
     * Send a synthesized speech message to an ongoing call.
     * <p>
     * The message will only play once, spoken with the default en-US voice.
     *
     * @param uuid The UUID of the call, obtained from the object returned by {@link #createCall(Call)}. This value can
     *             be obtained with {@link CallEvent#getUuid()}
     * @param text The message to be spoken to the call participants.
     *
     * @return The data returned from the Voice API.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     *
     * @deprecated Use {@link #startTalk(String, TalkPayload)}.
     */
    @Deprecated
    public TalkResponse startTalk(String uuid, String text) throws VonageResponseParseException, VonageClientException {
        return startTalk.execute(new TalkRequest(uuid, TalkPayload.builder(text).build()));
    }

    /**
     * @param uuid      The UUID of the call, obtained from the object returned by {@link #createCall(Call)}. This value
     *                   can be obtained with {@link CallEvent#getUuid()}
     * @param text       The message to be spoken to the call participants.
     *
     * @param language  The Language to use when converting text-to-speech.
     *
     * @return The data returned from the Voice API.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     *
     * @deprecated Use {@link #startTalk(String, TalkPayload)}.
     */
    @Deprecated
    public TalkResponse startTalk(String uuid, String text, TextToSpeechLanguage language) throws VonageResponseParseException, VonageClientException {
        return startTalk(uuid, TalkPayload.builder(text).language(language).build());
    }

    /**
     * Send a synthesized speech message to an ongoing call.
     * <p>
     * The message will be spoken with the default en-US voice.
     *
     * @param uuid The UUID of the call, obtained from the object returned by {@link #createCall(Call)}. This value can
     *             be obtained with {@link CallEvent#getUuid()}.
     * @param text The message to be spoken to the call participants.
     * @param loop The number of times to repeat the message. The default value is {@code 1}, or you can use {@code 0}
     *             to indicate that the message should be repeated indefinitely.
     *
     * @return The data returned from the Voice API.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     *
     * @deprecated Use {@link #startTalk(String, TalkPayload)}.
     */
    @Deprecated
    public TalkResponse startTalk(String uuid, String text, int loop) throws VonageResponseParseException, VonageClientException {
        return startTalk(uuid, TalkPayload.builder(text).loop(loop).build());
    }

    /**
     * Send a synthesized speech message to an ongoing call.
     *
     * @param uuid      The UUID of the call, obtained from the object returned by {@link #createCall(Call)}. This value
     *                  can be obtained with {@link CallEvent#getUuid()}.
     * @param text      The message to be spoken to the call participants.
     * @param language  The language to use for the text-to-speech.
     * @param style     The language style to use for the text-to-speech.
     * @param loop      The number of times to repeat the message. The default value is {@code 1}, or you can use {@code
     *                  0} to indicate that the message should be repeated indefinitely.
     *
     * @return The data returned from the Voice API.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     *
     * @deprecated Use {@link #startTalk(String, TalkPayload)}.
     */
    @Deprecated
    public TalkResponse startTalk(String uuid, String text, TextToSpeechLanguage language, int style, int loop) throws VonageResponseParseException, VonageClientException {
        return startTalk(uuid, TalkPayload.builder(text).loop(loop).language(language).style(style).build());
    }

    /**
     * Send a synthesized speech message to an ongoing call.
     *
     * @param uuid      The UUID of the call, obtained from the object returned by {@link #createCall(Call)}. This value
     *                  can be obtained with {@link CallEvent#getUuid()}.
     * @param text      The message to be spoken to the call participants.
     * @param language  The language to use for the text-to-speech.
     * @param style     The language style to use for the text-to-speech.
     *
     * @return The data returned from the Voice API.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     *
     * @deprecated Use {@link #startTalk(String, TalkPayload)}.
     */
    @Deprecated
    public TalkResponse startTalk(String uuid, String text, TextToSpeechLanguage language, int style) throws VonageResponseParseException, VonageClientException {
        return startTalk(uuid, TalkPayload.builder(text).language(language).style(style).build());
    }

    /**
     * Stop the message being spoken into a call.
     *
     * @param uuid The UUID of the call, obtained from the object returned by {@link #createCall(Call)}. This value can
     *             be obtained with {@link CallEvent#getUuid()}.
     *
     * @return The data returned from the Voice API.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public TalkResponse stopTalk(String uuid) throws VonageResponseParseException, VonageClientException {
        return stopTalk.execute(validateUuid(uuid));
    }

    /**
     * Download a recording, given the recordingUrl provided from the webhook callback.
     * <p>
     * This returns a {@link Recording} object which can provide an InputStream of the byte data, or can be used to
     * save directly to file.
     *
     * @param recordingUrl The recordingUrl provided by the webhook callback.
     *
     * @return A Recording object, providing access to the recording's bytes.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public Recording downloadRecording(String recordingUrl) throws VonageResponseParseException, VonageClientException {
        return downloadRecording.execute(validateUrl(recordingUrl));
    }
}
