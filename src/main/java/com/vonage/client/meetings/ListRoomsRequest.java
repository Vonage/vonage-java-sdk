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

import com.vonage.client.QueryParamsRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

class ListRoomsRequest implements QueryParamsRequest {
	final UUID themeId;
	final Integer pageSize, startId, endId;

	ListRoomsRequest(Integer startId, Integer endId, Integer pageSize, UUID themeId) {
		this.themeId = themeId;
		this.startId = startId;
		this.endId = endId;
		this.pageSize = pageSize;
	}

	@Override
	public Map<String, String> makeParams() {
		Map<String, String> params = new LinkedHashMap<>(4);
		if (startId != null) {
			params.put("start_id", String.valueOf(startId));
		}
		if (endId != null) {
			params.put("end_id", String.valueOf(endId));
		}
		if (pageSize != null) {
			params.put("page_size", String.valueOf(pageSize));
		}
		return params;
	}
}
