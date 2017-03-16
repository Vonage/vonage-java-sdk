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
 * Represents the details of a plain-text message that is to be submitted via the Nexmo REST api.
 *
 * @author Paul Cook
 */
public class TextMessage extends Message {
    private final String messageBody;

    private boolean unicode = false;

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
        return this.messageBody;
    }

    /**
     * @return boolean This flag is set to true if the message needs to be submitted as a unicode message. This would
     * be for scenario's where the message contains text that does not fit within the Latin GSM alphabet. Examples
     * would be messages to be sent in non-western scripts, such as Arabic, Kanji, Chinese, etc.
     */
    public boolean isUnicode() {
        return this.unicode;
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
