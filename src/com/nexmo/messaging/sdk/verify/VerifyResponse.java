/**
 * Represents a response from a verify request
 */
package com.nexmo.messaging.sdk.verify;

import java.io.Serializable;

/**
 * @author Wale Olaleye
 * @version 1.0
 * 
 */
public class VerifyResponse implements Serializable {
	
	private static final long serialVersionUID = -3365038631146344213L;
	
	private final String requestId;
	private final Integer status;
	private final String errorText;
	
	/**
	 * 
	 * @param requestId A unique ID of the request that was submitted (8 to 16 characters). You need to use that reference to check whether a verification is successful using the check verification call.
	 * @param status The return code.
	 * @param errorText If an error occurred, this will explain in readable terms the error encountered.
	 */
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
