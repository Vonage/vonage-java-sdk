package com.vonage.client.video;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Defines values for how streams will be selected for broadcasts and archives.
 */
public enum StreamMode {
	/**
	 * Streams will be automatically included in the archive or broadcast.
	 */
	AUTO,

	/**
	 * Streams will be included in the archive or broadcast based on calls to the
	 * {@link VideoClient#addArchiveStream(String, String, Boolean, Boolean)} /
	 * {@link VideoClient#addBroadcastStream(String, String, Boolean, Boolean)} and
	 * {@link VideoClient#removeArchiveStream(String, String)} /
	 * {@link VideoClient#removeBroadcastStream(String, String)} methods.
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
