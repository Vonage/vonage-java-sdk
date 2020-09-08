/*
 * Copyright (c) 2020 Vonage
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
package com.vonage.client.sns.response;


public class SnsResponse {

    public static final int STATUS_OK = 0;
    public static final int STATUS_BAD_COMMAND = 1;
    public static final int STATUS_INTERNAL_ERROR = 2;
    public static final int STATUS_INVALID_ACCOUNT = 3;
    public static final int STATUS_MISSING_TOPIC = 4;
    public static final int STATUS_INVALID_OR_MISSING_MSISDN = 5;
    public static final int STATUS_INVALID_OR_MISSING_FROM = 6;
    public static final int STATUS_INVALID_OR_MISSING_MSG = 7;
    public static final int STATUS_TOPIC_NOT_FOUND = 8;
    public static final int STATUS_TOPIC_PERMISSION_FAILURE = 9;
    public static final int STATUS_COMMS_FAILURE = 13;

    private final String command;
    private final int resultCode;
    private final String resultMessage;

    public SnsResponse(final String command,
                       final int resultCode,
                       final String resultMessage) {
        this.command = command;
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
    }

    public String getCommand() {
        return this.command;
    }

    public int getResultCode() {
        return this.resultCode;
    }

    public String getResultMessage() {
        return this.resultMessage;
    }
}
