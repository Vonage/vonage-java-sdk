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
import java.util.Map;

public class SignatureAuthMethod extends AbstractApiKeyQueryParamsAuthMethod {
    private static final int SORT_KEY = 20;

    private final String sigSecret;
    private final HashUtil.HashType hashType;

    public SignatureAuthMethod(String apiKey, String sigSecret) {
        this(apiKey, sigSecret, HashUtil.HashType.MD5);
    }

    public SignatureAuthMethod(String apiKey, String sigSecret, HashUtil.HashType hashType) {
        super(apiKey);
        this.sigSecret = sigSecret;
        this.hashType = hashType;
    }

    @Override
    public int getSortKey() {
        return SORT_KEY;
    }

    @Override
    public Map<String, String> getAuthParams(RequestQueryParams requestParams) {
        Map<String, String> inParams = requestParams.toMap(), outParams = super.getAuthParams(requestParams);
        inParams.putAll(outParams);
        outParams.putAll(RequestSigning.getSignatureForRequestParameters(inParams, sigSecret, hashType));
        return outParams;
    }
}
