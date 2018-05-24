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
package com.nexmo.client.verify;

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchEndpoint {
    private SearchMethod searchMethod;

    SearchEndpoint(HttpWrapper httpWrapper) {
        this.searchMethod = new SearchMethod(httpWrapper);
    }

    public SearchResult[] search(String... requestIds) throws IOException, NexmoClientException {
        return translateFromSearchVerifyResponse(this.searchMethod.execute(new SearchRequest(requestIds)));
    }

    private SearchResult[] translateFromSearchVerifyResponse(SearchVerifyResponse response) {
        // TODO: Remove in 4.0
        List<SearchResult> resultList = new ArrayList<>();

        if (response.getStatus() != VerifyStatus.OK) {
            resultList.add(new SearchResult(response.getStatus().getVerifyStatus(),
                    response.getErrorText(),
                    response.getStatus().isTemporaryError()));
        } else {
            for (VerifyDetails details : response.getVerificationRequests()) {
                resultList.add(new SearchResult(response.getStatus().getVerifyStatus(),
                        details.getRequestId(),
                        details.getAccountId(),
                        translateFromVerifyDetailsStatus(details.getStatus()),
                        details.getNumber(),
                        details.getPrice() != null ? details.getPrice().floatValue() : 0,
                        details.getCurrency(),
                        details.getSenderId(),
                        details.getDateSubmitted(),
                        details.getDateFinalized(),
                        details.getFirstEventDate(),
                        details.getLastEventDate(),
                        translateFromVerifyCheck(details.getChecks()),
                        response.getErrorText(),
                        false));
            }
        }

        return resultList.toArray(new SearchResult[0]);
    }

    private SearchResult.VerificationStatus translateFromVerifyDetailsStatus(VerifyDetails.Status status) {
        // TODO: Remove in 4.0
        // This operates in the same way the XML endpoint does without logging the error.
        try {
            return status != null ? SearchResult.VerificationStatus.valueOf(status.name()) : null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private List<SearchResult.VerifyCheck> translateFromVerifyCheck(List<VerifyCheck> checks) {
        // TODO: Remove in 4.0
        List<SearchResult.VerifyCheck> resultChecks = new ArrayList<>();

        for (VerifyCheck check : checks) {
            resultChecks.add(new SearchResult.VerifyCheck(check.getDate(),
                    check.getCode(),
                    SearchResult.VerifyCheck.Status.valueOf(check.getStatus().name()),
                    check.getIpAddress()));
        }

        return resultChecks;
    }
}
