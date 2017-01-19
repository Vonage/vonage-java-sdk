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

import com.fasterxml.jackson.annotation.JsonProperty;

public class StreamNcco implements Ncco {
    private static final String ACTION = "stream";

    private String streamUrl;
    private Float level = null;
    private Boolean bargeIn = null;
    private Integer loop = null;

    public StreamNcco(@JsonProperty("streamUrl") String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public Float getLevel() {
        return level;
    }

    public void setLevel(Float level) {
        this.level = level;
    }

    public Boolean getBargeIn() {
        return bargeIn;
    }

    public void setBargeIn(Boolean bargeIn) {
        this.bargeIn = bargeIn;
    }

    public Integer getLoop() {
        return loop;
    }

    public void setLoop(Integer loop) {
        this.loop = loop;
    }

    @Override
    public String getAction() {
        return ACTION;
    }

    @Override
    public String toJson() {
        return NccoSerializer.getInstance().serializeNcco(this);
    }
}
