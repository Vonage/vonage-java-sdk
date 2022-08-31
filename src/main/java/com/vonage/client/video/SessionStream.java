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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SessionStream {
	private String id;
	private List<String> layoutClassList;

	protected SessionStream() {
	}

	protected SessionStream(Builder<?> builder) {
		id = builder.id;
		layoutClassList = builder.layoutClassList;
	}
	
	public String getId() {
		return id;
	}
	
	public List<String> getLayoutClassList() {
		return layoutClassList;
	}

	public static Builder<?> builder(String sessionId) {
		return new Builder<>(sessionId);
	}

	@SuppressWarnings("unchecked")
	public static class Builder<B extends Builder<B>> {
		private final String id;
		private List<String> layoutClassList;

		protected Builder(String id) {
			this.id = id;
		}

		public B layoutClassList(List<String> layoutClassList) {
			this.layoutClassList = layoutClassList;
			return (B) this;
		}

		public SessionStream build() {
			return new SessionStream(this);
		}
	}

	protected String toJson() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(this);
		}
		catch (JsonProcessingException jpe) {
			throw new VonageUnexpectedException("Failed to produce JSON from "+getClass().getSimpleName()+" object.", jpe);
		}
	}
}
