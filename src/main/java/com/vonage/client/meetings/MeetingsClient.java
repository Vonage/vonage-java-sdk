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
package com.vonage.client.meetings;

import com.vonage.client.HttpWrapper;
import com.vonage.client.VonageBadRequestException;
import com.vonage.client.VonageClientException;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.RequestBuilder;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class MeetingsClient {
	final HttpClient httpClient;
	final ListRoomsEndpoint listRooms;
	final GetRoomEndpoint getRoom;
	final CreateRoomEndpoint createRoom;
	final UpdateRoomEndpoint updateRoom;
	final SearchThemeRoomsEndpoint searchThemeRooms;
	final ListThemesEndpoint listThemes;
	final GetThemeEndpoint getTheme;
	final CreateThemeEndpoint createTheme;
	final UpdateThemeEndpoint updateTheme;
	final DeleteThemeEndpoint deleteTheme;
	final ListRecordingsEndpoint listRecordings;
	final GetRecordingEndpoint getRecording;
	final DeleteRecordingEndpoint deleteRecording;
	final ListDialNumbersEndpoint listDialNumbers;
	final UpdateApplicationEndpoint updateApplication;
	final FinalizeLogosEndpoint finalizeLogos;
	final GetLogoUploadUrlsEndpoint getLogoUploadUrls;

	/**
	 * Constructor.
	 *
	 * @param httpWrapper (REQUIRED) shared HTTP wrapper object used for making REST calls.
	 */
	public MeetingsClient(HttpWrapper httpWrapper) {
		httpClient = httpWrapper.getHttpClient();
		listRooms = new ListRoomsEndpoint(httpWrapper);
		getRoom = new GetRoomEndpoint(httpWrapper);
		createRoom = new CreateRoomEndpoint(httpWrapper);
		updateRoom = new UpdateRoomEndpoint(httpWrapper);
		searchThemeRooms = new SearchThemeRoomsEndpoint(httpWrapper);
		listThemes = new ListThemesEndpoint(httpWrapper);
		getTheme = new GetThemeEndpoint(httpWrapper);
		createTheme = new CreateThemeEndpoint(httpWrapper);
		updateTheme = new UpdateThemeEndpoint(httpWrapper);
		deleteTheme = new DeleteThemeEndpoint(httpWrapper);
		listRecordings = new ListRecordingsEndpoint(httpWrapper);
		getRecording = new GetRecordingEndpoint(httpWrapper);
		deleteRecording = new DeleteRecordingEndpoint(httpWrapper);
		listDialNumbers = new ListDialNumbersEndpoint(httpWrapper);
		updateApplication = new UpdateApplicationEndpoint(httpWrapper);
		finalizeLogos = new FinalizeLogosEndpoint(httpWrapper);
		getLogoUploadUrls = new GetLogoUploadUrlsEndpoint(httpWrapper);
	}

	static UUID validateThemeId(UUID themeId) {
		return Objects.requireNonNull(themeId,  "Theme ID is required.");
	}

	static UUID validateRoomId(UUID roomId) {
		return Objects.requireNonNull(roomId,  "Room ID is required.");
	}

	static UUID validateRecordingId(UUID recordingId) {
		return Objects.requireNonNull(recordingId,  "Recording ID is required.");
	}

	static String validateSessionId(String sessionId) {
		if (sessionId == null || sessionId.trim().isEmpty()) {
			throw new IllegalArgumentException("Session ID cannot be null or empty.");
		}
		return sessionId;
	}


	/**
	 * Get all available rooms in the application.
	 *
	 * @param startId The ID to start returning events at (inclusive).
	 * @param endId The ID to end returning events at (exclusive).
	 * @param pageSize Number of results per page.
	 *
	 * @return The HAL response.
	 */
	public ListRoomsResponse listRooms(Integer startId, Integer endId, Integer pageSize) {
		return listRooms.execute(new ListRoomsRequest(startId, endId, pageSize, null));
	}

	/**
	 * Get all available rooms in the application.
	 *
	 * @return The list of all meeting rooms.
	 */
	public List<MeetingRoom> listRooms() {
		return listRooms(null, null, Integer.MAX_VALUE).getMeetingRooms();
	}

	/**
	 * Get details of an existing room.
	 *
	 * @param roomId ID of the room to retrieve.
	 *
	 * @return The meeting room associated with the ID.
	 */
	public MeetingRoom getRoom(UUID roomId) {
		return getRoom.execute(validateRoomId(roomId));
	}

	/**
	 * Create a new room.
	 *
	 * @param room Properties of the meeting room.
	 *
	 * @return Details of the created meeting room.
	 */
	public MeetingRoom createRoom(MeetingRoom room) {
		return createRoom.execute(Objects.requireNonNull(room, "Meeting room is required."));
	}

	/**
	 * Update an existing room.
	 *
	 * @param roomId ID of the meeting room to be updated.
	 * @param roomUpdate Properties of the meeting room to change.
	 *
	 * @return Details of the updated meeting room.
	 */
	public MeetingRoom updateRoom(UUID roomId, UpdateRoomRequest roomUpdate) {
		Objects.requireNonNull(roomUpdate, "Room update request properties is required.");
		roomUpdate.roomId = validateRoomId(roomId);
		return updateRoom.execute(roomUpdate);
	}

	/**
	 * Get rooms that are associated with a theme ID.
	 *
	 * @param themeId The theme ID to filter by.
	 * @param startId The ID to start returning events at (inclusive).
	 * @param endId The ID to end returning events at (exclusive).
	 * @param pageSize The number of results per page.
	 *
	 * @return The HAL response.
	 */
	public ListRoomsResponse searchRoomsByTheme(UUID themeId, Integer startId, Integer endId, Integer pageSize) {
		return searchThemeRooms.execute(new ListRoomsRequest(startId, endId, pageSize, validateThemeId(themeId)));
	}

	/**
	 * Get rooms that are associated with a theme ID.
	 *
	 * @param themeId The theme ID to filter by.
	 *
	 * @return The list of rooms which use the theme.
	 */
	public List<MeetingRoom> searchRoomsByTheme(UUID themeId) {
		return searchRoomsByTheme(themeId, null, null, Integer.MAX_VALUE).getMeetingRooms();
	}

	/**
	 * Get all application themes.
	 *
	 * @return The list of themes.
	 */
	public List<Theme> listThemes() {
		return listThemes.execute(null);
	}

	/**
	 * Retrieve details of a theme by ID.
	 *
	 * @param themeId The theme ID.
	 *
	 * @return The theme associated with the ID.
	 */
	public Theme getTheme(UUID themeId) {
		return getTheme.execute(validateThemeId(themeId));
	}

	/**
	 * Create a new theme.
	 *
	 * @param theme The partial theme properties.
	 *
	 * @return The full created theme details.
	 */
	public Theme createTheme(Theme theme) {
		Objects.requireNonNull(theme, "Theme creation properties are required.");
		Objects.requireNonNull(theme.getBrandText(), "Brand text is required.");
		Objects.requireNonNull(theme.getMainColor(), "Main color is required.");
		return createTheme.execute(theme);
	}

	/**
	 * Update an existing theme.
	 *
	 * @param themeId ID of the theme to update.
	 * @param theme The partial theme properties to update.
	 *
	 * @return The full updated theme details.
	 */
	public Theme updateTheme(UUID themeId, Theme theme) {
		Objects.requireNonNull(theme, "Theme update properties are required.");
		theme.themeId = validateThemeId(themeId);
		return updateTheme.execute(theme);
	}

	/**
	 * Delete a theme by its ID.
	 *
	 * @param themeId ID of the theme to delete.
	 * @param force Whether to delete the theme even if theme is used by rooms or as application default theme.
	 */
	public void deleteTheme(UUID themeId, boolean force) {
		deleteTheme.execute(new DeleteThemeRequest(validateThemeId(themeId), force));
	}

	/**
	 * Get recordings of a meeting session.
	 *
	 * @param sessionId The session ID to filter recordings by.
	 *
	 * @return The list of recordings for the session.
	 */
	public List<Recording> listRecordings(String sessionId) {
		ListRecordingsResponse response = listRecordings.execute(validateSessionId(sessionId));
		List<Recording> recordings = response.getRecordings();
		return recordings != null ? recordings : Collections.emptyList();
	}

	/**
	 * Get details of a recording.
	 *
	 * @param recordingId ID of the recording to retrieve.
	 *
	 * @return The recording properties.
	 */
	public Recording getRecording(UUID recordingId) {
		return getRecording.execute(validateRecordingId(recordingId));
	}

	/**
	 * Delete a recording.
	 *
	 * @param recordingId ID of the recording to delete.
	 */
	public void deleteRecording(UUID recordingId) {
		deleteRecording.execute(validateRecordingId(recordingId));
	}

	/**
	 * Get numbers that can be used to dial into a meeting.
	 *
	 * @return The list of dial-in numbers, along with their country code.
	 */
	public List<DialInNumber> listDialNumbers() {
		return listDialNumbers.execute(null);
	}

	/**
	 * Update an existing application.
	 *
	 * @param updateRequest Properties of the application to update.
	 *
	 * @return The updated application details.
	 */
	public Application updateApplication(UpdateApplicationRequest updateRequest) {
		return updateApplication.execute(Objects.requireNonNull(
				updateRequest, "Application update properties are required.")
		);
	}

	/**
	 * Change logos to be permanent for a given theme.
	 *
	 * @param themeId The theme ID containing the logos.
	 * @param keys List of temporary theme's logo keys to make permanent
	 */
	public void finalizeLogos(UUID themeId, List<String> keys) {
		if (keys == null || keys.isEmpty()) {
			throw new IllegalArgumentException("Logo keys are required.");
		}
		finalizeLogos.execute(new FinalizeLogosRequest(validateThemeId(themeId), keys));
	}

	/**
	 * Get URLs that can be used to upload logos for a theme via a POST.
	 *
	 * @return List of URLs and respective credentials / tokens needed for uploading logos to them.
	 */
	List<LogoUploadsUrlResponse> listLogoUploadUrls() {
		return getLogoUploadUrls.execute(null);
	}

	/**
	 * Uploads a logo to the cloud so that it can be used in themes.
	 *
	 * @param logoFile Absolute path to the image.
	 * @param details Credentials, logo key and URL to facilitate the upload request.
	 */
	void uploadLogo(Path logoFile, LogoUploadsUrlResponse details) {
		try {
			LogoUploadsUrlResponse.Fields fields = details.getFields();
			HttpResponse response = httpClient.execute(RequestBuilder.post(details.getUrl())
					.setHeader("Content-Type", "application/json")
					.addParameter("Content-Type", fields.getContentType().toString())
					.addParameter("key", fields.getKey())
					.addParameter("logoType", fields.getLogoType().toString())
					.addParameter("bucket", fields.getBucket())
					.addParameter("X-Amz-Algorithm", fields.getAmzAlgorithm())
					.addParameter("X-Amz-Credential", fields.getAmzCredential())
					.addParameter("X-Amz-Date", fields.getAmzDate())
					.addParameter("X-Amz-Security-Token", fields.getAmzSecurityToken())
					.addParameter("Policy", fields.getPolicy())
					.addParameter("X-Amz-Signature", fields.getAmzSignature())
					.addParameter("file", "@"+logoFile)
					.build()
			);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != 204) {
				throw new VonageBadRequestException(
						"Logo upload failed ("+status.getStatusCode()+"): "+status.getReasonPhrase()
				);
			}
		}
		catch (IOException ex) {
			throw new VonageClientException(ex);
		}
	}
}
