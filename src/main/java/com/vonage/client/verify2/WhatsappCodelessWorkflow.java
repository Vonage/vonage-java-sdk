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
package com.vonage.client.verify2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vonage.client.common.E164;

/**
 * Defines properties for sending a verification code to a user over WhatsApp
 * using an interaction prompt, where the user selects "Yes" or "No" to verify. See the
 * <a href=https://developer.vonage.com/en/verify/verify-v2/guides/using-whatsapp-interactive>
 * WhatsApp Interactive guide</a> for an overview of how this works.
 * <p>
 * By default, WhatsApp messages will be sent using a Vonage WhatsApp Business Account (WABA).
 * Please contact sales in order to configure Verify v2 to use your company’s WABA.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public final class WhatsappCodelessWorkflow extends Workflow {

	/**
	 * Constructs a new WhatsApp interactive verification workflow.
	 *
	 * @param to The number to send the verification prompt to, in E.164 format.
	 */
	public WhatsappCodelessWorkflow(String to) {
		super(Channel.WHATSAPP_INTERACTIVE, new E164(to).toString());
	}
}
