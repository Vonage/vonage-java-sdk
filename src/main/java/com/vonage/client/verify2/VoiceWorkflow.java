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
package com.vonage.client.verify2;


/**
 * Defines workflow properties for sending a verification code to a user over a voice call.
 */
public final class VoiceWorkflow extends AbstractNumberWorkflow {

	/**
	 * Constructs a new Voice verification workflow.
	 *
	 * @param to The number to call, in E.164 format.
	 */
	public VoiceWorkflow(String to) {
		super(Channel.VOICE, to);
	}
}
