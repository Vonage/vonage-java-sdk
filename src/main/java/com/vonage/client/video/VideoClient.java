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
package com.vonage.client.video;

import com.vonage.client.HttpWrapper;
import com.vonage.client.VonageClient;

/**
 * A client for talking to the Vonage Verify API. The standard way to obtain an instance of this class is to use
 * {@link VonageClient#getVideoClient()}.
 */
public class VideoClient {
	final CreateSessionEndpoint createSession;

	/**
	 * Constructor.
	 *
	 * @param httpWrapper (REQUIRED) shared HTTP wrapper object used for making REST calls.
	 */
	public VideoClient(HttpWrapper httpWrapper) {
		createSession = new CreateSessionEndpoint(httpWrapper);
	}

	public CreateSessionResponse createSession(CreateSessionRequest createSessionRequest) {
		return createSession.execute(createSessionRequest);
	}

}
