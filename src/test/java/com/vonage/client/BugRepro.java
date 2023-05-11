/*
 * Copyright  2023 Vonage
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
package com.vonage.client;

import com.vonage.client.verify2.*;

/**
 * Convenience class for debugging / live testing.
 */
public class BugRepro {
	static final String
			VONAGE_API_KEY = System.getenv("VONAGE_API_KEY"),
			VONAGE_API_SECRET = System.getenv("VONAGE_API_SECRET"),
			VONAGE_APPLICATION_ID = System.getenv("VONAGE_APPLICATION_ID"),
			VONAGE_PRIVATE_KEY_PATH = System.getenv("VONAGE_PRIVATE_KEY_PATH");

	public static void main(String[] args) throws Throwable {
		VonageClient client = VonageClient.builder()
				.apiKey(VONAGE_API_KEY)
				.apiSecret(VONAGE_API_SECRET)
				.privateKeyPath(VONAGE_PRIVATE_KEY_PATH)
				.applicationId(VONAGE_APPLICATION_ID)
				.build();

		// Debug code here
		Verify2Client verify2 = client.getVerify2Client();
		VerificationResponse response = verify2.sendVerification(VerificationRequest.builder()
						.brand("Devoxx")
						//.addWorkflow(new SmsWorkflow("447577739826"))
						//.code("d3V0xX23")
						.addWorkflow(new WhatsappCodelessWorkflow("447577739826"))
						.build()
		);


		System.out.println("Success");
	}
}
