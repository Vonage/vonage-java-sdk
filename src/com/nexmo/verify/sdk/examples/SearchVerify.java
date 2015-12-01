package com.nexmo.verify.sdk.examples;

import com.nexmo.verify.sdk.BaseResult;
import com.nexmo.verify.sdk.NexmoVerifyClient;
import com.nexmo.verify.sdk.SearchResult;

/**
 * An example on how to search for a previous verify request.
 * @author Daniele Ricci
 */
public class SearchVerify {

    public static final String API_KEY = "your-api-key-goes-here";
    public static final String API_SECRET = "your-api-secret-goes-here";

    public static final String REQUEST_ID = "your-request-id-goes-here";

    public static void main(String[] args) {
        // Create a client for submitting to Nexmo

        NexmoVerifyClient client;
        try {
            client = new NexmoVerifyClient(API_KEY, API_SECRET);
        } catch (Exception e) {
            System.err.println("Failed to instanciate a Nexmo Client");
            e.printStackTrace();
            throw new RuntimeException("Failed to instanciate a Nexmo Client");
        }

        SearchResult result;
        try {
            // you can also pass multiple request IDs and you'll have an array of results
            result = client.search(REQUEST_ID);
        } catch (Exception e) {
            System.err.println("Failed to communicate with the Nexmo Client");
            e.printStackTrace();
            throw new RuntimeException("Failed to communicate with the Nexmo Client");
        }

        if (result.getStatus() == BaseResult.STATUS_OK) {
            System.out.println("... Verify search was successful!");
            System.out.println("Account: " + result.getAccountId());
            System.out.println("Number: " + result.getNumber());
            System.out.println("Sender: " + result.getSenderId());
            System.out.println("Status: " + result.getVerificationStatus());
            System.out.println("Date submitted: " + result.getDateSubmitted());
            System.out.println("Date finalized: " + result.getDateFinalized());
            System.out.println("Price: " + result.getPrice() + " " + result.getCurrency());
            System.out.println("Checks: " + result.getChecks());
        } else {
            System.out.println("... Verify search failed with status " + result.getStatus());
            if (result.isTemporaryError())
                System.out.println("TEMPORARY FAILURE - PLEASE RETRY");
            System.out.println("Error: " + result.getErrorText());
        }
    }

}
