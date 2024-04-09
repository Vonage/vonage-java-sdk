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
package com.vonage.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Base class for requests and responses which are serialised and parsed to and from JSON.
 * In addition to the {@link Jsonable} interface, this class provides reflective implementations
 * of {@code equals}, {@code hashCode} and {@code toString} methods based on the class's fields.
 *
 * @since 8.2.0
 */
public abstract class JsonableBaseObject implements Jsonable {

    /**
     * Provides a mechanism for overriding the Jackson configuration options for this class.
     *
     * @return A new ObjectMapper with the desired serialisation options to use.
     * @since 8.5.0
     */
    protected ObjectMapper createJsonObjectMapper() {
        return Jsonable.createDefaultObjectMapper();
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+' '+toJson();
    }
}
