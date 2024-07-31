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
package com.vonage.client.numbers;

import com.vonage.client.JsonableBaseObject;

/**
 * Base class for shared fields of {@link OwnedNumber} and {@link AvailableNumber}.
 *
 * @since 8.10.0
 */
class JsonableNumber extends JsonableBaseObject {
    private String country, msisdn;
    private Type type;
    private Feature[] features;
    
    /**
     * Two character country code in ISO 3166-1 alpha-2 format.
     *
     * @return The number's country code.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Phone number in E.164 format.
     *
     * @return The MSISDN as a string.
     */
    public String getMsisdn() {
        return msisdn;
    }

    public String getType() {
        return type != null ? type.toString() : null;
    }

    public String[] getFeatures() {
        return Feature.getToString(features);
    }

    @Deprecated
    public void setCountry(String country) {
        this.country = country;
    }

    @Deprecated
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    @Deprecated
    public void setType(String type) {
        this.type = Type.fromString(type);
    }

    @Deprecated
    public void setFeatures(String[] features) {
        this.features = Feature.setFromString(features);
    }
}
