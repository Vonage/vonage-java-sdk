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
package com.nexmo.client.sms.messages;


import org.apache.http.client.methods.RequestBuilder;

/**
 * Represents the details of a wap-push message that is to be submitted via the Nexmo REST api.
 *
 * @author Paul Cook
 */
public class WapPushMessage extends Message {
    private final String url;
    private final String title;
    private Integer validity;

    /**
     * Instantiate a new wap-push message request, to submit a browsable / downloadable URL to the handset
     *
     * @param from  the 'from' address that will be seen on the handset when this message arrives,
     *              typically either a valid short-code / long code that can be replied to, or a short text description of the application sending the message (Max 11 chars)
     * @param to    the phone number of the handset that you wish to send the message to
     * @param url   This is the url that will be submitted to the handset and will appear as a browsable message in the Inbox.
     * @param title This is the title that will be associated with the url being submitted to the handset
     */
    public WapPushMessage(final String from,
                          final String to,
                          final String url,
                          final String title) {
        super(MessageType.WAPPUSH, from, to);

        this.url = url;
        this.title = title;
    }

    /**
     * @return String This is the url that will be submitted to the handset and will appear as a browsable message in the Inbox.
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * @return String This is the title that will be associated with the url being submitted to the handset
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * @return Integer This is the length of time (in seconds) that the message will be available for once delivered to
     * the handset.
     * Once this time has expired, the url will no longer be visible on the handset to be browsed (Subject to handset compatibility)
     */
    public Integer getValidity() {
        return this.validity;
    }

    public void setValidity(Integer validity) {
        this.validity = validity;
    }

    @Override
    public void addParams(RequestBuilder request) {
        super.addParams(request);
        request.addParameter("title", title)
                .addParameter("url", url);
        if (validity != null) {
            request.addParameter("validity", validity.toString());
        }
    }
}
