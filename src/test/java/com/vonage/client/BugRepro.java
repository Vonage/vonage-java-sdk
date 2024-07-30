/*
 * Copyright  2024 Vonage
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

import com.vonage.client.numbers.UpdateNumberRequest;

/**
 * Convenience class for debugging / live testing.
 */
public class BugRepro {
	public static void main(String[] args) throws Throwable {
		String TO_NUMBER = System.getenv("TO_NUMBER");

		VonageClient client = VonageClient.builder()
				.httpConfig(HttpConfig.builder().timeoutMillis(12_000).build())
				.apiKey(System.getenv("VONAGE_API_KEY"))
				.apiSecret(System.getenv("VONAGE_API_SECRET"))
				.applicationId(System.getenv("VONAGE_APPLICATION_ID"))
				.privateKeyPath(System.getenv("VONAGE_PRIVATE_KEY_PATH"))
				.build();

		try {
			// Debug code here

			System.out.println("Success");
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
