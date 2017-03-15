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
 * {@link com.nexmo.client.verify.endpoints.VerifyEndpoint#execute(Object)})}.
 * <p>
 * <b>Note</b>: This is currently an internal object. Use {@link VerifyClient#verify} methods instead of using this
 * class directly.
 */
public class VerifyRequest {
    // TODO: This class does not represent all values supported by the verification API.
    private final String number;
    private final String brand;
    private final String from;
    private final int length;
    private final Locale locale;
    private final LineType type;

    /**
     * Constructor.
     *
     * @param number (required) The recipient's phone number in <a href="https://en.wikipedia.org/wiki/E.164">E.164</a>
     *               format.
     * @param brand  (required) The name of the company or app to be verified for. Must not be longer than 18
     *               characters.
     */
    public VerifyRequest(final String number, final String brand) {
        this(number,
                brand,
                null,
                -1,
                null,
                null);
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
     *               from the country code included in <tt>number</tt>
     */
    public VerifyRequest(final String number, final String brand, final String from, final int length,
                         final Locale locale) {
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
     *               By default, sender_id is <tt>VERIFY</tt>. Must be 11 characters or fewer.
     * @param length (optional) The length of the verification code to be sent to the user. Must be either 4 or 6. Use
     *               -1 to use the default value.
     * @param locale (optional) Override the default locale used for verification. By default the locale is determined
     *               from the country code included in <tt>number</tt>
     * @param type   (optional) If provided, restrict the verification to the specified network type. Contact
     *               support@nexmo.com to enable this feature.
     */
    public VerifyRequest(final String number, final String brand, final String from, final int length,
                         final Locale locale, final LineType type) {
        if (number == null || brand == null)
            throw new IllegalArgumentException("Number and brand parameters cannot be null.");
        if (length > 0 && length != 4 && length != 6)
            throw new IllegalArgumentException("Code length must be 4 or 6.");

        this.number = number;
        this.brand = brand;
        this.from = from;
        this.length = length;
        this.locale = locale;
        this.type = type;
    }

    /**
     * @return the recipient's phone number provided in the constructor, in
     * <a href="https://en.wikipedia.org/wiki/E.164">E.164</a> format.
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
     * @return the short alphanumeric string to specify the SenderID for SMS sent by Verify, or <tt>null</tt> if one was
     * not provided. This value is specified in some {@link VerifyRequest} constructors.
     * <p>
     * If this value is <tt>null</tt>, the sender_id used will be <tt>VERIFY</tt>.
     */
    public String getFrom() {
        return from;
    }

    /**
     * @return the length of the verification code to be sent to the user, specified in some {@link VerifyRequest}
     * constructors. <tt>-1</tt> indicates the default length will be used.
     */
    public int getLength() {
        return length;
    }

    /**
     * @return the default locale used for verification. If this value is <tt>null</tt>, the locale will be determined
     * from the country code included in <tt>number</tt>
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * @return the type of network the verification will be restricted to. This value has no effect unless it has been
     * enabled by contacting <tt>support@nexmo.com</tt>.
     */
    public LineType getType() {
        return type;
    }

    /**
     * Types of phone line to be specified for {@link VerifyRequest#type}.
     */
    public enum LineType {
        ALL,
        MOBILE,
        LANDLINE,
    }
}
