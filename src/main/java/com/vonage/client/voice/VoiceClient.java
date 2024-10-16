/*
 *   Copyright 2024 Vonage
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

import com.vonage.client.*;
import com.vonage.client.auth.JWTAuthMethod;
import com.vonage.client.common.HttpMethod;
import com.vonage.client.voice.ncco.InputMode;
import com.vonage.client.voice.ncco.Ncco;
import com.vonage.jwt.Jwt;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Function;

/**
 * A client for talking to the Vonage Voice API. The standard way to obtain an instance of this class is to use {@link
 * VonageClient#getVoiceClient()}.
 */
public class VoiceClient {
    final RestEndpoint<Call, CallEvent> createCall;
    final RestEndpoint<String, CallInfo> getCall;
    final RestEndpoint<CallsFilter, CallInfoPage> listCalls;
    final RestEndpoint<ModifyCallPayload, Void> modifyCall;
    final RestEndpoint<StreamPayload, StreamResponse> startStream;
    final RestEndpoint<String, StreamResponse> stopStream;
    final RestEndpoint<TalkPayload, TalkResponse> startTalk;
    final RestEndpoint<String, TalkResponse> stopTalk;
    final RestEndpoint<DtmfPayload, DtmfResponse> sendDtmf;
    final RestEndpoint<AddDtmfListenerRequest, Void> addDtmfListener;
    final RestEndpoint<String, Void> removeDtmfListener;
    final RestEndpoint<String, byte[]> downloadRecording;

    /**
     * Constructor.
     *
     * @param wrapper (required) shared HTTP wrapper object used for making REST calls.
     */
    public VoiceClient(HttpWrapper wrapper) {

        @SuppressWarnings("unchecked")
        class Endpoint<T, R> extends DynamicEndpoint<T, R> {
            Endpoint(Function<T, String> pathGetter, HttpMethod method, R... type) {
                super(DynamicEndpoint.<T, R> builder(type).authMethod(JWTAuthMethod.class)
                        .responseExceptionType(VoiceResponseException.class)
                        .requestMethod(method).wrapper(wrapper).pathGetter((de, req) -> {
                            String base = de.getHttpWrapper().getHttpConfig().getVersionedApiBaseUri("v1");
                            String path = pathGetter.apply(req);
                            if (path.startsWith("http") && method == HttpMethod.GET) {
                                return path;
                            }
                            return base + "/calls" + (path.isEmpty() ? "" : "/" + path);
                        })
                );
            }
        }

        createCall = new Endpoint<>(req -> "", HttpMethod.POST);
        getCall = new Endpoint<>(Function.identity(), HttpMethod.GET);
        listCalls = new Endpoint<>(req -> "", HttpMethod.GET);
        modifyCall = new Endpoint<>(req -> req.uuid, HttpMethod.PUT);
        startStream = new Endpoint<>(req -> req.uuid + "/stream", HttpMethod.PUT);
        stopStream = new Endpoint<>(uuid -> uuid + "/stream", HttpMethod.DELETE);
        startTalk = new Endpoint<>(req -> req.uuid + "/talk", HttpMethod.PUT);
        stopTalk = new Endpoint<>(uuid -> uuid + "/talk", HttpMethod.DELETE);
        sendDtmf = new Endpoint<>(req -> req.uuid + "/dtmf", HttpMethod.PUT);
        addDtmfListener = new Endpoint<>(req -> req.uuid + "/input/dtmf", HttpMethod.PUT);
        removeDtmfListener = new Endpoint<>(uuid -> uuid + "/input/dtmf", HttpMethod.DELETE);
        downloadRecording = new Endpoint<>(Function.identity(), HttpMethod.GET);
    }

    private String validateUuid(String uuid) {
        return Objects.requireNonNull(uuid, "UUID is required.");
    }

    private String validateUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("URL is required.");
        }
        return URI.create(url).toString();
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
     * @param uuid (required) The UUID of the call, obtained from the object returned by {@link #createCall(Call)}.
     *             This value can be obtained with {@link CallEvent#getUuid()}.
     *
     * @return A CallInfo object, representing the response from the Vonage Voice API.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public CallInfo getCallDetails(String uuid) throws VonageResponseParseException, VonageClientException {
        return getCall.execute(validateUuid(uuid));
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
        return sendDtmf.execute(new DtmfPayload(digits, validateUuid(uuid)));
    }

    private void modifyCall(String callId, ModifyCallAction action) throws VoiceResponseException {
        modifyCall.execute(new ModifyCallPayload(action, validateUuid(callId)));
    }

    /**
     * Modify a call with the {@linkplain ModifyCallAction#EARMUFF} action.
     * This prevents the call from hearing audio.
     *
     * @param callId UUID of the call.
     *
     * @throws VoiceResponseException If there was an error performing the action.
     *
     * @since 7.11.0
     */
    public void earmuffCall(String callId) throws VoiceResponseException {
        modifyCall(callId, ModifyCallAction.EARMUFF);
    }

    /**
     * Modify a call with the {@linkplain ModifyCallAction#UNEARMUFF} action.
     * This allows the call to hear audio again.
     *
     * @param callId UUID of the call.
     *
     * @throws VoiceResponseException If there was an error performing the action.
     *
     * @since 7.11.0
     */
    public void unearmuffCall(String callId) throws VoiceResponseException {
        modifyCall(callId, ModifyCallAction.UNEARMUFF);
    }

    /**
     * Modify a call with the {@linkplain ModifyCallAction#MUTE} action.
     *
     * @param callId UUID of the call.
     *
     * @throws VoiceResponseException If there was an error performing the action.
     *
     * @since 7.11.0
     */
    public void muteCall(String callId) throws VoiceResponseException {
        modifyCall(callId, ModifyCallAction.MUTE);
    }

    /**
     * Modify a call with the {@linkplain ModifyCallAction#UNMUTE} action.
     *
     * @param callId UUID of the call.
     *
     * @throws VoiceResponseException If there was an error performing the action.
     *
     * @since 7.11.0
     */
    public void unmuteCall(String callId) throws VoiceResponseException {
        modifyCall(callId, ModifyCallAction.UNMUTE);
    }

    /**
     * Modify a call with the {@linkplain ModifyCallAction#HANGUP} action.
     *
     * @param callId UUID of the call.
     *
     * @throws VoiceResponseException If there was an error performing the action.
     *
     * @since 7.11.0
     */
    public void terminateCall(String callId) throws VoiceResponseException {
        modifyCall(callId, ModifyCallAction.HANGUP);
    }

    /**
     * Transfer a call to a different NCCO endpoint.
     *
     * @param uuid    The UUID of the call, obtained from the object returned by {@link #createCall(Call)}. This value
     *                can be obtained with {@link CallEvent#getUuid()}.
     * @param nccoUrl The URL of the NCCO endpoint the call should be transferred to.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public void transferCall(String uuid, String nccoUrl) throws VonageResponseParseException, VonageClientException {
        modifyCall.execute(new TransferCallPayload(validateUrl(nccoUrl), validateUuid(uuid)));
    }

    /**
     * Transfer a call to a different NCCO.
     *
     * @param uuid The UUID of the call, obtained from the object returned by {@link #createCall(Call)}. This value can
     *             be obtained with {@link CallEvent#getUuid()}.
     * @param ncco The new NCCO that will be used in the call.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public void transferCall(String uuid, Ncco ncco) throws VonageResponseParseException, VonageClientException {
        modifyCall.execute(new TransferCallPayload(ncco, validateUuid(uuid)));
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
        return startStream.execute(new StreamPayload(validateUrl(streamUrl), loop, level, validateUuid(uuid)));
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
        Objects.requireNonNull(properties, "TalkPayload is required").uuid = validateUuid(uuid);
        return startTalk.execute(properties);
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
        return startTalk(uuid, TalkPayload.builder(text).build());
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
     * @param uuid      The UUID of the call, obtained from the object returned by {@link #createCall(Call)}.
     *                  This value can be obtained with {@link CallEvent#getUuid()}.
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
     * Add a listener for asynchronous DTMF events sent by a caller to an
     * {@linkplain com.vonage.client.voice.ncco.InputAction} NCCO action, when the
     * {@linkplain com.vonage.client.voice.ncco.InputAction.Builder#mode(InputMode)} is
     * {@link com.vonage.client.voice.ncco.InputMode#ASYNCHRONOUS}.
     *
     * @param uuid The UUID of the call, obtained from the object returned by {@link #createCall(Call)}.
     * This value can be obtained with {@link CallEvent#getUuid()}.
     *
     * @param eventUrl The URL to send asynchronous DTMF user input events to.
     *
     * @throws VoiceResponseException If the call does not exist or the listener could not be added,
     * for example if the call's state or input mode are incompatible.
     *
     * @since 8.12.0
     */
    public void addDtmfListener(String uuid, String eventUrl) throws VoiceResponseException {
        addDtmfListener.execute(new AddDtmfListenerRequest(validateUuid(uuid), URI.create(validateUrl(eventUrl))));
    }

    /**
     * Remove the listener for asynchronous DTMF events sent by a caller to an
     * {@linkplain com.vonage.client.voice.ncco.InputAction} NCCO, when the
     * {@linkplain com.vonage.client.voice.ncco.InputAction.Builder#mode(InputMode)} is
     * {@link com.vonage.client.voice.ncco.InputMode#ASYNCHRONOUS}. Calling this method
     * stops sending DTMF events to the event URL set in {@link #addDtmfListener(String, String)}.
     *
     * @param uuid The UUID of the call, obtained from the object returned by {@link #createCall(Call)}.
     * This value can be obtained with {@link CallEvent#getUuid()}.
     *
     * @throws VoiceResponseException If the call does not exist or have a listener attached.
     *
     * @since 8.12.0
     */
    public void removeDtmfListener(String uuid) throws VoiceResponseException {
        removeDtmfListener.execute(validateUuid(uuid));
    }

    /**
     * Download a recording.
     *
     * @param recordingUrl The recording URL, as obtained from the webhook callback.
     *
     * @return The raw contents of the downloaded recording as a byte array.
     *
     * @throws IllegalArgumentException If the recordingUrl is invalid.
     * @throws VoiceResponseException If there was an error downloading the recording from the URL.
     *
     * @since 7.11.0
     */
    public byte[] downloadRecordingRaw(String recordingUrl) {
        if (validateUrl(recordingUrl).contains(".nexmo.com/v1/files")) {
            return downloadRecording.execute(recordingUrl);
        }
        else {
            throw new IllegalArgumentException("Invalid recording URL");
        }
    }

    /**
     * Download a recording and save it to a file.
     *
     * @param recordingUrl The recording URL, as obtained from the webhook callback.
     * @param destination Path to save the recording to.
     *
     * @throws IOException If there was an error writing to the file.
     * @throws VoiceResponseException If there was an error downloading the recording from the URL.
     * @throws IllegalArgumentException If the recordingUrl is invalid.
     *
     * @since 7.11.0
     */
    public void saveRecording(String recordingUrl, Path destination) throws IOException {
        Path path = Objects.requireNonNull(destination, "Save path is required.");
        byte[] binary = downloadRecordingRaw(recordingUrl);
        if (Files.isDirectory(destination)) {
            String fileName = recordingUrl.substring(recordingUrl.lastIndexOf('/') + 1);
            path = path.resolve(fileName);
        }
        Files.write(path, binary);
    }

    /**
     * Utility method for verifying whether a token was signed by a secret.
     * This is mostly useful when using signed callbacks to ensure that the inbound
     * data came from Vonage servers. The signature is performed using the SHA-256 HMAC algorithm.
     *
     * @param jwt The JSON Web Token to verify.
     * @param secret The symmetric secret key (HS256) to use for decrypting the token's signature.
     *
     * @return {@code true} if the token was signed by the secret, {@code false} otherwise.
     *
     * @since 7.11.0
     */
    public static boolean verifySignature(String jwt, String secret) {
        return Jwt.verifySignature(jwt, secret);
    }
}
