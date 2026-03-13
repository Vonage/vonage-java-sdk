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
package com.vonage.client.identityinsights;

/**
 * Enum representing the status codes for Identity Insights requests.
 * 
 * @since 9.1.0
 */
public enum InsightStatusCode {
    /**
     * The insight was processed successfully.
     */
    OK,
    
    /**
     * The phone number format is not valid for assignment by carriers to users.
     */
    INVALID_NUMBER_FORMAT,
    
    /**
     * The country or mobile network is not supported by available suppliers.
     */
    NO_COVERAGE,
    
    /**
     * The phone number could not be found for this Insight.
     */
    NOT_FOUND,
    
    /**
     * The request could not be authorized for the combination of application, supplier, and phone number.
     */
    UNAUTHORIZED,
    
    /**
     * The purpose used is not valid or allowed for this Insight.
     */
    INVALID_PURPOSE,
    
    /**
     * The supplier returned an error while processing the request.
     */
    SUPPLIER_ERROR,
    
    /**
     * An internal error occurred while processing the request.
     */
    INTERNAL_ERROR
}
