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

import org.apache.http.client.methods.RequestBuilder;

public class ListArchivesRequest {
	private final Integer offset, count;
	private final String sessionId;

	ListArchivesRequest(Builder builder) {
		sessionId = builder.sessionId;
		if ((offset = builder.offset) != null && offset < 0) {
			throw new IllegalArgumentException("Offset cannot be negative.");
		}
		if ((count = builder.count) != null && (count < 0 || count > 1000)) {
			throw new IllegalArgumentException("Count must be between 0 and 1000.");
		}
	}

	void addParams(RequestBuilder request) {
		if (offset != null) {
			request.addParameter("offset", offset.toString());
		}
		if (count != null) {
			request.addParameter("count", count.toString());
		}
		if (sessionId != null) {
			request.addParameter("sessionId", sessionId);
		}
	}

	/**
	 * @return The results offset index.
	 */
	public Integer getOffset() {
		return offset;
	}

	/**
	 * @return The maximum number of results.
	 */
	public Integer getCount() {
		return count;
	}

	/**
	 * @return The session ID to filter by.
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * Instantiates a Builder, used to construct this object.
	 *
	 * @return A new {@linkplain ListArchivesRequest.Builder}.
	 */
	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private Integer offset, count;
		private String sessionId;

		Builder() {}

		/**
		 * Set an offset query parameters to specify the index offset of the first archive.
		 * 0 is offset of the most recently started archive (excluding deleted archive).
		 * 1 is the offset of the archive that started prior to the most recent archive.
		 * The default value is 0.
		 *
		 * @param offset The offset index.
		 *
		 * @return This builder with the offset setting.
		 */
		public Builder offset(int offset) {
			this.offset = offset;
			return this;
		}


		/**
		 * Set a count query parameter to limit the number of archives to be returned. The default number of
		 * archives returned is 50 (or fewer, if there are fewer than 50 archives). The maximum number of archives the
		 * call will return is 1000.
		 *
		 * @param count The number of results to return, between 0 and 1000.
		 *
		 * @return This builder with the count setting.
		 */
		public Builder count(int count) {
			this.count = count;
			return this;
		}

		/**
		 * Set a sessionId query parameter to list archives for a specific session ID. This is useful
		 * 	when listing multiple archives for an automatically archived session.
		 *
		 * @param sessionId The session ID to filter by.
		 *
		 * @return This builder with the sessionId setting.
		 */
		public Builder sessionId(String sessionId) {
			this.sessionId = sessionId;
			return this;
		}

		/**
		 * Builds the {@linkplain ListArchivesRequest} with this builder's settings.
		 *
		 * @return A new {@link ListArchivesRequest} instance.
		 */
		public ListArchivesRequest build() {
			return new ListArchivesRequest(this);
		}
	}
}
