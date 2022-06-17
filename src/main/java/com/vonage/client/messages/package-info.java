/*
 *   Copyright 2020 Vonage
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

/**
 * This package contains classes and sub-packages to support usage of the
 * <a href=https://developer.vonage.com/api/messages-olympus>Messages v1 API</a>.
 * <br>
 *
 * The API allows sending various types of messages (text, video, image, audio, files) over
 * various channels (SMS, MMS, Viber, WhatsApp, Facebook Messenger). Only a specific combination
 * of message types and channels are valid, and each channel's service has varying requirements
 * and restrictions on messages. Where feasible, these restrictions are enforced by this SDK so
 * that requests to the API are correct by construction. That is, it should be very difficult,
 * ideally impossible to construct a malformed MessageRequest when calling
 * {@link com.vonage.client.messages.MessagesClient#sendMessage(com.vonage.client.messages.MessageRequest)}.
 * Hence, types are used heavily to minimize the possibility of creating invalid requests.
 * <br>
 *
 * Subclasses of {@link com.vonage.client.messages.MessageRequest} explicitly exhaust all supported combinations
 * of message type and channel. They all follow a builder pattern to enable convenient construction of messages
 * with named parameters in any order. However, it is the user's responsibility to ensure that required parameters
 * are set before calling the {@link com.vonage.client.messages.MessageRequest.Builder#build()} method, and to ensure
 * that they are sensible values. When in doubt, please consult the
 * <a href=https://developer.vonage.com/api/messages-olympus#SendMessage>API specification</a>
 * for required and optional parameters, as well as requirements for valid values.
 * <br>
 *
 * When calling {@link com.vonage.client.messages.MessagesClient#sendMessage(com.vonage.client.messages.MessageRequest)},
 * it is advised that the user catches {@link com.vonage.client.messages.MessageResponseException} to handle cases
 * where the message was not sent successfully.
 */
package com.vonage.client.messages;
