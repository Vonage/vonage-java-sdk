package com.vonage.client.video;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Defines valid video resolutions for an archive.
 */
public enum Resolution {
	/**
	 * 480p landscape (640x480)
	 */
	SD_LANDSCAPE("640x480"),

	/**
	 * 480p portrait (480x640)
	 */
	SD_PORTRAIT("480x640"),

	/**
	 * 720p landscape (1280x720)
	 */
	HD_LANDSCAPE("1280x720"),

	/**
	 * 720p portrait (720x1280)
	 */
	HD_PORTRAIT("720x1280"),

	/**
	 * 1080p landscape (1920x1080)
	 */
	FHD_LANDSCAPE("1920x1080"),

	/**
	 * 1080p portrait (1080x1920)
	 */
	FHD_PORTRAIT("1080x1920");

	private static final Map<String, Resolution> RESOLUTION_INDEX =
			Arrays.stream(Resolution.values()).collect(Collectors.toMap(
					Resolution::toString, Function.identity()
			));

	final String value;

	Resolution(String value) {
		this.value = value;
	}

	@JsonValue
	@Override
	public String toString() {
		return value;
	}

	/**
	 * Parse a string to a Resolution enum.
	 *
	 * @param resolution The string to convert.
	 *
	 * @return The Resolution enum, or {@code null} if the string is null.
	 * @throws IllegalArgumentException If the string is not a valid enum value.
	 */
	@JsonCreator
	public static Resolution fromString(String resolution) {
		return RESOLUTION_INDEX.getOrDefault(resolution, null);
	}
}
