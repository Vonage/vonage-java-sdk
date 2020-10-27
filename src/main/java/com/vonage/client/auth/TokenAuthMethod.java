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
package com.vonage.client.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vonage.client.VonageUnexpectedException;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class TokenAuthMethod extends AbstractAuthMethod {
    private final int SORT_KEY = 30;

    private String apiKey;
    private String apiSecret;

    public TokenAuthMethod(String apiKey, String apiSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    @Override
    public RequestBuilder apply(RequestBuilder request) {
        return request.addParameter("api_key", apiKey).addParameter("api_secret", apiSecret);
    }

    @Override
    public RequestBuilder applyAsBasicAuth(RequestBuilder request) {
        String headerValue = Base64.encodeBase64String((apiKey + ":" + apiSecret).getBytes());
        Header authHeader = new BasicHeader("Authorization", "Basic " + headerValue);
        return request.addHeader(authHeader);
    }

    @Override
    public RequestBuilder applyAsJsonProperties(RequestBuilder request) {
        HttpEntity entity = request.getEntity();
        try {
            ObjectNode json = (ObjectNode) new ObjectMapper().readTree(EntityUtils.toString(entity));
            json.put("api_key", apiKey);
            json.put("api_secret", apiSecret);

            return request.setEntity(new StringEntity(json.toString(), ContentType.APPLICATION_JSON));
        } catch (IOException e) {
            throw new VonageUnexpectedException("Failed to attach api key and secret to json.", e);
        }
    }

    @Override
    public int getSortKey() {
        return SORT_KEY;
    }
}
