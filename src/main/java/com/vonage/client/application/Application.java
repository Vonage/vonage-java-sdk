/*
 *   Copyright 2023 Vonage
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import com.vonage.client.application.capabilities.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Application implements Jsonable {
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

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Keys getKeys() {
        return keys;
    }

    public Capabilities getCapabilities() {
        return capabilities;
    }

    public Privacy getPrivacy() {
        return privacy;
    }

    public static Application fromJson(String json) {
        Application application = new Application();
        application.updateFromJson(json);
        return application;
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
         * Sets the application name.
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
         * Remove a capability from the application.
         *
         * @param type The type of capability to remove.
         *
         * @return This builder.
         */
        public Builder removeCapability(Capability.Type type) {
            if (capabilities != null) {
                capabilities.setCapability(type, null);
                boolean noCapabilities =  capabilities.voice == null &&
                        capabilities.rtc == null && capabilities.messages == null && capabilities.vbc == null;
                if (noCapabilities) {
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
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Privacy {
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Keys {
        @JsonProperty("public_key")
        private String publicKey;
        @JsonProperty("private_key")
        private String privateKey;

        public String getPublicKey() {
            return publicKey;
        }

        public String getPrivateKey() {
            return privateKey;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Capabilities {
        private Voice voice;
        private Messages messages;
        private Rtc rtc;
        private Vbc vbc;

        public Voice getVoice() {
            return voice;
        }

        public Messages getMessages() {
            return messages;
        }

        public Rtc getRtc() {
            return rtc;
        }

        public Vbc getVbc() {
            return vbc;
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
            }
        }

        private void setCapability(Capability capability) {
            setCapability(capability.getType(), capability);
        }
    }
}
