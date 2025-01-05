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

/**
 * Indicates that a class used for request is to be serialised as binary data (e.g. for uploads).
 *
 * @since 7.10.0
 */
public interface BinaryRequest {

	/**
	 * Serialises this request to a byte array.
	 *
	 * @return The binary data for this request.
	 */
	byte[] toByteArray();

	/**
	 * The MIME type header for this request to use as the {@code Content-Type}.
	 *
	 * @return The request MIME type as a string.
	 */
	default String getContentType() {
		return "multipart/form-data";
	}
}
