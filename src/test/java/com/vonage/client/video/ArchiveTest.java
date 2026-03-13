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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.Jsonable;
import static com.vonage.client.TestUtils.testJsonableBaseObject;
import com.vonage.client.VonageResponseParseException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ArchiveTest {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	private String loadJsonResource(String filename) throws IOException {
		try (InputStream is = getClass().getResourceAsStream(filename)) {
			if (is == null) {
				throw new IOException("Could not find resource: " + filename);
			}
			byte[] buffer = new byte[1024];
			StringBuilder sb = new StringBuilder();
			int bytesRead;
			while ((bytesRead = is.read(buffer)) != -1) {
				sb.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
			}
			return sb.toString().trim();
		}
	}

	private void assertJsonEquals(String expected, String actual) throws IOException {
		JsonNode expectedNode = objectMapper.readTree(expected);
		JsonNode actualNode = objectMapper.readTree(actual);
		assertEquals(expectedNode, actualNode);
	}

	@Test
	public void testSerializeAllParams() {
		String sessionId = "flR1ZSBPY3QgMjkgMTI6MTM6MjMgUERUIDIwMTN";
		String name = "Test archive", multiArchiveTag = "DemoArchive_TagName";
		StreamCompositionLayout layout = StreamCompositionLayout.standardLayout(ScreenLayoutType.VERTICAL);
		int maxBitrate = 321400;

		Archive request = Archive.builder(sessionId)
				.name(name).maxBitrate(maxBitrate)
				.hasAudio(true).hasVideo(true)
				.resolution(Resolution.HD_LANDSCAPE)
				.outputMode(OutputMode.COMPOSED)
				.streamMode(StreamMode.AUTO).layout(layout)
				.multiArchiveTag(multiArchiveTag).build();

		testJsonableBaseObject(request);
		String json = request.toJson();
		assertTrue(json.contains("\"name\":\""+name+"\""));
		assertTrue(json.contains("\"multiArchiveTag\":\""+multiArchiveTag+"\""));
		assertTrue(json.contains("\"sessionId\":\""+sessionId+"\""));
		assertTrue(json.contains("\"outputMode\":\"composed\""));
		assertTrue(json.contains("\"streamMode\":\"auto\""));
		assertTrue(json.contains("\"resolution\":\"1280x720\""));
		assertTrue(json.contains("\"layout\":{\"type\":\"verticalPresentation\"}"));
		assertTrue(json.contains("\"hasVideo\":true"));
		assertTrue(json.contains("\"hasAudio\":true"));
		assertTrue(json.contains("\"maxBitrate\":"+maxBitrate));
	}

	@Test
	public void testQuantizationParameter() {
		String sessionId = "flR1ZSBPY3QgMjkgMTI6MTM6MjMgUERUIDIwMTN";
		int qp = 19;
		Archive request = Archive.builder(sessionId).quantizationParameter(qp).build();
		testJsonableBaseObject(request);
		String json = request.toJson();
		assertTrue(json.contains("\"sessionId\":\""+sessionId+"\""));
		assertTrue(json.contains("\"quantizationParameter\":"+qp));
		assertThrows(IllegalStateException.class, () ->
				Archive.builder(sessionId).quantizationParameter(qp).maxBitrate(213450).build()
		);
	}

	@Test
	public void testQuantizationParameterBounds() {
		var builder = Archive.builder("SESSION_ID");
		int min = 15, max = 40;
		assertThrows(IllegalArgumentException.class, () -> builder.quantizationParameter(min-1).build());
		assertEquals(min, builder.quantizationParameter(min).build().getQuantizationParameter());
		assertThrows(IllegalArgumentException.class, () -> builder.quantizationParameter(max+1).build());
		assertEquals(max, builder.quantizationParameter(max).build().getQuantizationParameter());
	}

	@Test
	public void testSerializeCustomLayout() {
		String style = "stream.instructor {position: absolute; width: 100%;  height:50%;}";
		StreamCompositionLayout layout = StreamCompositionLayout.customLayout(style);

		Archive request = Archive.builder("s1")
				.hasAudio(false).resolution(Resolution.SD_PORTRAIT)
				.streamMode(StreamMode.MANUAL).layout(layout)
				.outputMode(OutputMode.COMPOSED).build();

		testJsonableBaseObject(request);
		String json = request.toJson();
		assertTrue(json.contains("\"outputMode\":\"composed\""));
		assertTrue(json.contains("\"streamMode\":\"manual\""));
		assertTrue(json.contains("\"sessionId\":\"s1\""));
		assertTrue(json.contains("\"resolution\":\"480x640\""));
		assertTrue(json.contains("\"hasAudio\":false"));
		assertTrue(json.contains("\"layout\":{\"type\":\"custom\",\"stylesheet\":\""+style+"\"}"));
	}

	@Test
	public void testConstructCustomLayoutOnNonComposedArchive() {
		assertThrows(IllegalStateException.class, () -> Archive.builder("sessionId")
				.layout(StreamCompositionLayout.standardLayout(ScreenLayoutType.BEST_FIT))
				.outputMode(OutputMode.INDIVIDUAL).build()
		);
	}

	@Test
	public void testConstructSessionIdOnly() {
		String sessionId = UUID.randomUUID().toString();
		String expectedJson = "{\"sessionId\":\""+sessionId+"\"}";
		assertEquals(expectedJson, Archive.builder(sessionId).build().toJson());
	}

	@Test
	public void testConstructNullSessionId() {
		assertThrows(IllegalArgumentException.class, () -> Archive.builder(null).build());
	}

	@Test
	public void testConstructEmptySessionId() {
		assertThrows(IllegalArgumentException.class, () -> Archive.builder("").build());
	}

	@Test
	public void testMaxBitrateBounds() {
		var builder = Archive.builder("SESSION_ID");
		int min = 100000, max = 6000000;
		assertThrows(IllegalArgumentException.class, () -> builder.maxBitrate(min-1).build());
		assertEquals(min, builder.maxBitrate(min).build().getMaxBitrate());
		assertThrows(IllegalArgumentException.class, () -> builder.maxBitrate(max+1).build());
		assertEquals(max, builder.maxBitrate(max).build().getMaxBitrate());
	}

	@Test
	public void testFromJsonInvalid() {
		assertThrows(VonageResponseParseException.class, () -> Jsonable.fromJson("{malformed]", Archive.class));
	}

	@Test
	public void testFromJsonEmpty() {
		var archive = Jsonable.fromJson("{}", Archive.class);
		assertNotNull(archive);
		assertNull(archive.getSessionId());
		assertNull(archive.getName());
		assertNull(archive.getMultiArchiveTag());
		assertNull(archive.getResolution());
		assertNull(archive.getOutputMode());
		assertNull(archive.getStreamMode());
		assertNull(archive.getLayout());
		assertNull(archive.hasAudio());
		assertNull(archive.hasVideo());
		assertNull(archive.getMaxBitrate());
		assertNull(archive.getDuration());
		assertNull(archive.getDurationSeconds());
		assertNull(archive.getCreatedAt());
		assertNull(archive.getCreatedAtMillis());
		assertNull(archive.getQuantizationParameter());
	}

	@Test
	public void testHasTranscription() throws Exception {
		String sessionId = "testSessionId";
		Archive archive = Archive.builder(sessionId).hasTranscription(true).build();
		
		assertTrue(archive.getHasTranscription());
		
		String expectedJson = loadJsonResource("archive-has-transcription.json");
		String actualJson = archive.toJson();
		assertJsonEquals(expectedJson, actualJson);
	}

	@Test
	public void testTranscriptionProperties() throws Exception {
		String sessionId = "testSessionId";
		String language = "ja-JP";
		
		Archive.TranscriptionProperties props = new Archive.TranscriptionProperties();
		props.setPrimaryLanguageCode(language);
		props.setHasSummary(true);
		
		Archive archive = Archive.builder(sessionId)
			.transcriptionProperties(props)
			.build();
		
		assertNotNull(archive.getTranscriptionProperties());
		assertEquals(language, archive.getTranscriptionProperties().getPrimaryLanguageCode());
		assertTrue(archive.getTranscriptionProperties().getHasSummary());
		
		String expectedJson = loadJsonResource("archive-transcription-properties-full.json");
		String actualJson = archive.toJson();
		assertJsonEquals(expectedJson, actualJson);
	}

	@Test
	public void testTranscriptionPropertiesOnlyLanguage() throws Exception {
		String sessionId = "testSessionId";
		String language = "es-ES";
		
		Archive.TranscriptionProperties props = new Archive.TranscriptionProperties();
		props.setPrimaryLanguageCode(language);
		
		Archive archive = Archive.builder(sessionId)
			.transcriptionProperties(props)
			.build();
		
		assertNotNull(archive.getTranscriptionProperties());
		assertEquals(language, archive.getTranscriptionProperties().getPrimaryLanguageCode());
		assertNull(archive.getTranscriptionProperties().getHasSummary());
		
		String expectedJson = loadJsonResource("archive-transcription-properties-language-only.json");
		String actualJson = archive.toJson();
		assertJsonEquals(expectedJson, actualJson);
	}

	@Test
	public void testTranscriptionPropertiesOnlySummary() throws Exception {
		String sessionId = "testSessionId";
		
		Archive.TranscriptionProperties props = new Archive.TranscriptionProperties();
		props.setHasSummary(false);
		
		Archive archive = Archive.builder(sessionId)
			.transcriptionProperties(props)
			.build();
		
		assertNotNull(archive.getTranscriptionProperties());
		assertNull(archive.getTranscriptionProperties().getPrimaryLanguageCode());
		assertFalse(archive.getTranscriptionProperties().getHasSummary());
		
		String expectedJson = loadJsonResource("archive-transcription-properties-summary-only.json");
		String actualJson = archive.toJson();
		assertJsonEquals(expectedJson, actualJson);
	}

	@Test
	public void testTranscriptionNotSet() {
		String sessionId = "testSessionId";
		Archive archive = Archive.builder(sessionId).build();
		
		assertNull(archive.getHasTranscription());
		assertNull(archive.getTranscription());
		assertNull(archive.getTranscriptionProperties());
		
		String json = archive.toJson();
		assertFalse(json.contains("transcription"));
	}

	@Test
	public void testFullTranscriptionCreation() throws Exception {
		String sessionId = "testSessionId";
		
		Archive.TranscriptionProperties props = new Archive.TranscriptionProperties();
		props.setPrimaryLanguageCode("en-US");
		props.setHasSummary(true);
		
		Archive archive = Archive.builder(sessionId)
			.hasTranscription(true)
			.transcriptionProperties(props)
			.build();
		
		assertTrue(archive.getHasTranscription());
		assertNotNull(archive.getTranscriptionProperties());
		assertEquals("en-US", archive.getTranscriptionProperties().getPrimaryLanguageCode());
		assertTrue(archive.getTranscriptionProperties().getHasSummary());
		
		String expectedJson = loadJsonResource("archive-full-transcription-creation.json");
		String actualJson = archive.toJson();
		assertJsonEquals(expectedJson, actualJson);
	}

	@Test
	public void testFromJsonWithTranscriptionResponse() throws Exception {
		String json = loadJsonResource("archive-transcription-response-available.json");
		Archive archive = Jsonable.fromJson(json, Archive.class);
		
		assertNotNull(archive);
		assertEquals("testSession", archive.getSessionId());
		assertTrue(archive.getHasTranscription());
		assertNotNull(archive.getTranscription());
		assertEquals("available", archive.getTranscription().getStatus());
		assertEquals("https://example.com/transcription.txt", archive.getTranscription().getUrl().toString());
		assertEquals("en-US", archive.getTranscription().getPrimaryLanguageCode());
		assertTrue(archive.getTranscription().getHasSummary());
	}

	@Test
	public void testFromJsonWithTranscriptionFailed() throws Exception {
		String json = loadJsonResource("archive-transcription-response-failed.json");
		Archive archive = Jsonable.fromJson(json, Archive.class);
		
		assertNotNull(archive);
		assertTrue(archive.getHasTranscription());
		assertNotNull(archive.getTranscription());
		assertEquals("failed", archive.getTranscription().getStatus());
		assertEquals("Transcription service unavailable", archive.getTranscription().getReason());
	}
}
