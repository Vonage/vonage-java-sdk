/**
 * Possible features in a number insight request
 * @see https://docs.nexmo.com/index.php/number-insight/request#ni_features
 */
package com.nexmo.messaging.sdk.insight;

/**
 * @author Wale Olaleye
 *
 */
public enum Feature {
	TYPE("type"),
	VALID("valid"),
	REACHABLE("reachable"),
	CARRIER("carrier"),
	PORTED("ported"),
	ROAMING("roaming");
	
	private String requestString;
	
	private Feature(String requestString) {
		this.requestString = requestString;
	}
	
	/**
	 * 
	 * @return the string representation of this feature to be used for example
	 * in the HTTP request.
	 */
	public String getRequestString() {
		return this.requestString;
	}
}
