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
 * Provides useful NCCO classes which can be serialized using Jackson when
 * implementing webhooks to drive the Vonage Voice API.
 * <p>
 * The simplest way to use these classes is to subclass {@link com.vonage.client.voice.servlet.AbstractAnswerServlet}
 * and implement {@link com.vonage.client.voice.servlet.AbstractAnswerServlet#handleRequest(javax.servlet.http.HttpServletRequest)}.
 * the returned NCCOResponse will automatically be serialized correctly.
 */
package com.vonage.client.voice.ncco;