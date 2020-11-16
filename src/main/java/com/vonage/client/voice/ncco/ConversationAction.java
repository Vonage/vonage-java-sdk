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
package com.vonage.client.voice.ncco;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Arrays;
import java.util.Collection;

/**
 * An NCCO conversation action which enables the ability to host conference calls.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConversationAction implements Action {
    private static final String ACTION = "conversation";

    private String name;
    private Collection<String> musicOnHoldUrl;
    private Boolean startOnEnter;
    private Boolean endOnExit;
    private Boolean record;
    private Collection<String> eventUrl;
    private EventMethod eventMethod;

    private ConversationAction(Builder builder) {
        name = builder.name;
        musicOnHoldUrl = builder.musicOnHoldUrl;
        startOnEnter = builder.startOnEnter;
        endOnExit = builder.endOnExit;
        record = builder.record;
        eventUrl = builder.eventUrl;
        eventMethod = builder.eventMethod;
    }

    @Override
    public String getAction() {
        return ACTION;
    }

    public String getName() {
        return name;
    }

    public Collection<String> getMusicOnHoldUrl() {
        return musicOnHoldUrl;
    }

    public Boolean getStartOnEnter() {
        return startOnEnter;
    }

    public Boolean getEndOnExit() {
        return endOnExit;
    }

    public Boolean getRecord() {
        return record;
    }

    public Collection<String> getEventUrl() {
        return eventUrl;
    }

    public EventMethod getEventMethod() {
        return eventMethod;
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public static class Builder {
        private String name;
        private Collection<String> musicOnHoldUrl;
        private Boolean startOnEnter;
        private Boolean endOnExit;
        private Boolean record;
        private Collection<String> eventUrl;
        private EventMethod eventMethod;

        /**
         * @param name The name of the Conversation room.
         */
        public Builder(String name) {
            this.name = name;
        }

        /**
         * @param name The name of the Conversation room.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * @param musicOnHoldUrl A URL to the mp3 file to stream to participants until the conversation starts.
         *                       By default the conversation starts when the first person calls the virtual number
         *                       associated with your Voice app. To stream this mp3 before the moderator joins the
         *                       conversation, set startOnEnter to false for all users other than the moderator.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder musicOnHoldUrl(Collection<String> musicOnHoldUrl) {
            this.musicOnHoldUrl = musicOnHoldUrl;
            return this;
        }

        /**
         * @param musicOnHoldUrl A URL to the mp3 file to stream to participants until the conversation starts.
         *                       By default the conversation starts when the first person calls the virtual number
         *                       associated with your Voice app. To stream this mp3 before the moderator joins the
         *                       conversation, set startOnEnter to false for all users other than the moderator.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder musicOnHoldUrl(String... musicOnHoldUrl) {
            return musicOnHoldUrl(Arrays.asList(musicOnHoldUrl));
        }

        /**
         * @param startOnEnter The default value of true ensures that the conversation starts when this caller joins
         *                     conversation name. Set to false for attendees in a moderated conversation.
         *
         * @return The {@link Builder} to keep building.
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
         * @return The {@link Builder} to keep building.
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
         *               By default audio is recorded in MP3 format. See the <a href="https://developer.nexmo.com/voice/voice-api/guides/recordingfile-formats">recording guide</a> for more details
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder record(Boolean record) {
            this.record = record;
            return this;
        }

        /**
         * @param eventUrl Set the URL to the webhook endpoint Vonage calls asynchronously on each of the
         *                 <a href="https://developer.nexmo.com/voice/voice-api/guides/call-flowcall-states">Call States</a>.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder eventUrl(Collection<String> eventUrl) {
            this.eventUrl = eventUrl;
            return this;
        }

        /**
         * @param eventUrl Set the URL to the webhook endpoint Vonage calls asynchronously on each of the
         *                 <a href="https://developer.nexmo.com/voice/voice-api/guides/call-flowcall-states">Call States</a>.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder eventUrl(String... eventUrl) {
            return eventUrl(Arrays.asList(eventUrl));
        }

        /**
         * @param eventMethod Set the HTTP method used to make the request to eventUrl. The default value is POST.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder eventMethod(EventMethod eventMethod) {
            this.eventMethod = eventMethod;
            return this;
        }

        /**
         * @return A new {@link ConversationAction} object from the stored builder options.
         */
        public ConversationAction build() {
            return new ConversationAction(this);
        }
    }
}
