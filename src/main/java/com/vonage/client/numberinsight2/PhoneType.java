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
package com.vonage.client.numberinsight2;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Represents the phone type in {@link Phone#getType()}.
 */
public enum PhoneType {
	MOBILE,
	LANDLINE,
	VOIP,
	PREPAID,
	PERSONAL,
	TOLL_FREE,
	UNMAPPED;

	@JsonCreator
	public static PhoneType fromString(String name) {
		try {
			return valueOf(name.toUpperCase().replace('-', '_'));
		}
		catch (IllegalArgumentException ex) {
			return UNMAPPED;
		}
		catch (NullPointerException ex) {
			return null;
		}
	}
}
