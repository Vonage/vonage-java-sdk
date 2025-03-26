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

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Base class for all requests that use string-based query parameters as the body.
 *
 * @since 9.0.0
 */
public abstract class AbstractQueryParamsRequest implements QueryParamsRequest {
    private LinkedHashMap<String, String> queryParams;

    protected void conditionalAdd(String name, Object value) {
        if (value != null) {
            String valueStr;
            if (value instanceof Instant) {
                valueStr = DateTimeFormatter.ISO_INSTANT.format(((Instant) value).truncatedTo(ChronoUnit.SECONDS));
            }
            else if (value instanceof Date) {
                valueStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) value);
            }
            else {
                valueStr = value.toString();
            }
            queryParams.put(name, valueStr);
        }
    }

    @Override
    public Map<String, String> makeParams() {
        return queryParams = new LinkedHashMap<>();
    }
}
