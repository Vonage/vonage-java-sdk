package com.nexmo.messaging.sdk.messages.parameters;
/*
 * Copyright (c) 2011-2013 Nexmo Inc
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

import java.util.Calendar;
import java.util.Date;

/**
 * ValidityPeriod.java<br><br>
 *
 * Created on 5 January 2011, 17:34<br><br>
 *
 * Represents the time period that the submitted message is valid for.
 * Once this time period has elapsed, if it has not yet been delivered, then delivery will be aborted
 * and the message will be expired.
 *
 * @author  Paul Cook
 * @version 1.0
 */
public class ValidityPeriod implements java.io.Serializable {

    static final long serialVersionUID = -194856825408876712L;

    private final Integer timeToLive;

    private final Integer validityPeriodHours;
    private final Integer validityPeriodMinutes;
    private final Integer validityPeriodSeconds;

    /**
     * Instanciate a new ValidityPeriod instance ..
     *
     * @param timeToLive the number of seconds before this message will be expired
     *
     * @throws IllegalArgumentException if the validity period is a negative timespam
     */
    public ValidityPeriod(final int timeToLive) {
        if (timeToLive < 1)
            throw new IllegalArgumentException("Negative TTL Not Allowed!");
        this.timeToLive = Integer.valueOf(timeToLive);
        this.validityPeriodHours = null;
        this.validityPeriodMinutes = null;
        this.validityPeriodSeconds = null;
    }

    /**
     * Instanciate a new ValidityPeriod instance, specifying the time-to-live as a number of hours, minutes and seconds
     *
     * @param validityPeriodHours the number of hours that make up the validity period for this message
     * @param validityPeriodMinutes the number of minutes that make up the validity period for this message
     * @param validityPeriodSeconds the number of seconds that make up the validity period for this message
     *
     * @throws IllegalArgumentException if the validity period is a negative timespam
     */
    public ValidityPeriod(final int validityPeriodHours,
                          final int validityPeriodMinutes,
                          final int validityPeriodSeconds) {
        this.timeToLive = null;
        this.validityPeriodHours = Integer.valueOf(validityPeriodHours);
        this.validityPeriodMinutes = Integer.valueOf(validityPeriodMinutes);
        this.validityPeriodSeconds = Integer.valueOf(validityPeriodSeconds);
    }

    /**
     * Instanciate a new ValidityPeriod instance ..
     *
     * @param validUntil The apsolute time when the message will expire (in the timezone local to this JVM)
     *
     * @throws IllegalArgumentException if the validity period is a negative timespam
     */
    public ValidityPeriod(final Date validUntil) {
        this((int)((validUntil.getTime() - System.currentTimeMillis()) / 1000));
    }

    /**
     * Instanciate a new ValidityPeriod instance ..
     *
     * @param validUntil The apsolute time when the message will expire (in the timezone local to this JVM)
     *
     * @throws IllegalArgumentException if the validity period is a negative timespam
     */
    public ValidityPeriod(final Calendar validUntil) {
        this((int)((validUntil.getTime().getTime() - System.currentTimeMillis()) / 1000));
    }

    /**
     * @return Integer the number of seconds before this message will be expired
     */
    public Integer getTimeToLive() {
        return this.timeToLive;
    }

    /**
     * @return Integer the number of hours that make up the validity period for this message
     */
    public Integer getValidityPeriodHours() {
        return this.validityPeriodHours;
    }

    /**
     * @return Integer the number of minutes that make up the validity period for this message
     */
    public Integer getValidityPeriodMinutes() {
        return this.validityPeriodMinutes;
    }

    /**
     * @return Integer the number of seconds that make up the validity period for this message
     */
    public Integer getValidityPeriodSeconds() {
        return this.validityPeriodSeconds;
    }

}
