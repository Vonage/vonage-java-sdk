/*
 * Copyright (c) 2020 Vonage
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
package com.vonage.client.verify;

public class SearchRequest {
    /**
     * Number of maximum request IDs that can be searched for.
     */
    private static final int MAX_SEARCH_REQUESTS = 10;

    private final String[] requestIds;

    public SearchRequest(final String... requestIds) {
        if (requestIds.length == 0) {
            throw new IllegalArgumentException("At least one SnsRequest ID must be provided in a SearchRequest");
        } else if (requestIds.length > MAX_SEARCH_REQUESTS) {
                throw new IllegalArgumentException("too many request IDs. Max is " + MAX_SEARCH_REQUESTS);
        }
        this.requestIds = requestIds;
    }

    public String[] getRequestIds() {
        return requestIds;
    }
}
