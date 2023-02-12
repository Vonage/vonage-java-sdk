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
import com.vonage.client.TestUtils;
import com.vonage.client.VonageBadRequestException;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import java.util.UUID;

public class SignalAllEndpointTest {
	private SignalAllEndpoint endpoint;
	private final String applicationId = UUID.randomUUID().toString();
	
	@Before
	public void setUp() {
		endpoint = new SignalAllEndpoint(new HttpWrapper(
			new JWTAuthMethod(applicationId, new byte[0])
		));
	}
	
	@Test
	public void testMakeRequestAllParameters() throws Exception {
		String sessionId = UUID.randomUUID().toString(),
				data = "The actual payload as a string",
				type = "Signal channel";
		SignalRequest properties = SignalRequest.builder().data(data).type(type).build();
		SignalRequestWrapper request = new SignalRequestWrapper(properties, sessionId);
		
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals("POST", builder.getMethod());
		String expectedUri = "https://video.api.vonage.com/v2/project/" +
				applicationId + "/session/" + sessionId + "/signal";
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		String expectedPayload = "{\"type\":\""+type+"\",\"data\":\""+data+"\"}";
		assertEquals(expectedPayload, EntityUtils.toString(builder.getEntity()));
	}

	@Test(expected = VonageBadRequestException.class)
	public void testUnsuccessfulResponse() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(500, ""));
	}
}