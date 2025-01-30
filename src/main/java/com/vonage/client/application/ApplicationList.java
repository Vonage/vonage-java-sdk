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
package com.vonage.client.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import com.vonage.client.common.HalPageResponse;
import java.util.List;

/**
 * HAL response returned from {@link ApplicationClient#listApplications(ListApplicationRequest)}.
 */
public class ApplicationList extends HalPageResponse {
    @JsonProperty("_embedded") private Embedded embedded;

    private static class Embedded {
        @JsonProperty("applications") List<Application> applications;
    }

    @Deprecated
    public ApplicationList() {}

    /**
     * Retrieves the embedded resource contents.
     *
     * @return The list of applications.
     */
    public List<Application> getApplications() {
        return embedded != null ? embedded.applications : null;
    }

    @Deprecated
    public static ApplicationList fromJson(String json) {
        return Jsonable.fromJson(json);
    }
}
