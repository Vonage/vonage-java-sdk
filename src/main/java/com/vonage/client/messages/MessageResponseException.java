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
package com.vonage.client.messages;

import com.vonage.client.VonageApiResponseException;

/**
 * Response returned when sending a message fails (i.e. returns a non-2xx status code).
 * Since this is an unchecked exception, users are advised to catch it when calling
 * {@link MessagesClient#sendMessage(MessageRequest)} to handle failures.
 */
public final class MessageResponseException extends VonageApiResponseException {
	MessageResponseException() {}

	@Override
	protected void setStatusCode(int statusCode) {
		super.setStatusCode(statusCode);
	}
}
