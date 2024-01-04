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
package com.vonage.client.auth;

import com.vonage.client.auth.hashutils.HashUtil;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.RequestBuilder;
import java.util.List;

public class SignatureAuthMethod implements AuthMethod {
    private static final int SORT_KEY = 20;

    private final String apiKey, apiSecret;
    private final HashUtil.HashType hashType;

    public SignatureAuthMethod(String apiKey, String apiSecret) {
        this(apiKey, apiSecret, HashUtil.HashType.MD5);
    }

    public SignatureAuthMethod(String apiKey, String apiSecret, HashUtil.HashType hashType) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.hashType = hashType;
    }

    public String getApiKey() {
        return apiKey;
    }

    @Override
    public RequestBuilder apply(RequestBuilder request) {
        request.addParameter("api_key", apiKey);
        List<NameValuePair> params = request.getParameters();
        RequestSigning.constructSignatureForRequestParameters(params, apiSecret, hashType);
        int last = params.size() - 1;
        request.addParameters(params.get(last), params.get(last - 1));
        return request;
    }

    @Override
    public int getSortKey() {
        return SORT_KEY;
    }
}
