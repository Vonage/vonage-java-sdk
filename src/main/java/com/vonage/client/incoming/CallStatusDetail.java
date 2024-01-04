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
package com.vonage.client.incoming;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum CallStatusDetail {
    /**
     * No detail field present
     */
    NO_DETAIL,
    /**
     * Detail present, but has not been mapped to an enum yet
     */
    UNMAPPED_DETAIL,
    /**
     * Number invalid
     */
    INVALID_NUMBER,
    /**
     * Rejected by carrier
     */
    RESTRICTED,
    /**
     * Call rejected by callee
     */
    DECLINED,
    /**
     * Cannot route to the number
     */
    CANNOT_ROUTE,
    /**
     * Number is no longer available
     */
    NUMBER_OUT_OF_SERVICE,
    /**
     * Server error or failure
     */
    INTERNAL_ERROR,
    /**
     * Carrier timed out
     */
    CARRIER_TIMEOUT,
    /**
     * Callee is temporarily unavailable.
     */
    UNAVAILABLE;


    @JsonCreator
    public static CallStatusDetail fromString(String detail) {
        if (detail == null) {
            return NO_DETAIL;
        }
        try {
            return CallStatusDetail.valueOf(detail.toUpperCase());
        }
        catch (IllegalArgumentException ex) {
            return UNMAPPED_DETAIL;
        }
    }
}
