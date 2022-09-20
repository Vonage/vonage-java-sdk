/*
 *   Copyright 2022 Vonage
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
package com.vonage.client.video;

import com.vonage.client.HttpWrapper;
import com.vonage.client.VonageClient;
import java.util.List;
import java.util.Objects;

/**
 * A client for talking to the Vonage Video API. The standard way to obtain an instance of this class is to use
 * {@link VonageClient#getVideoClient()}.
 */
public class VideoClient {
	final CreateSessionEndpoint createSession;
	final SetStreamLayoutEndpoint setStreamLayout;
	final ListStreamsEndpoint listStreams;
	final GetStreamEndpoint getStream;
	final SignalAllEndpoint signalAll;
	final SignalEndpoint signal;

	/**
	 * Constructor.
	 *
	 * @param httpWrapper (REQUIRED) shared HTTP wrapper object used for making REST calls.
	 */
	public VideoClient(HttpWrapper httpWrapper) {
		createSession = new CreateSessionEndpoint(httpWrapper);
		setStreamLayout = new SetStreamLayoutEndpoint(httpWrapper);
		listStreams = new ListStreamsEndpoint(httpWrapper);
		getStream = new GetStreamEndpoint(httpWrapper);
		signalAll = new SignalAllEndpoint(httpWrapper);
		signal = new SignalEndpoint(httpWrapper);
	}

	private void validateSessionId(String sessionId) {
		if (sessionId == null || sessionId.isEmpty()) {
			throw new IllegalArgumentException("Session ID is required.");
		}
	}

	/**
	 * Generate a new session.
	 *
	 * @param request The session properties.
	 * @return Details of the created session.
	 */
	public CreateSessionResponse createSession(CreateSessionRequest request) {
		return createSession.execute(request);
	}

	/**
	 *
	 * @param sessionId The session ID.
	 *
	 * @param streams The stream layouts to change.
	 */
	public void setStreamLayout(String sessionId, List<SessionStream> streams) {
		validateSessionId(sessionId);
		Objects.requireNonNull(streams, "Stream list is required.");
		setStreamLayout.execute(new SetStreamLayoutRequest(sessionId, streams));
	}

	/**
	 * Use this method to get information on all Vonage Video streams in a session.
	 *
	 * @param sessionId The session ID.
	 * @return Details for each stream, as a List.
	 * @see #getStream(String, String)
	 */
	public List<GetStreamResponse> listStreams(String sessionId) {
		validateSessionId(sessionId);
		return listStreams.execute(sessionId).getItems();
	}

	/**
	 * Use this method to get information on a Vonage Video stream. For example, you can call this method to get
	 * information about layout classes used by a Vonage Video stream. The layout classes define how the stream is
	 * displayed in the layout of a broadcast stream.
	 *
	 * @param sessionId The session ID.
	 * @param streamId The ID of the stream to retrieve.
	 * @return Details of the requested stream.
	 */
	public GetStreamResponse getStream(String sessionId, String streamId) {
		validateSessionId(sessionId);
		if (streamId == null || streamId.isEmpty()) {
			throw new IllegalArgumentException("Stream ID is required.");
		}
		return getStream.execute(new GetStreamRequest(sessionId, streamId));
	}

	private void validateSignalRequest(SignalRequest request) {
		if (request == null) {
			throw new IllegalArgumentException("Signal request properties are required.");
		}
	}

	/**
	 * Sends signals to all participants in an active Vonage Video session.
	 *
	 * @param sessionId The session ID.
	 * @param request Signal payload.
	 */
	public void signalAll(String sessionId, SignalRequest request) {
		validateSessionId(sessionId);
		validateSignalRequest(request);
		signalAll.execute(new SignalRequestWrapper(request, sessionId));
	}

	/**
	 * Sends signal to a specific participant in an active Vonage Video session.
	 *
	 * @param sessionId The session ID.
	 * @param connectionId Specific publisher connection ID.
	 * @param request Signal payload.
	 */
	public void signal(String sessionId, String connectionId, SignalRequest request) {
		validateSessionId(sessionId);
		if (connectionId == null || connectionId.isEmpty()) {
			throw new IllegalArgumentException("Connection ID is required");
		}
		validateSignalRequest(request);
		signal.execute(new SignalRequestWrapper(request, sessionId, connectionId));
	}
}
