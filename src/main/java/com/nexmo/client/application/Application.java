/*
 * Copyright (c) 2011-2017 Vonage Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nexmo.client.application;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.VonageUnexpectedException;
import com.nexmo.client.application.capabilities.*;

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
        this.id = builder.id;
        this.name = builder.name;
        this.keys = builder.keys;
        this.capabilities = builder.capabilities;
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
            this.id = application.id;
            this.name = application.name;
            this.keys = application.keys;
            this.capabilities = application.capabilities;
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
            this.keys = new Keys();
            this.keys.publicKey = publicKey;
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
            if (this.capabilities == null) {
                this.capabilities = new Capabilities();
            }

            this.capabilities.setCapability(capability);

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
            if (this.capabilities == null) {
                this.capabilities = new Capabilities();
            }

            this.capabilities.setCapability(type, null);
            this.capabilities = shouldBeDeleted(this.capabilities) ? null : this.capabilities;
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
                    this.voice = (Voice) capability;
                    break;
                case MESSAGES:
                    this.messages = (Messages) capability;
                    break;
                case RTC:
                    this.rtc = (Rtc) capability;
                    break;
                case VBC:
                    this.vbc = (Vbc) capability;
                    break;
            }
        }

        private void setCapability(Capability capability) {
            setCapability(capability.getType(), capability);
        }
    }
}
