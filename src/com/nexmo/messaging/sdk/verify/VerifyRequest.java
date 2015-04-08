
package com.nexmo.messaging.sdk.verify;

import java.io.Serializable;

/**
 * Represents a request passed to the Nexmo verify API.
 * 
 * @author Wale Olaleye
 * @version 1.0
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
	
	/**
	 * Full Constructor
	 * 
	 * @param number Phone number to be verified Ð can be a landline or mobile number. Must be in international format (E164) ex = 4478342080934
	 * @param brand Brand or name of your app, service the verification is for. This alphanumeric (maximum length 18 characters) will be used inside the body of all SMS and TTS messages sent (e.g. "Your <brand> PIN code is ..")
	 * @param senderId By default VERIFY will be used as the SenderID for SMS otherwise an alphanumeric address can be specified (maximum length 11 characters). Restrictions may apply, depending on the destination.
	 * @param codeLength Length of the PIN codes to be used for this verification request. Integer Ð allowed values to 6 or 4 (default).
	 * @param lg Use this to force the language and locale used for all SMS and TTS messages for this verification request. Default is the language matching the country of the number or en-US if the country's language is not supported.
	 * @param requireType Possible values are All (Default), Mobile and Landline. Used to restrict verification to users only belonging to a certain network type. Verifications to numbers belonging to the other network type will be rejected. NOTE: To have this feature enabled, please reach out to support@nexmo.com
	 * @param pinExpiry Time in seconds for which the PIN should remain valid from the time that it is generated. For reference, this is the same as the request being received and the first attempt to deliver the code being triggered. If unspecified, it defaults to 300 seconds. Range: 60 - 3600, both inclusive.
	 */
	
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
