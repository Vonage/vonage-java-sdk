package com.vonage.client.video;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.vonage.client.Jsonable;

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

	/**
	 * Parse a string to an OutputMode enum.
	 *
	 * @param value The string to convert.
	 *
	 * @return The OutputMode enum, or {@code null} if invalid.
	 */
	@JsonCreator
	public static OutputMode fromString(String value) {
		return Jsonable.fromString(value, OutputMode.class);
	}
}
