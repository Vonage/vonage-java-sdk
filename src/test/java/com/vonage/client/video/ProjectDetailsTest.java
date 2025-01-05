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
import com.vonage.client.VonageUnexpectedException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class ProjectDetailsTest {
	
	@Test
	public void testFromJsonAllFields() {
		String applicationId = "78d335fa-323d-0114-9c3d-d6f0d48968cf";
		ProjectStatus status = ProjectStatus.ACTIVE;
		String name = "Joe Montana";
		ProjectEnvironment environment = ProjectEnvironment.STANDARD;
		Long createdAt = 1414642898000L;
	
		ProjectDetails response = ProjectDetails.fromJson("{\n" +
				"\"applicationId\":\""+applicationId+"\",\n" +
				"\"status\":\""+status+"\",\n" +
				"\"name\":\""+name+"\",\n" +
				"\"environment\":\""+environment+"\",\n" +
				"\"createdAt\":\""+createdAt+"\"\n" +
		"}");

		TestUtils.testJsonableBaseObject(response);
		assertEquals(applicationId, response.getApplicationId());
		assertEquals(status, response.getStatus());
		assertEquals(name, response.getName());
		assertEquals(environment, response.getEnvironment());
		assertEquals(createdAt, response.getCreatedAt());
	}

	@Test
	public void testFromJsonUnknownEnums() {
		ProjectDetails response = ProjectDetails.fromJson("{\n" +
				"\"status\":\"u\",\n" +
				"\"environment\":\"e\",\n" +
				"\"name\":\"\""+
				"}");

		TestUtils.testJsonableBaseObject(response);
		assertEquals("", response.getName());
		assertNull(response.getStatus());
		assertNull(response.getEnvironment());
	}
	
	@Test
	public void testFromJsonInvalid() {
		assertThrows(VonageUnexpectedException.class, () -> ProjectDetails.fromJson("{malformed]"));
	}

	@Test
	public void testFromJsonEmpty() {
		ProjectDetails response = ProjectDetails.fromJson("{}");
		TestUtils.testJsonableBaseObject(response);
		assertNull(response.getApplicationId());
		assertNull(response.getStatus());
		assertNull(response.getName());
		assertNull(response.getEnvironment());
		assertNull(response.getCreatedAt());
	}

	@Test
	public void testFromJsonNullAndEmptyString() {
		assertNotNull(ProjectDetails.fromJson(""));
		assertNotNull(ProjectDetails.fromJson(null));
	}
}