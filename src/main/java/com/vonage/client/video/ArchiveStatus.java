package com.vonage.client.video;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Defines values returned by the {@link Archive#getStatus} method.
 */
public enum ArchiveStatus {
	/**
	 * The archive file is available for download from the OpenTok cloud. You can get the URL of the download file by
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
	 * The archive is available for download from the the upload target Amazon S3 bucket or Windows Azure container you
	 * set up for your
	 * <a href="https://tokbox.com/account">OpenTok project</a>.
	 */
	UPLOADED,

	/**
	 * The archive file is no longer available at the OpenTok cloud.
	 */
	EXPIRED;

	@JsonValue
	@Override
	public String toString() {
		return name().toLowerCase();
	}

	@JsonCreator
	public static ArchiveStatus fromString(String value) {
		try {
			return ArchiveStatus.valueOf(value.toUpperCase());
		}
		catch (NullPointerException | IllegalArgumentException ex) {
			return null;
		}
	}
}
