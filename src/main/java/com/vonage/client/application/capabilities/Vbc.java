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
package com.vonage.client.application.capabilities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Vbc extends Capability {
    private Vbc() {

    }

    private Vbc(Builder builder) {

    }

    @Override
    public Type getType() {
        return Type.VBC;
    }

    /**
     * @return A new Builder to start building.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        /**
         * @return A new VBC capability.
         */
        public Vbc build() {
            return new Vbc(this);
        }
    }
}
