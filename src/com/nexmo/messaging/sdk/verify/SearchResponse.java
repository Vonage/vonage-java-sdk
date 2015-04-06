
package com.nexmo.messaging.sdk.verify;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import com.nexmo.messaging.sdk.NexmoVerifyClient;

/**
 * A search response.
 * 
 * @author Wale Olaleye
 * @version 1.0
 *
 */
public class SearchResponse implements Serializable {

	private static final long serialVersionUID = 3796871160427045991L;
	
	private final String requestId;
	private final String accountId;
	private final String status;
	private final String number;
	private final BigDecimal price;
	private final String currency;
	private final String senderId;
	private final Date dateSubmitted;
	private final Date dateFinalized;
	private final Date firstEventSent;
	private final Date lastEventSent;
	private final Checks[] checks;
	private final String errorText;
	private final DateFormat format = new SimpleDateFormat(NexmoVerifyClient.DEFAULT_DATE_FORMAT, Locale.ENGLISH);
	
	/**
	 * 
	 * @param requestId Verification Request Identifier
	 * @param accountId Account ID the request was for
	 * @param status Latest status of the verification request
	 * @param number Number this verification request is for.
	 * @param price The price charged for the verification request.
	 * @param currency Currency code.
	 * @param senderId Actual sender_id used for SMS attempts.
	 * @param dateSubmitted Date the Verification Request was submitted.
	 * @param dateFinalized Date the Verification Request was either successfully checked or failed.
	 * @param firstEventSent Time first attempt was made.
	 * @param lastEventSent Time last attempt was made.
	 * @param checks List of checks made for this verification and their outcomes.
	 * @param errorText If an error occurred, this will explain in readable terms the error encountered.
	 */
	public SearchResponse(final String requestId,
						  final String accountId,
						  final String status,
						  final String number,
						  final BigDecimal price,
						  final String currency,
						  final String senderId,
						  final Date dateSubmitted,
						  final Date dateFinalized,
						  final Date firstEventSent,
						  final Date lastEventSent,
						  final Checks[] checks,
						  final String errorText) {
		this.requestId = requestId;
		this.accountId = accountId;
		this.status = status;
		this.number = number;
		this.price = price;
		this.currency = currency;
		this.senderId = senderId;
		this.dateSubmitted = dateSubmitted;
		this.dateFinalized = dateFinalized;
		this.firstEventSent = firstEventSent;
		this.lastEventSent = lastEventSent;
		this.checks = checks;
		this.errorText = errorText;
	}

	/**
	 * @return the requestId
	 */
	public String getRequestId() {
		return requestId;
	}

	/**
	 * @return the accountId
	 */
	public String getAccountId() {
		return accountId;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
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
	 * @return the senderId
	 */
	public String getSenderId() {
		return senderId;
	}

	/**
	 * @return the dateSubmitted
	 */
	public Date getDateSubmitted() {
		return dateSubmitted;
	}

	/**
	 * @return the dateFinalized
	 */
	public Date getDateFinalized() {
		return dateFinalized;
	}

	/**
	 * @return the firstEventSent
	 */
	public Date getFirstEventSent() {
		return firstEventSent;
	}

	/**
	 * @return the lastEventSent
	 */
	public Date getLastEventSent() {
		return lastEventSent;
	}

	/**
	 * @return the checks
	 */
	public Checks[] getChecks() {
		return checks;
	}

	/**
	 * @return the errorText
	 */
	public String getErrorText() {
		return errorText;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SearchResponse [requestId=" + requestId + ", accountId="
				+ accountId + ", status=" + status + ", number=" + number
				+ ", price=" + price + ", currency=" + currency + ", senderId="
				+ senderId + ", dateSubmitted=" + (dateSubmitted == null ? "" : format.format(dateSubmitted))
				+ ", dateFinalized=" + (dateFinalized == null ? "" : format.format(dateFinalized)) + ", firstEventSent="
				+ (firstEventSent == null ? "" : format.format(firstEventSent)) + ", lastEventSent=" + (lastEventSent == null ? "" : format.format(lastEventSent))
				+ ", checks=" + Arrays.toString(checks) + ", errorText=" + errorText + "]";
	}
	
	
	
	
	
}
