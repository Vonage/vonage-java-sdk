/*
 *   Copyright 2025 Vonage
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

import com.vonage.client.DynamicEndpoint;
import com.vonage.client.HttpWrapper;
import com.vonage.client.RestEndpoint;
import com.vonage.client.VonageClient;
import com.vonage.client.auth.JWTAuthMethod;
import com.vonage.client.auth.ApiKeyHeaderAuthMethod;
import com.vonage.client.common.HttpMethod;
import com.vonage.jwt.Jwt;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A client for using the Vonage Video API. The standard way to obtain an instance of this class is to use
 * {@link VonageClient#getVideoClient()}.
 *
 * @since 8.0.0-beta1
 */
public class VideoClient {
	final Supplier<? extends Jwt.Builder> newJwtSupplier;

	final RestEndpoint<CreateSessionRequest, CreateSessionResponse[]> createSession;
	final RestEndpoint<String, ListStreamsResponse> listStreams;
	final RestEndpoint<SessionResourceRequestWrapper, GetStreamResponse> getStream;
	final RestEndpoint<SetStreamLayoutRequest, Void> setStreamLayout;
	final RestEndpoint<SignalRequest, Void> signal, signalAll;
	final RestEndpoint<SessionResourceRequestWrapper, Void> forceDisconnect, muteStream;
	final RestEndpoint<MuteSessionRequest, ProjectDetails> muteSession;
	final RestEndpoint<SipDialRequest, SipDialResponse> sipDial;
	final RestEndpoint<SendDtmfRequest, Void> sendDtmfToSession, sendDtmfToConnection;
	final RestEndpoint<ListStreamCompositionsRequest, ListArchivesResponse> listArchives;
	final RestEndpoint<String, Archive> getArchive, stopArchive;
	final RestEndpoint<Archive, Archive> createArchive;
	final RestEndpoint<StreamCompositionLayout, Void> updateArchiveLayout, updateBroadcastLayout;
	final RestEndpoint<PatchComposedStreamsRequest, Void> patchArchiveStream, patchBroadcastStream;
	final RestEndpoint<String, Void> deleteArchive, stopCaptions, stopRender;
	final RestEndpoint<ListStreamCompositionsRequest, ListBroadcastsResponse> listBroadcasts;
	final RestEndpoint<String, Broadcast> getBroadcast, stopBroadcast;
	final RestEndpoint<Broadcast, Broadcast> createBroadcast;
	final RestEndpoint<CaptionsRequest, CaptionsResponse> startCaptions;
	final RestEndpoint<ConnectRequest, ConnectResponse> connect;
	final RestEndpoint<RenderRequest, RenderResponse> startRender;
	final RestEndpoint<String, RenderResponse> getRender;
	final RestEndpoint<ListStreamCompositionsRequest, ListRendersResponse> listRenders;

	/**
	 * Constructor.
	 *
	 * @param wrapper (REQUIRED) shared HTTP wrapper object used for making REST calls.
	 */
	public VideoClient(HttpWrapper wrapper) {
		super();
		Supplier<JWTAuthMethod> jwtAuthGetter = () -> wrapper.getAuthCollection().getAuth(JWTAuthMethod.class);
		Supplier<String> appIdGetter = () -> jwtAuthGetter.get().getApplicationId();
		newJwtSupplier = () -> jwtAuthGetter.get().newJwt();

		@SuppressWarnings("unchecked")
		class Endpoint<T, R> extends DynamicEndpoint<T, R> {
			Endpoint(Function<T, String> pathGetter, HttpMethod method, R... type) {
				super(DynamicEndpoint.<T, R> builder(type)
						.authMethod(JWTAuthMethod.class, ApiKeyHeaderAuthMethod.class)
						.responseExceptionType(VideoResponseException.class)
						.requestMethod(method).wrapper(wrapper).pathGetter((de, req) -> {
							String base = de.getHttpWrapper().getHttpConfig().getVideoBaseUri();
							String end = pathGetter.apply(req);
							String mid = end.startsWith("/") ? "" : "/v2/project/" + appIdGetter.get() + "/";
							return base + mid + end;
						})
				);
			}
		}

		createSession = new Endpoint<>(req -> "/session/create", HttpMethod.POST);
		listStreams = new Endpoint<>(req -> "session/"+req+"/stream", HttpMethod.GET);
		setStreamLayout = new Endpoint<>(req -> "session/"+req.sessionId+"/stream", HttpMethod.PUT);
		getStream = new Endpoint<>(req -> "session/"+req.sessionId+"/stream/"+req.resourceId, HttpMethod.GET);
		signalAll = new Endpoint<>(req -> "session/"+req.sessionId+"/signal", HttpMethod.POST);
		signal = new Endpoint<>(req -> "session/"+req.sessionId+"/connection/"+req.connectionId+"/signal", HttpMethod.POST);
		forceDisconnect = new Endpoint<>(req -> "session/"+req.sessionId+"/connection/"+req.resourceId, HttpMethod.DELETE);
		muteStream = new Endpoint<>(req -> "session/"+req.sessionId+"/stream/"+req.resourceId+"/mute", HttpMethod.POST);
		muteSession = new Endpoint<>(req -> "session/"+req.sessionId+"/mute", HttpMethod.POST);
		updateArchiveLayout = new Endpoint<>(req -> "archive/"+req.id+"/layout", HttpMethod.PUT);
		deleteArchive = new Endpoint<>(req -> "archive/"+req, HttpMethod.DELETE);
		patchArchiveStream = new Endpoint<>(req -> "archive/"+req.id+"/streams", HttpMethod.PATCH);
		stopArchive = new Endpoint<>(req -> "archive/"+req+"/stop", HttpMethod.POST);
		createArchive = new Endpoint<>(req -> "archive", HttpMethod.POST);
		listArchives = new Endpoint<>(req -> "archive", HttpMethod.GET);
		getArchive = new Endpoint<>(req -> "archive/"+req, HttpMethod.GET);
		sendDtmfToConnection = new Endpoint<>(req -> "session/"+req.sessionId+"/connection/"+req.connectionId+"/play-dtmf", HttpMethod.POST);
		sendDtmfToSession = new Endpoint<>(req -> "session/"+req.sessionId+"/play-dtmf", HttpMethod.POST);
		listBroadcasts = new Endpoint<>(req -> "broadcast", HttpMethod.GET);
		createBroadcast = new Endpoint<>(req -> "broadcast", HttpMethod.POST);
		getBroadcast = new Endpoint<>(req -> "broadcast/"+req, HttpMethod.GET);
		stopBroadcast = new Endpoint<>(req -> "broadcast/"+req+"/stop", HttpMethod.POST);
		updateBroadcastLayout = new Endpoint<>(req -> "broadcast/"+req.id+"/layout", HttpMethod.PUT);
		patchBroadcastStream = new Endpoint<>(req -> "broadcast/"+req.id+"/streams", HttpMethod.PATCH);
		sipDial = new Endpoint<>(req -> "dial", HttpMethod.POST);
		startCaptions = new Endpoint<>(req -> "captions", HttpMethod.POST);
		stopCaptions = new Endpoint<>(req -> "captions/"+req+"/stop", HttpMethod.POST);
		connect = new Endpoint<>(req -> "connect", HttpMethod.POST);
		startRender = new Endpoint<>(req -> "render", HttpMethod.POST);
		listRenders = new Endpoint<>(req -> "render", HttpMethod.GET);
		getRender = new Endpoint<>(req -> "render/"+req, HttpMethod.GET);
		stopRender = new Endpoint<>(req -> "render/"+req, HttpMethod.DELETE);
	}

	private String validateId(String param, String name, boolean uuid) {
		if (param == null || param.isEmpty()) {
			throw new IllegalArgumentException(name+" ID is required.");
		}
		if (uuid) {
			// Validate by construction
			Objects.requireNonNull(UUID.fromString(param));
		}
		return param;
	}

	private String validateSessionId(String sessionId) {
		return validateId(sessionId, "Session", false);
	}

	private String validateConnectionId(String connectionId) {
		return validateId(connectionId, "Connection", true);
	}

	private String validateStreamId(String streamId) {
		return validateId(streamId, "Stream", true);
	}

	private String validateArchiveId(String archiveId) {
		return validateId(archiveId, "Archive", true);
	}

	private String validateBroadcastId(String broadcastId) {
		return validateId(broadcastId, "Broadcast", true);
	}

	private String validateRenderId(String renderId) {
		return validateId(renderId, "Render", true);
	}

	private <T> T validateRequest(T request) {
		if (request == null) {
			throw new IllegalArgumentException("Request properties are required.");
		}
		return request;
	}

	/**
	 * Generates a signed JSON Web Token which can be passed to the client.
	 *
	 * @param sessionId The session ID.
	 * @param options Configuration parameters (claims) of the token, e.g. role, expiry time etc.
	 *
	 * @return The JWT with the specified properties, as a raw string.
	 *
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
	 *
	 * @see #generateToken(String, TokenOptions)
	 * @since 8.0.0-beta2
	 */
	public String generateToken(String sessionId) {
		return generateToken(sessionId, null);
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
		CreateSessionResponse[] response = createSession.execute(request);
        return response.length == 0 ? new CreateSessionResponse() : response[0];
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
		return getStream.execute(new SessionResourceRequestWrapper(
				validateSessionId(sessionId),
				validateStreamId(streamId)
		));
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
	 * Sends signals to all participants in an active Vonage Video session.
	 *
	 * @param sessionId The session ID.
	 * @param request Signal payload.
	 */
	public void signalAll(String sessionId, SignalRequest request) {
		validateRequest(request);
		request.sessionId = validateSessionId(sessionId);
		signalAll.execute(request);
	}

	/**
	 * Sends signal to a specific participant in an active Vonage Video session.
	 *
	 * @param sessionId The session ID.
	 * @param connectionId Specific publisher connection ID.
	 * @param request Signal payload.
	 */
	public void signal(String sessionId, String connectionId, SignalRequest request) {
		validateRequest(request);
		request.sessionId = validateSessionId(sessionId);
		request.connectionId = validateConnectionId(connectionId);
		signal.execute(request);
	}

	/**
	 * Force a client to disconnect from a session.
	 *
	 * @param sessionId The session ID.
	 * @param connectionId Specific publisher connection ID.
	 */
	public void forceDisconnect(String sessionId, String connectionId) {
		forceDisconnect.execute(new SessionResourceRequestWrapper(
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
		muteStream.execute(new SessionResourceRequestWrapper(
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
	 *
	 * @return Details about the Vonage Video project.
	 */
	public ProjectDetails muteSession(String sessionId, boolean active, Collection<String> excludedStreamIds) {
		return muteSession.execute(new MuteSessionRequest(
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
	 * Use this method to connect your SIP platform to a Vonage video session.
	 * The audio from your end of the SIP call is added to the video session as an audio-only stream. The Vonage
	 * Media Router mixes audio from other streams in the session and sends the mixed audio to your SIP endpoint.
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
	 *
	 * @since 8.0.0-beta4
	 */
	public SipDialResponse sipDial(SipDialRequest request) {
		return sipDial.execute(validateRequest(request));
	}

	/**
	 * Play DTMF tones into a specific connection.
	 * Telephony events are negotiated over SDP and transmitted as RFC4733/RFC2833 digits to the remote endpoint.
	 *
	 * @param sessionId The session ID.
	 * @param connectionId Specific publisher connection ID.
	 * @param digits The string of DTMF digits to send. This can include 0-9, '*', '#', and 'p'.
	 * A 'p' indicates a pause of 500ms (if you need to add a delay in sending the digits).
	 *
	 * @since 8.0.0-beta4
	 */
	public void sendDtmf(String sessionId, String connectionId, String digits) {
		sendDtmfToConnection.execute(new SendDtmfRequest(
				validateSessionId(sessionId), validateConnectionId(connectionId), digits
		));
	}

	/**
	 * Play DTMF tones into a SIP call.
	 * Telephony events are negotiated over SDP and transmitted as RFC4733/RFC2833 digits to the remote endpoint.
	 *
	 * @param sessionId The session ID.
	 * @param digits The string of DTMF digits to send. This can include 0-9, '*', '#', and 'p'.
	 * A 'p' indicates a pause of 500ms (if you need to add a delay in sending the digits).
	 *
	 * @since 8.0.0-beta4
	 */
	public void sendDtmf(String sessionId, String digits) {
		sendDtmfToSession.execute(new SendDtmfRequest(
				validateSessionId(sessionId), null, digits
		));
	}

	/**
	 * List all archives in the application. Deleted archives are not included in the results.
	 *
	 * @return The list of archives up to the first 1000, in order from newest to oldest.
	 *
	 * @see #listArchives(ListStreamCompositionsRequest)
	 */
	public List<Archive> listArchives() {
		return listArchives(ListStreamCompositionsRequest.maxResults());
	}

	/**
	 * List all archives in the application. Deleted archives are not included in the results.
	 *
	 * @param request (OPTIONAL) Filter properties of the request.
	 *
	 * @return The list of archives matching the filter criteria, in order from newest to oldest.
	 */
	public List<Archive> listArchives(ListStreamCompositionsRequest request) {
		return listArchives.execute(request).getItems();
	}

	/**
	 * Retrieve information about a specific archive.
	 *
	 * @param archiveId ID of the archive to retrieve.
	 *
	 * @return The Archive corresponding to the archiveId.
	 */
	public Archive getArchive(String archiveId) {
		return getArchive.execute(validateArchiveId(archiveId));
	}

	/**
	 * Create a new archive.
	 *
	 * @param request Properties of the archive.
	 *
	 * @return The created Archive.
	 */
	public Archive createArchive(Archive request) {
		return createArchive.execute(validateRequest(request));
	}

	/**
	 * Dynamically change the layout type of a composed archive while it is being recorded.
	 *
	 * @param archiveId ID of the archive to change.
	 * @param layout Properties of the layout change request.
	 */
	public void updateArchiveLayout(String archiveId, StreamCompositionLayout layout) {
		validateRequest(layout);
		layout.id = validateArchiveId(archiveId);
		updateArchiveLayout.execute(layout);
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
		patchArchiveStream(archiveId, new PatchComposedStreamsRequest(validateStreamId(streamId), audio, video));
	}

	/**
	 * Removes a stream from a composed archive that was started with the
	 * {@code streamMode} set to {@link StreamMode#MANUAL}.
	 *
	 * @param archiveId ID of the archive.
	 * @param streamId ID of the stream to remove.
	 */
	public void removeArchiveStream(String archiveId, String streamId) {
		patchArchiveStream(archiveId, new PatchComposedStreamsRequest(validateStreamId(streamId)));
	}

	private void patchArchiveStream(String archiveId, PatchComposedStreamsRequest request) {
		PatchComposedStreamsRequest pasr = validateRequest(request);
		pasr.id = validateArchiveId(archiveId);
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
	 *
	 * @return The Archive corresponding to the archiveId.
	 */
	public Archive stopArchive(String archiveId) {
		return stopArchive.execute(validateArchiveId(archiveId));
	}

	/**
	 * Deletes an archive. You can only delete an archive which has a status of
	 * {@linkplain ArchiveStatus#AVAILABLE} or {@linkplain ArchiveStatus#UPLOADED}.
	 * Deleting an archive removes its record from the list of archives (see {@linkplain #listArchives()}).
	 * For an "available" archive, it also removes the archive file, making it unavailable for download.
	 *
	 * @param archiveId ID of the archive to delete.
	 */
	public void deleteArchive(String archiveId) {
		deleteArchive.execute(validateArchiveId(archiveId));
	}

	/**
	 * List all broadcasts that are in progress and started in the application.
	 * Completed broadcasts are not included in the listing.
	 *
	 * @return The list of broadcasts up to the first 1000, in order from newest to oldest.
	 *
	 * @see #listBroadcasts(ListStreamCompositionsRequest)
	 * @since 8.0.0-beta4
	 */
	public List<Broadcast> listBroadcasts() {
		return listBroadcasts(ListStreamCompositionsRequest.maxResults());
	}

	/**
	 * List all broadcasts that are in progress and started in the application.
	 * Completed broadcasts are not included in the listing.
	 *
	 * @param request (OPTIONAL) Filter properties of the request.
	 *
	 * @return The list of broadcasts matching the filter criteria, in order from newest to oldest.
	 *
	 * @since 8.0.0-beta4
	 */
	public List<Broadcast> listBroadcasts(ListStreamCompositionsRequest request) {
		return listBroadcasts.execute(request).getItems();
	}

	/**
	 * Get Information about a Broadcast that is in progress.
	 *
	 * @param broadcastId ID of the broadcast to retrieve.
	 *
	 * @return The Broadcast corresponding to the broadcastId.
	 *
	 * @since 8.0.0-beta4
	 */
	public Broadcast getBroadcast(String broadcastId) {
		return getBroadcast.execute(validateBroadcastId(broadcastId));
	}

	/**
	 * Start a new live streaming broadcast.
	 * This broadcasts the session to an HLS (HTTP live streaming) or to RTMP streams.
	 * To successfully start broadcasting a session, at least one client must be connected to the session.
	 * <p>
	 * The live streaming broadcast can target one HLS endpoint and up to five RTMP servers simultaneously for
	 * a session. You can only start live streaming for sessions that use the Vonage Media Router (with the
	 * media mode set to {@linkplain MediaMode#ROUTED}); you cannot use live streaming with sessions that have the
	 * media mode set to {@linkplain MediaMode#RELAYED}.
	 *
	 * @param request Broadcast object with initial properties.
	 *
	 * @return The same Broadcast object that was passed in with additional fields populated from the server's response.
	 *
	 * @since 8.0.0-beta4
	 */
	public Broadcast createBroadcast(Broadcast request) {
		return createBroadcast.execute(validateRequest(request));
	}

	/**
	 * Dynamically change the layout type of a live streaming broadcast.
	 *
	 * @param broadcastId ID of the broadcast to change.
	 * @param layout Properties of the layout change request.
	 *
	 * @since 8.0.0-beta4
	 */
	public void updateBroadcastLayout(String broadcastId, StreamCompositionLayout layout) {
		validateRequest(layout);
		layout.id = validateBroadcastId(broadcastId);
		updateBroadcastLayout.execute(layout);
	}

	/**
	 * Adds a stream to a live broadcast that was started with the {@code streamMode} set to {@link StreamMode#MANUAL}.
	 *
	 * @param broadcastId ID of the broadcast.
	 * @param streamId ID of the stream to add.
	 *
	 * @see #addBroadcastStream(String, String, Boolean, Boolean)
	 * @since 8.0.0-beta4
	 */
	public void addBroadcastStream(String broadcastId, String streamId) {
		addBroadcastStream(broadcastId, streamId, null, null);
	}

	/**
	 * Adds a stream to a live broadcast that was started with the {@code streamMode} set to {@link StreamMode#MANUAL}.
	 *
	 * @param broadcastId ID of the broadcast.
	 * @param streamId ID of the stream to add.
	 * @param audio (OPTIONAL) Whether the broadcast should include the stream's audio (true by default).
	 * @param video (OPTIONAL) Whether the broadcast should include the stream's video (true by default).
	 *
	 * @since 8.0.0-beta4
	 */
	public void addBroadcastStream(String broadcastId, String streamId, Boolean audio, Boolean video) {
		patchBroadcastStream(broadcastId, new PatchComposedStreamsRequest(validateStreamId(streamId), audio, video));
	}

	/**
	 * Removes a stream from a live broadcast that was started with the
	 * {@code streamMode} set to {@link StreamMode#MANUAL}.
	 *
	 * @param broadcastId ID of the broadcastId.
	 * @param streamId ID of the stream to remove.
	 *
	 * @since 8.0.0-beta4
	 */
	public void removeBroadcastStream(String broadcastId, String streamId) {
		patchBroadcastStream(broadcastId, new PatchComposedStreamsRequest(validateStreamId(streamId)));
	}

	private void patchBroadcastStream(String broadcastId, PatchComposedStreamsRequest request) {
		PatchComposedStreamsRequest pasr = validateRequest(request);
		pasr.id = validateBroadcastId(broadcastId);
		patchBroadcastStream.execute(pasr);
	}

	/**
	 * Stop a live broadcast.
	 * Note that a broadcast stops automatically 60 seconds after the last client disconnects from the session.
	 * There is a default maximum duration of 4 hours (14400 seconds) for each HLS and RTMP stream (the live
	 * stream broadcast automatically stops when this duration is reached). You can change the maximum duration for
	 * the broadcast by setting the {@link Broadcast.Builder#maxDuration(int)} property when you start the broadcast
	 * using the {@link #createBroadcast(Broadcast)} method.
	 *
	 * @param broadcastId ID of the broadcast to stop.
	 *
	 * @return Details of the Broadcast.
	 *
	 * @since 8.0.0-beta4
	 */
	public Broadcast stopBroadcast(String broadcastId) {
		return stopBroadcast.execute(validateBroadcastId(broadcastId));
	}

	/**
	 * Start real-time Live Captions for a Vonage Video Session.
	 * <p>
	 * The maximum allowed duration is 4 hours, after which the audio captioning will stop without any
	 * effect on the ongoing Vonage Video Session. An event will be posted to your callback URL if provided
	 * when starting the captions. Each Vonage Video Session supports only one audio captioning session.
	 *
	 * @param request Properties of the live captioning.
	 *
	 * @return Live captioning metadata.
	 *
	 * @since 8.5.0
	 */
	public CaptionsResponse startCaptions(CaptionsRequest request) {
		return startCaptions.execute(validateRequest(request));
	}

	/**
	 * Stop live captions for a session.
	 *
	 * @param captionsId ID of the live captions to stop.
	 *
	 * @since 8.5.0
	 */
	public void stopCaptions(String captionsId) {
		stopCaptions.execute(validateId(captionsId, "Captions", true));
	}

	/**
	 * Send audio from a Vonage Video API session to a WebSocket.
	 *
	 * @param request Properties of the WebSocket connection.
	 *
	 * @return The connection metadata.
	 *
	 * @since 8.5.0
	 */
	public ConnectResponse connectToWebsocket(ConnectRequest request) {
		return connect.execute(validateRequest(request));
	}

	/**
	 * List all Experience Composers in the application.
	 *
	 * @return The list of Experience Composers up to the first 1000, in order from newest to oldest.
	 *
	 * @since 8.6.0
	 * @see #listRenders(ListStreamCompositionsRequest)
	 */
	public List<RenderResponse> listRenders() {
		return listRenders(ListStreamCompositionsRequest.maxResults());
	}

	/**
	 * List Experience Composers in the application, with the specified offset and number of results.
	 *
	 * @param request (OPTIONAL) Filter properties of the request.
	 * Note that only {@code offset} and {@code count} are applicable here.
	 *
	 * @return The list of broadcasts matching the filter criteria, in order from newest to oldest.
	 *
	 * @since 8.6.0
	 * @see #listRenders()
	 */
	public List<RenderResponse> listRenders(ListStreamCompositionsRequest request) {
		return listRenders.execute(request).getItems();
	}

	/**
	 * Retrieve details on an Experience Composer.
	 *
	 * @param renderId ID of the Experience Composer instance.
	 *
	 * @return The Experience Composer corresponding to the specified renderId.
	 *
	 * @since 8.6.0
	 */
	public RenderResponse getRender(String renderId) {
		return getRender.execute(validateRenderId(renderId));
	}

	/**
	 * Create an Experience Composer for a Vonage Video session.
	 *
	 * @param request Properties of the Experience Composer.
	 *
	 * @return Details of the created Experience Composer.
	 *
	 * @since 8.6.0
	 */
	public RenderResponse startRender(RenderRequest request) {
		return startRender.execute(validateRequest(request));
	}

	/**
	 * Stop an active Experience Composer.
	 *
	 * @param renderId ID of the Experience Composer instance.
	 *
	 * @since 8.6.0
	 */
	public void stopRender(String renderId) {
		stopRender.execute(validateRenderId(renderId));
	}
}
