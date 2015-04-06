
package com.nexmo.messaging.sdk.examples;

import java.util.ArrayList;
import java.util.List;

import com.nexmo.messaging.sdk.NexmoInsightClient;
import com.nexmo.messaging.sdk.insight.Feature;
import com.nexmo.messaging.sdk.insight.InsightRequest;
import com.nexmo.messaging.sdk.insight.InsightResponse;

/**
 * An example of using the Number Insight API
 * 
 * @author Wale Olaleye
 *
 */
public class NumberInsight {
	
	public static final String API_KEY = "your-public-api-key";
	public static final String API_SECRET = "your-api-secret";
	
	
	
	public static void main(String[] args) {
		NexmoInsightClient client = null;
		try {
			client = new NexmoInsightClient(API_KEY, API_SECRET);
			//client = new NexmoInsightClient("https://rest.nexmo.com",API_KEY, API_SECRET, 5000, 3000, true, "sryssyjskjy");
		} catch (Exception e) {
			System.err.println("Failed to instanciate a Nexmo Client");
			e.printStackTrace();
			throw new RuntimeException("Failed to instanciate a Nexmo Client");
		}
		
		//Check verification code sent to SMS
		try {
			List<Feature> features = new ArrayList<Feature>();
			features.add(Feature.CARRIER);
			features.add(Feature.PORTED);
			features.add(Feature.REACHABLE);
			features.add(Feature.ROAMING);
			features.add(Feature.TYPE);
			features.add(Feature.VALID);
			
			//InsightRequest request = new InsightRequest("12222222222", features, "http://requestb.in/1ate3t", 5000, "GET", "TEST");
			//InsightRequest request = new InsightRequest("12222222222", "http://requestb.in/1ate3t");
			//InsightRequest request = new InsightRequest("12222222222", features, "http://requestb.in/1ate3t");
			InsightRequest request = new InsightRequest("12222222222", "http://requestb.in/1ate3t", "POST");
			InsightResponse response = client.submit(request);
			System.out.println(response);
            
		} catch (Exception e) {
            System.err.println("Problems were encountered");
            e.printStackTrace();
            throw new RuntimeException("Problems were encountered");
        }
		

	}

}
