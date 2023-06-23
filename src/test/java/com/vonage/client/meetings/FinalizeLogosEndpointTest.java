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
package com.vonage.client.meetings;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import com.vonage.client.TestUtils;
import com.vonage.client.VonageUnexpectedException;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.http.client.methods.RequestBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.UUID;

public class FinalizeLogosEndpointTest {
	private FinalizeLogosEndpoint endpoint;
	
	@Before
	public void setUp() {
		endpoint = new FinalizeLogosEndpoint(new HttpWrapper(new JWTAuthMethod("app-id", new byte[0])));
	}
	
	@Test
	public void testDefaultUri() throws Exception {
		UUID themeId = UUID.randomUUID();
		FinalizeLogosRequest request = new FinalizeLogosRequest(themeId, Arrays.asList("col", "fff"));
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals("PUT", builder.getMethod());
		String expectedUri = "https://api-eu.vonage.com/meetings/themes/"+themeId+"/finalizeLogos";
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertNull(endpoint.parseResponse(TestUtils.makeJsonHttpResponse(200, "")));
	}

	@Test
	public void testCustomUri() throws Exception {
		UUID themeId = UUID.randomUUID();
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri(baseUri).build());
		endpoint = new FinalizeLogosEndpoint(wrapper);
		String expectedUri = baseUri + "/meetings/themes/"+themeId+"/finalizeLogos";
		FinalizeLogosRequest request = new FinalizeLogosRequest(themeId, Arrays.asList("lk1", "lk2"));
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals("PUT", builder.getMethod());
	}

	@Test(expected = MeetingsResponseException.class)
	public void testUnsuccessfulResponse() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(400, "{}"));
	}

	@Test(expected = VonageUnexpectedException.class)
	public void triggerJsonProcessingException() {
		class SelfRefrencing extends FinalizeLogosRequest {
			@JsonProperty("self") final SelfRefrencing self = this;
			SelfRefrencing() {
				super(null, null);
			}
		}
		new SelfRefrencing().toJson();
	}
}