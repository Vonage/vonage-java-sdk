package com.nexmo.messaging.sdk;

/**
 * SmsSubmissionReachabilityStatus.java<br><br>
 *
 * Created on 5 January 2011, 17:34<br><br>
 *
 * Represents the result of the reachability check that was performed when submitting the message if such a check was requested
 *
 * @author  Paul Cook
 * @version 1.0
 */
public class SmsSubmissionReachabilityStatus implements java.io.Serializable {

    static final long serialVersionUID = 8121497095898864184L;

    /**
     * The reachability status of this messages destination could not be determined
     */
    public static final int REACHABILITY_STATUS_UNKNOWN = 0;

    /**
     * The destination of this message is reachable
     */
    public static final int REACHABILITY_STATUS_REACHABLE = 1;

    /**
     * The destination of this message can not be reached and thus can not be delivered to
     */
    public static final int REACHABILITY_STATUS_UNDELIVERABLE = 2;

    /**
     * The destinationof this message is temporarily unavailable (eg, switched off)
     */
    public static final int REACHABILITY_STATUS_ABSENT = 3;

    /**
     * The destination of this message is not a valid destination and thus could not be delivered to
     */
    public static final int REACHABILITY_STATUS_BAD_NUMBER = 4;

    private final int status;
    private final String description;

    protected SmsSubmissionReachabilityStatus(final int status,
                                              final String description) {
        this.status = status;
        this.description = description;
    }

    /**
     * @return int status code representing outcome of the reachability check performed on this message
     */
    public int getStatus() {
        return this.status;
    }

    /**
     * @return String A description of the outcome of the reachability check performed on this message
     */
    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return "REACHABILITY -- STAT [ " + this.status + " ] [ " + this.description + " ] ";
    }

}
