
package com.nexmo.messaging.sdk.insight;

import java.io.Serializable;
import java.util.List;

/**
 * A wrapper for Number Insight Requests - https://docs.nexmo.com/index.php/number-insight/request
 * @author Wale Olaleye
 * @version 1.0
 */

public class InsightRequest implements Serializable {

	private static final long serialVersionUID = 6474951005835635421L;
	private final String number;
	private final List<Feature> features;
	private final String callback; /*This is the callback URL */
	private final Integer callbackTimeout;
	private final String callbackMethod;
	private final String clientRef;
	
	/**
	 * Main Constructor
	 * 
	 * @param number Phone number in international format and one recipient per request. Ex: to=447525856424 when sending to UK
	 * @param features A set of features to check, using a comma-separated values.
	 * @param callback A URL to which Nexmo will send the request's responses.
	 * @param callbackTimeout The maximum amount of time to get a response to your callback URL (default 30000 ms). Valid range: 1000 - 30000, both included.
	 * @param callbackMethod The HTTP method for your error_url. Must be GET (default) or POST.
	 * @param clientRef	Include any reference string for your reference. Useful for your internal reports (40 characters max).
	 */
	public InsightRequest(String number, List<Feature> features, String callback, Integer callbackTimeout,
					String callbackMethod, String clientRef) {
			this.number = number;
			this.features = features;
			this.callback = callback;
			this.callbackTimeout = callbackTimeout;
			this.callbackMethod = callbackMethod;
			this.clientRef = clientRef;
					
	}
	
	/**
	 * Simplified Constructor 
	 * 
	 * @param number Phone number in international format and one recipient per request. Ex: to=447525856424 when sending to UK
	 * @param callback A URL to which Nexmo will send the request's responses.
	 */
	public InsightRequest(String number, String callback) {
		this(number,null,callback,null,null,null);
	}
	
	/**
	 * Another Simplified Constructor
	 * 
	 * @param number Phone number in international format and one recipient per request. Ex: to=447525856424 when sending to UK
	 * @param callback A URL to which Nexmo will send the request's responses.
	 * @param callbackMethod The HTTP method for your error_url. Must be GET (default) or POST.
	 */
	public InsightRequest(String number, String callback, String callbackMethod) {
		this(number, null, callback, null, callbackMethod, null);
	}
	
	/**
	 * Yet Another Simplified Constructor
	 * 
	 * @param number Phone number in international format and one recipient per request. Ex: to=447525856424 when sending to UK
	 * @param features A set of features to check, using a comma-separated values.
	 * @param callback A URL to which Nexmo will send the request's responses.
	 */
	
	public InsightRequest(String number, List<Feature> features, String callback){
		this(number, features, callback, null, null, null);
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @return selected features
	 */
	public List<Feature> getFeatures() {
		return features;
	}

	/**
	 * @return the callback url
	 */
	public String getCallback() {
		return callback;
	}

	/**
	 * @return the callbackTimeout
	 */
	public Integer getCallbackTimeout() {
		return callbackTimeout;
	}

	/**
	 * @return the callbackMethod
	 */
	public String getCallbackMethod() {
		return callbackMethod;
	}

	/**
	 * @return the clientRef
	 */
	public String getClientRef() {
		return clientRef;
	}
}
