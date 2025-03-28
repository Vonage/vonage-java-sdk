/*
 *   Copyright 2025 Vonage
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
package com.vonage.client.verify;

import com.vonage.client.Jsonable;
import com.vonage.client.TestUtils;
import com.vonage.client.VonageResponseParseException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VerifyResponseTest {

	@Test
	public void testConstructor() {
		VerifyResponse response = new VerifyResponse(VerifyStatus.PARTNER_QUOTA_EXCEEDED);
		assertEquals(9, response.getStatus().getVerifyStatus());
	}

	@Test
	public void testFromJsonAllFields() {
		String
			errorText = "The number you are trying to verify is blacklisted for verification",
			network = "25503", requestId = "abcdef0123456789abcdef0123456789",
			json = "{\"status\":\"7\",\"error_text\":" + "\""+errorText+"\"," + "\"network\":\""+network+"\", \"request_id\":\""+requestId+"\"}";

		VerifyResponse response = Jsonable.fromJson(json);
		TestUtils.testJsonableBaseObject(response);
		assertEquals(VerifyStatus.NUMBER_BARRED, response.getStatus());
		assertEquals(7, response.getStatus().getVerifyStatus());
		assertEquals(errorText, response.getErrorText());
		assertEquals(requestId, response.getRequestId());
		assertEquals(network, response.getNetwork());
	}

	@Test
	public void testFromJsonEmptyThrows() {
		assertThrows(VonageResponseParseException.class, () -> Jsonable.fromJson("{}", VerifyResponse.class));
	}

	@Test
	public void testFromJsonStatusOnly() {
		VerifyResponse response = Jsonable.fromJson("{\"status\":\"4\"}");
		assertEquals(VerifyStatus.INVALID_CREDENTIALS, response.getStatus());
		assertEquals(4, response.getStatus().getVerifyStatus());
	}
}
