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
package com.vonage.client.video;

import com.vonage.client.HttpWrapper;
import com.vonage.client.TestUtils;
import com.vonage.client.VonageBadRequestException;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.http.client.methods.RequestBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.*;
import java.util.UUID;

public class ForceDisconnectEndpointTest {
	private ForceDisconnectEndpoint endpoint;
	private final String applicationId = UUID.randomUUID().toString();
	
	@BeforeEach
	public void setUp() {
		endpoint = new ForceDisconnectEndpoint(new HttpWrapper(
			new JWTAuthMethod(applicationId, new byte[0])
		));
	}
	
	@Test
	public void testMakeRequest() throws Exception {
		String sessionId = UUID.randomUUID().toString(),
				connectionId = UUID.randomUUID().toString(),
				expectedUri = "https://video.api.vonage.com/v2/project/" +
						applicationId+"/session/"+sessionId+"/connection/"+connectionId;
		
		RequestBuilder builder = endpoint.makeRequest(new SessionResourceRequestWrapper(sessionId, connectionId));
		Assertions.assertEquals("DELETE", builder.getMethod());
		Assertions.assertEquals(expectedUri, builder.build().getURI().toString());
	}

	@Test
	public void test500Response() throws Exception {
		assertThrows(VonageBadRequestException.class, () ->
				endpoint.parseResponse(TestUtils.makeJsonHttpResponse(500, ""))
		);
	}
}