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
package com.vonage.client.video;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import java.util.List;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SetStreamLayoutRequest {
	private final List<SessionStream> items;
	final String sessionId;

	SetStreamLayoutRequest(Builder builder) {
		this.items = builder.items;
		this.sessionId = builder.sessionId;
	}

	public List<SessionStream> getItems() {
		return items;
	}
	
	/**
	 * Generates a JSON payload from this request.
	 *
	 * @return JSON representation of this ChangeLayoutRequest object.
	 */
	public String toJson() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(this);
		}
		catch (JsonProcessingException jpe) {
			throw new VonageUnexpectedException("Failed to produce JSON from "+getClass().getSimpleName()+" object.", jpe);
		}
	}

	public static Builder builder(String sessionId) {
		return new Builder(sessionId);
	}
	
	public static class Builder {
		List<SessionStream> items;
		final String sessionId;
	
		Builder(String sessionId) {
			this.sessionId = sessionId;
		}
	
		/**
		 * @param items The stream layouts to change.
		 * @return This builder.
		 */
		public Builder items(List<SessionStream> items) {
			this.items = items;
			return this;
		}
	
		/**
		 * Builds the {@linkplain SetStreamLayoutRequest}.
		 *
		 * @return An instance of SetStreamLayoutRequest, populated with all fields from this builder.
		 */
		public SetStreamLayoutRequest build() {
			return new SetStreamLayoutRequest(this);
		}
	}
}
