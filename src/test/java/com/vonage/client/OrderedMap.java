/*
 *   Copyright 2025 Vonage
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
package com.vonage.client;

import java.util.LinkedHashMap;

import java.util.Map;
import java.util.Map.Entry;

public class OrderedMap extends LinkedHashMap<String, Object> implements Jsonable {

    public OrderedMap() {
        super();
    }

    @SafeVarargs
    public OrderedMap(Entry<String, ?>... entries) {
        super(entries.length);
        for (var entry : entries) {
            put(entry.getKey(), entry.getValue());
        }
    }

    public OrderedMap add(String key, Object value) {
        put(key, value);
        return this;
    }

    public OrderedMap addAll(Map<String, ?> other) {
        putAll(other);
        return this;
    }

    public static Entry<String, Object> entry(String key, Object value) {
        return new SimpleEntry<>(key, value);
    }
}
