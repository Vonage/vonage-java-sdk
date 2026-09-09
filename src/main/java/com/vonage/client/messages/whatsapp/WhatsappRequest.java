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
package com.vonage.client.messages.whatsapp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.common.E164;
import com.vonage.client.messages.Channel;
import com.vonage.client.messages.MessageRequest;
import com.vonage.client.common.MessageType;
import java.util.UUID;

public abstract class WhatsappRequest extends MessageRequest {
	final Context context;

	protected WhatsappRequest(Builder<?, ?> builder, MessageType messageType) {
		super(builder, Channel.WHATSAPP, messageType);
        context = builder.messageUuid != null ? new Context(builder.messageUuid) : null;
	}

	@Override
	protected void validateSenderAndRecipient(String from, String to) throws IllegalArgumentException {
		this.from = new E164(from).toString();
		this.to = sanitizeRecipient(to);
	}

	/**
	 * Sanitizes the recipient of a WhatsApp message. Unlike other channels, a WhatsApp recipient may be
	 * identified either by an E.164 phone number or by a business-scoped user ID (BSUID) for users who have
	 * adopted a WhatsApp username and whose phone number is not available.
	 * <p>
	 * When the value is a valid E.164 phone number it is normalised (stripping spaces, dashes and any leading
	 * {@code +}). Any other non-empty value is passed through unchanged: no format is imposed on non-phone
	 * identifiers such as BSUIDs, so that recipients accepted by the API are never rejected client-side even
	 * if the underlying provider format changes. The API validates the recipient and will reject genuinely
	 * unsupported values (e.g. error {@code 131062}).
	 *
	 * @param to The recipient phone number or opaque identifier (e.g. BSUID) passed in from the builder.
	 *
	 * @return The sanitized recipient identifier.
	 * @throws NullPointerException If the recipient is {@code null}.
	 * @throws IllegalArgumentException If the recipient is empty.
	 * @since 9.13.0
	 */
	static String sanitizeRecipient(String to) throws IllegalArgumentException {
		if (to == null) {
			throw new NullPointerException("Recipient cannot be null.");
		}
		if (to.isEmpty()) {
			throw new IllegalArgumentException("Recipient cannot be empty.");
		}
		try {
			return new E164(to).toString();
		}
		catch (IllegalArgumentException notAPhoneNumber) {
			return to;
		}
	}

	@JsonProperty("context")
	public Context getContext() {
		return context;
	}

	@SuppressWarnings("unchecked")
	protected abstract static class Builder<M extends WhatsappRequest, B extends Builder<? extends M, ? extends B>> extends MessageRequest.Builder<M, B> {
		UUID messageUuid;

		/**
		 * (REQUIRED)
		 * Sets the recipient of the message. For WhatsApp this may be either an E.164 phone number or a
		 * business-scoped user ID (BSUID) for users who have adopted a WhatsApp username and whose phone
		 * number is not available. When using a BSUID, provide the full value including the country code
		 * prefix, e.g. {@code US.13491208655302741918}. The value is accepted as-is (aside from normalising
		 * phone numbers), so it is not rejected client-side if the provider identifier format changes. Note
		 * that BSUID recipients are not supported for one-tap, zero-tap or copy-code authentication templates.
		 *
		 * @param to The recipient phone number or BSUID.
		 *
		 * @return This builder.
		 */
		@Override
		public B to(String to) {
			return super.to(to);
		}

		/**
		 * (REQUIRED for replies and reaction messages)
		 * An optional context used for quoting/replying to a specific message in a conversation. When used,
		 * the WhatsApp UI will display the new message along with a contextual bubble that displays the
		 * quoted/replied to message's content.<br>
		 * This field is the UUID of the message being replied to or quoted.
		 *
		 * @param messageUuid The context's message UUID as a string.
		 *
		 * @return This builder.
		 * @since 8.7.0
		 */
		public B contextMessageId(String messageUuid) {
			this.messageUuid = UUID.fromString(messageUuid);
			return (B) this;
		}
	}
}
