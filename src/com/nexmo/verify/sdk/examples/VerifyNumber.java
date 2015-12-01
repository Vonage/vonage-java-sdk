package com.nexmo.verify.sdk.examples;

import com.nexmo.verify.sdk.BaseResult;
import com.nexmo.verify.sdk.NexmoVerifyClient;
import com.nexmo.verify.sdk.VerifyResult;

/**
 * An example on how to request a number verification.
 * @author Daniele Ricci
 */
public class VerifyNumber {

    public static final String API_KEY = "your-api-key-goes-here";
    public static final String API_SECRET = "your-api-secret-goes-here";

    public static final String BRAND_NAME = "Nexmo Verify Test";
    public static final String VERIFY_FROM = "12345";
    public static final String VERIFY_NUMBER = "447777111222";

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

        VerifyResult result;
        try {
            result = client.verify(VERIFY_NUMBER, BRAND_NAME, VERIFY_FROM);
        } catch (Exception e) {
            System.err.println("Failed to communicate with the Nexmo Client");
            e.printStackTrace();
            throw new RuntimeException("Failed to communicate with the Nexmo Client");
        }

        if (result.getStatus() == BaseResult.STATUS_OK) {
            System.out.println("... Verify request submitted with ID " + result.getRequestId());
        } else {
            System.out.println("... Verify request failed with status " + result.getStatus());
            if (result.isTemporaryError())
                System.out.println("TEMPORARY FAILURE - PLEASE RETRY");
            System.out.println("Error: " + result.getErrorText());
        }
    }

}
