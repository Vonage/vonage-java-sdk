/**
 * Demonstrates a Nexmo verify check
 * @see https://docs.nexmo.com/index.php/verify/check
 */
package com.nexmo.messaging.sdk.examples;

import com.nexmo.messaging.sdk.NexmoVerifyClient;
import com.nexmo.messaging.sdk.verify.CheckResponse;

/**
 * @author Wale Olaleye
 *
 */
public class CheckVerificationCode {

	public static final String API_KEY = "493178a1";
	public static final String API_SECRET = "6b932842";
	public static final String REQUEST_ID = "31aae80d6d2c4feaa1952a4941bd405e";
	public static final String CODE = "4768";
	
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
