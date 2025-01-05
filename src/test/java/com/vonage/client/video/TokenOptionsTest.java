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
package com.vonage.client.video;

import com.vonage.client.TestUtils;
import com.vonage.jwt.Jwt;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class TokenOptionsTest {

	Jwt buildJwtWithClaims(TokenOptions options) {
		Jwt.Builder jwtBuilder = Jwt.builder().applicationId(TestUtils.APPLICATION_ID).unsigned();
		options.addClaims(jwtBuilder);
		return jwtBuilder.build();
	}

	@Test
	public void testAllValidOptions() {
		String data = String.join("", Collections.nCopies(100, "myConnData"));
		assertEquals(1000, data.length());
		Duration ttl = Duration.ofDays(30).minusMillis(1);
		List<String> layoutClassList = Arrays.asList("fill", "min");
		Role role = Role.MODERATOR;

		TokenOptions options = TokenOptions.builder()
			.initialLayoutClassList(layoutClassList)
			.data(data).expiryLength(ttl).role(role).build();

		Jwt jwt = buildJwtWithClaims(options);

		Instant expiresAt = jwt.getExpiresAt();
		assertFalse(expiresAt.isAfter(Instant.now().plus(ttl)));
		assertTrue(expiresAt.minus(ttl).isBefore(Instant.now().plusSeconds(1)));

		Map<String, ?> claims = jwt.getClaims();
		assertEquals(5, claims.size());
		assertEquals(data, claims.get("connection_data"));
		assertEquals(String.join(" ", layoutClassList), claims.get("initial_layout_class_list"));
		assertEquals(role.toString(), claims.get("role"));
	}

	@Test
	public void testMandatoryParameters() {
		TokenOptions options = TokenOptions.builder().build();

		assertEquals(Role.PUBLISHER, options.getRole());
		assertEquals(Duration.ofDays(1), options.getExpiryLength());
		assertNull(options.getInitialLayoutClassList());
		assertNull(options.getData());

		Jwt jwt = buildJwtWithClaims(options);

		Map<String, ?> claims = jwt.getClaims();
		assertEquals(3, claims.size());
		assertEquals("publisher", claims.get("role").toString());
		assertNull(claims.get("connection_data"));
		assertNull(claims.get("initial_layout_class_list"));
	}

	@Test
	public void testInvalidParameters() {
		assertThrows(NullPointerException.class, () -> TokenOptions.builder().role(null).build());
		assertThrows(NullPointerException.class, () -> TokenOptions.builder().expiryLength(null).build());

		Duration longTtl = Duration.ofDays(30).plusMillis(1);
		assertThrows(IllegalArgumentException.class, () -> TokenOptions.builder().expiryLength(longTtl).build());

		String data1001 = new String(new char[1001]).replace("\0", "d");
		assertEquals(1001, data1001.length());
		assertThrows(IllegalArgumentException.class, () -> TokenOptions.builder().data(data1001).build());
	}

	@Test
	public void testPublisherOnlyRole() {
		TokenOptions options = TokenOptions.builder().role(Role.PUBLISHER_ONLY).build();
		assertEquals(Role.PUBLISHER_ONLY, options.getRole());

		Jwt jwt = buildJwtWithClaims(options);

		Map<String, ?> claims = jwt.getClaims();
		assertEquals(3, claims.size());
		assertEquals("publisheronly", claims.get("role").toString());
	}
}
