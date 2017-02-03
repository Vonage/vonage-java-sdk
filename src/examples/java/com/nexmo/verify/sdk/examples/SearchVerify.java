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
package com.nexmo.verify.sdk.examples;

import com.nexmo.verify.sdk.BaseResult;
import com.nexmo.verify.sdk.NexmoVerifyClient;
import com.nexmo.verify.sdk.SearchResult;

/**
 * An example of how to search for a previous verify request.
 *
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
            System.err.println("Failed to instantiate a Nexmo Client");
            e.printStackTrace();
            System.exit(1);
            return;
        }

        SearchResult result;
        try {
            // you can also pass multiple request IDs and you'll have an array of results
            result = client.search(REQUEST_ID);
        } catch (Exception e) {
            System.err.println("Failed to communicate with the Nexmo Client");
            e.printStackTrace();
            System.exit(1);
            return;
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