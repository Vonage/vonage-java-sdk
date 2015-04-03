package com.nexmo.messaging.sdk.verify;

/**
 * Checks are part of a SearchResponse
 * @see com.nexmo.messaging.sdk.verify.SearchResponse
 */

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.nexmo.messaging.sdk.NexmoVerifyClient;
/**
 * 
 * @author Wale Olaleye
 */

public class Checks implements Serializable {

	
	private static final long serialVersionUID = 2906784402517257203L;
	
	private final Date dateRecieved;
	private final String code;
	private final String status;
	private final String ipAddress;
	private final DateFormat format = new SimpleDateFormat(NexmoVerifyClient.DEFAULT_DATE_FORMAT, Locale.ENGLISH);
	
	/**
	 * 
	 * @param dateRecieved Date recieved
	 * @param code Code
	 * @param status VALID, INVALID
	 * @param ipAddress IP Address
	 */
	public Checks(final Date dateRecieved,
				  final String code,
				  final String status,
				  final String ipAddress){
		this.dateRecieved = dateRecieved;
		this.code = code;
		this.status = status;
		this.ipAddress = ipAddress;
	}

	/**
	 * @return the dateRecieved
	 */
	public Date getDateRecieved() {
		return dateRecieved;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Checks [dateRecieved=" + format.format(dateRecieved) + ", code=" + code
				+ ", status=" + status + ", ipAddress=" + ipAddress + "]";
	}

}
