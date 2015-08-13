package com.nexmo.insight.sdk;
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


/**
 * Number insight request result.
 * @author Daniele Ricci
 */
public class InsightResult {

    /**
     * Insight was successfully submitted to the Nexmo service
     */
    public static final int STATUS_OK = 0;

    /**
     * Insight was rejected due to exceeding the maximum throughput allowed for this account.<br>
     * Insight can be re-requested after a short delay
     */
    public static final int STATUS_THROTTLED = 1;

    /**
     * Insight was rejected due to an illegal value in one or more elements of the submission request
     */
    public static final int STATUS_INVALID_PARAMS = 3;

    /**
     * Insight was rejected due to receiving invalid account api key and/or secret
     */
    public static final int STATUS_INVALID_CREDENTIALS = 4;

    /**
     * Insight was rejected due to a failure within the Nexmo systems.<br>
     * Insight can be re-submitted after a short delay
     */
    public static final int STATUS_INTERNAL_ERROR = 5;

    /**
     * A network error occured
     */
    public static final int STATUS_COMMS_FAILURE = -1;

    private final String requestId;
    private final String number;

    private final int status;
    private final String errorText;
    private final boolean temporaryError;

    private final float requestPrice;
    private final float remainingBalance;

    protected InsightResult(final int status,
                            final String requestId,
                            final String number,
                            final float requestPrice,
                            final float remainingBalance,
                            final String errorText,
                            final boolean temporaryError) {
        this.status = status;
        this.errorText = errorText;
        this.temporaryError = temporaryError;
        this.requestId = requestId;
        this.number = number;
        this.requestPrice = requestPrice;
        this.remainingBalance = remainingBalance;
    }

    public int getStatus() {
        return this.status;
    }

    public String getErrorText() {
        return this.errorText;
    }

    public boolean isTemporaryError() {
        return this.temporaryError;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public String getNumber() {
        return this.number;
    }

    public float getRequestPrice() {
        return this.requestPrice;
    }

    public float getRemainingBalance() {
        return this.remainingBalance;
    }

    @Override
    public String toString() {
        return "InsightResult [status=" + getStatus() + ", requestId=" + this.requestId + "]";
    }
}
