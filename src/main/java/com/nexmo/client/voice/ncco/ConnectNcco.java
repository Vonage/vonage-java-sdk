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
import com.nexmo.client.voice.Endpoint;
import com.nexmo.client.voice.MachineDetection;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConnectNcco implements Ncco {
    private static final String ACTION = "connect";

    private Endpoint[] endpoint;
    private String from = null;
    private Integer timeout = null;
    private Integer limit = null;
    private MachineDetection machineDetection = null;
    private String[] eventUrl = null;
    private String eventMethod = null;

    public ConnectNcco(@JsonProperty("endpoint") Endpoint[] endpoint) {
        this.endpoint = endpoint;
    }

    public ConnectNcco(Endpoint endpoint) {
        this(new Endpoint[]{endpoint});
    }

    public ConnectNcco(String number) {
        this(new Endpoint(number));
    }

    public Endpoint[] getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Endpoint endpoint) {
        setEndpoint(new Endpoint[]{endpoint});
    }

    @JsonProperty("endpoint")
    public void setEndpoint(Endpoint[] endpoint) {
        this.endpoint = endpoint;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public MachineDetection getMachineDetection() {
        return machineDetection;
    }

    public void setMachineDetection(MachineDetection machineDetection) {
        this.machineDetection = machineDetection;
    }

    public String[] getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String eventUrl) {
        setEventUrl(new String[]{eventUrl});
    }

    @JsonProperty("eventUrl")
    public void setEventUrl(String[] eventUrl) {
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
