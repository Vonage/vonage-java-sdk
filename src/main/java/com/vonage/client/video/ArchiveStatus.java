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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.vonage.client.Jsonable;

/**
 * Defines values returned by the {@link Archive#getStatus} method.
 */
public enum ArchiveStatus {
	/**
	 * The archive file is available for download from the cloud. You can get the URL of the download file by
	 * calling the {@link Archive#getUrl} method.
	 */
	AVAILABLE,

	/**
	 * The archive file has been deleted.
	 */
	DELETED,

	/**
	 * The recording of the archive failed.
	 */
	FAILED,

	/**
	 * The archive is in progress and no clients are publishing streams to the session. When an archive is in progress
	 * and any client publishes a stream, the status is STARTED. When an archive is PAUSED, nothing is recorded. When a
	 * client starts publishing a stream, the recording starts (or resumes). If all clients disconnect from a session
	 * that is being archived, the status changes to PAUSED, and after 60 seconds the archive recording stops (and the
	 * status changes to STOPPED).
	 */
	PAUSED,

	/**
	 * The archive recording has started and is in progress.
	 */
	STARTED,

	/**
	 * The archive recording has stopped, but the file is not available.
	 */
	STOPPED,

	/**
	 * The archive is available for download from the upload target Amazon S3 bucket or Windows Azure container you
	 * set up for your Vonage video project.
	 */
	UPLOADED,

	/**
	 * The archive file is no longer available in the cloud.
	 */
	EXPIRED;

	@JsonValue
	@Override
	public String toString() {
		return name().toLowerCase();
	}

	/**
	 * Convert a string to an ArchiveStatus enum.
	 *
	 * @param value The archive status as a string.
	 *
	 * @return The archive status as an enum, or {@code null} if invalid.
	 */
	@JsonCreator
	public static ArchiveStatus fromString(String value) {
		return Jsonable.fromString(value, ArchiveStatus.class);
	}
}
