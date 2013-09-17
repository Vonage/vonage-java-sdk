package com.nexmo.messaging.sdk.examples;

import com.nexmo.messaging.sdk.NexmoSmsClient;
import com.nexmo.messaging.sdk.NexmoSmsClientSSL;
import com.nexmo.messaging.sdk.SmsSubmissionResult;
import com.nexmo.messaging.sdk.messages.TextMessage;

/**
 * SendTextMessageHttps.java<br><br>
 *
 * An example of using the nexmo sms api to submit a simple text message ...<br><br>
 * Uses a https connection rather than the default http connection
 *
 * Created on 5 January 2011, 17:34
 *
 * @author  Paul Cook
 * @version 1.0
 */
public class SendTextMessageHttps {

    public static final String API_KEY = "your-api-key-goes-here";
    public static final String API_SECRET = "your-api-secret-goes-here";

    public static final String SMS_FROM = "12345";
    public static final String SMS_TO = "447777111222";
    public static final String SMS_TEXT = "Hello World!";

    public static void main(String[] args) {

        // Create a client for submitting to Nexmo

        NexmoSmsClient client = null;
        try {
            client = new NexmoSmsClientSSL(API_KEY, API_SECRET);
        } catch (Exception e) {
            System.err.println("Failed to instanciate a Nexmo Client");
            e.printStackTrace();
            throw new RuntimeException("Failed to instanciate a Nexmo Client");
        }

        // Create a Text SMS Message request object ...

        TextMessage message = new TextMessage(SMS_FROM, SMS_TO, SMS_TEXT);

        // Use the Nexmo client to submit the Text Message ...

        SmsSubmissionResult[] results = null;
        try {
            results = client.submitMessage(message);
        } catch (Exception e) {
            System.err.println("Failed to communicate with the Nexmo Client");
            e.printStackTrace();
            throw new RuntimeException("Failed to communicate with the Nexmo Client");
        }

        // Evaluate the results of the submission attempt ...
        System.out.println("... Message submitted in [ " + results.length + " ] parts");
        for (int i=0;i<results.length;i++) {
            System.out.println("--------- part [ " + (i + 1) + " ] ------------");
            System.out.println("Status [ " + results[i].getStatus() + " ] ...");
            if (results[i].getStatus() == SmsSubmissionResult.STATUS_OK)
                System.out.println("SUCCESS");
            else if (results[i].getTemporaryError())
                System.out.println("TEMPORARY FAILURE - PLEASE RETRY");
            else
                System.out.println("SUBMISSION FAILED!");
            System.out.println("Message-Id [ " + results[i].getMessageId() + " ] ...");
            System.out.println("Error-Text [ " + results[i].getErrorText() + " ] ...");

            if (results[i].getMessagePrice() != null)
                System.out.println("Message-Price [ " + results[i].getMessagePrice() + " ] ...");
            if (results[i].getRemainingBalance() != null)
                System.out.println("Remaining-Balance [ " + results[i].getRemainingBalance() + " ] ...");
        }
    }

}
