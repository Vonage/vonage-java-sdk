/*
 *   Copyright 2024 Vonage
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import java.util.List;
import java.util.UUID;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
class FinalizeLogosRequest implements Jsonable {
	@JsonProperty("keys") List<String> keys;
	@JsonIgnore UUID themeId;

	FinalizeLogosRequest(UUID themeId, List<String> keys) {
		this.keys = keys;
		this.themeId = themeId;
	}
}
