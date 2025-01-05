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
package com.vonage.client.auth;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Represents query parameters in a HTTP request without depending on a specific implementation.
 *
 * @since 8.8.0
 */
public class RequestQueryParams extends ArrayList<AbstractMap.SimpleEntry<String, String>> {

    public Map<String, String> toMap() {
        return stream().collect(Collectors.toMap(
                Entry::getKey, Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new
        ));
    }
}
