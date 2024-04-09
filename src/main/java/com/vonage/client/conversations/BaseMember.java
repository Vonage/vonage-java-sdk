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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.users.BaseUser;

/**
 * Represents the basic conversation member attributes, as returned from {@link ListMembersResponse#getMembers()}.
 */
public class BaseMember extends JsonableBaseObject {
	@JsonProperty("id") String id;
	@JsonProperty("state") MemberState state;
	@JsonProperty("_embedded") Embedded _embedded;
	@JsonProperty("user") BaseUser user;

	protected BaseMember() {}

	static final class Embedded extends JsonableBaseObject {
		@JsonProperty("user") private BaseUser user;
	}

	/**
	 * Unique member identifier.
	 * 
	 * @return The member ID, or {@code null} if unknown.
	 */
	public String getId() {
		return id;
	}

	/**
	 * State that the member is in.
	 * 
	 * @return The member state as an enum.
	 */
	public MemberState getState() {
		return state;
	}

	/**
	 * User associated with this member.
	 * Full details can be obtained via {@link com.vonage.client.users.UsersClient#getUserDetails(BaseUser)}.
	 *
	 * @return The basic user details, or {@code null} if unknown.
	 */
	@JsonIgnore
	public BaseUser getUser() {
		return _embedded != null ? _embedded.user : user;
	}
}
