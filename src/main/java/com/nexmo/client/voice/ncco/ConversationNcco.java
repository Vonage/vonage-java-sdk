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
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConversationNcco implements Ncco {
    private static final String ACTION = "conversation";

    private String name;
    private String musicOnHoldUrl = null;
    private Boolean startOnEnter = null;
    private Boolean endOnExit = null;
    private Boolean record = null;
    private String[] eventUrl = null;
    private String eventMethod = null;


    public ConversationNcco(@JsonProperty("name") String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMusicOnHoldUrl() {
        return musicOnHoldUrl;
    }

    @JsonProperty("musicOnHoldUrl")
    public String[] getMusicOnHoldUrlAsArray() {
        // TODO: Rework in 4.0.
        // This property is expected to be serialized as an array, however we want to also insure it remains null
        // if null.
        return this.musicOnHoldUrl != null ? new String[]{this.musicOnHoldUrl} : null;
    }

    public void setMusicOnHoldUrl(String... musicOnHoldUrl) {
        this.musicOnHoldUrl = musicOnHoldUrl[0];
    }

    public Boolean getStartOnEnter() {
        return startOnEnter;
    }

    public void setStartOnEnter(Boolean startOnEnter) {
        this.startOnEnter = startOnEnter;
    }

    public Boolean getEndOnExit() {
        return endOnExit;
    }

    public void setEndOnExit(Boolean endOnExit) {
        this.endOnExit = endOnExit;
    }

    public Boolean getRecord() {
        return record;
    }

    public void setRecord(Boolean record) {
        this.record = record;
    }

    public String[] getEventUrl() {
        return eventUrl;
    }

    @JsonProperty("eventUrl")
    public void setEventUrl(String... eventUrl) {
        this.eventUrl = eventUrl;
    }

    public String getEventMethod() {
        return eventMethod;
    }

    public void setEventMethod(String eventMethod) {
        this.eventMethod = eventMethod;
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