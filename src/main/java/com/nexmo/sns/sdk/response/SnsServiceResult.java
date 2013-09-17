package com.nexmo.sns.sdk.response;
/**
 * SnsServiceResult.java<br><br>
 *
 * Created on 5 January 2011, 17:34<br><br>
 *
 * Represents the result of the service request to the Nexmo SNS Service
 *
 * @author  Paul Cook
 * @version 1.0
 */
public class SnsServiceResult implements PublishResult,
                                         SubscribeResult {

    public static final int STATUS_OK = 0;
    public static final int STATUS_BAD_COMMAND = 1;
    public static final int STATUS_INTERNAL_ERROR = 2;
    public static final int STATUS_INVALID_ACCOUNT = 3;
    public static final int STATUS_MISSING_TOPIC = 4;
    public static final int STATUS_INVALID_OR_MISSING_MSISDN = 5;
    public static final int STATUS_INVALID_OR_MISSING_FROM = 6;
    public static final int STATUS_INVALID_OR_MISSING_MSG = 7;
    public static final int STATUS_TOPIC_NOT_FOUND = 8;
    public static final int STATUS_TOPIC_PERMISSION_FAILURE = 9;
    public static final int STATUS_COMMS_FAILURE = 13;

    private final String command;
    private final int resultCode;
    private final String resultMessage;
    private final String transactionId;
    private final String subscriberArn;

    public SnsServiceResult(final String command,
                            final int resultCode,
                            final String resultMessage,
                            final String transactionId,
                            final String subscriberArn) {
        this.command = command;
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
        this.transactionId = transactionId;
        this.subscriberArn = subscriberArn;
    }

    @Override
    public String getCommand() {
        return this.command;
    }

    @Override
    public int getResultCode() {
        return this.resultCode;
    }

    @Override
    public String getResultMessage() {
        return this.resultMessage;
    }

    @Override
    public String getTransactionId() {
        return this.transactionId;
    }

    @Override
    public String getSubscriberArn() {
        return this.subscriberArn;
    }

}
