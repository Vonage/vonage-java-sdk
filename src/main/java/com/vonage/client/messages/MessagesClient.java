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
package com.vonage.client.messages;

import com.vonage.client.*;
import com.vonage.client.auth.JWTAuthMethod;
import com.vonage.client.auth.ApiKeyHeaderAuthMethod;
import com.vonage.client.common.HttpMethod;
import com.vonage.jwt.Jwt;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MessagesClient {
	private boolean sandbox = false;
	final RestEndpoint<MessageRequest, MessageResponse> sendMessage, sendMessageSandbox;
	final RestEndpoint<UpdateStatusRequest, Void> updateMessage;

	/**
	 * Create a new MessagesClient.
	 *
	 * @param wrapper Http Wrapper used to create message requests.
	 */
	@SuppressWarnings("unchecked")
	public MessagesClient(HttpWrapper wrapper) {
		final String messagesPath = "/v1/messages";
		final class Endpoint<T, R> extends DynamicEndpoint<T, R> {
			Endpoint(Function<HttpConfig, String> basePathGetter, R... type) {
				super(DynamicEndpoint.<T, R> builder(type)
						.responseExceptionType(MessageResponseException.class)
						.wrapper(wrapper).requestMethod(HttpMethod.POST)
						.authMethod(JWTAuthMethod.class, ApiKeyHeaderAuthMethod.class)
						.pathGetter((de, req) ->
								basePathGetter.apply(de.getHttpWrapper().getHttpConfig()) + messagesPath
						)
				);
			}
		}

		sendMessage = new Endpoint<>(HttpConfig::getApiBaseUri);
		sendMessageSandbox = new Endpoint<>(hc -> "https://messages-sandbox.nexmo.com");
		updateMessage = DynamicEndpoint.<UpdateStatusRequest, Void> builder(Void.class)
				.responseExceptionType(MessageResponseException.class)
				.wrapper(wrapper).requestMethod(HttpMethod.PATCH)
				.authMethod(JWTAuthMethod.class).pathGetter((de, req) -> de.getHttpWrapper().getHttpConfig()
						.getRegionalBaseUri(req.region) + messagesPath + "/" + req.messageId
				).build();
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
	 * @throws MessageResponseException If the message could not be sent. This could be for the following reasons:
	 * <ul>
	 *     <li><b>401:</b> Missing or invalid credentials.</li>
	 *     <li><b>402:</b> Low balance.</li>
	 *     <li><b>422:</b> Invalid request parameters.</li>
	 *     <li><b>429:</b> Rate limit hit. Please wait and try again.</li>
	 *     <li><b>500:</b> Internal server error.</li>
	 * </ul>
	 */
	public MessageResponse sendMessage(MessageRequest request) throws MessageResponseException {
		return (sandbox ? sendMessageSandbox : sendMessage).execute(request);
	}

	/**
	 * Calling this method will make the client use the sandbox endpoint, which will enable you to
	 * use the <a href=https://dashboard.nexmo.com/messages/sandbox>Messages Sandbox</a>.
	 *
	 * @return This MessagesClient, for convenience.
	 * @since 7.1.0
	 */
	public MessagesClient useSandboxEndpoint() {
		sandbox = true;
		return this;
	}

	/**
	 * Calling this method will make the client use the regular endpoint returned by
	 * {@link HttpConfig#getApiBaseUri()}, which is the default behaviour. This method is
	 * provided for convenience if you wish to switch between sandbox and non-sandbox mode.
	 *
	 * @see #useSandboxEndpoint()
	 * @return This MessagesClient, for convenience.
	 * @since 7.1.0
	 */
	public MessagesClient useRegularEndpoint() {
		sandbox = false;
		return this;
	}

	/**
	 * Marks an inbound message as "read". Currently, this only applies to WhatsApp messages.
	 *
	 * @param messageId UUID of the message to acknowledge.
	 * @param region The regional server to use for this request. This must match the region of the message.
	 *
	 * @throws MessageResponseException If the acknowledgement fails. This could be for the following reasons:
	 * <ul>
	 *     <li><b>401:</b> Missing or invalid credentials.</li>
	 *     <li><b>404:</b> Message not found.</li>
	 *     <li><b>422:</b> Operation not supported for this channel.</li>
	 *     <li><b>429:</b> Rate limit hit. Please wait and try again.</li>
	 *     <li><b>500:</b> Internal server error.</li>
	 * </ul>
	 *
	 * @since 8.11.0
	 */
	public void ackInboundMessage(String messageId, ApiRegion region) throws MessageResponseException {
		updateMessage.execute(new UpdateStatusRequest("read", messageId, region));
	}

	/**
	 * Revokes an outbound message. Currently, this only applies to RCS messages.
	 *
	 * @param messageId UUID of the message to revoke.
	 * @param region The regional server to use for this request. This must match the region of the message.
	 *
	 * @throws MessageResponseException If the acknowledgement fails. This could be for the following reasons:
	 * <ul>
	 *     <li><b>401:</b>Missing or invalid credentials.</li>
	 *     <li><b>404:</b>Message not found.</li>
	 *     <li><b>422:</b>Operation not supported for this channel.</li>
	 *     <li><b>429:</b>Rate limit hit. Please wait and try again.</li>
	 *     <li><b>500:</b>Internal server error.</li>
	 * </ul>
	 *
	 * @since 8.11.0
	 */
	public void revokeOutboundMessage(String messageId, ApiRegion region) throws MessageResponseException {
		updateMessage.execute(new UpdateStatusRequest("revoked", messageId, region));
	}

	/**
	 * Utility method for verifying whether a token was signed by a secret.
	 * This is mostly useful when using signed callbacks to ensure that the inbound
	 * data came from Vonage servers. The signature is performed using the SHA-256 HMAC algorithm.
	 *
	 * @param jwt The JSON Web Token to verify.
	 * @param secret The symmetric secret key (HS256) to use for decrypting the token's signature.
	 *
	 * @return {@code true} if the token was signed by the secret, {@code false} otherwise.
	 *
	 * @since 7.11.0
	 */
	public static boolean verifySignature(String jwt, String secret) {
		return Jwt.verifySignature(jwt, secret);
	}
}
