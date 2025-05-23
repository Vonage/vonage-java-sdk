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
package com.vonage.client.users.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Represents a Vonage Business Cloud (VBC) channel.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE, visible = true)
public class Vbc extends Channel {
	private String extension;

	protected Vbc() {}

	/**
	 * Creates a new VBC channel.
	 *
	 * @param extension The VBC extension number.
	 */
	public Vbc(int extension) {
		this.extension = String.valueOf(extension);
	}

	/**
	 * VBC extension.
	 * 
	 * @return The extension as a string.
	 */
	@JsonProperty("extension")
	public String getExtension() {
		return extension;
	}
}
