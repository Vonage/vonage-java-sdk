package com.nexmo.messaging.sdk;

public enum SmsSubmissionErrorCode {
    DELIVERED(0),
    UNKNOWN(1),
    ABSENT_SUBSCRIBER_TEMP(2),
    ABSENT_SUBSCRIBER_PERMANENT(3),
    CALL_BARRED_BY_USER(4),
    PORTABILITY_ERROR(5),
    ANTI_SPAM_REJECTION(6),
    HANDSET_BUSY(7),
    NETWORK_ERROR(8),
    ILLEGAL_NUMBER(9),
    INVALID_MESSAGE(10),
    UNROUTABLE(11),
    GENERAL_ERROR(99);
    
    public int value;
    
    private SmsSubmissionErrorCode(int value) {
        this.value = value;
    }
}
