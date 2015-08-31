package com.nexmo.messaging.sdk.messages;
/*
 * Copyright (c) 2011-2013 Nexmo Inc
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

import com.nexmo.messaging.sdk.messages.parameters.MessageClass;

/**
 * BinaryMessage.java<br><br>
 *
 * Represents the details of a binary message that is to be submitted via the Nexmo REST api<br><br>
 *
 * Created on 5 January 2011, 17:34
 *
 * @author  Paul Cook
 * @version 1.0
 */
public class BinaryMessage extends Message {

    static final long serialVersionUID = -5301775803396025570L;

    /**
     * Instanciate a new binary sms message request.
     *
     * @param from the 'from' address that will be seen on the handset when this message arrives,
     *             typically either a valid short-code / long code that can be replied to, or a short text description of the application sending the message (Max 11 chars)
     * @param to   the phone number of the handset that you wish to send the message to
     * @param binaryMessageBody The raw binary message data to be sent to a handset.
     *                          This api, and the Nexmo messaging service will send this data 'as-is' (in conjunction with the binary UDH) and will not make any corrections.
     *                          so you should ensure that it is a correctly constructed message
     * @param binaryMessageUdh Most binary content will require a UserDataHeader portion of the message containing commands to enable the handset to interpret the binary data
     *                         (for example, a binary ringtone, a wap-push, OverTheAir configuration, etc).
     *                         Additionally, if you are sending a long text message as multiple concatenated messages and are performing this operation manually rather than
     *                         using the automated long messaging handling in the Nexmo messaging service, then you will need to construct and include here an appropriate
     *                         UserDataHeader field that describes the segmentation/re-assembly fields required to sucessfully concatenate multiple short messages.
     */
    public BinaryMessage(final String from,
                         final String to,
                         final byte[] binaryMessageBody,
                         final byte[] binaryMessageUdh) {
        super(MESSAGE_TYPE_BINARY,
              from,
              to,
              null,
              binaryMessageBody,
              binaryMessageUdh,
              null,
              false,
              false,
              null,
              null,
              0,
              null,
              null);
    }

    /**
     * Instanciate a new binary sms message request, attaching an optional client reference, message-class or TTL, and optionally requesting a delivery notification.
     *
     * @param from the 'from' address that will be seen on the handset when this message arrives,
     *             typically either a valid short-code / long code that can be replied to, or a short text description of the application sending the message (Max 11 chars)
     * @param to   the phone number of the handset that you wish to send the message to
     * @param binaryMessageBody The raw binary message data to be sent to a handset.
     *                          This api, and the Nexmo messaging service will send this data 'as-is' (in conjunction with the binary UDH) and will not make any corrections.
     *                          so you should ensure that it is a correctly constructed message
     * @param binaryMessageUdh Most binary content will require a UserDataHeader portion of the message containing commands to enable the handset to interpret the binary data
     *                         (for example, a binary ringtone, a wap-push, OverTheAir configuration, etc).
     *                         Additionally, if you are sending a long text message as multiple concatenated messages and are performing this operation manually rather than
     *                         using the automated long messaging handling in the Nexmo messaging service, then you will need to construct and include here an appropriate
     *                         UserDataHeader field that describes the segmentation/re-assembly fields required to sucessfully concatenate multiple short messages.
     * @param clientReference This is a user definable reference that will be stored in the Nexmo messaging records. It will be available in detailed reporting / analytics
     *                        In order to help with reconcilliation of messages
     * @param statusReportRequired If set to true, then a delivery notification will be requested for this message delivery attempt.
     *                             Upon receiving notification of delivery or failure from the network, the Nexmo platform will submit a notification to the url configured in your
     *                             Nexmo REST account that represents the outcome of this message.
     * @param messageClass (Optional) The message class that is to be applied to this message.
     * @param protocolId The value of the GSM Protocol ID field to be submitted with this message. Ordinarily this should be left as the default value of 0
     */
    public BinaryMessage(final String from,
                         final String to,
                         final byte[] binaryMessageBody,
                         final byte[] binaryMessageUdh,
                         final String clientReference,
                         final boolean statusReportRequired,
                         final MessageClass messageClass,
                         final Integer protocolId) {
        super(MESSAGE_TYPE_BINARY,
              from,
              to,
              null,
              binaryMessageBody,
              binaryMessageUdh,
              clientReference,
              false,
              statusReportRequired,
              null,
              null,
              0,
              messageClass,
              protocolId);
    }

}
