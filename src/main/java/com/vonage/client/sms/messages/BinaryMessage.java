/*
 *   Copyright 2025 Vonage
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

import java.util.Formatter;
import java.util.Map;

/**
 * A binary message to be submitted via the Vonage SMS API.
 */
public class BinaryMessage extends Message {
    private final byte[] messageBody, udh;
    private int protocolId = 0;

    /**
     * Instantiate a new binary SMS message request.
     *
     * @param from              The sender address that will be seen on the handset when this message arrives,
     *                          typically either a valid short-code / long code that can be replied to, or a short
     *                          text description of the application sending the message (Max 11 chars).
     *
     * @param to The phone number of the handset that you wish to send the message to.
     *
     * @param messageBody The raw binary message data to be sent to a handset. The Vonage SMS service will send this
     *                    data 'as-is' (in conjunction with the binary UDH) and will not make any corrections, so you
     *                    should ensure that it is correct.
     *
     * @param udh Most binary content will require a UserDataHeader portion of the message containing commands to
     *            enable the handset to interpret the binary data (for example, a binary ringtone, a WAP-push,
     *            OverTheAir configuration, etc.). Additionally, if you are sending a long text message as multiple
     *            concatenated messages and are performing this operation manually rather than using the automated
     *            long sms handling in the Vonage SMS service, then you will need to construct and include here an
     *            appropriate UserDataHeader field that describes the segmentation / re-assembly fields required to
     *            successfully concatenate multiple short messages.
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
     * The raw binary message data to be sent to a handset. The Vonage SMS service will send this data 'as-is'
     * (in conjunction with the binary UDH) and will not make any corrections, so you should ensure that it is correct.
     *
     * @return The message body as a byte array.
     */
    public byte[] getMessageBody() {
        return messageBody.clone();
    }

    /**
     * Most binary content will require a UserDataHeader portion of the message containing commands to enable the
     * handset to interpret the binary data (for example, a binary ringtone, a WAP-push, OverTheAir configuration, etc.).
     * Additionally, if you are sending a long text message as multiple concatenated messages and are performing this
     * operation manually rather than using the automated long sms handling in the Vonage SMS service, then you will
     * need to construct and include here an appropriate UserDataHeader field that describes the segmentation /
     * re-assembly fields required to successfully concatenate multiple short messages.
     *
     * @return The User Data Header as a byte array.
     */
    public byte[] getUdh() {
        return udh.clone();
    }

    /**
     * The value of the GSM Protocol ID field to be submitted with this message.
     * Ordinarily this should be left as the default value of 0.
     *
     * @return The protocol ID.
     */
    public int getProtocolId() {
        return protocolId;
    }

    /**
     * Sets the protocol ID for this message.
     *
     * @param protocolId The protocol ID.
     */
    public void setProtocolId(int protocolId) {
        this.protocolId = protocolId;
    }

    @Override
    public Map<String, String> makeParams() {
        Map<String, String> params = super.makeParams();
        params.put("udh", toHexString(getUdh()));
        params.put("body", toHexString(getMessageBody()));
        params.put("protocol-id", Integer.toString(getProtocolId()));
        return params;
    }

    private static String toHexString(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        Formatter formatter = new Formatter(sb);
        for (byte b : data) {
            formatter.format("%02x", b);
        }
        return sb.toString();
    }
}
