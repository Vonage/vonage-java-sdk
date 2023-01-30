/*
 *   Copyright 2022 Vonage
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

public class CheckRequest {
    private final String requestId, code, ipAddress;

    /**
     * @param requestId The Verify request to check.
     * This is the request_id you received in the response to the Verify request.
     *
     * @param code The verification code entered by your user. Between 4 and 6 characters.
     */
    public CheckRequest(String requestId, String code) {
        this(requestId, code, null);
    }

    /**
     *
     * @param requestId The request ID.
     * @param code The verification code.
     * @param ipAddress No longer used.
     * @deprecated Please use {@link CheckRequest#CheckRequest(String, String)}.
     */
    @Deprecated
    public CheckRequest(String requestId, String code, String ipAddress) {
        if ((this.requestId = requestId) == null) {
            throw new IllegalArgumentException("request_id is required");
        }
        if ((this.code = code) == null) {
            throw new IllegalArgumentException("code is required");
        }
        if (requestId.length() > 32) {
            throw new IllegalArgumentException("request_id '"+requestId+"' is longer than 32 characters");
        }
        if (code.length() < 4 || code.length() > 6) {
            throw new IllegalArgumentException("code '"+code+"' is not between 4 and 6 characters long");
        }
        this.ipAddress = ipAddress;
    }

    /**
     * @return The Verify request to check.
     * This is the request_id you received in the response to the Verify request.
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * @return The verification code entered by your user.
     */
    public String getCode() {
        return code;
    }

    /**
     * @deprecated This field is no longer used.
     * @return The IP address, as a string.
     */
    @Deprecated
    public String getIpAddress() {
        return ipAddress;
    }
}
