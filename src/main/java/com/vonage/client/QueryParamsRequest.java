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

import java.util.Map;

/**
 * Indicates that a request object has query parameters. This interface
 * is an internal implementation detail and not part of the SDK's public API.
 *
 * @since 7.7.0
 */
public interface QueryParamsRequest {

	/**
	 * Internal method for serializing the fields of this class into query parameters.
	 * The Map should be mutable so that subclasses can add fields, otherwise the
	 * method or implementing class should be marked as {@code final}. Duplicate entries
	 * can be handled by mapping the key to a list of the entries.
	 *
	 * @return The query parameters as unique key-value pairs.
	 */
	Map<String, ?> makeParams();
}
