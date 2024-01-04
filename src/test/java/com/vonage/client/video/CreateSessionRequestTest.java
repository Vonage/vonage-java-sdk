/* Copyright 2024 Vonage
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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.net.Inet4Address;
import java.net.InetAddress;

public class CreateSessionRequestTest {

	@Test
	public void testDisabledAndManualIsValid() {
		CreateSessionRequest request = CreateSessionRequest.builder()
				.mediaMode(MediaMode.ROUTED).archiveMode(ArchiveMode.MANUAL).build();

		Assertions.assertEquals("manual", request.getArchiveMode().toString());
		Assertions.assertEquals("disabled", request.getMediaMode().toString());
		Assertions.assertNull(request.getLocation());
	}

	@Test
	public void testEnabledAndManualIsValid() {
		CreateSessionRequest request = CreateSessionRequest.builder()
				.mediaMode(MediaMode.RELAYED).archiveMode(ArchiveMode.MANUAL).build();

		Assertions.assertEquals("manual", request.getArchiveMode().toString());
		Assertions.assertEquals("enabled", request.getMediaMode().toString());
		Assertions.assertNull(request.getLocation());
	}

	@Test
	public void testValidLocation() throws Exception {
		String ipStr = "188.180.180.180";
		CreateSessionRequest.Builder builder = CreateSessionRequest.builder();
		CreateSessionRequest request = builder.location(ipStr).build();
		Assertions.assertEquals(ipStr, request.getLocation().getHostAddress());
		InetAddress localhost = Inet4Address.getLocalHost();
		request = builder.location(localhost).build();
		Assertions.assertEquals(localhost, request.getLocation());
		ipStr = "localhost";
		request = builder.location(ipStr).build();
		Assertions.assertEquals("localhost", request.getLocation().getHostName());
		Assertions.assertEquals("127.0.0.1", request.getLocation().getHostAddress());
	}

	@Test
	public void testInvalidLocation() {
		assertThrows(IllegalArgumentException.class, () ->
				CreateSessionRequest.builder().location("not an IP").build()
		);
	}

	@Test
	public void testEnabledAndAlwaysIsInvalid() {
		assertThrows(IllegalStateException.class, () ->
				CreateSessionRequest.builder().mediaMode(MediaMode.RELAYED).archiveMode(ArchiveMode.ALWAYS).build()
		);
	}
}
