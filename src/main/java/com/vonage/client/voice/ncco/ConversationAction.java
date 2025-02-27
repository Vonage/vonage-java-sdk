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
import java.util.*;
import java.util.stream.Collectors;

/**
 * An NCCO conversation action which enables the ability to host conference calls.
 */
public class ConversationAction extends JsonableBaseObject implements Action {
    private String name;
    private Boolean startOnEnter, endOnExit, record, mute;
    private EventMethod eventMethod;
    private Collection<String> musicOnHoldUrl, eventUrl, canSpeak, canHear;
    private TranscriptionSettings transcription;

    ConversationAction() {}

    private ConversationAction(Builder builder) {
        name = builder.name;
        musicOnHoldUrl = builder.musicOnHoldUrl;
        startOnEnter = builder.startOnEnter;
        endOnExit = builder.endOnExit;
        mute = builder.mute;
        record = builder.record;
        eventUrl = builder.eventUrl;
        eventMethod = builder.eventMethod;
        canSpeak = builder.canSpeak;
        canHear = builder.canHear;
        if ((transcription = builder.transcription) != null && (record == null || !record)) {
            throw new IllegalStateException("Recording must be enabled for transcription.");
        }
    }

    @Override
    public String getAction() {
        return "conversation";
    }

    /**
     * Name of the Conversation room.
     *
     * @return The conversation name as a string.
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * A URL to the MP3 file to stream to participants until the conversation starts. By default, the conversation
     * starts when the first person calls the virtual number associated with your Voice app.
     *
     * @return The music on hold URL wrapped in a collection.
     */
    @JsonProperty("musicOnHoldUrl")
    public Collection<String> getMusicOnHoldUrl() {
        return musicOnHoldUrl;
    }

    /**
     * The default value of true ensures that the conversation starts when this caller joins conversation name.
     *
     * @return Whether to start the conversation when joining, or {@code null} if unspecified.
     */
    @JsonProperty("startOnEnter")
    public Boolean getStartOnEnter() {
        return startOnEnter;
    }

    /**
     * Specifies whether a moderated conversation ends when the moderator hangs up. This is set to false by default,
     * which means that the conversation only ends when the last remaining participant hangs up, regardless of whether
     * the moderator is still on the call. or hangs up.
     *
     * @return Whether to end the conversation on hangup, or {@code null} if unspecified.
     */
    @JsonProperty("endOnExit")
    public Boolean getEndOnExit() {
        return endOnExit;
    }

    /**
     * Determines if the audio from the participant will be played to the conversation and recorded.
     * When using {@code canSpeak}, the mute parameter is not supported.
     *
     * @return Whether the participant will be muted, or {@code null} if unspecified.
     */
    @JsonProperty("mute")
    public Boolean getMute() {
        return mute;
    }

    /**
     * Determines if this conversation is recorded. For standard conversations, recordings start when one or more
     * attendees connect to the conversation. For moderated conversations, recordings start when the moderator joins.
     *
     * @return Whether recording is enabled, or {@code null} if unspecified.
     */
    @JsonProperty("record")
    public Boolean getRecord() {
        return record;
    }

    /**
     * URL to the webhook endpoint Vonage calls asynchronously on each of the Call States.
     *
     * @return The event URL wrapped in a collection, or {@code null} if unspecified.
     */
    @JsonProperty("eventUrl")
    public Collection<String> getEventUrl() {
        return eventUrl;
    }

    /**
     * HTTP method used to make the request to eventUrl. The default value is POST.
     *
     * @return The event method as an enum, or {@code null} if unspecified.
     */
    @JsonProperty("eventMethod")
    public EventMethod getEventMethod() {
        return eventMethod;
    }

    /**
     * Call leg IDs that can hear this participant speak.
     *
     * @return The leg UUIDs that can hear this participant speak, or {@code null} if unspecified.
     */
    @JsonProperty("canSpeak")
    public Collection<String> getCanSpeak() {
        return canSpeak;
    }

    /**
     * Call leg IDs that this participant can hear.
     *
     * @return The leg UUIDs that this participant can hear, or {@code null} if unspecified.
     */
    @JsonProperty("canHear")
    public Collection<String> getCanHear() {
        return canHear;
    }

    /**
     * Transcription settings. If present (even if all settings are default), transcription is activated.
     *
     * @return The transcription settings, or {@code null} if unspecified.
     */
    @JsonProperty("transcription")
    public TranscriptionSettings getTranscription() {
        return transcription;
    }

    /**
     * Entrypoint for constructing an instance of this class.
     *
     * @param name The name of the Conversation room.
     *
     * @return A new Builder.
     */
    public static Builder builder(String name) {
        return new Builder(name);
    }

    /**
     * Builder for specifying the properties of a Conversation NCCO.
     */
    public static class Builder {
        private String name;
        private EventMethod eventMethod;
        private Boolean startOnEnter, endOnExit, record, mute;
        private Collection<String> musicOnHoldUrl, eventUrl, canSpeak, canHear;
        private TranscriptionSettings transcription;

        Builder(String name) {
            this.name = name;
        }

        /**
         * Sets the name of the Conversation room.
         *
         * @param name The conversation name.
         *
         * @return This builder.
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * A URL to the MP3 file to stream to participants until the conversation starts. By default, the
         * conversation starts when the first person calls the virtual number associated with your Voice app. To
         * stream this MP3 before the moderator joins the conversation, set {@linkplain #startOnEnter(boolean)}
         * to {@code false} for all users other than the moderator.
         *
         * @param musicOnHoldUrl Absolute URL to the hold music in MP3 format, wrapped in a string collection.
         *
         * @return This builder.
         *
         * @deprecated Use {@linkplain #musicOnHoldUrl(String)}. This will be removed in the next major release.
         */
        @Deprecated
        public Builder musicOnHoldUrl(Collection<String> musicOnHoldUrl) {
            this.musicOnHoldUrl = musicOnHoldUrl;
            return this;
        }

        /**
         * A URL to the MP3 file to stream to participants until the conversation starts. By default, the
         * conversation starts when the first person calls the virtual number associated with your Voice app. To
         * stream this MP3 before the moderator joins the conversation, set {@linkplain #startOnEnter(boolean)}
         * to {@code false} for all users other than the moderator.
         *
         * @param musicOnHoldUrl Absolute URL to the hold music in MP3 format, as a string.
         *
         * @return This builder.
         *
         * @deprecated Use {@link #musicOnHoldUrl(Collection)}. This will be removed in the next major release.
         */
        @Deprecated
        public Builder musicOnHoldUrl(String... musicOnHoldUrl) {
            return musicOnHoldUrl(Arrays.asList(musicOnHoldUrl));
        }

        /**
         * A URL to the MP3 file to stream to participants until the conversation starts. By default, the
         * conversation starts when the first person calls the virtual number associated with your Voice app. To
         * stream this MP3 before the moderator joins the conversation, set {@linkplain #startOnEnter(boolean)}
         * to {@code false} for all users other than the moderator.
         *
         * @param musicOnHoldUrl Absolute URL to the hold music in MP3 format, as a string.
         *
         * @return This builder.
         */
        public Builder musicOnHoldUrl(String musicOnHoldUrl) {
            return musicOnHoldUrl(new String[]{musicOnHoldUrl});
        }

        /**
         * The default value of {@code true} ensures that the conversation starts when this caller joins
         * the conversation. Set to false for attendees in a moderated conversation.
         *
         * @param startOnEnter Whether to start the conversation when joining.
         *
         * @return This builder.
         *
         * @deprecated Use {@link #startOnEnter(boolean)}. This will be removed in the next major release.
         */
        @Deprecated
        public Builder startOnEnter(Boolean startOnEnter) {
            this.startOnEnter = startOnEnter;
            return this;
        }

        /**
         * The default value of {@code true} ensures that the conversation starts when this caller joins
         * the conversation. Set to false for attendees in a moderated conversation.
         *
         * @param startOnEnter Whether to start the conversation when joining.
         *
         * @return This builder.
         */
        public Builder startOnEnter(boolean startOnEnter) {
            return startOnEnter(Boolean.valueOf(startOnEnter));
        }

        /**
         * For moderated conversations, set to {@code true} in the moderator NCCO so the conversation is ended
         * when the moderator hangs up. The default value of false means the conversation is not terminated
         * when a caller hangs up; the conversation ends when the last caller hangs up.
         *
         * @param endOnExit Whether to end the conversation when the moderator hangs up.
         *
         * @return This builder.
         *
         * @deprecated Use {@link #endOnExit(boolean)}. This will be removed in the next major release.
         */
        @Deprecated
        public Builder endOnExit(Boolean endOnExit) {
            this.endOnExit = endOnExit;
            return this;
        }

        /**
         * For moderated conversations, set to {@code true} in the moderator NCCO so the conversation is ended
         * when the moderator hangs up. The default value of false means the conversation is not terminated
         * when a caller hangs up; the conversation ends when the last caller hangs up.
         *
         * @param endOnExit Whether to end the conversation when the moderator hangs up.
         *
         * @return This builder.
         */
        public Builder endOnExit(boolean endOnExit) {
            return endOnExit(Boolean.valueOf(endOnExit));
        }

        /**
         * Set to {@code true} to record this conversation. For standard conversations, recordings start
         * when one or more attendees connects to the conversation. For moderated conversations, recordings
         * start when the moderator joins. That is, when an NCCO is executed for the named conversation where
         * startOnEnter is set to true. When the recording is terminated, the URL you download the recording
         * from is sent to the event URL. By default, audio is recorded in MP3 format.
         *
         * @param record Whether to enable recording.
         *
         * @return This builder.
         *
         * @deprecated Use {@link #record(boolean)}. This will be removed in the next major release.
         */
        @Deprecated
        public Builder record(Boolean record) {
            this.record = record;
            return this;
        }

        /**
         * Set to {@code true} to record this conversation. For standard conversations, recordings start
         * when one or more attendees connects to the conversation. For moderated conversations, recordings
         * start when the moderator joins. That is, when an NCCO is executed for the named conversation where
         * startOnEnter is set to true. When the recording is terminated, the URL you download the recording
         * from is sent to the event URL. By default, audio is recorded in MP3 format.
         *
         * @param record Whether to enable recording.
         *
         * @return This builder.
         */
        public Builder record(boolean record) {
            return record(Boolean.valueOf(record));
        }

        /**
         * Set the URL to the webhook endpoint Vonage calls asynchronously on each of the Call States.
         *
         * @param eventUrl The event URL wrapped in a string collection.
         *
         * @return This builder.
         *
         * @deprecated Use {@linkplain #eventUrl(String)}. This will be removed in the next major release.
         */
        @Deprecated
        public Builder eventUrl(Collection<String> eventUrl) {
            this.eventUrl = eventUrl;
            return this;
        }

        /**
         * Set the URL to the webhook endpoint Vonage calls asynchronously on each of the Call States.
         *
         * @param eventUrl The event URL as a string array.
         *
         * @return This builder.
         *
         * @deprecated Use {@linkplain #eventUrl(String)}. This will be removed in the next major release.
         */
        @Deprecated
        public Builder eventUrl(String... eventUrl) {
            return eventUrl(Arrays.asList(eventUrl));
        }

        /**
         * Set the URL to the webhook endpoint Vonage calls asynchronously on each of the Call States.
         *
         * @param eventUrl The event URL as a string.
         *
         * @return This builder.
         */
        public Builder eventUrl(String eventUrl) {
            return eventUrl(new String[]{eventUrl});
        }

        /**
         * Set the HTTP method used to make the request to eventUrl. The default value is POST.
         *
         * @param eventMethod The event method as an enum.
         *
         * @return This builder.
         */
        public Builder eventMethod(EventMethod eventMethod) {
            this.eventMethod = eventMethod;
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
         * @since 8.2.0
         */
        public Builder mute(boolean mute) {
            this.mute = mute;
            return this;
        }

        /**
         * Convenience method for adding a leg ID to the {@code canSpeak} collection.
         * The added leg ID will be able to hear this participant.
         *
         * @param uuid The participant leg ID to add as a string.
         *
         * @return This builder.
         * @since 8.2.0
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
         * @param uuid The participant leg ID to add as a string.
         *
         * @return This builder.
         * @since 8.2.0
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
         * @since 8.2.0
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
         * @since 8.2.0
         * @see #addCanHear(String...)
         */
        public Builder canHear(Collection<String> canHear) {
            this.canHear = canHear;
            return this;
        }

        /**
         * Transcription settings. If present (even if all settings are default), transcription is activated.
         * The {@linkplain #record(Boolean)} parameter must be set to {@code true}.
         *
         * @param transcription The transcriptions settings.
         *
         * @return This builder.
         * @since 8.2.0
         */
        public Builder transcription(TranscriptionSettings transcription) {
            this.transcription = transcription;
            return this;
        }

        /**
         * Builds the Conversation NCCO action.
         *
         * @return A new {@link ConversationAction} object from the stored builder options.
         */
        public ConversationAction build() {
            if (canSpeak != null) {
                canSpeak = canSpeak.stream().distinct().collect(Collectors.toList());
            }
            if (canHear != null) {
                canHear = canHear.stream().distinct().collect(Collectors.toList());
            }
            return new ConversationAction(this);
        }
    }
}
