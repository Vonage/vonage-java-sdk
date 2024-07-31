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

public class AvailableNumber extends JsonableNumber {
    private Double cost;

    /**
     * Constructor.
     *
     * @deprecated This will be made package-private in a future release.
     */
    @Deprecated
    public AvailableNumber() {}

    public String getCost() {
        return cost != null ? cost.toString() : null;
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
    public void setCost(String cost) {
        this.cost = cost;
    }

    @Deprecated
    public void setType(String type) {
        this.type = type;
    }

    @Deprecated
    public void setFeatures(String[] features) {
        this.features = Feature.setFromString(features);
    }
}
