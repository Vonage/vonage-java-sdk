package com.vonage.client.video;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Defines values used in the {@link Archive.Builder#outputMode(OutputMode)} method and returned by the
 * {@link Archive#getOutputMode} method.
 */
public enum OutputMode {
	/**
	 * All streams in the archive are recorded to a single (composed) file.
	 */
	COMPOSED,
	/**
	 * Each stream in the archive is recorded to its own individual file.
	 */
	INDIVIDUAL;

	@JsonValue
	@Override
	public String toString() {
		return name().toLowerCase();
	}

	@JsonCreator
	public static OutputMode fromString(String value) {
		try {
			return OutputMode.valueOf(value.toUpperCase());
		}
		catch (NullPointerException | IllegalArgumentException ex) {
			return null;
		}
	}
}
