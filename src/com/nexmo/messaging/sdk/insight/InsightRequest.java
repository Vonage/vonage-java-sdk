/**
 * A number insight request
 * @see https://docs.nexmo.com/index.php/number-insight/request
 */
package com.nexmo.messaging.sdk.insight;

import java.io.Serializable;
import java.util.List;

/**
 * 
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
	
	public InsightRequest(String number, List<Feature> features, String callback, Integer callbackTimeout,
					String callbackMethod, String clientRef) {
			this.number = number;
			this.features = features;
			this.callback = callback;
			this.callbackTimeout = callbackTimeout;
			this.callbackMethod = callbackMethod;
			this.clientRef = clientRef;
					
	}
	
	public InsightRequest(String number, String callback) {
		this(number,null,callback,null,null,null);
	}
	
	public InsightRequest(String number, String callback, String callbackMethod) {
		this(number, null, callback, null, callbackMethod, null);
	}
	
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
