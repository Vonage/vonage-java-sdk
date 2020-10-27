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
package com.vonage.client.sms.messages;


import org.apache.http.client.methods.RequestBuilder;

/**
 * Represents the details of a wap-push message that is to be submitted via the Vonage REST api.
 *
 *
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
        return url;
    }

    /**
     * @return String This is the title that will be associated with the url being submitted to the handset
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return Integer This is the length of time (in seconds) that the message will be available for once delivered to
     * the handset.
     * Once this time has expired, the url will no longer be visible on the handset to be browsed (Subject to handset compatibility)
     */
    public Integer getValidity() {
        return validity;
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
