package com.vonage.client.video;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Defines values used in the {@link CreateArchiveRequest.Builder#streamMode(StreamMode)} method and returned by the
 * {@link Archive#getStreamMode()} method.
 */
public enum StreamMode {
	/**
	 * Streams will be automatically included in the broadcast or archive.
	 */
	AUTO,

	/**
	 * Streams will be included in the broadcast or archive based on calls to the
	 * {@link VideoClient#addArchiveStream(String, String, Boolean, Boolean)} and
	 * {@link VideoClient#removeArchiveStream(String, String)} methods.
	 */
	MANUAL;

	@JsonValue
	@Override
	public String toString() {
		return name().toLowerCase();
	}

	@JsonCreator
	public static StreamMode fromString(String value) {
		try {
			return StreamMode.valueOf(value.toUpperCase());
		}
		catch (NullPointerException | IllegalArgumentException ex) {
			return null;
		}
	}
}
