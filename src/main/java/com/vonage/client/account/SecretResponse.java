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
package com.vonage.client.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vonage.client.VonageUnexpectedException;
import io.openapitools.jackson.dataformat.hal.HALLink;
import io.openapitools.jackson.dataformat.hal.HALMapper;
import io.openapitools.jackson.dataformat.hal.annotation.Link;
import io.openapitools.jackson.dataformat.hal.annotation.Resource;

import java.io.IOException;
import java.util.Date;

@Resource
public class SecretResponse {
    @Link
    private HALLink self;

    private String id;

    @JsonProperty("created_at")
    private Date created;

    public HALLink getSelf() {
        return self;
    }

    public String getId() {
        return id;
    }

    public Date getCreated() {
        return created;
    }

    public static SecretResponse fromJson(String json) {
        try {
            HALMapper mapper = new HALMapper();
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper.readValue(json, SecretResponse.class);
        } catch (IOException e) {
            throw new VonageUnexpectedException("Failed to produce SecretResponse from json.", e);
        }
    }
}
