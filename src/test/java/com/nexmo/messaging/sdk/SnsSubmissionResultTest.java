package com.nexmo.messaging.sdk;
/*
 * Copyright (c) 2011-2016 Nexmo Inc
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

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class SnsSubmissionResultTest {
    private SmsSubmissionResult result;

    @Before
    public void setUp() {
        this.result = new SmsSubmissionResult(0,
        "destination",
        "messageId",
        "errorText",
        "clientReference",
        new BigDecimal("15.00"),
        new BigDecimal("0.03"),
        false,
        new SmsSubmissionReachabilityStatus(5, "reachability is probably fine"),
        "spider");
    }

    @Test
    public void testBean() {
        assertEquals(0, result.getStatus());
        assertEquals("destination", result.getDestination());
        assertEquals("messageId", result.getMessageId());
        assertEquals("errorText", result.getErrorText());
        assertEquals("clientReference", result.getClientReference());
        assertEquals(new BigDecimal("15.00"), result.getRemainingBalance());
        assertEquals(new BigDecimal("0.03"), result.getMessagePrice());
        assertEquals(false, result.getTemporaryError());
        assertEquals(
                new SmsSubmissionReachabilityStatus(5, "reachability is probably fine"),
                result.getSmsSubmissionReachabilityStatus());
        assertEquals("spider", result.getNetwork());
    }

    @Test
    public void testToString() {
        assertEquals(
                "SMS-SUBMIT-RESULT -- STATUS:0 ERR:errorText DEST:destination MSG-ID:messageId " +
                        "CLIENT-REF:clientReference PRICE:0.03 BALANCE:15.00 TEMP-ERR?:false REACHABLE?:REACHABILITY " +
                        "-- STAT [ 5 ] [ reachability is probably fine ] " +
                        "NETWORK:spider",
                result.toString()
        );

    }
}
