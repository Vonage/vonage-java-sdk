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
package com.vonage.client.messages.viber;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.messages.internal.MessagePayload;

/**
 * @since 7.2.0
 */
public class File extends MessagePayload {
	protected String name;

	protected File(String url, String name) {
		super(url);
		this.name = name;
		validateFileExtensions();
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	protected void validateFileExtensions() {
		validateExtension(name != null ? name : url.getPath(),
				"doc", "docx", "rtf", "dot", "dotx", "odt", "odf", "fodt", "txt", "info",
				"pdf", "xps", "pdax", "eps", "xls", "xlsx", "ods", "fods", "csv", "xlsm", "xltx"
		);
	}
}
