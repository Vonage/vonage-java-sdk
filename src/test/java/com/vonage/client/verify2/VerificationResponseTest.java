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
package com.vonage.client.verify2;

import com.vonage.client.VonageResponseParseException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.net.URI;
import java.util.UUID;

public class VerificationResponseTest {

	@Test
	public void testConstructFromValidJson() {
		UUID rqid = UUID.randomUUID();
		String checkUrl = "https://example.com/v2/"+rqid+"/silent-auth/redirect";
		VerificationResponse response = VerificationResponse.fromJson(
				"{\"request_id\":\""+rqid+"\",\"check_url\":\""+checkUrl+"\"}"
		);
		assertEquals(rqid, response.getRequestId());
		assertEquals(URI.create(checkUrl), response.getCheckUrl());
		String toString = response.toString();
		assertTrue(toString.contains("VerificationResponse"));
		assertTrue(toString.contains(rqid.toString()));
	}

	@Test
	public void testConstructFromEmptyJson() {
		VerificationResponse response = VerificationResponse.fromJson("{}");
		assertNull(response.getRequestId());
	}

	@Test
	public void testConstructFromInvalidJson() {
		assertThrows(VonageResponseParseException.class, () -> VerificationResponse.fromJson("{_malformed_}"));
	}
}
