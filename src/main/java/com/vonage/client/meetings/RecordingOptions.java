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
package com.vonage.client.meetings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordingOptions {
	private Boolean autoRecord, recordOnlyOwner;

	protected RecordingOptions() {
	}

	RecordingOptions(Builder builder) {
		autoRecord = builder.autoRecord;
		recordOnlyOwner = builder.recordOnlyOwner;
	}

	/**
	 * Automatically record all sessions in this room. Recording cannot be stopped when this is set to true.
	 *
	 * @return Whether all sessions are automatically recorded, or {@code null} if unknown.
	 */
	@JsonProperty("auto_record")
	public Boolean getAutoRecord() {
		return autoRecord;
	}

	/**
	 * Record only the owner screen or any share screen of the video.
	 *
	 * @return Whether only the owner screen is recorded, or {@code null} if unknown.
	 */
	@JsonProperty("record_only_owner")
	public Boolean getRecordOnlyOwner() {
		return recordOnlyOwner;
	}
	
	/**
	 * Generates a JSON payload from this request.
	 *
	 * @return JSON representation of this RecordingOptions object.
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
	
	/**
	 * Entry point for constructing an instance of this class.
	 * 
	 * @return A new Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private Boolean autoRecord, recordOnlyOwner;
	
		Builder() {}
	
		/**
		 *
		 * @param autoRecord Automatically record all sessions in this room. Recording cannot be stopped when this is set to true.
		 *
		 * @return This builder.
		 */
		public Builder autoRecord(Boolean autoRecord) {
			this.autoRecord = autoRecord;
			return this;
		}

		/**
		 *
		 * @param recordOnlyOwner Record only the owner screen or any share screen of the video.
		 *
		 * @return This builder.
		 */
		public Builder recordOnlyOwner(Boolean recordOnlyOwner) {
			this.recordOnlyOwner = recordOnlyOwner;
			return this;
		}

	
		/**
		 * Builds the {@linkplain RecordingOptions}.
		 *
		 * @return An instance of RecordingOptions, populated with all fields from this builder.
		 */
		public RecordingOptions build() {
			return new RecordingOptions(this);
		}
	}
}
