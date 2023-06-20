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
package com.vonage.client.proactiveconnect;

import com.vonage.client.VonageResponseParseException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class SyncStatusTest {
	
	@Test
	public void testFromJsonAllFields() {
		SyncStatusValue value = SyncStatusValue.CANCELLED;
		String details = "Not found";
		boolean metadataModified = false, dataModified = true, dirty = true;
	
		SyncStatus response = SyncStatus.fromJson("{\n" +
				"\"value\":\""+value+"\",\n" +
				"\"details\":\""+details+"\",\n" +
				"\"metadata_modified\":\""+metadataModified+"\",\n" +
				"\"data_modified\":\""+dataModified+"\",\n" +
				"\"dirty\":\""+dirty+"\"\n" +
		"}");
		
		assertEquals(value, response.getValue());
		assertEquals(details, response.getDetails());
		assertEquals(metadataModified, response.getMetadataModified());
		assertEquals(dataModified, response.getDataModified());
		assertEquals(dirty, response.getDirty());
	}
	
	@Test(expected = VonageResponseParseException.class)
	public void testFromJsonInvalid() {
		SyncStatus.fromJson("{malformed]");
	}

	@Test
	public void testFromJsonEmpty() {
		SyncStatus response = SyncStatus.fromJson("{}");
		assertNull(response.getValue());
		assertNull(response.getDetails());
		assertNull(response.getMetadataModified());
		assertNull(response.getDataModified());
		assertNull(response.getDirty());
	}

	@Test
	public void testInvalidSyncStatusValue() {
		SyncStatus valid = SyncStatus.fromJson("{\"value\":\"clearing\"}");
		assertEquals(SyncStatusValue.CLEARING, valid.getValue());
		SyncStatus invalid = SyncStatus.fromJson("{\"value\":\"foobar\"}");
		assertNull(invalid.getValue());
	}
}