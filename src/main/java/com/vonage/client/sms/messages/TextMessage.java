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
 * Represents the details of a plain-text message that is to be submitted via the Vonage REST api.
 *
 *
 */
public class TextMessage extends Message {
    private final String messageBody;

    private boolean unicode;

    /**
     * Instantiate a new text-message request.<br>
     * This message will be submitted as a regular 8 bit text message
     *
     * @param from        the 'from' address that will be seen on the handset when this message arrives,
     *                    typically either a valid short-code / long code that can be replied to, or a short text
     *                    description of the application sending the message (Max 11 chars)
     * @param to          the phone number of the handset that you wish to send the message to
     * @param messageBody The text of the message to be sent to the handset
     */
    public TextMessage(final String from,
                       final String to,
                       final String messageBody) {
        this(from, to, messageBody, false);
    }

    /**
     * Instantiate a new text-message request.<br>
     * This message will be submitted as a regular 8 bit text message
     *
     * @param from        the 'from' address that will be seen on the handset when this message arrives,
     *                    typically either a valid short-code / long code that can be replied to, or a short text
     *                    description of the application sending the message (Max 11 chars)
     * @param to          the phone number of the handset that you wish to send the message to
     * @param messageBody The text of the message to be sent to the handset
     * @param unicode     set this flag to true if the message needs to be submitted as a unicode message
     */
    public TextMessage(final String from,
                       final String to,
                       final String messageBody,
                       final boolean unicode) {
        super(null, from, to);
        this.messageBody = messageBody;
        this.unicode = unicode;
    }

    /**
     * @return String The text of the message to be sent to the handset
     */
    public String getMessageBody() {
        return messageBody;
    }

    /**
     * @return boolean This flag is set to true if the message needs to be submitted as a unicode message. This would
     * be for scenario's where the message contains text that does not fit within the Latin GSM alphabet. Examples
     * would be messages to be sent in non-western scripts, such as Arabic, Kanji, Chinese, etc.
     */
    public boolean isUnicode() {
        return unicode;
    }

    @Override
    public MessageType getType() {
        if (unicode) {
            return MessageType.UNICODE;
        } else {
            return MessageType.TEXT;
        }
    }

    @Override
    public void addParams(RequestBuilder request) {
        super.addParams(request);
        request.addParameter("text", messageBody);
    }
}
