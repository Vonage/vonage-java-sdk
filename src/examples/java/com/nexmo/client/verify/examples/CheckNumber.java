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

import com.nexmo.client.verify.BaseResult;
import com.nexmo.client.verify.CheckResult;
import com.nexmo.client.verify.VerifyClient;

/**
 * An example og how to check a verification code.
 *
 * @author Daniele Ricci
 */
public class CheckNumber {

    public static final String API_KEY = "your-api-key-goes-here";
    public static final String API_SECRET = "your-api-secret-goes-here";

    public static final String VERIFICATION_CODE = "your-verification-code-goes-here";
    public static final String REQUEST_ID = "your-request-id-goes-here";

    public static void main(String[] args) {
        // Create a client for submitting to Nexmo

        VerifyClient client;
        try {
            client = new VerifyClient(API_KEY, API_SECRET);
        } catch (Exception e) {
            System.err.println("Failed to instantiate a Nexmo Client");
            e.printStackTrace();
            System.exit(1);
            return;
        }

        CheckResult result;
        try {
            result = client.check(REQUEST_ID, VERIFICATION_CODE);
        } catch (Exception e) {
            System.err.println("Failed to communicate with the Nexmo Client");
            e.printStackTrace();
            System.exit(1);
            return;
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
