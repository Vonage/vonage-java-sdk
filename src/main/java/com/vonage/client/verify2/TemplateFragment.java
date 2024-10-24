/*
 *   Copyright 2024 Vonage
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
package com.vonage.client.verify2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import com.vonage.client.JsonableBaseObject;
import java.time.Instant;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a custom template fragment.
 *
 * @since 8.13.0
 */
public final class TemplateFragment extends JsonableBaseObject {
	private String text;
	private Locale locale;
	private FragmentChannel channel;
	private Instant dateCreated, dateUpdated;
	@JsonProperty(value = "template_fragment_id", access = JsonProperty.Access.WRITE_ONLY) UUID fragmentId;
	@JsonIgnore UUID templateId;

	TemplateFragment() {}

	TemplateFragment(String text) {
		this.text = Objects.requireNonNull(text, "Fragment text is required.");
	}

	/**
	 * Create a new template fragment. All parameters are required.
	 *
	 * @param channel The channel type for the template.
	 * @param locale The BCP-47 locale for the template.
	 * @param text The text content of the template.
	 */
	public TemplateFragment(FragmentChannel channel, String locale, String text) {
		this(text);
		this.channel = Objects.requireNonNull(channel, "Channel is required.");
		this.locale = Locale.forLanguageTag(Objects.requireNonNull(locale, "Locale is required."));
	}

	/**
	 * Text content of the template.
	 *
	 * @return The template fragment text.
	 */
	@JsonProperty("text")
	public String getText() {
		return text;
	}

	/**
	 * Template Locale in IETF BCP 47 format.
	 *
	 * @return The locale as an object, or {@code null} if this is an update request.
	 */
	@JsonSerialize(converter = LocaleSerializer.class)
	@JsonProperty("locale")
	public Locale getLocale() {
		return locale;
	}

	/**
	 * Channel type for the template.
	 *
	 * @return The channel as an enum, or {@code null} if this is an update request.
	 */
	@JsonProperty("channel")
	public FragmentChannel getChannel() {
		return channel;
	}

	/**
	 * Unique fragment identifier.
	 *
	 * @return The template fragment ID, or {@code null} if this is a request object.
	 */
	public UUID getFragmentId() {
		return fragmentId;
	}

	/**
	 * Timestamp of when the template fragment was created.
	 *
	 * @return The creation date-time in ISO-8601 format, or {@code null} if this is a request object.
	 */
	@JsonProperty("date_created")
	public Instant getDateCreated() {
		return dateCreated;
	}

	/**
	 * Timestamp of when the template fragment was last updated.
	 *
	 * @return The latest update time in ISO-8601 format, or {@code null} if this is a request object.
	 */
	@JsonProperty("date_updated")
	public Instant getDateUpdated() {
		return dateUpdated;
	}

	private static final class LocaleSerializer extends StdConverter<Locale, String> {

		@Override
		public String convert(Locale value) {
			return value == null ? null : value.toString().toLowerCase().replace('_', '-');
		}
	}
}
