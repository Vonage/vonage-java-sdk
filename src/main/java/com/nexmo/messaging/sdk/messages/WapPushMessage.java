package com.nexmo.messaging.sdk.messages;

/**
 * WapPushMessage.java<br><br>
 *
 * Represents the details of a wap-push message that is to be submitted via the Nexmo REST api<br><br>
 *
 * Created on 5 January 2011, 17:34
 *
 * @author  Paul Cook
 * @version 1.0
 */
public class WapPushMessage extends Message {

    static final long serialVersionUID = -8412926886570923963L;

    /**
     * Instanciate a new wap-push message request, to submit a browsable / downloadable URL to the handset
     *
     * @param from the 'from' address that will be seen on the handset when this message arrives,
     *             typically either a valid short-code / long code that can be replied to, or a short text description of the application sending the message (Max 11 chars)
     * @param to   the phone number of the handset that you wish to send the message to
     * @param wapPushUrl This is the url that will be submitted to the handset and will appear as a browsable message in the Inbox.
     * @param wapPushTitle This is the title that will be associated with the url being submitted to the handset
     */
    public WapPushMessage(final String from,
                          final String to,
                          final String wapPushUrl,
                          final String wapPushTitle) {
        super(MESSAGE_TYPE_WAPPUSH,
              from,
              to,
              null,
              null,
              null,
              null,
              false,
              false,
              wapPushUrl,
              wapPushTitle,
              0,
              null,
              null);
    }

    /**
     * Instanciate a new wap-push message request, to submit a browsable / downloadable URL to the handset,
     * attaching an optional client reference, and optionally requesting a delivery notification
     *
     * @param from the 'from' address that will be seen on the handset when this message arrives,
     *             typically either a valid short-code / long code that can be replied to, or a short text description of the application sending the message (Max 11 chars)
     * @param to   the phone number of the handset that you wish to send the message to
     * @param clientReference This is a user definable reference that will be stored in the Nexmo messaging records. It will be available in detailed reporting / analytics
     *                        In order to help with reconcilliation of messages
     * @param statusReportRequired If set to true, then a delivery notification will be requested for this message delivery attempt.
     *                             Upon receiving notification of delivery or failure from the network, the Nexmo platform will submit a notification to the url configured in your
     *                             Nexmo REST account that represents the outcome of this message.
     * @param wapPushUrl This is the url that will be submitted to the handset and will appear as a browsable message in the Inbox.
     * @param wapPushTitle This is the title that will be associated with the url being submitted to the handset
     * @param wapPushValidity This is the length of time (in seconds) that the message will be available for once delivered to the handset.
     *                        Once this time has expired, the url will no longer be visible on the handset to be browsed (Subject to handset compatibility)
     */
    public WapPushMessage(final String from,
                          final String to,
                          final String clientReference,
                          final boolean statusReportRequired,
                          final String wapPushUrl,
                          final String wapPushTitle,
                          final int wapPushValidity) {
        super(MESSAGE_TYPE_WAPPUSH,
              from,
              to,
              null,
              null,
              null,
              clientReference,
              false,
              statusReportRequired,
              wapPushUrl,
              wapPushTitle,
              wapPushValidity,
              null,
              null);
    }

}
