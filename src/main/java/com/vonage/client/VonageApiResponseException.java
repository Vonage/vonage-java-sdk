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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import java.net.URI;
import java.util.List;
import java.util.Objects;

/**
 * Base class for exceptions which represent an error API response conforming to
 * <a href=https://www.rfc-editor.org/rfc/rfc7807>RFC 7807</a> standard.
 */
public class VonageApiResponseException extends VonageClientException implements Jsonable {
	protected URI type;
	protected String title, detail, instance, code, errorCodeLabel, errorCode;
	protected List<?> errors, invalidParameters;
	@JsonIgnore protected int statusCode;

	protected VonageApiResponseException() {
	}

	protected VonageApiResponseException(String message) {
		super(message);
	}

	protected VonageApiResponseException(String message, Throwable cause) {
		super(message, cause);
	}

	protected VonageApiResponseException(Throwable cause) {
		super(cause);
	}

	/**
	 * Sets the HTTP status code of the response. Intended to be called reflectively.
	 *
	 * @param statusCode The status code as an integer (typically in the 4xx range).
	 */
	@JsonIgnore
	protected void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	@JsonSetter("error-code")
	private void setErrorCode(String errorCode) {
		if (errorCode != null && !errorCode.trim().isEmpty()) {
			statusCode = Integer.parseInt(errorCode);
		}
	}

	/**
	 * The status code description.
	 *
	 * @return The error code label, or {@code null} if unknown.
	 */
	@JsonProperty("error-code-label")
	public String getErrorCodeLabel() {
		return errorCodeLabel;
	}

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
	 * Additional description of problems encountered with the request.
	 * This is typically only applicable to 400 or 409 error codes.
	 *
	 * @return The list of errors returned from the server (could be a Map or String),
	 * or {@code null} if none / not applicable.
	 */
	@JsonProperty("errors")
	public List<?> getErrors() {
		return errors;
	}

	/**
	 * If the request was rejected due to invalid parameters, this will return the
	 * offending parameters, sometimes along with a description of the errors.
	 *
	 * @return The list of invalid parameters, typically as a Map, or {@code null} if not applicable.
	 *
	 * @since 7.7.0
	 */
	@JsonProperty("invalid_parameters")
	public List<?> getInvalidParameters() {
		return invalidParameters;
	}

	/**
	 * Name of the error code.
	 *
	 * @return The Vonage error code as a string, or {@code null} if unknown / not applicable.
	 */
	@JsonProperty("code")
	public String getCode() {
		return code;
	}

	/**
	 * The API response status code, usually 4xx or 500.
	 *
	 * @return The status code.
	 */
	@JsonIgnore
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
	@Override
	public String toJson() {
		try {
			return Jsonable.createDefaultObjectMapper()
					.setAnnotationIntrospector(new IgnoreInheritedIntrospector())
					.writeValueAsString(this);
		}
		catch (JsonProcessingException e) {
			throw new VonageUnexpectedException("Failed to produce JSON from "+getClass().getSimpleName(), e);
		}
	}

	protected static <E extends VonageApiResponseException> E fromJson(Class<E> clazz, String json) {
		if (json == null || json.length() < 2) {
			try {
				return clazz.getConstructor().newInstance();
			}
			catch (Exception ex) {
				throw new VonageUnexpectedException(ex);
			}
		}
		else return Jsonable.fromJson(json, clazz);
	}
}
