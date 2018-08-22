/*
 * Copyright (c) 2011-2017 Nexmo Inc
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
package com.nexmo.client.voice.ncco;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Arrays;
import java.util.Collection;

/**
 * An NCCO record action which allows for the call to be recorded.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordAction implements Action {
    private static final String ACTION = "record";

    private RecordingFormat format;
    private Integer endOnSilence;
    private Character endOnKey;
    private Integer timeOut;
    private Boolean beepStart;
    private Collection<String> eventUrl;
    private EventMethod eventMethod;
    private SplitRecording split;

    public RecordAction(Builder builder) {
        this.format = builder.format;
        this.endOnSilence = builder.endOnSilence;
        this.endOnKey = builder.endOnKey;
        this.timeOut = builder.timeOut;
        this.beepStart = builder.beepStart;
        this.eventUrl = builder.eventUrl;
        this.eventMethod = builder.eventMethod;
        this.split = builder.split;
    }

    @Override
    public String getAction() {
        return ACTION;
    }

    public RecordingFormat getFormat() {
        return format;
    }

    public Integer getEndOnSilence() {
        return endOnSilence;
    }

    public Character getEndOnKey() {
        return endOnKey;
    }

    public Integer getTimeOut() {
        return timeOut;
    }

    public Boolean getBeepStart() {
        return beepStart;
    }

    public Collection<String> getEventUrl() {
        return eventUrl;
    }

    public EventMethod getEventMethod() {
        return eventMethod;
    }

    public SplitRecording getSplit() {
        return split;
    }

    public static class Builder {
        private RecordingFormat format = null;
        private Integer endOnSilence = null;
        private Character endOnKey = null;
        private Integer timeOut = null;
        private Boolean beepStart = null;
        private Collection<String> eventUrl = null;
        private EventMethod eventMethod = null;
        private SplitRecording split = null;

        public Builder format(RecordingFormat format) {
            this.format = format;
            return this;
        }

        public Builder endOnSilence(Integer endOnSilence) {
            this.endOnSilence = endOnSilence;
            return this;
        }

        public Builder endOnKey(Character endOnKey) {
            this.endOnKey = endOnKey;
            return this;
        }

        public Builder timeOut(Integer timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        public Builder beepStart(Boolean beepStart) {
            this.beepStart = beepStart;
            return this;
        }

        public Builder eventUrl(Collection<String> eventUrl) {
            this.eventUrl = eventUrl;
            return this;
        }

        public Builder eventUrl(String... eventUrl) {
            return eventUrl(Arrays.asList(eventUrl));
        }

        public Builder eventMethod(EventMethod eventMethod) {
            this.eventMethod = eventMethod;
            return this;
        }

        public Builder split(SplitRecording split) {
            this.split = split;
            return this;
        }

        public RecordAction build() {
            return new RecordAction(this);
        }
    }
}
