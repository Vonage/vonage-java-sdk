/*
 *   Copyright 2020 Vonage
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

public class ListApplicationRequest {
    private long pageSize;
    private long page;

    private ListApplicationRequest(Builder builder) {
        pageSize = builder.pageSize;
        page = builder.page;
    }

    public long getPageSize() {
        return pageSize;
    }

    public long getPage() {
        return page;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long pageSize;
        private long page;

        /**
         * @param pageSize The number of applications per page.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder pageSize(long pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        /**
         * @param page The current page number, starts at 1.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder page(long page) {
            this.page = page;
            return this;
        }

        /**
         * @return A new {@link ListApplicationRequest} from the stored configuration.
         */
        public ListApplicationRequest build() {
            return new ListApplicationRequest(this);
        }
    }
}
