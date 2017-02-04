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
            System.err.println("Failed to instantiate a Nexmo Client");
            e.printStackTrace();
            System.exit(1);
            return;
        }

        InsightResult result;
        try {
            result = client.request(VERIFY_NUMBER, CALLBACK_URL);
        } catch (Exception e) {
            System.err.println("Failed to communicate with the Nexmo Client");
            e.printStackTrace();
            System.exit(1);
            return;
        }

        if (result.getStatus() == InsightResult.STATUS_OK) {
            System.out.println("... Insight request submitted with ID " + result.getRequestId());
            System.out.println("Your URL will be called soon with the result...");
            System.out.println("See here for documentation on the results: https://docs.nexmo.com/number-insight");
        }
        else {
            System.out.println("... Insight request failed with status " + result.getStatus());
            if (result.isTemporaryError())
                System.out.println("TEMPORARY FAILURE - PLEASE RETRY");
            System.out.println("Error: " + result.getErrorText());
        }
    }

}