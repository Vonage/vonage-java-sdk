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
package com.vonage.client.voice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import com.vonage.client.common.HttpMethod;
import com.vonage.client.voice.ncco.Action;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Call encapsulates the information required to create a call using {@link VoiceClient#createCall(Call)}
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"_links"})
public class Call {
    private Endpoint[] to;
    private Endpoint from;
    private HttpMethod answerMethod = HttpMethod.GET, eventMethod;
    private String answerUrl, eventUrl;
    private MachineDetection machineDetection;
    private AdvancedMachineDetection advancedMachineDetection;
    private Integer lengthTimer, ringingTimer;
    private Boolean fromRandomNumber;
    private Collection<? extends Action> ncco;

    /**
     * Builder pattern constructor.
     *
     * @param builder The builder to instantiate from.
     * @since 7.3.0
     */
    Call(Builder builder) {
        if ((to = builder.to) == null || to.length == 0 || to[0] == null) {
            throw new IllegalStateException("At least one recipient should be specified.");
        }
        if ((lengthTimer = builder.lengthTimer) != null && (lengthTimer > 7200 || lengthTimer < 1)) {
            throw new IllegalArgumentException("Length timer must be between 1 and 7200.");
        }
        if ((ringingTimer = builder.ringingTimer) != null && (ringingTimer > 120 || ringingTimer < 1)) {
            throw new IllegalArgumentException("Ringing timer must be between 1 and 120.");
        }
        if (builder.answerMethod != null) switch (answerMethod = builder.answerMethod) {
            case GET: case POST: break;
            default: throw new IllegalArgumentException("Answer method must be GET or POST");
        }
        if ((eventMethod = builder.eventMethod) != null) switch (eventMethod) {
            case GET: case POST: break;
            default: throw new IllegalArgumentException("Event method must be GET or POST");
        }
        from = builder.from;
        if ((fromRandomNumber = builder.fromRandomNumber) != null && fromRandomNumber && from != null) {
            throw new IllegalStateException("From number shouldn't be set if using random.");
        }
        answerUrl = builder.answerUrl;
        eventUrl = builder.eventUrl;
        ncco = builder.ncco;
        advancedMachineDetection = builder.advancedMachineDetection;
        if ((machineDetection = builder.machineDetection) != null && advancedMachineDetection != null) {
            throw new IllegalStateException("Cannot set both machineDetection and advancedMachineDetection.");
        }
    }

    /**
     * @deprecated Use {@link #builder()}.
     */
    @Deprecated
    public Call() {
    }

    public Call(String to, String from, String answerUrl) {
        this(new PhoneEndpoint(to), new PhoneEndpoint(from), answerUrl);
    }

    public Call(Endpoint to, Endpoint from, String answerUrl) {
        this(new Endpoint[]{to}, from, answerUrl);
    }

    public Call(String to, String from, Collection<? extends Action> ncco) {
        this(new PhoneEndpoint(to), new PhoneEndpoint(from), ncco);
    }

    public Call(Endpoint to, Endpoint from, Collection<? extends Action> ncco) {
        this(new Endpoint[]{to}, from, ncco);
    }

    public Call(Endpoint[] to, Endpoint from, String answerUrl) {
        this(builder().to(to).from(from).answerUrl(answerUrl));
    }

    public Call(Endpoint[] to, Endpoint from, Collection<? extends Action> ncco) {
        this(builder().to(to).from(from).ncco(ncco));
    }

    @JsonProperty("to")
    public Endpoint[] getTo() {
        return to;
    }

    @JsonProperty("from")
    public Endpoint getFrom() {
        return from;
    }

    @JsonProperty("answer_url")
    public String[] getAnswerUrl() {
        return (answerUrl != null) ? new String[]{answerUrl} : null;
    }

    @JsonProperty("answer_method")
    public String getAnswerMethod() {
        // Hide the answer method if the answer url isn't defined
        if (answerUrl == null || answerMethod == null) return null;
        return answerMethod.toString();
    }

    @JsonProperty("event_url")
    public String[] getEventUrl() {
        if (eventUrl == null) {
            return null;
        }
        return new String[]{eventUrl};
    }

    @JsonProperty("event_method")
    public String getEventMethod() {
        return eventMethod != null ? eventMethod.toString() : null;
    }

    @JsonProperty("machine_detection")
    public MachineDetection getMachineDetection() {
        return machineDetection;
    }

    /**
     * Premium machine detection, overrides {@link #getMachineDetection()} if both are set.
     *
     * @return The advanced machine detection settings, or {@code null} if not set.
     *
     * @since 7.4.0
     */
    @JsonProperty("advanced_machine_detection")
    public AdvancedMachineDetection getAdvancedMachineDetection() {
        return advancedMachineDetection;
    }

    @JsonProperty("length_timer")
    public Integer getLengthTimer() {
        return lengthTimer;
    }

    @JsonProperty("ringing_timer")
    public Integer getRingingTimer() {
        return ringingTimer;
    }

    @JsonProperty("random_from_number")
    public Boolean getFromRandomNumber() {
        return fromRandomNumber;
    }

    @Deprecated
    public void setTo(Endpoint[] to) {
        this.to = to;
    }

    @Deprecated
    public void setFrom(Endpoint from) {
        this.from = from;
    }

    @Deprecated
    public void setAnswerUrl(String answerUrl) {
        this.answerUrl = answerUrl;
    }

    @Deprecated
    public void setAnswerMethod(String answerMethod) {
        this.answerMethod = HttpMethod.fromString(answerMethod);
    }

    @Deprecated
    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }

    @Deprecated
    public void setEventMethod(String eventMethod) {
        this.eventMethod = HttpMethod.fromString(eventMethod);
    }

    @Deprecated
    public void setMachineDetection(MachineDetection machineDetection) {
        this.machineDetection = machineDetection;
    }

    @Deprecated
    public void setLengthTimer(Integer lengthTimer) {
        this.lengthTimer = lengthTimer;
    }

    @Deprecated
    public void setRingingTimer(Integer ringingTimer) {
        this.ringingTimer = ringingTimer;
    }

    /**
     * Set to true to use random phone number as from. The number will be selected from the list of the numbers assigned to the current application. random_from_number: true cannot be used together with from.
     * @param fromRandomNumber Whether to use random number.
     *
     * @deprecated Use {@link Builder#fromRandomNumber(boolean)}.
     */
    @Deprecated
    public void setFromRandomNumber(Boolean fromRandomNumber) {
        this.fromRandomNumber = fromRandomNumber;
    }

    @JsonProperty("ncco")
    public Collection<? extends Action> getNcco() {
        return ncco;
    }

    /**
     * Generates a JSON payload from this request.
     *
     * @return JSON representation of this Call object.
     */
    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException jpe) {
            throw new VonageUnexpectedException("Failed to produce json from Call object.", jpe);
        }
    }

    /**
     * Creates an instance of this class from a JSON payload.
     *
     * @param json The JSON string to parse.
     *
     * @return An instance of this class with the fields populated, if present.
     */
    public static Call fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, Call.class);
        } catch (IOException jpe) {
            throw new VonageUnexpectedException("Failed to produce json from Call object.", jpe);
        }
    }

    /**
     * Entrypoint for constructing an instance of this class.
     *
     * @return A new Builder.
     * @since 7.3.0
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for constructing a Call object.
     *
     * @since 7.3.0
     */
    public static class Builder {
        private Endpoint[] to;
        private Endpoint from;
        private HttpMethod answerMethod, eventMethod;
        private String answerUrl, eventUrl;
        private MachineDetection machineDetection;
        private AdvancedMachineDetection advancedMachineDetection;
        private Integer lengthTimer, ringingTimer;
        private Boolean fromRandomNumber;
        private Collection<Action> ncco;

        Builder() {}

        /**
         * Sets the endpoints (recipients) of the call.
         *
         * @param endpoints The recipients of the call in order.
         *
         * @return This builder.
         */
        public Builder to(Endpoint... endpoints) {
            to = endpoints;
            return this;
        }

        /**
         * Connect to a Phone (PSTN) number.
         *
         * @param number The number to place the call from in E.164 format.
         *
         * @return This builder.
         */
        public Builder from(String number) {
            return from(new PhoneEndpoint(number));
        }

        /**
         * Sets the outbound caller.
         *
         * @param from The caller endpoint.
         *
         * @return This builder.
         */
        Builder from(Endpoint from) {
            this.from = from;
            return this;
        }

        /**
         * The HTTP method used to send event information to {@code event_url}.
         *
         * @param eventMethod The method type (must be {@code GET} or {@code POST}).
         *
         * @return This builder.
         */
        public Builder eventMethod(HttpMethod eventMethod) {
            this.eventMethod = eventMethod;
            return this;
        }

        /**
         * The HTTP method used to send event information to {@code answer_url}.
         *
         * @param answerMethod The method type (must be {@code GET} or {@code POST}).
         *
         * @return This builder.
         */
        public Builder answerMethod(HttpMethod answerMethod) {
            this.answerMethod = answerMethod;
            return this;
        }

        /**
         * The webhook endpoint where call progress events are sent to.
         * For more information about the values sent, see the
         * <a href=https://developer.vonage.com/en/voice/voice-api/webhook-reference#event-webhook>
         * Event webhook documentation</a>.
         *
         * @param eventUrl The event updates URL.
         *
         * @return This builder.
         */
        public Builder eventUrl(String eventUrl) {
            this.eventUrl = eventUrl;
            return this;
        }

        /**
         * The webhook endpoint where you provide the Nexmo Call Control Object that governs this call.
         *
         * @param answerUrl The NCCO answer URL.
         *
         * @return This builder.
         */
        public Builder answerUrl(String answerUrl) {
            this.answerUrl = answerUrl;
            return this;
        }

        /**
         * Configure the behavior when Vonage detects that the call is answered by voicemail. If
         * {@linkplain MachineDetection#CONTINUE}, Vonage sends an HTTP request to {@code event_url} with the Call
         * event machine. If {@linkplain MachineDetection#HANGUP}, Vonage ends the call.
         *
         * @param machineDetection The machine detection mode.
         *
         * @return This builder.
         */
        public Builder machineDetection(MachineDetection machineDetection) {
            this.machineDetection = machineDetection;
            return this;
        }

        /**
         * Configure the behavior of Vonage's advanced machine detection. This overrides the
         * {@link #machineDetection(MachineDetection)}, so you cannot set both.
         *
         * @param advancedMachineDetection The advanced machine detection settings.
         *
         * @return This builder.
         *
         * @since 7.4.0
         */
        public Builder advancedMachineDetection(AdvancedMachineDetection advancedMachineDetection) {
            this.advancedMachineDetection = advancedMachineDetection;
            return this;
        }

        /**
         * Sets the number of seconds that elapse before Vonage hangs up after the call is answered.
         *
         * @param lengthTimer The call length in seconds. The default and maximum is 7200.
         *
         * @return This builder.
         */
        public Builder lengthTimer(int lengthTimer) {
            this.lengthTimer = lengthTimer;
            return this;
        }

        /**
         * Sets the number of seconds that elapse before Vonage hangs up after the call state changes to ‘ringing’.
         *
         * @param ringingTimer The time to wait whilst ringing in seconds. Maximum is 120, default is 60.
         *
         * @return This builder.
         */
        public Builder ringingTimer(int ringingTimer) {
            this.ringingTimer = ringingTimer;
            return this;
        }

        /**
         * Set to @{code true} to use random phone number as the caller. The number will be selected from the list
         * of the numbers assigned to the current application. Cannot be used if {@link #from(String)} is set.
         *
         * @param fromRandomNumber Whether to use a random number instead of {@code from}.
         *
         * @return This builder.
         */
        public Builder fromRandomNumber(boolean fromRandomNumber) {
            this.fromRandomNumber = fromRandomNumber;
            return this;
        }

        /**
         * Sets the actions to take on the call.
         *
         * @param actions The actions in order.
         *
         * @return This builder.
         */
        public Builder ncco(Action... actions) {
            return ncco(Arrays.asList(actions));
        }

        /**
         * Sets the actions to take on the call.
         *
         * @param nccos The NCCOs to use for this call.
         *
         * @return This builder.
         */
        public Builder ncco(Collection<? extends Action> nccos) {
            ncco = new ArrayList<>(nccos);
            return this;
        }

        /**
         * Builds the Call object with this builder's properties.
         *
         * @return The constructed Call instance.
         */
        public Call build() {
            return new Call(this);
        }
    }
}
