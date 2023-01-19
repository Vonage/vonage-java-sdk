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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import java.io.IOException;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
class GetRecordingsResponse {
	@JsonProperty("_embedded") private Embedded embedded;

	static class Embedded {
		@JsonProperty("recordings") List<Recording> recordings;
	}

	public List<Recording> getRecordings() {
		return embedded.recordings;
	}

	public static GetRecordingsResponse fromJson(String json) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, GetRecordingsResponse.class);
		}
		catch (IOException ex) {
			throw new VonageUnexpectedException("Failed to produce GetRecordingsResponse from json.", ex);
		}
	}
}