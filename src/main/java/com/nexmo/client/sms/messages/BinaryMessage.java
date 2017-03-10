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


import com.nexmo.client.sms.HexUtil;
import org.apache.http.client.methods.RequestBuilder;

/**
 * A binary message to be submitted via the Nexmo SMS API.
 */
public class BinaryMessage extends Message {
    private final byte[] messageBody;
    private final byte[] udh;
    private int protocolId = 0;

    /**
     * Instantiate a new binary sms message request.
     *
     * @param from              the 'from' address that will be seen on the handset when this message arrives,
     *                          typically either a valid short-code / long code that can be replied to, or a short text description of the application sending the message (Max 11 chars)
     * @param to                the phone number of the handset that you wish to send the message to
     * @param messageBody The raw binary message data to be sent to a handset.
     *                          This api, and the Nexmo sms service will send this data 'as-is' (in conjunction with the binary UDH) and will not make any corrections.
     *                          so you should ensure that it is a correctly constructed message
     * @param udh  Most binary content will require a UserDataHeader portion of the message containing commands to enable the handset to interpret the binary data
     *                          (for example, a binary ringtone, a wap-push, OverTheAir configuration, etc).
     *                          Additionally, if you are sending a long text message as multiple concatenated messages and are performing this operation manually rather than
     *                          using the automated long sms handling in the Nexmo sms service, then you will need to construct and include here an appropriate
     *                          UserDataHeader field that describes the segmentation/re-assembly fields required to successfully concatenate multiple short messages.
     */
    public BinaryMessage(final String from,
                         final String to,
                         final byte[] messageBody,
                         final byte[] udh
    ) {
        super(MessageType.BINARY, from, to);
        this.messageBody = messageBody;
        this.udh = udh;
    }

    /**
     * @return byte[] The raw binary message data to be sent to a handset.
     * This api, and the Nexmo sms service will send this data 'as-is' (in conjunction with the binary UDH) and will not make any corrections.
     * so you should ensure that it is a correctly constructed message
     */
    public byte[] getMessageBody() {
        return this.messageBody == null ? null : this.messageBody.clone();
    }

    /**
     * @return byte[] Most binary content will require a UserDataHeader portion of the message containing commands to enable the handset to interpret the binary data
     * (for example, a binary ringtone, a wap-push, OverTheAir configuration, etc).
     * Additionally, if you are sending a long text message as multiple concatenated messages and are performing this operation manually rather than
     * using the automated long sms handling in the Nexmo sms service, then you will need to construct and include here an appropriate
     * UserDataHeader field that describes the segmentation/re-assembly fields required to successfully concatenate multiple short messages.
     */
    public byte[] getUdh() {
        return this.udh == null ? null : this.udh.clone();
    }

    /**
     * @return Integer The value of the GSM Protocol ID field to be submitted with this message. Ordinarily this should be left as the default value of 0
     */
    public int getProtocolId() {
        return this.protocolId;
    }

    public void setProtocolId(int protocolId) {
        this.protocolId = protocolId;
    }

    @Override
    public void addParams(RequestBuilder request) {
        super.addParams(request);
        request.addParameter("udh", HexUtil.bytesToHex(getUdh()));
        request.addParameter("body", HexUtil.bytesToHex(getMessageBody()));
        request.addParameter("protocol-id", Integer.toString(protocolId));
    }
}
