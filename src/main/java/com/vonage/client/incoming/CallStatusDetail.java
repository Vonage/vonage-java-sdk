package com.vonage.client.incoming;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.HashMap;
import java.util.Map;

public enum CallStatusDetail {
    /**
     * no detail field present
     * **/
    NO_DETAIL,
    /**
     * detail present, but has not been mapped to an enum yet
     * **/
    UNMAPPED_DETAIL,
    /**
     * number invalid
     * **/
    INVALID_NUMBER,
    /**
     * Rejected By Carrier
     * **/
    RESTRICTED,
    /**
     * call rejected by callee
     * **/
    DECLINED,
    /**
     * cannot route to the number
     * **/
    CANNOT_ROUTE,
    /**
     * number is no longer available
     * **/
    NUMBER_OUT_OF_SERVICE,
    /**
     * Server error or failure
     * **/
    INTERNAL_ERROR,
    /**
     * Carrier timed out
     * **/
    CARRIER_TIMEOUT,
    /**
     * Callee is temporarily unavailable.
     * **/
    UNAVAILABLE;

    private static final Map<String, CallStatusDetail> CALL_DETAIL_INDEX = new HashMap<>();

    static {
        for (CallStatusDetail detail : CallStatusDetail.values()) {
            CALL_DETAIL_INDEX.put(detail.name(), detail);
        }
    }

    @JsonCreator
    public static CallStatusDetail fromString(String detail) {
        if(detail == null)
            return NO_DETAIL;
        CallStatusDetail foundCallStatusDetail = CALL_DETAIL_INDEX.get(detail.toUpperCase());
        if(foundCallStatusDetail == null){
            foundCallStatusDetail = UNMAPPED_DETAIL;
        }
        return foundCallStatusDetail;
    }
}
