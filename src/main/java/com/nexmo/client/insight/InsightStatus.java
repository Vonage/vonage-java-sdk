/*
 * Copyright (c) 2011-2018 Nexmo Inc
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
package com.nexmo.client.insight;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.HashMap;
import java.util.Map;

public enum InsightStatus {
    SUCCESS(0),
    THROTTLED(1),
    INVALID_PARAMS(3),
    INVALID_CREDENTIALS(4),
    INTERNAL_ERROR(5),
    PARTNER_QUOTA_EXCEEDED(9),
    UNKNOWN(Integer.MAX_VALUE);

    private int insightStatus;

    private static final Map<Integer, InsightStatus> INSIGHT_STATUS_INDEX = new HashMap<>();

    static {
        for (InsightStatus insightStatus : InsightStatus.values()) {
            INSIGHT_STATUS_INDEX.put(insightStatus.insightStatus, insightStatus);
        }
    }

    /**
     * Look up the {@link InsightStatus} based on the int value.
     *
     * @param insightStatus the int value of the insight status.
     *
     * @return InsightStatus based on the int value given.
     */
    @JsonCreator
    public static InsightStatus fromInt(int insightStatus) {
        InsightStatus foundInsightStatus = INSIGHT_STATUS_INDEX.get(insightStatus);
        return (foundInsightStatus != null) ? foundInsightStatus : UNKNOWN;
    }

    InsightStatus(int insightStatus) {
        this.insightStatus = insightStatus;
    }

    public int getInsightStatus() {
        return insightStatus;
    }
}
