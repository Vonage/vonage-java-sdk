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

package com.nexmo.client.sms.callback;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;


/**
 * This represents an incoming Delivery Receipt callback request. See also the
 * <a href="https://docs.nexmo.com/messaging/sms-api/api-reference#delivery_receipt">Web API reference</a>.
 */
public class DeliveryReceiptRequest implements Serializable {

    /**
     * 0 - Delivered.
     */
    public static final int ERR_CODE_DELIVERED = 0;
    /**
     * 1 - Unknown - either:
     * <ul>
     * <li>An unknown error was received from the carrier who tried to send this this message.</li>
     * <li>Depending on the carrier, that to is unknown.</li>
     * </ul>
     * When you see this error, and status is rejected, always check if to in your request was valid.
     */
    public static final int ERR_CODE_UNKNOWN = 1;
    /**
     * 2 - Absent Subscriber Temporary - this message was not delivered because to was temporarily unavailable. For
     * example, the handset used for to was out of coverage or switched off. This is a temporary failure, retry
     * later for a positive result.
     */
    public static final int ERR_CODE_ABSENT_SUBSCRIBER_TEMPORARY = 2;
    /**
     * 3 - Absent Subscriber Permanent - to is no longer active, you should remove this phone number from your database.
     */
    public static final int ERR_CODE_ABSENT_SUBSCRIBER_PERMANENT = 3;
    /**
     * 4 - Call barred by user - you should remove this phone number from your database. If the user wants to receive
     * messages from you, they need to contact their carrier directly.
     */
    public static final int ERR_CODE_CALL_BARRED_BY_USER = 4;
    /**
     * 5- Portability Error - there is an issue after the user has changed carrier for to. If the user wants to receive
     * messages from you, they need to contact their carrier directly.
     */
    public static final int ERR_CODE_PORTABILITY_ERROR = 5;
    /**
     * 6 - Anti-Spam Rejection - carriers often apply restrictions that block messages following different criteria. For
     * example, on SenderID or message content.
     */
    public static final int ERR_CODE_ANTI_SPAM_REJECTION = 6;
    /**
     * 7 - Handset Busy - the handset associated with to was not available when this message was sent. If status is
     * Failed, this is a temporary failure; retry later for a positive result. If status is Accepted, this message has
     * is in the retry scheme and will be resent until it expires in 24-48 hours.
     */
    public static final int ERR_CODE_HANDSET_BUSY = 7;
    /**
     * 8 - Network Error - a network failure while sending your message. This is a temporary failure, retry later for a
     * positive result.
     */
    public static final int ERR_CODE_NETWORK_ERROR = 8;
    /**
     * 9 - Illegal Number - you tried to send a message to a blacklisted phone number. That is, the user has already
     * sent a STOP opt-out message and no longer wishes to receive messages from you.
     */
    public static final int ERR_CODE_ILLEGAL_NUMBER = 9;


    /**
     * 10 - Invalid Message - the message could not be sent because one of the parameters in the message was incorrect.
     * For example, incorrect type or udh.
     */
    public static final int ERR_CODE_INVALID_MESSAGE = 10;
    /**
     * 11 - Unroutable - the chosen route to send your message is not available. This is because the phone number is
     * either:
     * <ul>
     * <li>currently on an unsupported network.</li>
     * <li>On a pre-paid or reseller account that could not receive a message sent by from.</li>
     * </ul>
     * To resolve this issue either email us at support@nexmo.com or create a helpdesk ticket at https://help.nexmo.com.
     */
    public static final int ERR_CODE_UNROUTABLE = 11;
    /**
     * 12 - Destination unreachable - the message could not be delivered to the phone number.
     */
    public static final int ERR_CODE_DEST_UNREACHABLE = 12;
    /**
     * 13 - Subscriber Age Restriction - the carrier blocked this message because the content is not suitable for to
     * based on age restrictions.
     */
    public static final int ERR_CODE_SUBSCRIBER_AGE_RESTRICTION = 13;
    /**
     * 14 - Number Blocked by Carrier - the carrier blocked this message. This could be due to several reasons. For
     * example, to's plan does not include SMS or the account is suspended.
     */
    public static final int ERR_CODE_NUMBER_BLOCKED_BY_CARRIER = 14;
    /**
     * 15 - Pre-Paid - Insufficient funds - to’s pre-paid account does not have enough credit to receive the message.
     */
    public static final int ERR_CODE_PRE_PAID_INSUFFICIENT_FUNDS = 15;
    /**
     * 99 - General Error - there is a problem with the chosen route to send your message. To resolve this issue either
     * email us at support@nexmo.com or create a helpdesk ticket at https://help.nexmo.com.
     */
    public static final int ERR_CODE_GENERAL_ERROR = 99;

    /**
     * Describes the delivery status.
     */
    public enum DeliveryStatus {

        /**
         * This message has been delivered to the phone number.
         */
        DELIVERED("delivered"),

        /**
         * The target carrier did not send a status in the 72 hours after this message was delivered to them.
         */
        EXPIRED("expired"),

        /**
         * The target carrier failed to deliver this message.
         */
        FAILED("failed"),
        /**
         * The target carrier rejected this message.
         */
        REJECTED("rejected"),
        /**
         * The target carrier has accepted to send this message.
         */
        ACCEPTED("accepted"),
        /**
         * This message is in the process of being delivered.
         */
        BUFFERED("buffered"),
        /**
         * The target carrier has returned an undocumented status code.
         */
        UNKNOWN("unknown");

        final String status;

        DeliveryStatus(final String status) {
            this.status = status;
        }

        /**
         * @return String A descriptive value representing this status
         */
        public String getStatus() {
            return this.status;
        }

    }

    private static final long serialVersionUID = 966400578542404933L;

    private final String sender;
    private final String destination;
    private final String messageId;
    private final String networkCode;

    private final DeliveryStatus status;
    private final Integer errorCode;

    private final BigDecimal price;
    private final Date scts;
    private final Date timeStamp;
    private final String clientRef;


    public DeliveryReceiptRequest(String sender, String destination, String messageId, String networkCode,
                                  DeliveryStatus status, Integer errorCode, BigDecimal price, Date scts, Date timeStamp,
                                  String clientRef) {
        this.sender = sender;
        this.destination = destination;
        this.messageId = messageId;
        this.networkCode = networkCode;
        this.status = status;
        this.errorCode = errorCode;
        this.price = price;
        this.scts = scts;
        this.timeStamp = timeStamp;
        this.clientRef = clientRef;
    }

    /**
     * Returns the SenderID you set in from in your request.
     *
     * @return the SenderID you set in from in your request.
     */
    public String getSender() {
        return sender;
    }

    /**
     * Returns the phone number this message was sent to.
     *
     * @return the phone number this message was sent to.
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Returns the Nexmo ID for this message.
     *
     * @return the Nexmo ID for this message.
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Returns the Mobile Country Code Mobile Network Code (MCCMNC) of the carrier this phone number is registered with.
     *
     * @return the Mobile Country Code Mobile Network Code (MCCMNC) of the carrier this phone number is registered with.
     */
    public String getNetworkCode() {
        return networkCode;
    }

    /**
     * Returns a code that explains where the message is in the delivery process. If status is not delivered check
     * err-code for more information. If status is accepted ignore the value of err-code.
     *
     * @return a code that explains where the message is in the delivery process.
     */
    public DeliveryStatus getStatus() {
        return status;
    }

    /**
     * Returns the error code if the status is not accepted. See the {@code ERR_CODE_XXX} constants.
     *
     * @return the error code if the status is not accepted.
     */
    public Integer getErrorCode() {
        return errorCode;
    }

    /**
     * Returns how much it cost to send this message.
     *
     * @return how much it cost to send this message.
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Returns the Coordinated Universal Time (UTC) when the DLR was received from the carrier.
     *
     * @return the Coordinated Universal Time (UTC) when the DLR was received from the carrier.
     */
    public Date getScts() {
        return scts;
    }

    /**
     * Returns the time at UTC±00:00 when Nexmo started to push this Delivery Receipt to your webhook endpoint.
     *
     * @return the time at UTC±00:00 when Nexmo started to push this Delivery Receipt to your webhook endpoint.
     */
    public Date getTimeStamp() {
        return timeStamp;
    }

    /**
     * Returns the client-ref you set in the request.
     *
     * @return the client-ref you set in the request.
     */
    public String getClientRef() {
        return clientRef;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SMS-DELIVERY-RECEIPT -- STATUS:").append(status);
        if(errorCode!=null){
            sb.append(" ERROR CODE: ").append(errorCode);
        }
        sb.append(" SENDER: ").append(sender);
        sb.append(" DEST: ").append(destination);
        sb.append(" MSG-ID: ").append(messageId);
        sb.append(" CLIENT-REF: ").append(clientRef);
        sb.append(" PRICE: ").append(price == null ? "-" : price.toPlainString());
        if (networkCode != null) {
            sb.append(" NETWORK: ").append(networkCode);
        }
        DateFormat df = DateFormat.getDateTimeInstance();
        if (timeStamp != null) {
            sb.append(" TIMESTAMP: ").append(df.format(timeStamp));
        }
        if (scts != null) {
            sb.append(" SCTS: ").append(df.format(scts));
        }
        return sb.toString();
    }
}
