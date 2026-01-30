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
package com.vonage.client.voice.ncco;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * An NCCO transfer action which enables transferring call legs from a current conversation into another existing conversation.
 * <p>
 * The transfer action is synchronous and terminal for the current conversation.
 * All legs of the current conversation are transferred into the target conversation,
 * respecting the audio settings (canHear, canSpeak, mute) if they're provided.
 * The target conversation's NCCO continues to control the target conversation behaviour.
 */
public class TransferAction extends JsonableBaseObject implements Action {
    private String conversationId;
    private Boolean mute;
    private Collection<String> canSpeak, canHear;

    /**
     * Constructor used reflectively by Jackson for instantiation.
     */
    TransferAction() {}

    private TransferAction(Builder builder) {
        conversationId = Objects.requireNonNull(builder.conversationId, "Conversation ID is required.");
        if (conversationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Conversation ID cannot be empty.");
        }
        mute = builder.mute;
        canSpeak = builder.canSpeak;
        canHear = builder.canHear;
    }

    @Override
    public String getAction() {
        return "transfer";
    }

    /**
     * Target conversation ID to transfer the call legs into.
     *
     * @return The target conversation ID as a string.
     */
    @JsonProperty("conversationId")
    public String getConversationId() {
        return conversationId;
    }

    /**
     * Whether the participant is muted. When muted, the audio from the participant will not be played to
     * the conversation and will not be recorded. When using {@code canSpeak}, the mute parameter is not supported.
     *
     * @return {@code true} if the participant is muted, {@code false} if not muted, or {@code null} if unspecified.
     */
    @JsonProperty("mute")
    public Boolean getMute() {
        return mute;
    }

    /**
     * A list of leg UUIDs that this participant can be heard by. If not provided, the participant can be heard by
     * everyone. If an empty list is provided, the participant will not be heard by anyone.
     *
     * @return The leg UUIDs that can hear this participant speak, or {@code null} if unspecified.
     */
    @JsonProperty("canSpeak")
    public Collection<String> getCanSpeak() {
        return canSpeak;
    }

    /**
     * A list of leg UUIDs that this participant can hear. If not provided, the participant can hear everyone.
     * If an empty list is provided, the participant will not hear any other participants.
     *
     * @return The leg UUIDs that this participant can hear, or {@code null} if unspecified.
     */
    @JsonProperty("canHear")
    public Collection<String> getCanHear() {
        return canHear;
    }

    /**
     * Entrypoint for constructing an instance of this class.
     *
     * @param conversationId The target conversation ID to transfer the call legs into.
     *
     * @return A new Builder with the conversation ID initialized.
     */
    public static Builder builder(String conversationId) {
        return new Builder(conversationId);
    }

    /**
     * Builder for specifying the properties of a Transfer NCCO action. The conversation ID is mandatory.
     */
    public static class Builder {
        private String conversationId;
        private Boolean mute;
        private Collection<String> canSpeak, canHear;

        Builder(String conversationId) {
            this.conversationId = conversationId;
        }

        /**
         * Sets the target conversation ID to transfer the call legs into.
         *
         * @param conversationId The target conversation ID as a string.
         *
         * @return This builder.
         */
        public Builder conversationId(String conversationId) {
            this.conversationId = conversationId;
            return this;
        }

        /**
         * Whether to mute the participant. The audio from the participant will not be played to the
         * conversation and will not be recorded. When using {@linkplain #canSpeak(Collection)}, the
         * mute parameter is not supported.
         *
         * @param mute {@code true} to mute the participant.
         *
         * @return This builder.
         */
        public Builder mute(boolean mute) {
            this.mute = mute;
            return this;
        }

        /**
         * Convenience method for adding a leg ID to the {@code canSpeak} collection.
         * The added leg ID will be able to hear this participant.
         *
         * @param uuid The participant leg ID(s) to add as string(s).
         *
         * @return This builder.
         * @see #canSpeak(Collection)
         */
        public Builder addCanSpeak(String... uuid) {
            if (canSpeak == null) {
                canSpeak = new LinkedHashSet<>();
            }
            canSpeak.addAll(Arrays.asList(uuid));
            return this;
        }

        /**
         * Convenience method for adding a leg ID to the {@code canHear} collection.
         * This participant will be able to hear the participant associated with the provided leg ID.
         *
         * @param uuid The participant leg ID(s) to add as string(s).
         *
         * @return This builder.
         */
        public Builder addCanHear(String... uuid) {
            if (canHear == null) {
                canHear = new LinkedHashSet<>(uuid.length);
            }
            canHear.addAll(Arrays.asList(uuid));
            return this;
        }

        /**
         * A collection of leg UUIDs that this participant can be heard by. If not provided, the participant can
         * be heard by everyone. If an empty collection is provided, the participant will not be heard by anyone.
         * This will replace the current collection.
         *
         * @param canSpeak The leg UUIDs that can hear this participant speak.
         *
         * @return This builder.
         * @see #addCanSpeak(String...)
         */
        public Builder canSpeak(Collection<String> canSpeak) {
            this.canSpeak = canSpeak;
            return this;
        }

        /**
         * A collection of leg UUIDs that this participant can hear. If not provided, the participant can hear
         * everyone. If an empty collection is provided, the participant will not hear any other participants.
         * This will replace the current collection.
         *
         * @param canHear The leg UUIDs that this participant can hear.
         *
         * @return This builder.
         * @see #addCanHear(String...)
         */
        public Builder canHear(Collection<String> canHear) {
            this.canHear = canHear;
            return this;
        }

        /**
         * Builds the Transfer NCCO action.
         *
         * @return A new {@link TransferAction} object from the stored builder options.
         */
        public TransferAction build() {
            if (canSpeak != null) {
                canSpeak = canSpeak.stream().distinct().collect(Collectors.toList());
            }
            if (canHear != null) {
                canHear = canHear.stream().distinct().collect(Collectors.toList());
            }
            return new TransferAction(this);
        }
    }
}
