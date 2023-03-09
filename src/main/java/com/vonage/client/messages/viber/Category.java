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
package com.vonage.client.messages.viber;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The use of different category tags enables the business to send messages for different use cases.
 * For Viber Service Messages the first message sent from a business to a user must be personal, informative and a
 * targeted message - not promotional. By default, Vonage sends the transaction category to Viber Service Messages.
 */
public enum Category {
	TRANSACTION,
	PROMOTION;

	@JsonValue
	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
