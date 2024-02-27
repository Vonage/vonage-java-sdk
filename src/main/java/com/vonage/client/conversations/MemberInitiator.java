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
package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberInitiator extends JsonableBaseObject {
	@JsonProperty("invited") private Invited invited;
	@JsonProperty("joined") private Joined joined;

	protected MemberInitiator() {
	}

	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class Invited extends JsonableBaseObject {
		@JsonProperty("is_system") Boolean isSystem;
	}

	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class Joined extends Invited {
		@JsonProperty("user_id") String userId;
		@JsonProperty("member_id") String memberId;
	}

	/**
	 * Whether the member was invited by an admin JWT or a user.
	 *
	 * @return {@code true} if invited by admin JWT,
	 * {@code false} if invited by user (joined), {@code null} if unknown.
	 */
	@JsonIgnore
	public Boolean invitedByAdmin() {
		return invited != null ? invited.isSystem : joined != null ? joined.isSystem : null;
	}

	/**
	 * If {@linkplain #invitedByAdmin()} is {@code false}, returns the ID of the inviting user.
	 *
	 * @return The user ID that invited this member, or {@code null} if not applicable.
	 */
	@JsonIgnore
	public String getUserId() {
		return joined != null ? joined.userId : null;
	}

	/**
	 * If {@linkplain #invitedByAdmin()} is {@code false}, returns the ID of the inviting member.
	 *
	 * @return The member ID that sent the invite, or {@code null} if not applicable.
	 */
	@JsonIgnore
	public String getMemberId() {
		return joined != null ? joined.memberId : null;
	}
}
