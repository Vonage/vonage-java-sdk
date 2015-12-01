package com.nexmo.verify.sdk.examples;

import com.nexmo.verify.sdk.BaseResult;
import com.nexmo.verify.sdk.CheckResult;
import com.nexmo.verify.sdk.NexmoVerifyClient;

/**
 * An example on how to check a verification code.
 * @author Daniele Ricci
 */
public class CheckNumber {

    public static final String API_KEY = "your-api-key-goes-here";
    public static final String API_SECRET = "your-api-secret-goes-here";

    public static final String VERIFICATION_CODE = "your-verification-code-goes-here";
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

        CheckResult result;
        try {
            result = client.check(REQUEST_ID, VERIFICATION_CODE);
        } catch (Exception e) {
            System.err.println("Failed to communicate with the Nexmo Client");
            e.printStackTrace();
            throw new RuntimeException("Failed to communicate with the Nexmo Client");
        }

        if (result.getStatus() == BaseResult.STATUS_OK) {
            System.out.println("... Verify check was successful!");
            System.out.println("Price: " + result.getPrice() + " " + result.getCurrency());
        } else {
            System.out.println("... Verify check failed with status " + result.getStatus());
            if (result.isTemporaryError())
                System.out.println("TEMPORARY FAILURE - PLEASE RETRY");
            System.out.println("Error: " + result.getErrorText());
        }
    }

}
