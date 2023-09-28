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
package com.vonage.client.voice;

import com.vonage.client.HttpWrapper;
import com.vonage.client.TestUtils;
import org.apache.http.client.methods.RequestBuilder;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class DownloadRecordingEndpointTest {
    private DownloadRecordingEndpoint endpoint;

    @BeforeEach
    public void setUp() throws Exception {
        endpoint = new DownloadRecordingEndpoint(new HttpWrapper());
    }

    @Test
    public void testMakeRequest() throws Exception {
        String url = "https://example.com/"+UUID.randomUUID()+"/recording.wav";
        RequestBuilder builder = endpoint.makeRequest(url);
        assertEquals("GET", builder.getMethod());
        assertEquals(url, builder.build().getURI().toString());
    }

    @Test
    public void testParseResponse() throws Exception {
        String content = "{\"download_url\":\"foo\"}";
        Recording recording = endpoint.parseResponse(TestUtils.makeJsonHttpResponse(200, content));
        Path temp = Files.createTempFile(null, null);
        Files.delete(temp);
        recording.save(temp);
        assertEquals(content, new String(Files.readAllBytes(temp)));
    }
}
