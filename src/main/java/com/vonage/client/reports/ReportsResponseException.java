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
package com.vonage.client.reports;

import com.vonage.client.VonageApiResponseException;

/**
 * Exception thrown when the Reports API returns an error response.
 */
public class ReportsResponseException extends VonageApiResponseException {

    ReportsResponseException() {}

    /**
     * Construct a new ReportsResponseException.
     *
     * @param message The error message.
     */
    public ReportsResponseException(String message) {
        super(message);
    }

    /**
     * Creates an instance of this class from a JSON payload.
     *
     * @param json The JSON string to parse.
     *
     * @return A new instance of this class.
     */
    public static ReportsResponseException fromJson(String json) {
        return fromJson(ReportsResponseException.class, json);
    }
}
