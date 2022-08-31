/*
 *   Copyright 2022 Vonage
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

import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SetStreamLayoutRequestTest {
	
	@Test
	public void testSerializeValid() {
		String id = UUID.randomUUID().toString();
		List<SessionStream> items = Arrays.asList(
			SessionStream.builder(id).layoutClassList(Arrays.asList(
				"full"
			))
			.build()
		);
		SetStreamLayoutRequest request = SetStreamLayoutRequest.builder(id)
			.items(items).build();
		
		String json = request.toJson();
	}
	
}