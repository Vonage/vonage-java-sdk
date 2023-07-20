/*
 *   Copyright 2023 Vonage
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;

/**
 * Represents the {@code _links} section of a HAL response.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HalLinks {
	@JsonProperty("first") UrlContainer first;
	@JsonProperty("self") UrlContainer self;
	@JsonProperty("prev") UrlContainer prev;
	@JsonProperty("next") UrlContainer next;
	@JsonProperty("last") UrlContainer last;

	protected HalLinks() {
	}

	/**
	 * The {@code href} property of {@code first}.
	 *
	 * @return URL of the first page, or {@code null} if absent.
	 */
	public URI getFirstUrl() {
		return first != null ? first.getHref() : null;
	}

	/**
	 * The {@code href} property of {@code self}.
	 *
	 * @return URL of the current page, or {@code null} if absent.
	 */
	public URI getSelfUrl() {
		return self != null ? self.getHref() : null;
	}

	/**
	 * The {@code href} property of {@code prev}.
	 *
	 * @return URL of the previous page, or {@code null} if absent.
	 */
	public URI getPrevUrl() {
		return prev != null ? prev.getHref() : null;
	}

	/**
	 * The {@code href} property of {@code next}.
	 *
	 * @return URL of the next page, or {@code null} if absent.
	 */
	public URI getNextUrl() {
		return next != null ? next.getHref() : null;
	}

	/**
	 * The {@code href} property of {@code last}.
	 *
	 * @return URL of the last page, or {@code null} if absent.
	 */
	public URI getLastUrl() {
		return last != null ? last.getHref() : null;
	}
}
