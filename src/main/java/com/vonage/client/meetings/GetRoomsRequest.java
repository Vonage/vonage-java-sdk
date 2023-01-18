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

import org.apache.http.client.methods.RequestBuilder;

class GetRoomsRequest {
	final String startId, endId, themeId;

	GetRoomsRequest(String startId, String endId, String themeId) {
		this.startId = startId;
		this.endId = endId;
		this.themeId = themeId;
	}

	RequestBuilder addParameters(RequestBuilder builder) {
		if (startId != null) {
			builder.addParameter("start_id", startId);
		}
		if (endId != null) {
			builder.addParameter("end_id", endId);
		}
		return builder;
	}
}
