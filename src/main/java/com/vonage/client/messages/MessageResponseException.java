/*
 *   Copyright 2022 Vonage
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
package com.vonage.client.messages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.vonage.client.VonageClientException;
import com.vonage.client.VonageUnexpectedException;
import java.io.IOException;
import java.util.Objects;

/**
 * Response returned when sending a message fails (i.e. returns a non-2xx status code).
 * Since this is an unchecked exception, users are advised to catch it when calling
 * {@link MessagesClient#sendMessage(MessageRequest)} to handle failures.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class MessageResponseException extends VonageClientException {
	String type, title, detail, instance;
	int statusCode;

	/**
	 * Package-private constructor is to prevent users from explicitly creating this object.
	 */
	MessageResponseException() {
	}

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	@JsonProperty("title")
	public String getTitle() {
		return title;
	}

	@JsonProperty("detail")
	public String getDetail() {
		return detail;
	}

	@JsonProperty("instance")
	public String getInstance() {
		return instance;
	}

	public int getStatusCode() {
		return statusCode;
	}

	private static class IgnoreInheritedIntrospector extends JacksonAnnotationIntrospector {
		@Override
		public boolean hasIgnoreMarker(final AnnotatedMember m) {
			return m.getDeclaringClass() != MessageResponseException.class || super.hasIgnoreMarker(m);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MessageResponseException response = (MessageResponseException) o;
		return statusCode == response.statusCode &&
				Objects.equals(type, response.type) &&
				Objects.equals(title, response.title) &&
				Objects.equals(detail, response.detail) &&
				Objects.equals(instance, response.instance);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, title, detail, instance, statusCode);
	}


	@Override
	public String toString() {
		return getClass().getSimpleName() +
				" {type='" + type + '\'' +
				", title='" + title + '\'' +
				", detail='" + detail + '\'' +
				", instance='" + instance + '\'' +
				", statusCode=" + statusCode + '}';
	}

	/**
	 * Generates JSON from this object. Excludes fields inherited from superclasses.
	 *
	 * @return The JSON representation of this response object.
	 */
	public String toJson() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setAnnotationIntrospector(new IgnoreInheritedIntrospector());
			return mapper.writeValueAsString(this);
		}
		catch (JsonProcessingException e) {
			throw new VonageUnexpectedException("Failed to produce JSON from "+getClass().getSimpleName(), e);
		}
	}

	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with all known fields populated from the JSON payload, if present.
	 */
	public static MessageResponseException fromJson(String json) {
		if (json == null || json.length() < 2) {
			return new MessageResponseException();
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, MessageResponseException.class);
		}
		catch (IOException e) {
			throw new VonageUnexpectedException("Failed to produce MessageResponseException from json.", e);
		}
	}
}
