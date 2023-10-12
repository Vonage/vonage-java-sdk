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
package com.vonage.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.lang.reflect.Constructor;

/**
 * Indicates that a class can be serialized to and parsed from JSON.
 *
 * @since 7.7.0
 */
public interface Jsonable {

	default String toJson() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			return mapper.writeValueAsString(this);
		}
		catch (JsonProcessingException jpe) {
			throw new VonageUnexpectedException("Failed to produce JSON from "+getClass().getSimpleName()+" object.", jpe);
		}
	}

	default void updateFromJson(String json) {
		if (json == null || json.trim().isEmpty()) {
			return;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.readerForUpdating(this).readValue(json);
		}
		catch (IOException ex) {
			throw new VonageResponseParseException("Failed to produce "+getClass().getSimpleName()+" from json.", ex);
		}
	}

	static <J extends Jsonable> J fromJson(String json, Class<? extends J> jsonable) {
		try {
			Constructor<? extends J> constructor = jsonable.getDeclaredConstructor();
			if (!(constructor.isAccessible())) {
				constructor.setAccessible(true);
			}
			J instance = constructor.newInstance();
			instance.updateFromJson(json);
			return instance;
		}
		catch (ReflectiveOperationException ex) {
			throw new VonageUnexpectedException(ex);
		}
	}
}
