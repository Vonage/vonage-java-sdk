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
package com.vonage.client.redact;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;

/**
 * Represents a request to the Redact API.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RedactRequest {

    private String id;
    private Product product;
    private Type type;

    /**
     * Construct a RedactRequest object with all required fields.
     *
     * @param id      The transaction id to redact.
     * @param product The {@link Product} that the id relates to.
     */
    public RedactRequest(String id, Product product) {
        this.id = id;
        this.product = product;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException jpe) {
            throw new VonageUnexpectedException("Failed to produce json from RedactRequest object.", jpe);
        }
    }

    public enum Product {
        SMS("sms"),
        VOICE("voice"),
        NUMBER_INSIGHTS("number-insight"),
        VERIFY("verify"),
        VERIFY_SDK("verify-sdk"),
        MESSAGES("messages"),
        WORKFLOW("workflow");

        private String value;

        Product(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }
    }

    public enum Type {
        INBOUND, OUTBOUND;

        @JsonValue
        public String getValue() {
            return name().toLowerCase();
        }
    }
}
