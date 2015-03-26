/**
 * 
 */
package com.nexmo.messaging.sdk.verify;

import java.io.Serializable;
import java.math.BigDecimal;

/**
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
