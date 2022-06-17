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
package com.vonage.client.messages;

import com.vonage.client.HttpWrapper;
import com.vonage.client.VonageClientException;
import com.vonage.client.VonageResponseParseException;

public class MessagesClient {
	final SendMessageEndpoint sendMessage;

	/**
	 * Create a new SmsClient.
	 * @param httpWrapper Http Wrapper used to create a Message requests
	 */
	public MessagesClient(HttpWrapper httpWrapper) {
		sendMessage = new SendMessageEndpoint(httpWrapper);
	}

	/**
	 * Sends a message. The details of its format, channel, sender, recipient etc. are
	 * specified entirely by the type and contents of the MessageRequest. For example, to send
	 * a text via Viber, you would construct the request like so: <br>
	 * <pre>{@code
	 *     ViberTextRequest request = ViberTextRequest.builder()
	 *         .from("My Company")
	 *         .to("447700900000")
	 *         .text("Hello from Vonage!")
	 *         .build();
	 * }</pre><br>
	 *
	 * If the message was sent successfully, a {@link MessageResponse} will be returned containing the
	 * unique identifier of the message. Otherwise, a {@link MessageResponseException} will be thrown,
	 * which contains details of why the request failed.
	 *
	 * @param request The message request object, as described above.
	 * @return The response, if the request was successful (i.e.a 202 was received from the server).
	 *
	 * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
	 * @throws VonageResponseParseException if the response from the API could not be parsed.
	 * @throws MessageResponseException     if the request was unsuccessful (a 4xx or 500 status code was returned).
	 */
	public MessageResponse sendMessage(MessageRequest request) throws VonageClientException, VonageResponseParseException {
		return sendMessage.execute(request);
	}
}
