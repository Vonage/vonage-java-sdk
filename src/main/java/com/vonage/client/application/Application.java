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
package com.vonage.client.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.application.capabilities.*;

/**
 * Represents a Vonage Application (both request and response).
 */
public class Application extends JsonableBaseObject {
    private String id, name;
    private Keys keys;
    private Capabilities capabilities;
    private Privacy privacy;

    private Application() {
    }

    private Application(Builder builder) {
        id = builder.id;
        name = builder.name;
        keys = builder.keys;
        capabilities = builder.capabilities;
        privacy = builder.privacy;
    }

    /**
     * Unique application ID.
     *
     * @return The application ID as a string, or {@code null} if unknown.
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * Friendly identifier for your application. This is not unique.
     *
     * @return The application name.
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Cryptographic keys associated with this application.
     *
     * @return The keys for this application, or {@code null} if unknown / not applicable for this object.
     */
    @JsonProperty("keys")
    public Keys getKeys() {
        return keys;
    }

    /**
     * Your application can use multiple products. This contains the configuration for each product.
     *
     * @return The capabilities of this application, or {@code null} if unknown / not applicable for this object.
     */
    @JsonProperty("capabilities")
    public Capabilities getCapabilities() {
        return capabilities;
    }

    /**
     * Application privacy configuration.
     *
     * @return The privacy preferences, or {@code null} if unknown / not applicable for this object.
     *
     * @since 7.7.0
     */
    @JsonProperty("privacy")
    public Privacy getPrivacy() {
        return privacy;
    }

    /**
     * Entry point for creating an instance of this class.
     * 
     * @return A new Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Copy an application to a new builder to modify into a new application object.
     *
     * @param application An existing application to modify.
     *
     * @return A new Builder to start building.
     */
    public static Builder builder(Application application) {
        return new Builder(application);
    }

    public static class Builder {
        private Privacy privacy;
        private String id, name;
        private Keys keys;
        private Capabilities capabilities;

        public Builder() {}

        public Builder(Application application) {
            id = application.id;
            name = application.name;
            keys = application.keys;
            capabilities = application.capabilities;
        }

        /**
         * Whether Vonage may store and use your content and data for the improvement of
         * Vonage's AI based services and technologies. Default is {@code true}.
         *
         * @param improveAi {@code true} if you consent to data being used for AI improvement,
         * or {@code false} to opt out.
         *
         * @return This builder.
         *
         * @since 7.7.0
         */
        public Builder improveAi(boolean improveAi) {
            if (privacy == null) {
                privacy = new Privacy();
            }
            privacy.improveAi = improveAi;
            return this;
        }

        /**
         * Set the friendly identifier for your application. This is not unique.
         *
         * @param name The name of the application.
         *
         * @return This builder.
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the application's public key.
         *
         * @param publicKey The public key for use with the application as a string.
         *
         * @return This builder.
         */
        public Builder publicKey(String publicKey) {
            keys = new Keys();
            keys.publicKey = publicKey;
            return this;
        }

        /**
         * Add a capability for the application. Each capability can only be used one time. Adding a capability of a
         * duplicate type will overwrite the previous capability of that type.
         *
         * @param capability The capability to add to it.
         *
         * @return This builder.
         */
        public Builder addCapability(Capability capability) {
            if (capabilities == null) {
                capabilities = new Capabilities();
            }
            capabilities.setCapability(capability);
            return this;
        }

        /**
         * Removes the specified capabilities from the application.
         *
         * @param types The type of capabilities to remove as an array / varargs.
         *
         * @return This builder.
         * @since 8.14.0
         */
        public Builder removeCapabilities(Capability.Type... types) {
            for (Capability.Type type : types) {
                removeCapability(type);
            }
            return this;
        }

        /**
         * Remove a capability from the application.
         *
         * @param type The type of capability to remove.
         *
         * @return This builder.
         */
        public Builder removeCapability(Capability.Type type) {
            if (capabilities != null) {
                capabilities.setCapability(type, null);
                if (
                    capabilities.voice == null &&
                    capabilities.rtc == null &&
                    capabilities.messages == null &&
                    capabilities.vbc == null &&
                    capabilities.verify == null &&
                    capabilities.networkApis == null
                ) {
                    capabilities = null;
                }
            }
            return this;
        }

        /**
         * Builds the Application object.
         *
         * @return A new Application containing the configured properties.
         */
        public Application build() {
            return new Application(this);
        }
    }

    /**
     * Application privacy configuration settings.
     *
     * @since 7.7.0
     */
    public static class Privacy extends JsonableBaseObject {
        private Boolean improveAi;

        /**
         * Whether Vonage may store and use your content and data for the improvement of
         * Vonage's AI based services and technologies.
         *
         * @return {@code true} if Vonage may use the data for improving its AI services,
         * or {@code null} if unspecified.
         */
        @JsonProperty("improve_ai")
        public Boolean getImproveAi() {
            return improveAi;
        }
    }

    /**
     * Represents the cryptographic keys of an Application.
     */
    public static class Keys extends JsonableBaseObject {
        private String publicKey, privateKey;

        /**
         * The application's public key.
         *
         * @return The public key as a string, or {@code null} if absent.
         */
        @JsonProperty("public_key")
        public String getPublicKey() {
            return publicKey;
        }

        /**
         * The application's private key.
         *
         * @return The private key as a string, or {@code null} if absent (the default).
         */
        @JsonProperty("private_key")
        public String getPrivateKey() {
            return privateKey;
        }
    }

    public static class Capabilities extends JsonableBaseObject {
        private Voice voice;
        private Messages messages;
        private Rtc rtc;
        private Vbc vbc;
        private Verify verify;
        private NetworkApis networkApis;

        /**
         * Voice capability.
         *
         * @return The Voice capability, or {@code null} if absent.
         */
        @JsonProperty("voice")
        public Voice getVoice() {
            return voice;
        }

        /**
         * Messages capability.
         *
         * @return The Messages capability, or {@code null} if absent.
         */
        @JsonProperty("messages")
        public Messages getMessages() {
            return messages;
        }

        /**
         * RTC capability.
         *
         * @return The RTC capability, or {@code null} if absent.
         */
        @JsonProperty("rtc")
        public Rtc getRtc() {
            return rtc;
        }

        /**
         * VBC capability.
         *
         * @return The VBC capability, or {@code null} if absent.
         */
        @JsonProperty("vbc")
        public Vbc getVbc() {
            return vbc;
        }

        /**
         * Verify capability.
         *
         * @return The Verify capability, or {@code null} if absent.
         * @since 8.6.0
         */
        @JsonProperty("verify")
        public Verify getVerify() {
            return verify;
        }

        /**
         * Network APIs capability.
         *
         * @return The Network APIs capability, or {@code null} if absent.
         * @since 8.12.0
         */
        @JsonProperty("network_apis")
        public NetworkApis getNetworkApis() {
            return networkApis;
        }

        private void setCapability(Capability.Type type, Capability capability) {
            switch (type) {
                case VOICE:
                    voice = (Voice) capability;
                    break;
                case MESSAGES:
                    messages = (Messages) capability;
                    break;
                case RTC:
                    rtc = (Rtc) capability;
                    break;
                case VBC:
                    vbc = (Vbc) capability;
                    break;
                case VERIFY:
                    verify = (Verify) capability;
                    break;
                case NETWORK:
                    networkApis = (NetworkApis) capability;
                    break;
            }
        }

        private void setCapability(Capability capability) {
            setCapability(capability.getType(), capability);
        }
    }
}
