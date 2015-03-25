/**
 * 
 */
package com.nexmo.messaging.sdk.verify;

import java.io.Serializable;

/**
 * @author Wale Olaleye
 * @version 1.0
 * 
 * Represents a request passed to the Nexmo verify API.
 *
 */
public class VerifyRequest implements Serializable {

	private static final long serialVersionUID = 5491921259061774740L;
	
	private final String number;
	private final String brand;
	private final String senderId;
	private final Integer codeLength;
	private final String lg;
	private final String requireType;
	private final Integer pinExpiry;
	
	public VerifyRequest (final String number,
						  final String brand) {
		this (number,
		     	brand,
		     	null,
		     	null,
		     	null,
		     	null,
		     	null);
	}
	
	public VerifyRequest (final String number,
						  final String brand,
						  final String senderId,
						  final Integer codeLength,
						  final String lg,
						  final String requireType,
						  final Integer pinExpiry) {
		
		this.number = number;
		this.brand = brand;
		this.senderId = senderId;
		this.codeLength = codeLength;
		this.lg = lg;
		this.requireType = requireType;
		this.pinExpiry = pinExpiry;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @return the brand
	 */
	public String getBrand() {
		return brand;
	}

	/**
	 * @return the senderId
	 */
	public String getSenderId() {
		return senderId;
	}

	/**
	 * @return the codeLength
	 */
	public Integer getCodeLength() {
		return codeLength;
	}

	/**
	 * @return the lg
	 */
	public String getLg() {
		return lg;
	}

	/**
	 * @return the requireType
	 */
	public String getRequireType() {
		return requireType;
	}

	/**
	 * @return the pinExpiry
	 */
	public Integer getPinExpiry() {
		return pinExpiry;
	}

	
}
