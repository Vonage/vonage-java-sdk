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

import static org.junit.Assert.assertThrows;
import org.junit.jupiter.api.*;

public class SignalRequestTest {

	@Test
	public void testValidConstruction() {
		SignalRequest.Builder builder = SignalRequest.builder();
		SignalRequest sr = builder.data("d").type("t").build();
		Assertions.assertEquals("d", sr.getData());
		Assertions.assertEquals("t", sr.getType());
		sr = builder.data("").type("").build();
		Assertions.assertEquals("", sr.getData());
		Assertions.assertEquals("", sr.getType());
	}

	@Test
	public void testInvalidConstruction() {
		assertThrows(NullPointerException.class, SignalRequest.builder()::build);
		assertThrows(NullPointerException.class, () -> SignalRequest.builder().type("type").build());
		assertThrows(NullPointerException.class, () -> SignalRequest.builder().data("data").build());
	}
}
