/*
 *   Copyright 2025 Vonage
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
package com.vonage.client.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.common.HalPageResponse;
import java.util.List;

/**
 * Paginated HAL response for {@link UsersClient#listUsers(ListUsersRequest)}.
 */
public class ListUsersResponse extends HalPageResponse {
    @JsonProperty("_embedded") private Embedded embedded;

    @Deprecated
    public ListUsersResponse() {
        super();
    }

    private static class Embedded extends JsonableBaseObject {
        @JsonProperty("users") List<BaseUser> users;
    }

    /**
     * Retrieves the embedded resource contents. <br>
     * <b>NOTE: The users returned from this method will only contain the name and ID, not the full record.</b>
     *
     * @return The list of users, or {@code null} if absent.
     */
    @JsonIgnore
    public List<BaseUser> getUsers() {
        return embedded != null ? embedded.users : null;
    }
}
