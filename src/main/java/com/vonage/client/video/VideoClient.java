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
import com.vonage.client.auth.JWTAuthMethod;
import com.vonage.jwt.Jwt;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * A client for using the Vonage Video API. The standard way to obtain an instance of this class is to use
 * {@link VonageClient#getVideoClient()}.
 *
 * @since 8.0.0-beta1
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
	final SetArchiveLayoutEndpoint setArchiveLayout;
	final DeleteArchiveEndpoint deleteArchive;
	final PatchArchiveStreamEndpoint patchArchiveStream;
	final StopArchiveEndpoint stopArchive;
	final GetArchiveEndpoint getArchive;
	final ListArchivesEndpoint listArchives;
	final CreateArchiveEndpoint createArchive;
	final SipDialEndpoint sipDial;
	final Supplier<? extends Jwt.Builder> newJwtSupplier;

	/**
	 * Constructor.
	 *
	 * @param httpWrapper (REQUIRED) shared HTTP wrapper object used for making REST calls.
	 */
	public VideoClient(final HttpWrapper httpWrapper) {
		newJwtSupplier = () -> httpWrapper.getAuthCollection().getAuth(JWTAuthMethod.class).newJwt();
		createSession = new CreateSessionEndpoint(httpWrapper);
		setStreamLayout = new SetStreamLayoutEndpoint(httpWrapper);
		listStreams = new ListStreamsEndpoint(httpWrapper);
		getStream = new GetStreamEndpoint(httpWrapper);
		signalAll = new SignalAllEndpoint(httpWrapper);
		signal = new SignalEndpoint(httpWrapper);
		forceDisconnect = new ForceDisconnectEndpoint(httpWrapper);
		muteStream = new MuteStreamEndpoint(httpWrapper);
		muteSession = new MuteSessionEndpoint(httpWrapper);
		setArchiveLayout = new SetArchiveLayoutEndpoint(httpWrapper);
		deleteArchive = new DeleteArchiveEndpoint(httpWrapper);
		patchArchiveStream = new PatchArchiveStreamEndpoint(httpWrapper);
		stopArchive = new StopArchiveEndpoint(httpWrapper);
		getArchive = new GetArchiveEndpoint(httpWrapper);
		listArchives = new ListArchivesEndpoint(httpWrapper);
		createArchive = new CreateArchiveEndpoint(httpWrapper);
		sipDial = new SipDialEndpoint(httpWrapper);
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

	private String validateArchiveId(String archiveId) {
		return validateId(archiveId, "Archive");
	}

	private <T> T validateRequest(T request) {
		if (request == null) {
			throw new IllegalArgumentException("Request properties are required.");
		}
		return request;
	}

	/**
	 * Generate a new session, using default properties.
	 *
	 * @return Details of the created session.
	 * @see #createSession(CreateSessionRequest)
	 */
	public CreateSessionResponse createSession() {
		return createSession(null);
	}

	/**
	 * Generate a new session.
	 *
	 * @param request (OPTIONAL) The session properties.
	 * @return Details of the created session.
	 */
	public CreateSessionResponse createSession(CreateSessionRequest request) {
		return createSession.execute(request);
	}

	/**
	 * Use this method to change layout classes for a Vonage Video stream. The layout classes define how the stream is
	 * displayed in the layout of a composed Vonage Video archive.
	 *
	 * @param sessionId The session ID.
	 * @param streams The stream layouts to change.
	 */
	public void setStreamLayout(String sessionId, List<SessionStream> streams) {
		setStreamLayout.execute(new SetStreamLayoutRequest(validateSessionId(sessionId), streams));
	}

	/**
	 * Use this method to change layout classes for a Vonage Video stream. The layout classes define how the stream is
	 * displayed in the layout of a composed Vonage Video archive.
	 *
	 * @param sessionId The session ID.
	 * @param streams The stream layouts to change.
	 *
	 * @see #setStreamLayout(String, List)
	 * @since 8.0.0-beta2
	 */
	public void setStreamLayout(String sessionId, SessionStream... streams) {
		setStreamLayout(sessionId, Arrays.asList(streams));
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
	 * @param streamId ID of the stream to retrieve.
	 * @return Details of the requested stream.
	 */
	public GetStreamResponse getStream(String sessionId, String streamId) {
		return getStream.execute(new GetStreamRequestWrapper(
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
	 * @param streamId ID of the stream to mute.
	 */
	public void muteStream(String sessionId, String streamId) {
		muteStream.execute(new MuteStreamRequestWrapper(
				validateSessionId(sessionId),
				validateStreamId(streamId)
		));
	}

	/**
	 * Force all streams (except for an optional list of streams) in a session to mute published audio.
	 * You can also use this method to disable the force mute state of a session.
	 *
	 * @param sessionId The session ID.
	 *
	 * @param active Whether to mute streams in the session (true) and enable the mute state of the session, or to
	 * disable the mute state of the session (false). With the mute state enabled (true), all current and future
	 * streams published to the session (except streams in "excludedStreamIds") are muted. If this is
	 * set to {@code false}, future streams published to the session are not muted (but any existing muted
	 * streams will remain muted).
	 *
	 * @param excludedStreamIds (OPTIONAL) The stream IDs for streams that should not be muted.
	 * If you omit this, all streams in the session will be muted. This only applies when the "active" property is set
	 * {@code true}. When the "active" property is set to {@code false}, it is ignored.
	 */
	public void muteSession(String sessionId, boolean active, Collection<String> excludedStreamIds) {
		muteSession.execute(new MuteSessionRequest(
				validateSessionId(sessionId),
				active,
				excludedStreamIds
		));
	}

	/**
	 * Force all streams (except for an optional list of streams) in a session to mute published audio.
	 * You can also use this method to disable the force mute state of a session.
	 *
	 * @param sessionId The session ID.
	 *
	 * @param active Whether to mute streams in the session (true) and enable the mute state of the session, or to
	 * disable the mute state of the session (false). With the mute state enabled (true), all current and future
	 * streams published to the session (except streams in "excludedStreamIds") are muted. If this is
	 * set to {@code false}, future streams published to the session are not muted (but any existing muted
	 * streams will remain muted).
	 *
	 * @param excludedStreamIds (OPTIONAL) The stream IDs for streams that should not be muted.
	 * If you omit this, all streams in the session will be muted. This only applies when the "active" property is set
	 * {@code true}. When the "active" property is set to {@code false}, it is ignored.
	 *
	 * @see #muteSession(String, boolean, Collection)
	 */
	public void muteSession(String sessionId, boolean active, String... excludedStreamIds) {
		muteSession(sessionId, active,
				excludedStreamIds != null && excludedStreamIds.length > 0 ?
					Arrays.asList(excludedStreamIds) : null
		);
	}

	/**
	 * Dynamically change the layout type of a composed archive while it is being recorded.
	 *
	 * @param archiveId ID of the archive to change.
	 * @param layout Properties of the layout change request.
	 */
	public void setArchiveLayout(String archiveId, ArchiveLayout layout) {
		setArchiveLayout.execute(new SetArchiveLayoutRequestWrapper(
				validateArchiveId(archiveId),
				validateRequest(layout)
		));
	}

	/**
	 * Deletes an archive.
	 *
	 * @param archiveId ID of the archive to delete.
	 */
	public void deleteArchive(String archiveId) {
		deleteArchive.execute(validateArchiveId(archiveId));
	}

	/**
	 * Removes a stream from a composed archive that was started with the
	 * {@code streamMode} set to {@link StreamMode#MANUAL}.
	 *
	 * @param archiveId ID of the archive.
	 * @param streamId ID of the stream to remove.
	 */
	public void removeArchiveStream(String archiveId, String streamId) {
		patchArchiveStream(archiveId, new PatchArchiveStreamRequest(validateStreamId(streamId)));
	}

	/**
	 * Adds a stream to a composed archive that was started with the
	 * {@code streamMode} set to {@link StreamMode#MANUAL}.
	 *
	 * @param archiveId ID of the archive.
	 * @param streamId ID of the stream to add.
	 *
	 * @see #addArchiveStream(String, String, Boolean, Boolean)
	 */
	public void addArchiveStream(String archiveId, String streamId) {
		addArchiveStream(archiveId, streamId, null, null);
	}

	/**
	 * Adds a stream to a composed archive.
	 *
	 * @param archiveId ID of the archive.
	 * @param streamId ID of the stream to add.
	 * @param audio (OPTIONAL) Whether the composed archive should include the stream's audio (true by default).
	 * @param video (OPTIONAL) Whether the composed archive should include the stream's video (true by default).
	 */
	public void addArchiveStream(String archiveId, String streamId, Boolean audio, Boolean video) {
		patchArchiveStream(archiveId, new PatchArchiveStreamRequest(validateStreamId(streamId), audio, video));
	}

	private void patchArchiveStream(String archiveId, PatchArchiveStreamRequest request) {
		PatchArchiveStreamRequest pasr = validateRequest(request);
		pasr.archiveId = validateArchiveId(archiveId);
		patchArchiveStream.execute(pasr);
	}

	/**
	 * Archives stop recording after 4 hours (14,400 seconds), or 60 seconds after the last client disconnects from
	 * the session, or 60 minutes after the last client stops publishing. However, automatic archives continue
	 * recording to multiple consecutive files of up to 4 hours in length each.
	 * <p>
	 * Calling this method for automatic archives has no effect. Automatic archives continue recording to multiple
	 * consecutive files of up to 4 hours (14,400 seconds) in length each, until 60 seconds after the last client
	 * disconnects from the session, or 60 minutes after the last client stops publishing a stream to the session.
	 *
	 * @param archiveId ID of the archive to stop.
	 * @return The Archive corresponding to the archiveId.
	 */
	public Archive stopArchive(String archiveId) {
		return stopArchive.execute(validateArchiveId(archiveId));
	}

	/**
	 * Retrieve information about a specific archive.
	 *
	 * @param archiveId ID of the archive to retrieve.
	 * @return The Archive corresponding to the archiveId.
	 */
	public Archive getArchive(String archiveId) {
		return getArchive.execute(validateArchiveId(archiveId));
	}

	/**
	 * List all archives in an application.
	 *
	 * @return The list of archives, in order from newest to oldest.
	 * @see #listArchives(ListArchivesRequest)
	 */
	public List<Archive> listArchives() {
		return listArchives(null);
	}

	/**
	 * List all archives in an application.
	 *
	 * @param request (OPTIONAL) Filter properties of the request.
	 *
	 * @return The list of archives matching the filter criteria, in order from newest to oldest.
	 */
	public List<Archive> listArchives(ListArchivesRequest request) {
		return listArchives.execute(request).getItems();
	}

	/**
	 * Create a new archive.
	 *
	 * @param request Properties of the archive.
	 *
	 * @return The created Archive.
	 */
	public Archive createArchive(CreateArchiveRequest request) {
		return createArchive.execute(validateRequest(request));
	}

	/**
	 * To connect your SIP platform to an OpenTok session, submit an HTTP POST request to the dial method. The audio
	 * from your end of the SIP call is added to the video session as an audio-only stream. The Vonage Media Router
	 * mixes audio from other streams in the session and sends the mixed audio to your SIP endpoint.
	 *
	 * <p>
	 * The call ends when your SIP server sends a BYE message (to terminate the call). You can also end a call
	 * using {@link #forceDisconnect(String, String)}. The Vonage Video SIP gateway automatically ends a call after
	 * 5 minutes of inactivity (5 minutes without media received). Also, as a security measure, the Vonage Video SIP
	 * gateway closes any SIP call that lasts longer than 6 hours.
	 *
	 * <p>
	 * The SIP interconnect feature requires that you use video session that uses the Vonage Media Router
	 * (a session with the media mode set to {@link MediaMode#ROUTED}).
	 *
	 * <p>
	 * For more information, including technical details and security considerations, see the
	 * Vonage SIP interconnect developer guide.
	 *
	 * @param request The outbound SIP call's properties.
	 *
	 * @return Details of the SIP connection.
	 */
	public OutboundSipResponse sipDial(OutboundSipRequest request) {
		return sipDial.execute(request);
	}

	/**
	 * Generates a signed JSON Web Token which can be passed to the client.
	 *
	 * @param sessionId The session ID.
	 * @param options Configuration parameters (claims) of the token, e.g. role, expiry time etc.
	 *
	 * @return The JWT with the specified properties, as a raw string.
	 * @since 8.0.0-beta2
	 */
	public String generateToken(String sessionId, TokenOptions options) {
		Jwt.Builder jwtBuilder = newJwtSupplier.get();
		if (options == null) {
			options = TokenOptions.builder().build();
		}
		options.addClaims(jwtBuilder);
		return jwtBuilder
				.addClaim("session_id", validateSessionId(sessionId))
			    .addClaim("scope", "session.connect")
				.issuedAt(ZonedDateTime.now())
				.build().generate();
	}

	/**
	 * Generates a signed JSON Web Token which can be passed to the client, using the default token options.
	 *
	 * @param sessionId The session ID.
	 *
	 * @return The JWT with the default properties, as a raw string.
	 * @see #generateToken(String, TokenOptions)
	 * @since 8.0.0-beta2
	 */
	public String generateToken(String sessionId) {
		return generateToken(sessionId, null);
	}
}
