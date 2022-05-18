/*
 *   Copyright 2020 Vonage
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
package com.vonage.client.insight;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum InsightStatus {
    SUCCESS(0),
    THROTTLED(1),
    INVALID_PARAMS(3),
    INVALID_CREDENTIALS(4),
    INTERNAL_ERROR(5),
    PARTNER_QUOTA_EXCEEDED(9),
    UNKNOWN(Integer.MAX_VALUE);

    private final int insightStatus;

    private static final Map<Integer, InsightStatus> INSIGHT_STATUS_INDEX =
        Arrays.stream(InsightStatus.values()).collect(Collectors.toMap(
            InsightStatus::getInsightStatus, Function.identity()
        ));

    /**
     * Look up the {@link InsightStatus} based on the int value.
     *
     * @param insightStatus the int value of the insight status.
     *
     * @return InsightStatus based on the int value given.
     */
    @JsonCreator
    public static InsightStatus fromInt(int insightStatus) {
        return INSIGHT_STATUS_INDEX.getOrDefault(insightStatus, UNKNOWN);
    }

    InsightStatus(int insightStatus) {
        this.insightStatus = insightStatus;
    }

    public int getInsightStatus() {
        return insightStatus;
    }
}
