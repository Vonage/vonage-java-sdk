
package com.nexmo.messaging.sdk.verify;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Response from number verification
 * 
 * @author Wale Olaleye
 * @version 1.0
 *
 */
public class CheckResponse implements Serializable {

	private static final long serialVersionUID = 3079658417667456388L;
	
	private final String eventId;
	private final Integer status;
	private final BigDecimal price;
	private final String currency;
	private final String errorText;
	
	/**
	 * 
	 * @param eventId Identifier of either the SMS (message-id) or TTS call (call-id) that enabled the verification. You can get more details on these events using the Nexmo Search API or the Nexmo Dashboard.
	 * @param status The return code.
	 * @param price The price charged for the verification request.
	 * @param currency Currency code.
	 * @param errorText If an error occurred, this will explain in readable terms the error encountered.
	 */
	
	public CheckResponse(final String eventId,
						final Integer status,
						final BigDecimal price,
						final String currency,
						final String errorText){
		this.eventId = eventId;
		this.status = status;
		this.price = price;
		this.currency = currency;
		this.errorText = errorText;
	}

	/**
	 * @return the eventId
	 */
	public String getEventId() {
		return eventId;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
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
		return "CheckResponse [eventId=" + eventId + ", status=" + status
				+ ", price=" + price + ", currency=" + currency
				+ ", errorText=" + errorText + "]";
	}

}
