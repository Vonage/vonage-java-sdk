/*
 *   Copyright 2024 Vonage
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.vonage.client.verify;

import com.vonage.client.QueryParamsRequest;
import com.vonage.client.common.E164;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Base request class for {@link VerifyRequest} and {@link Psd2Request}.
 * @since 5.5.0
 */
public abstract class BaseRequest implements QueryParamsRequest {
    private final String number, country;
    private final Integer length, pinExpiry, nextEventWait;
    private final Locale locale;

    protected BaseRequest(String number, Integer length, Locale locale, String country, Integer pinExpiry, Integer nextEventWait) {
        this.number = new E164(number).toString();
        this.locale = locale;
        this.country = country;
        if ((this.length = length) != null && (length != 4 && length != 6)) {
            throw new IllegalArgumentException("code_length must be 4 or 6.");
        }
        if ((this.pinExpiry = pinExpiry) != null && (pinExpiry < 60 || pinExpiry > 3600)) {
            throw new IllegalArgumentException("pin_expiry '"+pinExpiry+"' is out of bounds.");
        }
        if ((this.nextEventWait = nextEventWait) != null && (nextEventWait < 60 || nextEventWait > 900)) {
            throw new IllegalArgumentException("next_event_wait '"+nextEventWait+"' is out of bounds.");
        }
    }

    /**
     * @return the recipient's phone number provided in the constructor, in
     * <a href="https://en.wikipedia.org/wiki/E.164">E.164</a> format.
     */
    public String getNumber() {
        return number;
    }

    /**
     * @return the length of the verification code to be sent to the user, specified in some {@link VerifyRequest}
     * constructors. {@code -1} indicates the default length will be used.
     */
    public Integer getLength() {
        return length;
    }

    /**
     * @return the default locale used for verification. If this value is {@code null}, the locale will be determined
     * from the country code included in {@code number}
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * @return the default locale used for verification in snake case.
     * Ex: {@code en-gb}
     * If this value is {@code null}, the locale will be determined
     * from the country code included in {@code number}
     */
    public String getDashedLocale() {
        if (locale != null) {
           return locale.toLanguageTag().toLowerCase();
        }
        else return null;
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
     * How long the generated verification code is valid for, in seconds. When you specify both {@code pin_expiry}
     * and {@code next_event_wait} then {@code pin_expiry} must be an integer multiple of
     * {@code next_event_wait}, otherwise {@code pin_expiry} will be equal to {@code next_event_wait}.
     *
     * @return An Integer between {@code 60} and {@code 3600}, or {@code null}.
     */
    public Integer getPinExpiry() {
        return pinExpiry;
    }

    /**
     * The wait time between attempts to deliver the PIN.
     *
     * @return An Integer between {@code 60} and {@code 900}, or {@code null}.
     */
    public Integer getNextEventWait() {
        return nextEventWait;
    }

    @Override
    public String toString() {
        return  "number='" + number + '\'' +
                ", length=" + length +
                ", locale=" + locale +
                ", country='" + country + '\'' +
                ", pinExpiry=" + pinExpiry +
                ", nextEventWait=" + nextEventWait;
    }

    @Override
    public Map<String, String> makeParams() {
        Map<String, String> params = new LinkedHashMap<>();
        if (number != null) {
            params.put("number", number);
        }
        if (length != null && length > 0) {
            params.put("code_length", String.valueOf(length));
        }
        if (locale != null) {
            params.put("lg", getDashedLocale());
        }
        if (country != null) {
            params.put("country", country);
        }
        if (pinExpiry != null) {
            params.put("pin_expiry", String.valueOf(pinExpiry));
        }
        if (nextEventWait != null) {
            params.put("next_event_wait", String.valueOf(nextEventWait));
        }
        return params;
    }
}
