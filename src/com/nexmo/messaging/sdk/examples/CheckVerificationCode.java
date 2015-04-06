
package com.nexmo.messaging.sdk.examples;

import com.nexmo.messaging.sdk.NexmoVerifyClient;
import com.nexmo.messaging.sdk.verify.CheckResponse;

/**
 * Demonstrates a Nexmo verify check - https://docs.nexmo.com/index.php/verify/check
 * 
 * @author Wale Olaleye
 *
 */
public class CheckVerificationCode {

	public static final String API_KEY = "your-public-api-key";
	public static final String API_SECRET = "your-api-secret";
	public static final String REQUEST_ID = "5f8d1d0a08d4072ba6122d68d9de60";
	public static final String CODE = "253";
	
	public static void main(String[] args) {
		
		NexmoVerifyClient client = null;
		try {
			client = new NexmoVerifyClient(API_KEY, API_SECRET);
		} catch (Exception e) {
			System.err.println("Failed to instanciate a Nexmo Client");
			e.printStackTrace();
			throw new RuntimeException("Failed to instanciate a Nexmo Client");
		}
		
		//Check verification code sent to SMS
		try {
            CheckResponse check = client.checkRequest(REQUEST_ID, CODE);
            System.out.println(check);
            
		} catch (Exception e) {
            System.err.println("Failed to communicate with the Nexmo Client");
            e.printStackTrace();
            throw new RuntimeException("Failed to communicate with the Nexmo Client");
        }

	}

}
