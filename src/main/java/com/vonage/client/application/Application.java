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
package com.vonage.client.application;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import com.vonage.client.application.capabilities.*;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Application {
    private String id;
    private String name;
    private Keys keys;
    private Capabilities capabilities;

    private Application() {

    }

    private Application(Builder builder) {
        id = builder.id;
        name = builder.name;
        keys = builder.keys;
        capabilities = builder.capabilities;
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

    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException jpe) {
            throw new VonageUnexpectedException("Failed to produce json from Application object.", jpe);
        }
    }

    /**
     * @return A new Builder to start building.
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

    public static Application fromJson(String json) {
        try {
            return new ObjectMapper().readValue(json, Application.class);
        } catch (IOException e) {
            throw new VonageUnexpectedException("Failed to produce Application from json.", e);
        }
    }

    public static class Builder {
        private String id;
        private String name;
        private Keys keys;
        private Capabilities capabilities;

        public Builder() {

        }

        public Builder(Application application) {
            id = application.id;
            name = application.name;
            keys = application.keys;
            capabilities = application.capabilities;
        }

        /**
         * @param name The name of the application.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * @param publicKey The public key for use with the application.
         *
         * @return The {@link Builder} to keep building.
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
         * @return The {@link Builder} to keep building.
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
         * @return The {@link Builder} to keep building.
         */
        public Builder removeCapability(Capability.Type type) {
            if (capabilities == null) {
                capabilities = new Capabilities();
            }

            capabilities.setCapability(type, null);
            capabilities = shouldBeDeleted(capabilities) ? null : capabilities;
            return this;
        }

        /**
         * @return A new Application containing the configured properties.
         */
        public Application build() {
            return new Application(this);
        }

        private boolean shouldBeDeleted(Capabilities capabilities) {
            return (capabilities.voice == null
                    && capabilities.rtc == null
                    && capabilities.messages == null
                    && capabilities.vbc == null);
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
