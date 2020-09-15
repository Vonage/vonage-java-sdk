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
package com.vonage.client;

/**
 * Abstract class for different API clients.
 * <p>
 * Currently this class simply provides boiler-plate for storing an {@link HttpWrapper} object, which is required by
 * all client implementations.
 */
public abstract class AbstractClient {
    protected final HttpWrapper httpWrapper;

    public AbstractClient(HttpWrapper httpWrapper) {
        this.httpWrapper = httpWrapper;
    }

}