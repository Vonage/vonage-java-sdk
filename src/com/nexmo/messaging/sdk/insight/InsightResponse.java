
package com.nexmo.messaging.sdk.insight;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A number insight HTTP response. Note that the full response with all fields is sent to
 * the callback url used in the request.
 * 
 * @author Wale Olaleye
 * @version 1.0
 *
 */
public class InsightResponse implements Serializable {

	private static final long serialVersionUID = -6186415063247156991L;
	private final String requestId;
	private final String number;
	private final Integer status;
	private final String errorText;
	private final BigDecimal remainingBalance;
	private final Double requestPrice;
	
	public InsightResponse(String requestId, String number, Integer status, String errorText,
			BigDecimal remainingBalance, Double requestPrice) {
		this.requestId = requestId;
		this.number = number;
		this.status = status;
		this.errorText = errorText;
		this.remainingBalance = remainingBalance;
		this.requestPrice = requestPrice;
	}

	/**
	 * @return the requestId
	 */
	public String getRequestId() {
		return requestId;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
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
	 * @return the remainingBalance
	 */
	public BigDecimal getRemainingBalance() {
		return remainingBalance;
	}

	/**
	 * @return the requestPrice
	 */
	public Double getRequestPrice() {
		return requestPrice;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InsightResponse [requestId=" + requestId + ", number=" + number
				+ ", status=" + status + ", errorText=" + errorText
				+ ", remainingBalance=" + remainingBalance + ", requestPrice="
				+ requestPrice + "]";
	}

	
	
	

}
