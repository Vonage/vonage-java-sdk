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
package com.vonage.client.verify2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.common.HalPageResponse;
import java.util.List;

/**
 * Represents a response page for listing template fragments.
 *
 * @since 8.13.0
 */
public final class ListTemplateFragmentsResponse extends HalPageResponse {
	@JsonProperty("_embedded") private Embedded embedded;

	private static final class Embedded {
		@JsonProperty("template_fragments") List<TemplateFragment> fragments;
	}

	/**
     * Gets the embedded collection of template fragments.
	 *
	 * @return The list of {@linkplain TemplateFragment} objects in this response, or {@code null} if absent.
	 */
	@JsonIgnore
	public List<TemplateFragment> getTemplateFragments() {
		return embedded != null ? embedded.fragments : null;
	}
}
