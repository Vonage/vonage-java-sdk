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
 * A client for using the Vonage Video API. The standard way to obtain an instance of this class is to use
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
	final SetArchiveLayoutEndpoint setArchiveLayout;
	final DeleteArchiveEndpoint deleteArchive;
	final PatchArchiveStreamEndpoint patchArchiveStream;
	final StopArchiveEndpoint stopArchive;
	final GetArchiveEndpoint getArchive;
	final ListArchivesEndpoint listArchives;
	final CreateArchiveEndpoint createArchive;

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
		setArchiveLayout = new SetArchiveLayoutEndpoint(httpWrapper);
		deleteArchive = new DeleteArchiveEndpoint(httpWrapper);
		patchArchiveStream = new PatchArchiveStreamEndpoint(httpWrapper);
		stopArchive = new StopArchiveEndpoint(httpWrapper);
		getArchive = new GetArchiveEndpoint(httpWrapper);
		listArchives = new ListArchivesEndpoint(httpWrapper);
		createArchive = new CreateArchiveEndpoint(httpWrapper);
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
	 * @param streamId ID of the stream to retrieve.
	 * @return Details of the requested stream.
	 */
	public GetStreamResponse getStream(String sessionId, String streamId) {;
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

	/**
	 * Dynamically change the layout type of a composed archive while it is being recorded.
	 *
	 * @param archiveId ID of the archive to change.
	 * @param request Properties of the layout change request.
	 */
	public void setArchiveLayout(String archiveId, ArchiveLayout request) {
		setArchiveLayout.execute(new SetArchiveLayoutRequestWrapper(
				validateArchiveId(archiveId),
				validateRequest(request)
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
	 * <code>streamMode</code> set to {@link StreamMode#MANUAL}.
	 *
	 * @param archiveId ID of the archive.
	 * @param streamId ID of the stream to remove.
	 */
	public void removeArchiveStream(String archiveId, String streamId) {
		patchArchiveStream(archiveId, new PatchArchiveStreamRequest(validateStreamId(streamId)));
	}

	/**
	 * Adds a stream to a composed archive that was started with the
	 * <code>streamMode</code> set to {@link StreamMode#MANUAL}.
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
	 */
	public List<Archive> listArchives() {
		return listArchives(null, null, null);
	}

	/**
	 * List all archives in an application.
	 *
	 * @param sessionId Set a sessionId query parameter to list archives for a specific session ID.
	 * This is useful when listing multiple archives for an automatically archived session.
	 *
	 * @return The list of archives for the session, in order from newest to oldest.
	 */
	public List<Archive> listArchives(String sessionId) {
		return listArchives(validateSessionId(sessionId), null, null);
	}

	/**
	 * List all archives in an application.
	 *
	 * @param sessionId Set a sessionId query parameter to list archives for a specific session ID. This is useful
	 * when listing multiple archives for an automatically archived session.
	 *
	 * @param offset Set an offset query parameters to specify the index offset of the first archive.
	 * 0 is offset of the most recently started archive (excluding deleted archive). 1 is the offset of the archive
	 * that started prior to the most recent archive. The default value is 0.
	 *
	 * @param count Set a count query parameter to limit the number of archives to be returned. The default number of
	 * archives returned is 50 (or fewer, if there are fewer than 50 archives). The maximum number of archives the
	 * call will return is 1000.
	 *
	 * @return The list of archives matching the filter criteria, in order from newest to oldest.
	 */
	public List<Archive> listArchives(String sessionId, Integer offset, Integer count) {
		return listArchives.execute(new ListArchivesRequestWrapper(sessionId, offset, count)).getItems();
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
}
