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
import java.util.List;
import java.util.UUID;

public class MeetingsClient {
	final GetAvailableRoomsEndpoint getAvailableRooms;
	final GetRoomEndpoint getRoom;
	final CreateRoomEndpoint createRoom;
	final UpdateRoomEndpoint updateRoom;
	final GetThemeRoomsEndpoint getThemeRooms;
	final GetThemesEndpoint getThemes;
	final GetThemeEndpoint getTheme;
	final CreateThemeEndpoint createTheme;
	final UpdateThemeEndpoint updateTheme;
	final DeleteThemeEndpoint deleteTheme;
	final GetRecordingsEndpoint getRecordings;
	final GetRecordingEndpoint getRecording;
	final DeleteRecordingEndpoint deleteRecording;
	final GetDialNumbersEndpoint getDialNumbers;
	final GetLogoUploadUrlsEndpoint getLogoUploadUrls;
	final FinalizeLogosEndpoint finalizeLogos;
	final UpdateApplicationEndpoint updateApplication;

	/**
	 * Constructor.
	 *
	 * @param httpWrapper (REQUIRED) shared HTTP wrapper object used for making REST calls.
	 */
	public MeetingsClient(HttpWrapper httpWrapper) {
		getAvailableRooms = new GetAvailableRoomsEndpoint(httpWrapper);
		getRoom = new GetRoomEndpoint(httpWrapper);
		createRoom = new CreateRoomEndpoint(httpWrapper);
		updateRoom = new UpdateRoomEndpoint(httpWrapper);
		getThemeRooms = new GetThemeRoomsEndpoint(httpWrapper);
		getThemes = new GetThemesEndpoint(httpWrapper);
		getTheme = new GetThemeEndpoint(httpWrapper);
		createTheme = new CreateThemeEndpoint(httpWrapper);
		updateTheme = new UpdateThemeEndpoint(httpWrapper);
		deleteTheme = new DeleteThemeEndpoint(httpWrapper);
		getRecordings = new GetRecordingsEndpoint(httpWrapper);
		getRecording = new GetRecordingEndpoint(httpWrapper);
		deleteRecording = new DeleteRecordingEndpoint(httpWrapper);
		getDialNumbers = new GetDialNumbersEndpoint(httpWrapper);
		getLogoUploadUrls = new GetLogoUploadUrlsEndpoint(httpWrapper);
		finalizeLogos = new FinalizeLogosEndpoint(httpWrapper);
		updateApplication = new UpdateApplicationEndpoint(httpWrapper);
	}

	public GetRoomsResponse getAvailableRooms(String startId, String endId) {
		return getAvailableRooms.execute(new GetRoomsRequest(startId, endId, null));
	}

	public MeetingRoom getRoom(String roomId) {
		return getRoom.execute(roomId);
	}

	public MeetingRoom createRoom(MeetingRoom request) {
		return createRoom.execute(request);
	}

	public MeetingRoom updateRoom(UpdateRoomRequest request) {
		return updateRoom.execute(request);
	}

	public GetRoomsResponse getThemeRooms(UUID themeId, String startId, String endId) {
		return getThemeRooms.execute(new GetRoomsRequest(startId, endId, themeId.toString()));
	}

	public List<Theme> getThemes() {
		return getThemes.execute(null);
	}

	public Theme getTheme(String themeId) {
		return getTheme.execute(themeId);
	}

	public Theme createTheme(Theme request) {
		return createTheme.execute(request);
	}

	public Theme updateTheme(Theme request) {
		return updateTheme.execute(request);
	}

	/**
	 * Delete a theme by its ID.
	 *
	 * @param themeId UUID of the theme to delete.
	 * @param force Whether to delete the theme even if theme is used by rooms or as application default theme.
	 */
	public void deleteTheme(String themeId, boolean force) {
		deleteTheme.execute(new DeleteThemeRequest(themeId, force));
	}

	public List<Recording> getRecordings(String sessionId) {
		return getRecordings.execute(sessionId).getRecordings();
	}

	public Recording getRecording(String recordingId) {
		return getRecording.execute(recordingId);
	}

	public void deleteRecording(String recordingId) {
		deleteRecording.execute(recordingId);
	}

	public List<DialNumbers> getDialNumbers() {
		return getDialNumbers.execute(null);
	}

	public List<LogoUploadsUrlResponse> getLogoUploadUrls() {
		return getLogoUploadUrls.execute(null);
	}

	/**
	 * Change logos to be permanent.
	 *
	 * @param keys List of temporary theme's logos to make permanent
	 */
	public void finalizeLogos(List<String> keys) {
		finalizeLogos.execute(new FinalizeLogosRequest(keys));
	}

	public Application updateApplication(UpdateApplicationRequest request) {
		return updateApplication.execute(request);
	}
}
