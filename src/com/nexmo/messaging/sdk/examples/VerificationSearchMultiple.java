
package com.nexmo.messaging.sdk.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nexmo.messaging.sdk.NexmoVerifyClient;
import com.nexmo.messaging.sdk.verify.SearchResponse;

/**
 * A verification search with multiple requestIds
 * 
 * @author Wale Olaleye
 *
 */
public class VerificationSearchMultiple {
	public static final String API_KEY = "your-public-api-key";
	public static final String API_SECRET = "your-api-secret";
	
	public static void main(String[] args) {
		
		List<String> requestIds = new ArrayList<String>();
		requestIds.add("31aae80d6d2c4feaa1952a4941d45e");
		requestIds.add("ba9ffa72ac43b7b1580e5248b90");
		requestIds.add("25f8d1d0a08d4072ba6122d6d9dei");
		
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
