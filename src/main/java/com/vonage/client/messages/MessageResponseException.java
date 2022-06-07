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
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageResponseException extends VonageClientException {
	private String type, title, detail, instance;
	private int statusCode;

	protected MessageResponseException() {
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

	protected void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	protected void setType(String type) {
		this.type = type;
	}

	protected void setTitle(String title) {
		this.title = title;
	}

	protected void setDetail(String detail) {
		this.detail = detail;
	}

	protected void setInstance(String instance) {
		this.instance = instance;
	}

	private static class IgnoreInheritedIntrospector extends JacksonAnnotationIntrospector {
		@Override
		public boolean hasIgnoreMarker(final AnnotatedMember m) {
			return m.getDeclaringClass() != MessageResponseException.class || super.hasIgnoreMarker(m);
		}
	}

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

	public static MessageResponseException fromJson(String json) {
		try {
			return new ObjectMapper().readValue(json, MessageResponseException.class);
		}
		catch (IOException e) {
			throw new VonageUnexpectedException("Failed to produce MessageResponseException from json.", e);
		}
	}
}
