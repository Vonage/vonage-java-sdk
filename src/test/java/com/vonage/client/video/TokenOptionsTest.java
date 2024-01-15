/*
 *   Copyright 2024 Vonage
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

import com.vonage.jwt.Jwt;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;

public class TokenOptionsTest {
	final String applicationId = UUID.randomUUID().toString();
	final String privateKeyContents = "blah";

	Jwt buildJwtWithClaims(TokenOptions options) {
		Jwt.Builder jwtBuilder = Jwt.builder().applicationId(applicationId).privateKeyContents(privateKeyContents);
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

		ZonedDateTime expiresAt = jwt.getExpiresAt();
		assertFalse(expiresAt.isAfter(ZonedDateTime.now().plus(ttl)));
		assertTrue(expiresAt.minus(ttl).isBefore(ZonedDateTime.now().plusSeconds(1)));

		Map<String, ?> claims = jwt.getClaims();
		assertEquals(4, claims.size());
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
		assertEquals(2, claims.size());
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
}
