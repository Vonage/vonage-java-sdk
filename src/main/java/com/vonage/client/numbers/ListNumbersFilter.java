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
package com.vonage.client.numbers;

import java.util.Map;
import java.util.UUID;

/**
 * Filter criteria used in {@link NumbersClient#listNumbers(ListNumbersFilter)}.
 */
public class ListNumbersFilter extends BaseNumbersFilter {
    private final UUID applicationId;
    private final Boolean hasApplication;

    private ListNumbersFilter(Builder builder) {
        super(builder);
        applicationId = builder.applicationId;
        hasApplication = builder.hasApplication;
    }

    /**
     * Application to return the numbers for.
     *
     * @return The selected application ID to list numbers from, or {@code null} if unspecified.
     * @since 8.10.0
     */
    public UUID getApplicationId() {
        return applicationId;
    }

    /**
     * Whether results should be filtered to numbers assigned to an application.
     *
     * @return {@code true} if results should contain only numbers associated with an application,
     * {@code false} if only numbers unassigned to an application should be returned, or {@code null}
     * if unspecified (i.e. the application assignment status is not considered).
     *
     * @since 8.10.0
     */
    public Boolean getHasApplication() {
        return hasApplication;
    }

    @Override
    public Map<String, String> makeParams() {
        Map<String, String> params = super.makeParams();
        conditionalAdd("application_id", applicationId);
        conditionalAdd("has_application", hasApplication);
        return params;
    }

    /**
     * Entrypoint for constructing an instance of this class.
     *
     * @return A new Builder.
     * @since 8.10.0
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for setting the parameters of ListNumbersFilter.
     *
     * @since 8.10.0
     */
    public static final class Builder extends BaseNumbersFilter.Builder<ListNumbersFilter, Builder> {
        private UUID applicationId;
        private Boolean hasApplication;

        Builder() {}

        /**
         * Set this to only return numbers assigned to a specific application.
         *
         * @param applicationId The application ID to return numbers for as a string.
         * @return This builder.
         */
        public Builder applicationId(String applicationId) {
            return applicationId(UUID.fromString(applicationId));
        }

        /**
         * Set this to only return numbers assigned to a specific application.
         *
         * @param applicationId The application ID to return numbers for.
         * @return This builder.
         */
        public Builder applicationId(UUID applicationId) {
            this.applicationId = applicationId;
            return this;
        }

        /**
         * Set this optional field to {@code true} to restrict your results to numbers associated with any
         * application. Set to {@code false} to find all numbers not associated with an application. Omit the
         * field to avoid filtering on whether or not the number is assigned to an application.
         *
         * @param hasApplication Whether to return only numbers that are assigned to an application.
         * @return This builder.
         */
        public Builder hasApplication(boolean hasApplication) {
            this.hasApplication = hasApplication;
            return this;
        }

        @Override
        public ListNumbersFilter build() {
            return new ListNumbersFilter(this);
        }
    }
}
