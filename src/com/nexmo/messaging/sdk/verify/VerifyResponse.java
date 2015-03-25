/**
 * 
 */
package com.nexmo.messaging.sdk.verify;

import java.io.Serializable;

/**
 * @author Wale Olaleye
 * @version 1.0
 * 
 * Represents a response from a verify request
 */
public class VerifyResponse implements Serializable {
	
	private static final long serialVersionUID = -3365038631146344213L;
	
	private final String requestId;
	private final Integer status;
	private final String errorText;
	
	public VerifyResponse (final String requestId,
						   final Integer status,
						   final String errorText) {
		
		this.requestId = requestId;
		this.status = status;
		this.errorText = errorText;
	}

	/**
	 * @return the requestId
	 */
	public String getRequestId() {
		return requestId;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @return the errorText
	 */
	public String getErrorText() {
		return errorText;
	}
	
	/**
	 * String representation of this class
	 */
	@Override
	public String toString() {
		return "VerifyResponse [requestId=" + requestId + ", status=" + status
				+ ", errorText=" + errorText + "]";
	}
	
	
}
