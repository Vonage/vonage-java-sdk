/*
 *   Copyright 2024 Vonage
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

/**
 * An NCCO conversation action which enables the ability to host conference calls.
 */
public class ConversationAction extends JsonableBaseObject implements Action {
    private static final String ACTION = "conversation";

    private String name;
    private Boolean startOnEnter, endOnExit, record, mute;
    private EventMethod eventMethod;
    private Collection<String> musicOnHoldUrl, eventUrl;
    private Collection<UUID> canSpeak, canHear;
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
        return ACTION;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("musicOnHoldUrl")
    public Collection<String> getMusicOnHoldUrl() {
        return musicOnHoldUrl;
    }

    @JsonProperty("startOnEnter")
    public Boolean getStartOnEnter() {
        return startOnEnter;
    }

    @JsonProperty("endOnExit")
    public Boolean getEndOnExit() {
        return endOnExit;
    }

    @JsonProperty("mute")
    public Boolean getMute() {
        return mute;
    }

    @JsonProperty("record")
    public Boolean getRecord() {
        return record;
    }

    @JsonProperty("eventUrl")
    public Collection<String> getEventUrl() {
        return eventUrl;
    }

    @JsonProperty("eventMethod")
    public EventMethod getEventMethod() {
        return eventMethod;
    }

    @JsonProperty("canSpeak")
    public Collection<UUID> getCanSpeak() {
        return canSpeak;
    }

    @JsonProperty("canHear")
    public Collection<UUID> getCanHear() {
        return canHear;
    }

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

    public static class Builder {
        private String name;
        private EventMethod eventMethod;
        private Boolean startOnEnter, endOnExit, record, mute;
        private Collection<String> musicOnHoldUrl, eventUrl;
        private Collection<UUID> canSpeak, canHear;
        private TranscriptionSettings transcription;

        Builder(String name) {
            this.name = name;
        }

        /**
         * @param name The name of the Conversation room.
         *
         * @return This builder.
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * @param musicOnHoldUrl A URL to the mp3 file to stream to participants until the conversation starts.
         *                       By default, the conversation starts when the first person calls the virtual number
         *                       associated with your Voice app. To stream this mp3 before the moderator joins the
         *                       conversation, set startOnEnter to false for all users other than the moderator.
         *
         * @return This builder.
         */
        public Builder musicOnHoldUrl(Collection<String> musicOnHoldUrl) {
            this.musicOnHoldUrl = musicOnHoldUrl;
            return this;
        }

        /**
         * @param musicOnHoldUrl A URL to the mp3 file to stream to participants until the conversation starts.
         *                       By default, the conversation starts when the first person calls the virtual number
         *                       associated with your Voice app. To stream this mp3 before the moderator joins the
         *                       conversation, set startOnEnter to false for all users other than the moderator.
         *
         * @return This builder.
         */
        public Builder musicOnHoldUrl(String... musicOnHoldUrl) {
            return musicOnHoldUrl(Arrays.asList(musicOnHoldUrl));
        }

        /**
         * @param startOnEnter The default value of true ensures that the conversation starts when this caller joins
         *                     conversation name. Set to false for attendees in a moderated conversation.
         *
         * @return This builder.
         */
        public Builder startOnEnter(Boolean startOnEnter) {
            this.startOnEnter = startOnEnter;
            return this;
        }

        /**
         * @param endOnExit For moderated conversations, set to true in the moderator NCCO so the conversation is
         *                  ended when the moderator hangs up. The default value of false means the conversation
         *                  is not terminated when a caller hangs up; the conversation ends when the last caller
         *                  hangs up.
         *
         * @return This builder.
         */
        public Builder endOnExit(Boolean endOnExit) {
            this.endOnExit = endOnExit;
            return this;
        }

        /**
         * @param record Set to true to record this conversation. For standard conversations, recordings start when one
         *               or more attendees connects to the conversation. For moderated conversations, recordings start
         *               when the moderator joins. That is, when an NCCO is executed for the named conversation where
         *               startOnEnter is set to true. When the recording is terminated, the URL you download the
         *               recording from is sent to the event URL.
         *               <p>
         *               By default, audio is recorded in MP3 format. See the <a href="https://developer.nexmo.com/voice/voice-api/guides/recordingfile-formats">recording guide</a> for more details
         *
         * @return This builder.
         */
        public Builder record(Boolean record) {
            this.record = record;
            return this;
        }

        /**
         * @param eventUrl Set the URL to the webhook endpoint Vonage calls asynchronously on each of the
         *                 <a href="https://developer.nexmo.com/voice/voice-api/guides/call-flowcall-states">Call States</a>.
         *
         * @return This builder.
         */
        public Builder eventUrl(Collection<String> eventUrl) {
            this.eventUrl = eventUrl;
            return this;
        }

        /**
         * @param eventUrl Set the URL to the webhook endpoint Vonage calls asynchronously on each of the
         *                 <a href="https://developer.nexmo.com/voice/voice-api/guides/call-flowcall-states">Call States</a>.
         *
         * @return This builder.
         */
        public Builder eventUrl(String... eventUrl) {
            return eventUrl(Arrays.asList(eventUrl));
        }

        /**
         * @param eventMethod Set the HTTP method used to make the request to eventUrl. The default value is POST.
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
        public Builder addCanSpeak(String uuid) {
            if (canSpeak == null) {
                canSpeak = new LinkedHashSet<>();
            }
            canSpeak.add(UUID.fromString(uuid));
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
         * @see #canHear(Collection)
         */
        public Builder addCanHear(String uuid) {
            if (canHear == null) {
                canHear = new LinkedHashSet<>();
            }
            canHear.add(UUID.fromString(uuid));
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
         * @see #addCanSpeak(String)
         */
        public Builder canSpeak(Collection<UUID> canSpeak) {
            if (canSpeak == null) {
                this.canSpeak = null;
            }
            else {
                this.canSpeak = new LinkedHashSet<>(canSpeak);
            }
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
         * @see #addCanHear(String)
         */
        public Builder canHear(Collection<UUID> canHear) {
            if (canHear == null) {
                this.canHear = null;
            }
            else {
                this.canHear = new LinkedHashSet<>(canHear);
            }
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
                canSpeak = new ArrayList<>(canSpeak);
            }
            if (canHear != null) {
                canHear = new ArrayList<>(canHear);
            }
            return new ConversationAction(this);
        }
    }
}
