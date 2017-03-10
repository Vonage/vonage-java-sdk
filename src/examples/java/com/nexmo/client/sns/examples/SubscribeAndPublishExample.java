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
package com.nexmo.client.sns.examples;


import com.nexmo.client.sns.SnsClient;
import com.nexmo.client.sns.request.SnsPublishRequest;
import com.nexmo.client.sns.request.SnsSubscribeRequest;
import com.nexmo.client.sns.response.SnsPublishResponse;
import com.nexmo.client.sns.response.SnsSubscribeResponse;

/**
 * An example of using the nexmo sns api to subscribe a user, then broadcast a message.
 *
 * @author  Paul Cook
 */
public class SubscribeAndPublishExample {

    public static final String API_KEY = "your-api-key-goes-here";
    public static final String API_SECRET = "your-api-secret-goes-here";

    public static final String SMS_FROM = "12345";
    public static final String SMS_TO = "447777111222";
    public static final String SMS_TEXT = "Hello World!";

    // TODO: fill in your own topic ARN from your amazon SNS console
    public static final String TOPIC_ARN = "arn:aws:sns:region:num:id";

    public static void main(String[] args) {

        // Create a client for submitting to Nexmo

        SnsClient client = null;
        try {
            client = new SnsClient(API_KEY, API_SECRET);
        } catch (Exception e) {
            System.err.println("Failed to instantiate a Nexmo Client");
            e.printStackTrace();
            System.exit(1);
            return;
        }

        // Create a SnsRequest to subscribe a phone number to the SNS service

        SnsSubscribeRequest subscribeRequest = null;
        try {
            subscribeRequest = new SnsSubscribeRequest(TOPIC_ARN, SMS_TO);
        } catch (Exception e) {
            System.err.println("Failed to construct a SubscriptionRequest");
            e.printStackTrace();
            System.exit(1);
            return;
        }

        // Use the Nexmo client to submit this subscription

        SnsSubscribeResponse snsSubscribeResponse = null;
        try {
            snsSubscribeResponse = client.submit(subscribeRequest);
        } catch (Exception e) {
            System.err.println("Failed to perform subscription");
            e.printStackTrace();
            System.exit(1);
            return;
        }

        // Evaluate the subscription response .....

        System.out.println("RESULT OF SUBSCRIPTION REQUEST ....");
        System.out.println("Command: " + snsSubscribeResponse.getCommand());
        System.out.println("Result: " + snsSubscribeResponse.getResultCode() + " - " + snsSubscribeResponse.getResultMessage());
        System.out.println("Subscriber ARN: " + snsSubscribeResponse.getSubscriberArn());

        // Create a request to publish a message to all subscribers

        SnsPublishRequest publishRequest = null;
        try {
            publishRequest = new SnsPublishRequest(TOPIC_ARN, SMS_FROM, SMS_TEXT);
        } catch (Exception e) {
            System.err.println("Failed to construct a SnsPublishRequest");
            e.printStackTrace();
            System.exit(1);
            return;
        }

        // Use the Nexmo client to submit this subscription

        SnsPublishResponse snsPublishResponse = null;
        try {
            snsPublishResponse = client.submit(publishRequest);
        } catch (Exception e) {
            System.err.println("Failed to perform publish");
            e.printStackTrace();
            System.exit(1);
            return;
        }

        // Evaluate the publish response .....

        System.out.println("RESULT OF SUBSCRIPTION REQUEST ....");
        System.out.println("Command: " + snsPublishResponse.getCommand());
        System.out.println("Result: " + snsPublishResponse.getResultCode() + " - " + snsPublishResponse.getResultMessage());
        System.out.println("Transaction ID: " + snsPublishResponse.getTransactionId());
    }

}
