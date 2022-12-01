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

import static org.junit.Assert.*;
import org.junit.Test;

public class ListArchivesRequestTest {

	@Test
	public void testNoParameters() {
		ListArchivesRequest request = ListArchivesRequest.builder().build();
		assertNull(request.getSessionId());
		assertNull(request.getCount());
		assertNull(request.getOffset());
	}

	@Test
	public void testAllParameters() {
		String sessionId = "flR1ZSBPY3QgMjkgMTI6MTM6MjMgUERUIDIwMTW";
		Integer count = 37, offset = 8;
		ListArchivesRequest request = ListArchivesRequest.builder()
				.count(count).offset(offset)
				.sessionId(sessionId).build();
		assertEquals(sessionId, request.getSessionId());
		assertEquals(count, request.getCount());
		assertEquals(offset, request.getOffset());
	}

	@Test
	public void testNullParameters() {
		ListArchivesRequest request = ListArchivesRequest.builder().sessionId(null).build();
		assertNull(request.getSessionId());
		assertNull(request.getOffset());
		assertNull(request.getCount());
	}

	@Test
	public void testCountBoundaries() {
		assertThrows(IllegalArgumentException.class, () ->
			ListArchivesRequest.builder().count(-1).build()
		);
		assertThrows(IllegalArgumentException.class, () ->
			ListArchivesRequest.builder().count(1001).build()
		);
		ListArchivesRequest.builder().count(1000).build();
		ListArchivesRequest.builder().count(0).build();
	}

	@Test
	public void testOffsetBoundaries() {
		assertThrows(IllegalArgumentException.class, () ->
			ListArchivesRequest.builder().offset(-1).build()
		);
		ListArchivesRequest.builder().offset(0).build();
		ListArchivesRequest.builder().offset(1).build();
	}
}
