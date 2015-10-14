package com.nexmo.insight.sdk.examples;

import com.nexmo.insight.sdk.InsightResult;
import com.nexmo.insight.sdk.NexmoInsightClient;

/**
 * An example on how to request a number insight.<br>
 * Number insight requests are asynchronous: Nexmo will return a response
 * immediately and then call the provided URL with the insight results when
 * it's finished.
 * @author Daniele Ricci
 */
public class InsightNumber {

    public static final String API_KEY = "your-api-key-goes-here";
    public static final String API_SECRET = "your-api-secret-goes-here";

    public static final String VERIFY_NUMBER = "447777111222";
    public static final String CALLBACK_URL = "http://callback/url/goes/here";

    public static void main(String[] args) {
        // Create a client for submitting to Nexmo

        NexmoInsightClient client;
        try {
            client = new NexmoInsightClient(API_KEY, API_SECRET);
        } catch (Exception e) {
            System.err.println("Failed to instanciate a Nexmo Client");
            e.printStackTrace();
            throw new RuntimeException("Failed to instanciate a Nexmo Client");
        }

        InsightResult result;
        try {
            result = client.request(VERIFY_NUMBER, CALLBACK_URL);
        } catch (Exception e) {
            System.err.println("Failed to communicate with the Nexmo Client");
            e.printStackTrace();
            throw new RuntimeException("Failed to communicate with the Nexmo Client");
        }

        if (result.getStatus() == InsightResult.STATUS_OK) {
            System.out.println("... Insight request submitted with ID " + result.getRequestId());
            System.out.println("Your URL will be called soon with the result...");
            System.out.println("See here for documentation on the results: https://docs.nexmo.com/index.php/number-insight/response");
        }
        else {
            System.out.println("... Insight request failed with status " + result.getStatus());
            if (result.isTemporaryError())
                System.out.println("TEMPORARY FAILURE - PLEASE RETRY");
            System.out.println("Error: " + result.getErrorText());
        }
    }

}
