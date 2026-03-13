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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.util.List;

/**
 * Response containing format validation and metadata for a phone number.
 * 
 * @since 9.1.0
 */
public class FormatInsightResponse extends JsonableBaseObject {
    private String countryCode, countryName, countryPrefix, offlineLocation;
    private List<String> timeZones;
    private String numberInternational, numberNational;
    private Boolean isFormatValid;
    private InsightStatus status;
    
    protected FormatInsightResponse() {}
    
    /**
     * Gets the two-character country code (ISO 3166-1 alpha-2).
     * 
     * @return The country code, or {@code null} if unavailable.
     */
    @JsonProperty("country_code")
    public String getCountryCode() {
        return countryCode;
    }
    
    /**
     * Gets the full name of the country.
     * 
     * @return The country name, or {@code null} if unavailable.
     */
    @JsonProperty("country_name")
    public String getCountryName() {
        return countryName;
    }
    
    /**
     * Gets the numeric prefix for the country.
     * 
     * @return The country prefix, or {@code null} if unavailable.
     */
    @JsonProperty("country_prefix")
    public String getCountryPrefix() {
        return countryPrefix;
    }
    
    /**
     * Gets the offline location based on the number's prefix.
     * 
     * @return The offline location, or {@code null} if unavailable.
     */
    @JsonProperty("offline_location")
    public String getOfflineLocation() {
        return offlineLocation;
    }
    
    /**
     * Gets the time zones for the location.
     * 
     * @return List of time zone identifiers, or {@code null} if unavailable.
     */
    @JsonProperty("time_zones")
    public List<String> getTimeZones() {
        return timeZones;
    }
    
    /**
     * Gets the phone number in international E.164 format.
     * 
     * @return The international number format, or {@code null} if unavailable.
     */
    @JsonProperty("number_international")
    public String getNumberInternational() {
        return numberInternational;
    }
    
    /**
     * Gets the phone number in national format.
     * 
     * @return The national number format, or {@code null} if unavailable.
     */
    @JsonProperty("number_national")
    public String getNumberNational() {
        return numberNational;
    }
    
    /**
     * Indicates if the phone number format is valid.
     * 
     * @return {@code true} if format is valid, {@code false} otherwise, or {@code null} if unknown.
     */
    @JsonProperty("is_format_valid")
    public Boolean getIsFormatValid() {
        return isFormatValid;
    }
    
    /**
     * Gets the status of this insight.
     * 
     * @return The insight status.
     */
    @JsonProperty("status")
    public InsightStatus getStatus() {
        return status;
    }
}
