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
package com.nexmo.client.applications;

import org.apache.http.client.methods.RequestBuilder;

public class CreateApplicationRequest {
    private final String name;
    private final ApplicationType type;
    private final String answerUrl;
    private final String eventUrl;

    private String answerMethod;
    private String eventMethod;

    public CreateApplicationRequest(String name, String answerUrl, String eventUrl) {
        this(name, ApplicationType.VOICE, answerUrl, null, eventUrl, null);
    }

    public CreateApplicationRequest(
            String name, ApplicationType type,
            String answerUrl, String answerMethod,
            String eventUrl, String eventMethod) {
        this.name = name;
        this.type = type;
        this.answerUrl = answerUrl;
        this.eventUrl = eventUrl;
        this.answerMethod = answerMethod;
        this.eventMethod = eventMethod;
    }

    public String getName() {
        return name;
    }

    public ApplicationType getType() {
        return type;
    }

    public String getAnswerUrl() {
        return answerUrl;
    }

    public String getEventUrl() {
        return eventUrl;
    }

    public String getAnswerMethod() {
        return answerMethod;
    }

    public void setAnswerMethod(String answerMethod) {
        this.answerMethod = answerMethod;
    }

    public String getEventMethod() {
        return eventMethod;
    }

    public void setEventMethod(String eventMethod) {
        this.eventMethod = eventMethod;
    }

    public void addParams(RequestBuilder request) {
        request.addParameter("name", this.name)
                .addParameter("type", this.type.toString())
                .addParameter("answer_url", this.answerUrl)
                .addParameter("event_url", this.eventUrl);
        if (this.eventMethod != null) {
            request.addParameter("event_method", this.eventMethod);
        }
        if (this.answerMethod != null) {
            request.addParameter("answer_method", this.answerMethod);
        }
    }

}
