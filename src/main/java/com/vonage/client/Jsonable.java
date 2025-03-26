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
package com.vonage.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

/**
 * Indicates that a class can be serialized to and parsed from JSON.
 *
 * @since 7.7.0
 */
public interface Jsonable {

	/**
	 * Convenience method for creating an ObjectMapper with standard settings.
	 *
	 * @return A new ObjectMapper with appropriate configuration.
	 */
	static ObjectMapper createDefaultObjectMapper() {
		return new ObjectMapper()
				.registerModule(new JavaTimeModule())
				.setSerializationInclusion(JsonInclude.Include.NON_NULL)
				.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}

	/**
	 * Serialises this class to a JSON payload.
	 *
	 * @return The JSON string representing this class's marked properties.
	 */
	default String toJson() {
		try {
			ObjectMapper mapper = this instanceof JsonableBaseObject ?
					((JsonableBaseObject) this).createJsonObjectMapper() : createDefaultObjectMapper();

			return mapper.writeValueAsString(this);
		}
		catch (JsonProcessingException jpe) {
			throw new VonageUnexpectedException("Failed to produce JSON from "+getClass().getSimpleName()+" object.", jpe);
		}
	}

	/**
	 * Updates this class's fields from the JSON payload.
	 *
	 * @param json The JSON string.
	 *
	 * @throws VonageResponseParseException If the JSON was invalid or this class couldn't be updated.
	 */
	default void updateFromJson(String json) {
		if (json == null || json.trim().isEmpty()) return;
		try {
			ObjectMapper mapper = this instanceof JsonableBaseObject ?
					((JsonableBaseObject) this).createJsonObjectMapper() : createDefaultObjectMapper();

			mapper.readerForUpdating(this).readValue(json);
		}
		catch (IOException ex) {
			throw new VonageResponseParseException("Failed to produce "+getClass().getSimpleName()+" from JSON.", ex);
		}
	}

	/**
	 * Delegates to {@linkplain #fromJson(String, Class)}, using the type varargs for inferring the class.
	 *
	 * @param json The JSON string to parse.
	 * @param type Unused. This is a hack to get the array class's component type.
	 *
	 * @return A new instance of the inferred Jsonable.
	 *
	 * @param <J> A class which implements this interface.
	 *
	 * @throws VonageUnexpectedException If a no-args constructor for the class was not found.
	 * @throws VonageResponseParseException If the JSON was invalid or this class couldn't be updated.
	 */
	@SuppressWarnings("unchecked")
	static <J extends Jsonable> J fromJson(String json, J... type) {
		return fromJson(json, (Class<J>) type.getClass().getComponentType());
	}

	/**
	 * Creates a new instance of the designated Jsonable class, calling its no-args constructor
	 * followed by {@link #updateFromJson(String)}.
	 *
	 * @param json The JSON string to parse.
	 * @param jsonable The Jsonable class to construct.
	 *
	 * @return A new instance of the Jsonable class.
	 *
	 * @param <J> A class which implements this interface.
	 *
	 * @throws VonageUnexpectedException If a no-args constructor for the class was not found.
	 * @throws VonageResponseParseException If the JSON was invalid or this class couldn't be updated.
	 */
	static <J extends Jsonable> J fromJson(String json, Class<? extends J> jsonable) {
		try {
			if (Modifier.isAbstract(jsonable.getModifiers())) {
				return createDefaultObjectMapper().readValue(json, jsonable);
			}
			Constructor<? extends J> constructor = jsonable.getDeclaredConstructor();
			constructor.setAccessible(true);
			J instance = constructor.newInstance();
			instance.updateFromJson(json);
			return instance;
		}
		catch (ReflectiveOperationException | JsonProcessingException ex) {
			throw new VonageUnexpectedException(ex);
		}
    }

	/**
	 * Convenience method for converting a string to an enum.
	 *
	 * @param <E> The enum type.
	 * @param value String representation of the enum.
	 * @param type The enum class to parse.
	 *
	 * @return The parsed value as an enum, or {@code null} if an invalid value was passed in.
	 * @since 9.0.0
	 */
	static <E extends Enum<E>> E fromString(String value, Class<E> type) {
		try {
			return Enum.valueOf(type, value.toUpperCase().replace('-', '_').replace(':', '_'));
		}
		catch (IllegalArgumentException | NullPointerException ex) {
			return null;
		}
	}
}
