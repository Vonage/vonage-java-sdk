/* Copyright 2023 Vonage
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

import static org.junit.Assert.*;
import org.junit.jupiter.api.*;

public class ListStreamCompositionsRequestTest {

	@Test
	public void testNoParameters() {
		ListStreamCompositionsRequest request = ListStreamCompositionsRequest.builder().build();
		Assertions.assertNull(request.getSessionId());
		Assertions.assertNull(request.getCount());
		Assertions.assertNull(request.getOffset());
	}

	@Test
	public void testAllParameters() {
		String sessionId = "flR1ZSBPY3QgMjkgMTI6MTM6MjMgUERUIDIwMTW";
		Integer count = 37, offset = 8;
		ListStreamCompositionsRequest request = ListStreamCompositionsRequest.builder()
				.count(count).offset(offset)
				.sessionId(sessionId).build();
		Assertions.assertEquals(sessionId, request.getSessionId());
		Assertions.assertEquals(count, request.getCount());
		Assertions.assertEquals(offset, request.getOffset());
	}

	@Test
	public void testNullParameters() {
		ListStreamCompositionsRequest request = ListStreamCompositionsRequest.builder().sessionId(null).build();
		Assertions.assertNull(request.getSessionId());
		Assertions.assertNull(request.getOffset());
		Assertions.assertNull(request.getCount());
	}

	@Test
	public void testCountBoundaries() {
		assertThrows(IllegalArgumentException.class, () ->
			ListStreamCompositionsRequest.builder().count(-1).build()
		);
		assertThrows(IllegalArgumentException.class, () ->
			ListStreamCompositionsRequest.builder().count(1001).build()
		);
		ListStreamCompositionsRequest.builder().count(1000).build();
		ListStreamCompositionsRequest.builder().count(0).build();
	}

	@Test
	public void testOffsetBoundaries() {
		assertThrows(IllegalArgumentException.class, () ->
			ListStreamCompositionsRequest.builder().offset(-1).build()
		);
		ListStreamCompositionsRequest.builder().offset(0).build();
		ListStreamCompositionsRequest.builder().offset(1).build();
	}
}
