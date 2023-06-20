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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;

/**
 * Base class for exceptions which represent an error API response conforming to
 * <a href=https://www.rfc-editor.org/rfc/rfc7807>RFC 7807</a> standard.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class VonageApiResponseException extends VonageClientException {
	protected URI type;
	protected String title, detail, instance;
	@JsonIgnore protected int statusCode;

	/**
	 * Link to the <a href=https://developer.vonage.com/en/api-errors>API error type</a>.
	 *
	 * @return URL of the error type / description.
	 */
	@JsonProperty("type")
	public URI getType() {
		return type;
	}

	/**
	 * Brief description / name of the error.
	 *
	 * @return The error title.
	 */
	@JsonProperty("title")
	public String getTitle() {
		return title;
	}

	/**
	 * Extended description of the error, explaining the cause.
	 *
	 * @return The detailed error description.
	 */
	@JsonProperty("detail")
	public String getDetail() {
		return detail;
	}

	/**
	 * Internal trace ID of the request.
	 *
	 * @return The instance ID.
	 */
	@JsonProperty("instance")
	public String getInstance() {
		return instance;
	}

	/**
	 * The API response status code, usually 4xx or 500.
	 *
	 * @return The status code.
	 */
	public int getStatusCode() {
		return statusCode;
	}

	@JsonIgnore
	@Override
	public String getMessage() {
		if (statusCode > 0 && title != null) {
			String message = statusCode+" ("+title+")";
			if (detail != null) {
				message += ": "+detail;
			}
			return message;
		}
		else return super.getMessage();
	}

	private static class IgnoreInheritedIntrospector extends JacksonAnnotationIntrospector {
		@Override
		public boolean hasIgnoreMarker(final AnnotatedMember m) {
			return m.getDeclaringClass().equals(Throwable.class) || super.hasIgnoreMarker(m);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		VonageApiResponseException response = (VonageApiResponseException) o;
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

	protected static <E extends VonageApiResponseException> E fromJson(Class<E> clazz, String json) {
		if (json == null || json.length() < 2) {
			try {
				return clazz.newInstance();
			}
			catch (InstantiationException | IllegalAccessException ex) {
				throw new VonageUnexpectedException(ex);
			}
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, clazz);
		}
		catch (IOException ex) {
			throw new VonageUnexpectedException("Failed to produce "+clazz.getSimpleName()+" from json.", ex);
		}
	}

	protected static <E extends VonageApiResponseException> E fromHttpResponse(Class<E> clazz, HttpResponse response) throws IOException {
		E crx = fromJson(clazz, EntityUtils.toString(response.getEntity()));
		if (crx.title == null) {
			crx.title = response.getStatusLine().getReasonPhrase();
		}
		crx.statusCode = response.getStatusLine().getStatusCode();
		return crx;
	}
}
