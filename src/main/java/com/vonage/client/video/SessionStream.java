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
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SessionStream {
	private String id;
	private List<String> layoutClassList;

	protected SessionStream() {
	}

	protected SessionStream(Builder builder) {
		id = builder.id;
		layoutClassList = builder.layoutClassList;
	}
	
	public String getId() {
		return id;
	}
	
	public List<String> getLayoutClassList() {
		return layoutClassList;
	}

	/**
	 * Entry point for building an instance of {@linkplain SessionStream}.
	 *
	 * @param streamId The ID of the stream.
	 *
	 * @return A new {@linkplain Builder} with the specified streamId property.
	 */
	public static Builder builder(String streamId) {
		return new Builder(streamId);
	}

	public static class Builder {
		private final String id;
		private List<String> layoutClassList;

		protected Builder(String id) {
			this.id = id;
		}

		public Builder layoutClassList(List<String> layoutClassList) {
			this.layoutClassList = layoutClassList;
			return this;
		}

		public SessionStream build() {
			return new SessionStream(this);
		}
	}
}
