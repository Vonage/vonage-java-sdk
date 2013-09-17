package com.nexmo.messaging.sdk.messages;

import com.nexmo.messaging.sdk.messages.parameters.MessageClass;

/**
 * Message.java<br><br>
 *
 * Created on 5 January 2011, 17:34<br><br>
 *
 * Represents the details of a message that is to be submitted via the http api.<br>
 * Not instanciated directly, but instead via one of the sub-classes that represent a particular message type
 *
 * @author  Paul Cook
 * @version 1.0
 */
public abstract class Message implements java.io.Serializable {

    private static final long serialVersionUID = 3847700435531116012L;

    /**
     * Message ia a regular TEXT SMS message
     */
    public static final int MESSAGE_TYPE_TEXT = 1;
    /**
     * Message ia a binary SMS message with a custom UDH and binary payload
     */
    public static final int MESSAGE_TYPE_BINARY = 2;
    /**
     * Message is a wap-push message to send a browsable / downloadable url to the handset
     */
    public static final int MESSAGE_TYPE_WAPPUSH = 3;
    /**
     * Message is a unicode message, for sending messages in non-latin script to a supported handset
     */
    public static final int MESSAGE_TYPE_UNICODE = 4;

    private final int type;

    private final String from;
    private final String to;
    private final String messageBody;
    private final byte[] binaryMessageBody;
    private final byte[] binaryMessageUdh;
    private final String clientReference;
    private final boolean unicode;
    private final boolean statusReportRequired;
    private final String wapPushUrl;
    private final String wapPushTitle;
    private final int wapPushValidity;
    private final MessageClass messageClass;
    private final Integer protocolId;

    /**
     * Instanciate a new message request.<br>
     * This constructor exposes the full range of possible parameters and is not for general use
     * Instead, it is accessed via super() in the constructors of various sub-classes that expose a relevant
     * sub-set of the available parameters
     *
     * @param type the type of message will influence the makeup of the request we post to the Nexmo server, and also the action taken by the Nexmo server in response to this message
     * @param from the 'from' address that will be seen on the handset when this message arrives,
     *             typically either a valid short-code / long code that can be replied to, or a short text description of the application sending the message (Max 11 chars)
     * @param to   the phone number of the handset that you wish to send the message to
     * @param messageBody The text of the message to be sent to the handset
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
     * @param unicode This flag is set to true if the message needs to be submitted as a unicode message. This would be for scenario's where the message contains text that does not
     *                fit within the Latin GSM alphabet. Examples would be messages to be sent in non-western scripts, such as Arabic, Kanji, Chinese, etc.
     * @param statusReportRequired If set to true, then a delivery notification will be requested for this message delivery attempt.
     *                             Upon receiving notification of delivery or failure from the network, the Nexmo platform will submit a notification to the url configured in your
     *                             Nexmo REST account that represents the outcome of this message.
     * @param wapPushUrl This is the url that will be submitted to the handset and will appear as a browsable message in the Inbox.
     * @param wapPushTitle This is the title that will be associated with the url being submitted to the handset
     * @param wapPushValidity This is the length of time (in seconds) that the message will be available for once delivered to the handset.
     *                        Once this time has expired, the url will no longer be visible on the handset to be browsed (Subject to handset compatibility)
     * @param messageClass (Optional) The message class that is to be applied to this message.
     * @param protocolId The value of the GSM Protocol ID field to be submitted with this message. Ordinarily this should be left as the default value of 0
     */
    protected Message(final int type,
                      final String from,
                      final String to,
                      final String messageBody,
                      final byte[] binaryMessageBody,
                      final byte[] binaryMessageUdh,
                      final String clientReference,
                      final boolean unicode,
                      final boolean statusReportRequired,
                      final String wapPushUrl,
                      final String wapPushTitle,
                      final int wapPushValidity,
                      final MessageClass messageClass,
                      final Integer protocolId) {
        this.type = type;

        this.from = from;
        this.to = to;
        this.messageBody = messageBody;
        this.binaryMessageBody = binaryMessageBody;
        this.binaryMessageUdh = binaryMessageUdh;
        this.clientReference = clientReference;
        this.unicode = unicode;
        this.statusReportRequired = statusReportRequired;
        this.wapPushUrl = wapPushUrl;
        this.wapPushTitle = wapPushTitle;
        this.wapPushValidity = wapPushValidity;
        this.messageClass = messageClass;
        this.protocolId = protocolId;
    }

    /**
     * @return int the type of message will influence the makeup of the request we post to the Nexmo server, and also the action taken by the Nexmo server in response to this message
     */
    public int getType() {
        return this.type;
    }

    /**
     * @return String the 'from' address that will be seen on the handset when this message arrives,
     *                typically either a valid short-code / long code that can be replied to, or a short text description of the application sending the message (Max 11 chars)
     */
    public String getFrom() {
        return this.from;
    }

    /**
     * @return String the phone number of the handset that you wish to send the message to
     */
    public String getTo() {
        return this.to;
    }

    /**
     * @return String The text of the message to be sent to the handset
     */
    public String getMessageBody() {
        return this.messageBody;
    }

    /**
     * @return byte[] The raw binary message data to be sent to a handset.
     *                This api, and the Nexmo messaging service will send this data 'as-is' (in conjunction with the binary UDH) and will not make any corrections.
     *                so you should ensure that it is a correctly constructed message
     */
    public byte[] getBinaryMessageBody() {
        return this.binaryMessageBody == null ? null : this.binaryMessageBody.clone();
    }

    /**
     * @return byte[] Most binary content will require a UserDataHeader portion of the message containing commands to enable the handset to interpret the binary data
     *                (for example, a binary ringtone, a wap-push, OverTheAir configuration, etc).
     *                Additionally, if you are sending a long text message as multiple concatenated messages and are performing this operation manually rather than
     *                using the automated long messaging handling in the Nexmo messaging service, then you will need to construct and include here an appropriate
     *                UserDataHeader field that describes the segmentation/re-assembly fields required to sucessfully concatenate multiple short messages.
     */
    public byte[] getBinaryMessageUdh() {
        return this.binaryMessageUdh == null ? null : this.binaryMessageUdh.clone();
    }

    /**
     * @return String This is a user definable reference that will be stored in the Nexmo messaging records. It will be available in detailed reporting / analytics
     *                In order to help with reconcilliation of messages
     */
    public String getClientReference() {
        return this.clientReference;
    }

    /**
     * @return boolean This flag is set to true if the message needs to be submitted as a unicode message. This would be for scenario's where the message contains text that does not
     *                 fit within the Latin GSM alphabet. Examples would be messages to be sent in non-western scripts, such as Arabic, Kanji, Chinese, etc.
     */
    public boolean isUnicode() {
        return this.unicode;
    }

    /**
     * @return boolean If set to true, then a delivery notification will be requested for this message delivery attempt.
     *                 Upon receiving notification of delivery or failure from the network, the Nexmo platform will submit a notification to the url configured in your
     *                 Nexmo REST account that represents the outcome of this message.
     */
    public boolean getStatusReportRequired() {
        return this.statusReportRequired;
    }

    /**
     * @return String This is the url that will be submitted to the handset and will appear as a browsable message in the Inbox.
     */
    public String getWapPushUrl() {
        return this.wapPushUrl;
    }

    /**
     * @return String This is the title that will be associated with the url being submitted to the handset
     */
    public String getWapPushTitle() {
        return this.wapPushTitle;
    }

    /**
     * @return int This is the length of time (in seconds) that the message will be available for once delivered to the handset.
     *             Once this time has expired, the url will no longer be visible on the handset to be browsed (Subject to handset compatibility)
     */
    public int getWapPushValidity() {
        return this.wapPushValidity;
    }

    /**
     * @return com.nexmo.messaging.sdk.messages.parameters.MessageClass The message class that is to be applied to this message.
     */
    public MessageClass getMessageClass() {
        return this.messageClass;
    }

    /**
     * @return Integer The value of the GSM Protocol ID field to be submitted with this message. Ordinarily this should be left as the default value of 0
     */
    public Integer getProtocolId() {
        return this.protocolId;
    }

}
