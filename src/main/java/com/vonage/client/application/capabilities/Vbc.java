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
package com.vonage.client.application.capabilities;

/**
 * VBC capability configuration settings.
 */
public final class Vbc extends Capability {

    private Vbc() {
    }

    private Vbc(Builder builder) {
        super(builder);
    }

    @Override
    public Type getType() {
        return Type.VBC;
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @return A new Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Capability.Builder<Vbc, Builder> {

        /**
         * Builds the Vbc object.
         *
         * @return A new VBC capability.
         */
        @Override
        public Vbc build() {
            return new Vbc(this);
        }
    }
}
