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
package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.net.URI;

/**
 * Callback properties for a {@link Conversation}.
 */
public class Callback extends JsonableBaseObject {
	private URI url;
	private String eventMask;
	private CallbackParams params;
	private CallbackHttpMethod method;

	protected Callback(URI url, String eventMask, CallbackParams params, CallbackHttpMethod method) {
		this.url = url;
		this.eventMask = eventMask;
		this.params = params;
		this.method = method;
	}

	/**
	 * Event URL for the callback.
	 * 
	 * @return The callback URL, or {@code null} if unspecified.
	 */
	@JsonProperty("url")
	public URI getUrl() {
		return url;
	}

	
	@JsonProperty("evnet_mask")
	public String getEventMask() {
		return eventMask;
	}

	/**
	 * Additional parameters.
	 * 
	 * @return The callback parameters, or {@code null} if unspecified.
	 */
	@JsonProperty("params")
	public CallbackParams getParams() {
		return params;
	}

	/**
	 * HTTP method to use for the callback.
	 * 
	 * @return The HTTP method as an enum, or {@code null} if unspecified.
	 */
	@JsonProperty("method")
	public CallbackHttpMethod getMethod() {
		return method;
	}
}
