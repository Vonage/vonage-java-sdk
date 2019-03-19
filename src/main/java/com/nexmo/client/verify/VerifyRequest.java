/*
 * Copyright (c) 2011-2017 Nexmo Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nexmo.client.verify;

import java.util.Locale;

/**
 * Describes a Verify request when passed to
 * {@link com.nexmo.client.verify.VerifyEndpoint})}.
 */
public class VerifyRequest {
    // Compulsory attrs:
    private final String number;
    private final String brand;

    private String from = null;     // sender_id
    private int length = -1;        // code_length
    private Locale locale = null;   // lg
    private LineType type = null;   // require_type

    private String country = null;
    private Integer pinExpiry = null;
    private Integer nextEventWait = null;


    /**
     * Constructor.
     *
     * @param number (required) The recipient's phone number in <a href="https://en.wikipedia.org/wiki/E.164">E.164</a>
     *               format.
     * @param brand  (required) The name of the company or app to be verified for. Must not be longer than 18
     *               characters.
     */
    public VerifyRequest(final String number, final String brand) {
        this(number, brand, null, -1, null, null);
    }

    /**
     * Constructor.
     *
     * @param number (required) The recipient's phone number in <a href="https://en.wikipedia.org/wiki/E.164">E.164</a> format.
     * @param brand  (required) The name of the company or app you are verifying for. Must not be longer than 18
     *               characters.
     * @param from   (optional The Nexmo number to use as the sender for the verification SMS message and calls, in
     *               <a href="https://en.wikipedia.org/wiki/E.164">E.164</a> format.
     */
    public VerifyRequest(final String number, final String brand, final String from) {
        this(number, brand, from, -1, null, null);
    }

    /**
     * Constructor.
     *
     * @param number (required) The recipient's phone number in <a href="https://en.wikipedia.org/wiki/E.164">E.164</a> format.
     * @param brand  (required) The name of the company or app you are verifying for. Must not be longer than 18
     *               characters.
     * @param from   (optional The Nexmo number to use as the sender for the verification SMS message and calls, in
     *               <a href="https://en.wikipedia.org/wiki/E.164">E.164</a> format.
     * @param length (optional) The length of the verification code to be sent to the user. Must be either 4 or 6. Use
     *               -1 to use the default value.
     * @param locale (optional) Override the default locale used for verification. By default the locale is determined
     *               from the country code included in {@code number}
     */
    public VerifyRequest(final String number, final String brand, final String from, final int length, final Locale locale) {
        this(number, brand, from, length, locale, null);
    }

    /**
     * Constructor.
     *
     * @param number (required) The recipient's phone number in <a href="https://en.wikipedia.org/wiki/E.164">E.164</a> format.
     * @param brand  (required) The name of the company or app you are verifying for. Must not be longer than 18
     *               characters.
     * @param from   (optional A short alphanumeric string to specify the SenderID for SMS sent by Verify.
     *               Depending on the destination of the phone number you are applying, restrictions may apply.
     *               By default, sender_id is {@code VERIFY}. Must be 11 characters or fewer.
     * @param length (optional) The length of the verification code to be sent to the user. Must be either 4 or 6. Use
     *               -1 to use the default value.
     * @param locale (optional) Override the default locale used for verification. By default the locale is determined
     *               from the country code included in {@code number}
     * @param type   (optional) If provided, restrict the verification to the specified network type. Contact
     *               support@nexmo.com to enable this feature.
     */
    public VerifyRequest(final String number, final String brand, final String from, final int length, final Locale locale, final LineType type) {
        if (number == null || brand == null)
            throw new IllegalArgumentException("Number and brand parameters cannot be null.");
        if (length > 0 && length != 4 && length != 6) throw new IllegalArgumentException("Code length must be 4 or 6.");

        this.number = number;
        this.brand = brand;
        this.from = from;
        this.length = length;
        this.locale = locale;
        this.type = type;
    }

    /**
     * @return the recipient's phone number provided in the constructor, in
     *         <a href="https://en.wikipedia.org/wiki/E.164">E.164</a> format.
     */
    public String getNumber() {
        return number;
    }

    /**
     * @return the name of the company or app to be verified for.
     */
    public String getBrand() {
        return brand;
    }

    /**
     * @return the short alphanumeric string to specify the SenderID for SMS sent by Verify, or {@code null} if one was
     *         not provided. This value is specified in some {@link VerifyRequest} constructors.
     *         <p>
     *         If this value is {@code null</tt>, the sender_id used will be <tt>VERIFY}.
     */
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * @return the length of the verification code to be sent to the user, specified in some {@link VerifyRequest}
     *         constructors. {@code -1} indicates the default length will be used.
     */
    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    /**
     * @return the default locale used for verification. If this value is {@code null}, the locale will be determined
     *         from the country code included in {@code number}
     */
    public Locale getLocale() {
        return locale;
    }


    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * @return the type of network the verification will be restricted to. This value has no effect unless it has been
     *         enabled by contacting {@code support@nexmo.com}.
     */
    public LineType getType() {
        return type;
    }


    public void setType(LineType type) {
        this.type = type;
    }

    /**
     * The country for the destination phone number.
     *
     * @return a String containing a 2-character country code
     */
    public String getCountry() {
        return country;
    }

    /**
     * The country for the destination phone number.
     * <p>
     * If you wish to used localised number formats or you are not sure if number is correctly formatted, set this to
     * a two-character country code. For example, GB, US. Verify will work out the international phone number for you.
     *
     * @param country a String containing a 2-character country code
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Get the PIN validity time from generation, in seconds, or null if this has not been set.
     */
    public Integer getPinExpiry() {
        return pinExpiry;
    }

    /**
     * Set the PIN validity time from generation, in seconds. The default (null) is 300 seconds.
     */
    public void setPinExpiry(Integer pinExpiry) {
        this.pinExpiry = pinExpiry;
    }

    /**
     * Get the wait time between attempts to deliver the PIN.
     *
     * @return An Integer between 600-900, or null.
     */
    public Integer getNextEventWait() {
        return nextEventWait;
    }

    /**
     * Set the wait time between attempts to deliver the PIN.
     *
     * @param nextEventWait An Integer value between 60 and 900 seconds, or null to use the default duration.
     */
    public void setNextEventWait(Integer nextEventWait) {
        this.nextEventWait = nextEventWait;
    }

    /**
     * Types of phone line to be specified for {@link VerifyRequest#type}.
     */
    public enum LineType {
        ALL, MOBILE, LANDLINE,
    }
}
