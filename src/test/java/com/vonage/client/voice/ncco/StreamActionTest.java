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
package com.vonage.client.voice.ncco;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class StreamActionTest {
    @Test
    public void testBuilderMultipleInstances() {
        StreamAction.Builder builder = StreamAction.builder("http://example.com");
        assertNotSame(builder.build(), builder.build());
    }

    @Test
    public void testAllFields() {
        StreamAction stream = StreamAction.builder("http://example.com")
                .streamUrl("http://example.org")
                .level(0.33f)
                .bargeIn(true)
                .loop(3)
                .build();

        String expectedJson = "[{\"streamUrl\":[\"http://example.org\"],\"level\":0.33,\"bargeIn\":true,\"loop\":3,\"action\":\"stream\"}]";
        assertEquals(expectedJson, new Ncco(stream).toJson());
    }

    @Test
    public void testGetAction() {
        StreamAction stream = StreamAction.builder("http://example.com").build();
        assertEquals("stream", stream.getAction());
    }

    @Test
    public void testStreamUrlField() {
        StreamAction stream = StreamAction.builder("http://example.com").streamUrl("http://example.org").build();

        String expectedJson = "[{\"streamUrl\":[\"http://example.org\"],\"action\":\"stream\"}]";
        assertEquals(expectedJson, new Ncco(stream).toJson());
    }

    @Test
    public void testLevelField() {
        StreamAction stream = StreamAction.builder("http://example.com").level(-0.35f).build();

        String expectedJson = "[{\"streamUrl\":[\"http://example.com\"],\"level\":-0.35,\"action\":\"stream\"}]";
        assertEquals(expectedJson, new Ncco(stream).toJson());
    }

    @Test
    public void testBargeInField() {
        StreamAction stream = StreamAction.builder("http://example.com").bargeIn(true).build();

        String expectedJson = "[{\"streamUrl\":[\"http://example.com\"],\"bargeIn\":true,\"action\":\"stream\"}]";
        assertEquals(expectedJson, new Ncco(stream).toJson());
    }

    @Test
    public void testLoopField() {
        StreamAction stream = StreamAction.builder("http://example.com").loop(3).build();

        String expectedJson = "[{\"streamUrl\":[\"http://example.com\"],\"loop\":3,\"action\":\"stream\"}]";
        assertEquals(expectedJson, new Ncco(stream).toJson());
    }
}
