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

public class VerifyRequest {
    private final String number;
    private final String brand;
    private final String from;
    private final int length;
    private final Locale locale;
    private final com.nexmo.client.verify.VerifyClient.LineType type;

    public VerifyRequest(final String number, final String brand) {
        this(number,
                brand,
                null,
                -1,
                null,
                null);
    }

    public VerifyRequest(final String number, final String brand, final String from) {
        this(number, brand, from, -1, null, null);
    }

    public VerifyRequest(final String number, final String brand, final String from, final int length,
                         final Locale locale) {
        this(number, brand, from, length, locale, null);
    }

    public VerifyRequest(final String number, final String brand, final String from, final int length,
                         final Locale locale, final VerifyClient.LineType type) {
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

    public String getNumber() {
        return number;
    }

    public String getBrand() {
        return brand;
    }

    public String getFrom() {
        return from;
    }

    public int getLength() {
        return length;
    }

    public Locale getLocale() {
        return locale;
    }

    public VerifyClient.LineType getType() {
        return type;
    }

}
