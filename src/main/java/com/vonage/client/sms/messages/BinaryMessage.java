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


import com.vonage.client.sms.HexUtil;
import org.apache.http.client.methods.RequestBuilder;

/**
 * A binary message to be submitted via the Vonage SMS API.
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
     *                          This api, and the Vonage sms service will send this data 'as-is' (in conjunction with the binary UDH) and will not make any corrections.
     *                          so you should ensure that it is a correctly constructed message
     * @param udh  Most binary content will require a UserDataHeader portion of the message containing commands to enable the handset to interpret the binary data
     *                          (for example, a binary ringtone, a wap-push, OverTheAir configuration, etc).
     *                          Additionally, if you are sending a long text message as multiple concatenated messages and are performing this operation manually rather than
     *                          using the automated long sms handling in the Vonage sms service, then you will need to construct and include here an appropriate
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
     * This api, and the Vonage sms service will send this data 'as-is' (in conjunction with the binary UDH) and will not make any corrections.
     * so you should ensure that it is a correctly constructed message
     */
    public byte[] getMessageBody() {
        return messageBody == null ? null : messageBody.clone();
    }

    /**
     * @return byte[] Most binary content will require a UserDataHeader portion of the message containing commands to enable the handset to interpret the binary data
     * (for example, a binary ringtone, a wap-push, OverTheAir configuration, etc).
     * Additionally, if you are sending a long text message as multiple concatenated messages and are performing this operation manually rather than
     * using the automated long sms handling in the Vonage sms service, then you will need to construct and include here an appropriate
     * UserDataHeader field that describes the segmentation/re-assembly fields required to successfully concatenate multiple short messages.
     */
    public byte[] getUdh() {
        return udh == null ? null : udh.clone();
    }

    /**
     * @return Integer The value of the GSM Protocol ID field to be submitted with this message. Ordinarily this should be left as the default value of 0
     */
    public int getProtocolId() {
        return protocolId;
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
