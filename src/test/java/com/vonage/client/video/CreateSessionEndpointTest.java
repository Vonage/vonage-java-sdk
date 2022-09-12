/* Copyright 2022 Vonage
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.vonage.client.video;

import com.vonage.client.HttpWrapper;
import com.vonage.client.TestUtils;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import static org.junit.Assert.assertEquals;

public class CreateSessionEndpointTest {
	private CreateSessionEndpoint endpoint;

	@Before
	public void setUp() {
		endpoint = new CreateSessionEndpoint(new HttpWrapper());
	}

	@Test
	public void testMakeRequestAllParameters() {
		String ipAddress = "192.168.1.2";
		CreateSessionRequest request = CreateSessionRequest.builder()
				.mediaMode(MediaMode.ROUTED).location(ipAddress)
				.archiveMode(ArchiveMode.ALWAYS).build();

		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals("POST", builder.getMethod());
		assertEquals("https://video.api.vonage.com/session/create", builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_FORM_URLENCODED.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
		assertEquals(3, params.size());
		assertEquals(ipAddress, params.get("location"));
		assertEquals("disabled", params.get("p2p.preference"));
		assertEquals("always", params.get("archiveMode"));
	}

	@Test
	public void testMakeRequestRequiredParameters() {
		CreateSessionRequest request = CreateSessionRequest.builder().build();
		RequestBuilder builder = endpoint.makeRequest(request);
		Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
		assertEquals(0, params.size());
	}
}