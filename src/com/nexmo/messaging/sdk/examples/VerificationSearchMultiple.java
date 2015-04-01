/**
 * A verification search with multiple requestIds
 */
package com.nexmo.messaging.sdk.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nexmo.messaging.sdk.NexmoVerifyClient;
import com.nexmo.messaging.sdk.verify.SearchResponse;

/**
 * @author Wale Olaleye
 *
 */
public class VerificationSearchMultiple {
	public static final String API_KEY = "your-api-key";
	public static final String API_SECRET = "your-api-secret";
	
	public static void main(String[] args) {
		
		List<String> requestIds = new ArrayList<String>();
		requestIds.add("31aae80d6d2c4feaa1952a4941bd405e");
		requestIds.add("ba0a9ffa72ac43b7b15824b0e5248b90");
		
		NexmoVerifyClient client = null;
		
		try {
			client = new NexmoVerifyClient(API_KEY, API_SECRET);
		} catch (Exception e) {
			System.err.println("Failed to instanciate a Nexmo Client");
			e.printStackTrace();
			throw new RuntimeException("Failed to instanciate a Nexmo Client");
		}
		
		//Search verification code
		try {
            SearchResponse[] result = client.search(requestIds);
            System.out.println(Arrays.toString(result));
            
		} catch (Exception e) {
            System.err.println("Failed to process results");
            e.printStackTrace();
            throw new RuntimeException("Failed to process results");
        }

	}

}
