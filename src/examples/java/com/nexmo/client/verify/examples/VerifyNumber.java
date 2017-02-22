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
package com.nexmo.client.verify.examples;

import com.nexmo.client.NexmoClient;
import com.nexmo.client.auth.TokenAuthMethod;
import com.nexmo.client.verify.BaseResult;
import com.nexmo.client.verify.VerifyClient;
import com.nexmo.client.verify.VerifyResult;

/**
 * An example of how to request a number verification.
 *
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

        VerifyClient client;
        try {
            client = new NexmoClient(new TokenAuthMethod(API_KEY, API_SECRET)).getVerifyClient();
        } catch (Exception e) {
            System.err.println("Failed to instantiate a Nexmo Client");
            e.printStackTrace();
            System.exit(1);
            return;
        }

        VerifyResult result;
        try {
            result = client.verify(VERIFY_NUMBER, BRAND_NAME, VERIFY_FROM);
        } catch (Exception e) {
            System.err.println("Failed to communicate with the Nexmo Client");
            e.printStackTrace();
            System.exit(1);
            return;
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
