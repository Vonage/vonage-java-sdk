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
 * An NCCO stream action which allows for media to be streamed to a call.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StreamAction implements Action {
    private static final String ACTION = "stream";

    private Collection<String> streamUrl;
    private Float level;
    private Boolean bargeIn;
    private Integer loop;

    private StreamAction(Builder builder) {
        this.streamUrl = builder.streamUrl;
        this.level = builder.level;
        this.bargeIn = builder.bargeIn;
        this.loop = builder.loop;
    }

    @Override
    public String getAction() {
        return ACTION;
    }

    public Collection<String> getStreamUrl() {
        return streamUrl;
    }

    public Float getLevel() {
        return level;
    }

    public Boolean getBargeIn() {
        return bargeIn;
    }

    public Integer getLoop() {
        return loop;
    }

    public static class Builder {
        private Collection<String> streamUrl;
        private Float level = null;
        private Boolean bargeIn = null;
        private Integer loop = null;

        public Builder(Collection<String> streamUrl) {
            this.streamUrl = streamUrl;
        }

        public Builder(String... streamUrl) {
            this(Arrays.asList(streamUrl));
        }

        public Builder streamUrl(Collection<String> streamUrl) {
            this.streamUrl = streamUrl;
            return this;
        }

        public Builder streamUrl(String... streamUrl) {
            return streamUrl(Arrays.asList(streamUrl));
        }

        public Builder level(Float level) {
            this.level = level;
            return this;
        }

        public Builder bargeIn(Boolean bargeIn) {
            this.bargeIn = bargeIn;
            return this;
        }

        public Builder loop(Integer loop) {
            this.loop = loop;
            return this;
        }

        public StreamAction build() {
            return new StreamAction(this);
        }
    }
}
