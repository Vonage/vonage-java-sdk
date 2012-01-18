package com.nexmo.sns.sdk.response;
/**
 * PublishResult.java<br><br>
 *
 * Created on 5 January 2011, 17:34<br><br>
 *
 * Represents the result of a publish service request to the Nexmo SNS Service
 *
 * @author  Paul Cook
 * @version 1.0
 */
public interface PublishResult {

    public String getCommand();
    public int getResultCode();
    public String getResultMessage();
    public String getTransactionId();

}
