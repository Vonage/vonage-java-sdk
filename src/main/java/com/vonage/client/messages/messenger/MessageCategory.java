/*
 *   Copyright 2022 Vonage
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
package com.vonage.client.messages.messenger;

/**
 * See the
 * <a href="https://developers.facebook.com/docs/messenger-platform/send-messages#messaging_types">Facebook documentation</a>
 * for details on what these mean.
 */
public enum MessageCategory {
	RESPONSE,
	UPDATE,
	MESSAGE_TAG;

	@Override
	public String toString() {
		return name().toLowerCase().replace('_', '-');
	}
}
