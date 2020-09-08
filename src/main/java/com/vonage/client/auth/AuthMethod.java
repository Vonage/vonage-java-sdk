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
package com.vonage.client.auth;

import org.apache.http.client.methods.RequestBuilder;


public interface AuthMethod extends Comparable<AuthMethod> {
    RequestBuilder apply(RequestBuilder request);

    /**
     * Apply the authentication to the header as basic authentication.
     *
     * @param requestBuilder The request being built
     *
     * @return RequestBuilder for more building of the request.
     */
    RequestBuilder applyAsBasicAuth(RequestBuilder requestBuilder);

    /**
     * Apply the authentication by adding it to the entity payload.
     *
     * @param requestBuilder The request being built
     *
     * @return RequestBuilder for more building of the request.
     */
    RequestBuilder applyAsJsonProperties(RequestBuilder requestBuilder);

    int getSortKey();
}