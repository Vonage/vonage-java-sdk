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
	final ForceDisconnectEndpoint forceDisconnect;
	final MuteStreamEndpoint muteStream;
	final MuteSessionEndpoint muteSession;

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
		forceDisconnect = new ForceDisconnectEndpoint(httpWrapper);
		muteStream = new MuteStreamEndpoint(httpWrapper);
		muteSession = new MuteSessionEndpoint(httpWrapper);
	}

	private String validateId(String param, String name) {
		if (param == null || param.isEmpty()) {
			throw new IllegalArgumentException(name+" ID is required.");
		}
		return param;
	}

	private String validateSessionId(String sessionId) {
		return validateId(sessionId, "Session");
	}

	private String validateConnectionId(String connectionId) {
		return validateId(connectionId, "Connection");
	}

	private String validateStreamId(String streamId) {
		return validateId(streamId, "Stream");
	}

	private <T> T validateRequest(T request) {
		if (request == null) {
			throw new IllegalArgumentException("Request properties are required.");
		}
		return request;
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
	 * Use this method to change layout classes for a Vonage Video stream. The layout classes define how the stream is
	 * displayed in the layout of a composed Vonage Video archive.
	 * @param sessionId The session ID.
	 * @param streams The stream layouts to change.
	 */
	public void setStreamLayout(String sessionId, List<SessionStream> streams) {
		setStreamLayout.execute(new SetStreamLayoutRequest(
				validateSessionId(sessionId),
				Objects.requireNonNull(streams, "Stream list is required.")
		));
	}

	/**
	 * Use this method to get information on all Vonage Video streams in a session.
	 *
	 * @param sessionId The session ID.
	 * @return Details for each stream, as a List.
	 * @see #getStream(String, String)
	 */
	public List<GetStreamResponse> listStreams(String sessionId) {
		return listStreams.execute(validateSessionId(sessionId)).getItems();
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
	public GetStreamResponse getStream(String sessionId, String streamId) {;
		return getStream.execute(new GetStreamRequest(
				validateSessionId(sessionId),
				validateStreamId(streamId)
		));
	}

	/**
	 * Sends signals to all participants in an active Vonage Video session.
	 *
	 * @param sessionId The session ID.
	 * @param request Signal payload.
	 */
	public void signalAll(String sessionId, SignalRequest request) {
		signalAll.execute(new SignalRequestWrapper(
				validateRequest(request),
				validateSessionId(sessionId)
		));
	}

	/**
	 * Sends signal to a specific participant in an active Vonage Video session.
	 *
	 * @param sessionId The session ID.
	 * @param connectionId Specific publisher connection ID.
	 * @param request Signal payload.
	 */
	public void signal(String sessionId, String connectionId, SignalRequest request) {
		signal.execute(new SignalRequestWrapper(
				validateRequest(request),
				validateSessionId(sessionId),
				validateConnectionId(connectionId)
		));
	}

	/**
	 * Force a client to disconnect from a session.
	 *
	 * @param sessionId The session ID.
	 * @param connectionId Specific publisher connection ID.
	 */
	public void forceDisconnect(String sessionId, String connectionId) {
		forceDisconnect.execute(new ForceDisconnectRequestWrapper(
				validateSessionId(sessionId),
				validateConnectionId(connectionId)
		));
	}

	/**
	 * Force mute a specific publisher stream.
	 *
	 * @param sessionId The session ID.
	 * @param streamId The ID of the stream to mute.
	 * @return The project details.
	 */
	public ProjectDetails muteStream(String sessionId, String streamId) {
		return muteStream.execute(new MuteStreamRequestWrapper(
				validateSessionId(sessionId),
				validateStreamId(streamId)
		));
	}

	/**
	 * Force all streams (except for an optional list of streams) in a session to mute published audio.
	 * You can also use this method to disable the force mute state of a session.
	 *
	 * @param sessionId The session ID.
	 * @param request Properties of the mute request.
	 * @return The project details.
	 */
	public ProjectDetails muteSession(String sessionId, MuteSessionRequest request) {
		return muteSession.execute(new MuteSessionRequestWrapper(
				validateSessionId(sessionId),
				validateRequest(request)
		));
	}
}
