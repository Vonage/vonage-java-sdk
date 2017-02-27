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
package com.nexmo.client.examples;

import com.nexmo.client.NexmoClient;
import com.nexmo.client.auth.JWTAuthMethod;
import com.nexmo.client.auth.TokenAuthMethod;
import com.nexmo.client.sms.SmsClient;
import com.nexmo.client.voice.Call;
import com.nexmo.client.sms.messages.TextMessage;
import com.nexmo.client.verify.VerifyClient;
import com.nexmo.client.verify.VerifyResult;

import java.nio.file.Paths;

/**
 * FlierCode is an executable object designed to demonstrate code from the flier we print for conferences.
 */
public class FlierCode {
    public static void main(String[] argv) throws Exception {
        String apiKey = System.getenv("API_KEY");
        String apiSecret = System.getenv("API_SECRET");
        String applicationId = System.getenv("APP_ID");
        String privateKeyPath = "valid_application_key.pem";

        String fromNumber = "447700900077";
        String toNumber = "447700900275";

        // Sending an SMS
        SmsClient smsClient = new NexmoClient(new TokenAuthMethod(apiKey, apiSecret)).getSmsClient();
        smsClient.submitMessage(new TextMessage(
                fromNumber, toNumber, "Hello from Nexmo!"
        ));

        // Making a voice call then sending text-to-speech:
        NexmoClient client = new NexmoClient(new JWTAuthMethod(
                applicationId,
                Paths.get(privateKeyPath)));
        client.getVoiceClient().createCall(new Call(
                toNumber,
                fromNumber,
                "https://nexmo-community.github.io/ncco-examples/first_call_talk.json"
        ));

        // Verifying a phone number:
        VerifyClient verifyClient = new NexmoClient(new TokenAuthMethod(apiKey, apiSecret)).getVerifyClient();
        VerifyResult result = verifyClient.verify(toNumber, "AcmeCorp");

        // Confirming the code the user provides:
        verifyClient.check(result.getRequestId(), "1234").getStatus();
    }
}
