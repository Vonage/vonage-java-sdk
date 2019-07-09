/*
 * Copyright (c) 2011-2019 Nexmo Inc
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
package com.nexmo.client.common;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
/**
 * Data class which can be deserialized into a webhook for the Nexmo API.
 */
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

        private String name;

        private static final Map<String, Type> TYPE_INDEX = new HashMap<>();

        static {
            for (Type type : Type.values()) {
                TYPE_INDEX.put(type.name, type);
            }
        }

        Type(String name) {
            this.name = name;
        }

        @JsonValue
        public String getName() {
            return name;
        }

        @JsonCreator
        public static Type fromName(String name) {
            Type foundType = TYPE_INDEX.get(name.toLowerCase());
            return (foundType != null) ? foundType : UNKNOWN;
        }
    }
}
