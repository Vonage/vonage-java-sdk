/*
 * Copyright (c) 2011-2019 Nexmo Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nexmo.client.application;

public class ListApplicationRequest {
    private long pageSize;
    private long page;

    private ListApplicationRequest(Builder builder) {
        this.pageSize = builder.pageSize;
        this.page = builder.page;
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
