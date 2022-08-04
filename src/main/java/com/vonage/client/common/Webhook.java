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
package com.vonage.client.common;

import com.fasterxml.jackson.annotation.*;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Data class which can be deserialized into a webhook for the Vonage API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Webhook {
    private String address;
    @JsonProperty("http_method")
    private HttpMethod method;

    private Webhook() {
        // Required for Deserialization
    }

    public Webhook(String address, HttpMethod method) {
        this.address = address;
        this.method = method;
    }

    public String getAddress() {
        return address;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public enum Type {
        ANSWER("answer_url"),
        EVENT("event_url"),
        INBOUND("inbound_url"),
        STATUS("status_url"),
        UNKNOWN("unknown");

        private final String name;

        private static final Map<String, Type> TYPE_INDEX =
            Arrays.stream(Type.values()).collect(Collectors.toMap(
                    Type::getName, Function.identity()
            ));

        Type(String name) {
            this.name = name;
        }

        @JsonValue
        public String getName() {
            return name;
        }

        @JsonCreator
        public static Type fromName(String name) {
            return TYPE_INDEX.getOrDefault(name.toLowerCase(), UNKNOWN);
        }
    }
}
