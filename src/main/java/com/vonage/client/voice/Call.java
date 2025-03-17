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
package com.vonage.client.voice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.voice.ncco.Action;
import com.vonage.client.voice.ncco.EventMethod;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Call encapsulates the information required to create a call using {@link VoiceClient#createCall(Call)}
 */
public class Call extends JsonableBaseObject {
    private Endpoint[] to;
    private Endpoint from;
    private EventMethod answerMethod, eventMethod;
    private Collection<URI> answerUrl, eventUrl;
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
        if ((lengthTimer = builder.lengthTimer) != null && (lengthTimer > 86400 || lengthTimer < 1)) {
            throw new IllegalArgumentException("Length timer must be between 1 and 86400.");
        }
        if ((ringingTimer = builder.ringingTimer) != null && (ringingTimer > 120 || ringingTimer < 1)) {
            throw new IllegalArgumentException("Ringing timer must be between 1 and 120.");
        }
        if ((answerMethod = builder.answerMethod) != null) switch (answerMethod) {
            case GET: case POST: break;
            default: throw new IllegalArgumentException("Answer method must be GET or POST.");
        }
        if ((eventMethod = builder.eventMethod) != null) switch (eventMethod) {
            case GET: case POST: break;
            default: throw new IllegalArgumentException("Event method must be GET or POST.");
        }
        if ((from = builder.from) == null) {
            fromRandomNumber = true;
        }
        else if ((fromRandomNumber = builder.fromRandomNumber) != null && fromRandomNumber) {
            throw new IllegalStateException("From number shouldn't be set if using random.");
        }
        answerUrl = builder.answerUrl != null ? Collections.singletonList(URI.create(builder.answerUrl)) : null;
        eventUrl = builder.eventUrl != null ? Collections.singletonList(URI.create(builder.eventUrl)) : null;
        ncco = builder.ncco;
        advancedMachineDetection = builder.advancedMachineDetection;
        if ((machineDetection = builder.machineDetection) != null && advancedMachineDetection != null) {
            throw new IllegalStateException("Cannot set both machineDetection and advancedMachineDetection.");
        }
    }

    Call() {}

    /**
     * Create a new Call with {@linkplain PhoneEndpoint} as the recipient and caller.
     *
     * @param to The recipient phone number.
     * @param from The caller phone number.
     * @param answerUrl The URL to fetch the NCCO from.
     */
    public Call(String to, String from, String answerUrl) {
        this(new PhoneEndpoint(to), new PhoneEndpoint(from), answerUrl);
    }

    /**
     * Create a new Call with the specified recipient and caller endpoints.
     *
     * @param to The recipient endpoint.
     * @param from The caller endpoint.
     * @param answerUrl The URL to fetch the NCCO from.
     */
    public Call(Endpoint to, Endpoint from, String answerUrl) {
        this(builder().to(to).from(from).answerUrl(answerUrl));
    }

    /**
     * Create a new Call with {@linkplain PhoneEndpoint} as the recipient and caller.
     *
     * @param to The recipient phone number.
     * @param from The caller phone number.
     * @param ncco The NCCO actions to take.
     */
    public Call(String to, String from, Collection<? extends Action> ncco) {
        this(new PhoneEndpoint(to), new PhoneEndpoint(from), ncco);
    }

    /**
     * Create a new Call with the specified recipient and caller endpoints.
     *
     * @param to The recipient endpoint.
     * @param from The caller endpoint.
     * @param ncco The NCCO actions to take.
     */
    public Call(Endpoint to, Endpoint from, Collection<? extends Action> ncco) {
        this(builder().to(to).from(from).ncco(ncco));
    }

    /**
     * Endpoint of the call recipient.
     *
     * @return The callee endpoint wrapped in an array.
     */
    @JsonProperty("to")
    public Endpoint[] getTo() {
        return to;
    }

    /**
     * Endpoint from which the call will be made.
     *
     * @return The caller number / URI, or {@code null} if unspecified.
     */
    @JsonProperty("from")
    public Endpoint getFrom() {
        return from;
    }

    /**
     * URL that Vonage makes a request to when the call is answered to retrieve NCCOs.
     *
     * @return The publicly accessible NCCO URL wrapped in a collection, or {@code null} if unspecified.
     */
    @JsonProperty("answer_url")
    public Collection<URI> getAnswerUrl() {
        return answerUrl;
    }

    /**
     * HTTP method used to send event information to {@code answer_url}.
     *
     * @return The HTTP answer method type as an enum, or {@code null} if not set.
     */
    @JsonProperty("answer_method")
    public EventMethod getAnswerMethod() {
        // Hide the answer method if the answer url isn't defined
        if (answerUrl == null || answerMethod == null) return null;
        return answerMethod;
    }

    /**
     * URL to send event information to.
     *
     * @return The event URL wrapped in a collection, or {@code null} if not set.
     */
    @JsonProperty("event_url")
    public Collection<URI> getEventUrl() {
        return eventUrl;
    }

    /**
     * HTTP method used to send event information to {@code event_url}.
     *
     * @return The HTTP event method type as an enum, or {@code null} if unspecified (the default).
     */
    @JsonProperty("event_method")
    public EventMethod getEventMethod() {
        return eventMethod;
    }

    /**
     * Machine detection behaviour.
     *
     * @return The machine detection mode as an enum, or {@code null} if not set.
     */
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

    /**
     * Call timeout i.e. when Vonage will hang up after the call is answered.
     *
     * @return The call length in seconds, or {@code null} if unspecified (the default).
     */
    @JsonProperty("length_timer")
    public Integer getLengthTimer() {
        return lengthTimer;
    }

    /**
     * The time to wait whilst ringing in seconds.
     *
     * @return The ringing timer in seconds, or {@code null} if unspecified (the default).
     */
    @JsonProperty("ringing_timer")
    public Integer getRingingTimer() {
        return ringingTimer;
    }

    /**
     * Whether the call will be made from a random number assigned to the application.
     *
     * @return {@code true} if a random number will be used to for the caller ID, or {@code null} if unspecified.
     */
    @JsonProperty("random_from_number")
    public Boolean getFromRandomNumber() {
        return fromRandomNumber;
    }

    /**
     * Gets the NCCOs to use for this call.
     *
     * @return The NCCO actions.
     */
    @JsonProperty("ncco")
    public Collection<? extends Action> getNcco() {
        return ncco;
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
        private EventMethod answerMethod = EventMethod.GET, eventMethod;
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
         * Sets the outbound caller.
         *
         * @param caller The caller's phone number (in E.164 format) or SIP URI.
         *
         * @return This builder.
         */
        public Builder from(String caller) {
            return from(new PhoneEndpoint(caller));
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
        public Builder eventMethod(EventMethod eventMethod) {
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
        public Builder answerMethod(EventMethod answerMethod) {
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
         * @param lengthTimer The call length in seconds. The default is 7200 (2 hours).
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
