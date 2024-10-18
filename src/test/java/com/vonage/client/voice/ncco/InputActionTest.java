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
package com.vonage.client.voice.ncco;

import static com.vonage.client.TestUtils.testJsonableBaseObject;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

public class InputActionTest {

    @Test
    public void testBuilderMultipleInstances() {
        var builder = InputAction.builder().dtmf().mode(InputMode.SYNCHRONOUS);
        assertNotSame(builder.build(), builder.build());
    }

    @Test
    public void testAllFields() {
        var speechSettings = SpeechSettings.builder()
                .uuid("aaaaaaaa-bbbb-cccc-dddd-0123456789ab")
                .startTimeout(3).endOnSilence(5).maxDuration(50)
                .language(SpeechSettings.Language.ENGLISH_NIGERIA)
                .context(Arrays.asList("support", "buy", "credit")).build();

        var dtmfSettings = DtmfSettings.builder().maxDigits(4).submitOnHash(true).timeOut(10).build();

        InputAction input = InputAction.builder()
                .speech(speechSettings)
                .dtmf(dtmfSettings)
                .mode(InputMode.SYNCHRONOUS)
                .eventUrl("http://example.com")
                .eventMethod(EventMethod.POST)
                .build();

        String expectedJson = "[" +
                "{\"type\":[\"speech\",\"dtmf\"]," +
                "\"dtmf\":{" +
                "\"timeOut\":10," +
                "\"maxDigits\":4," +
                "\"submitOnHash\":true" +
                "}," +
                "\"eventUrl\":[\"http://example.com\"]," +
                "\"speech\":{" +
                "\"uuid\":[\"aaaaaaaa-bbbb-cccc-dddd-0123456789ab\"]," +
                "\"context\":[\"support\",\"buy\",\"credit\"]," +
                "\"endOnSilence\":5.0," +
                "\"startTimeout\":3," +
                "\"maxDuration\":50," +
                "\"language\":\"en-NG\"" +
                "}," +
                "\"eventMethod\":\"POST\"," +
                "\"mode\":\"synchronous\"," +
                "\"action\":\"input\"" +
                "}]";

        assertEquals(expectedJson, new Ncco(input).toJson());
    }

    @Test
    public void testGetActionOnly() {
        InputAction input = InputAction.builder().dtmf().build();
        assertEquals("input", input.getAction());
        var type = input.getType();
        assertNotNull(type);
        assertEquals(1, type.size());
        assertEquals("dtmf", type.iterator().next());
        assertNull(input.getDtmf());
        assertNull(input.getSpeech());
        assertNull(input.getEventUrl());
        assertNull(input.getMode());
    }

    @Test
    public void testAtLeastOneTypeIsSpecified() {
        assertThrows(IllegalStateException.class, () -> InputAction.builder().build());
        assertThrows(IllegalStateException.class, () -> InputAction.builder().dtmf().type(null).build());
        for (String type : new String[] {"dtmf", "speech"}) {
            var input = InputAction.builder().type(Collections.singletonList(type)).build();
            String expectedJson = "[{\"type\":[\""+type+"\"],\"action\":\"input\"}]";
            assertEquals(expectedJson, new Ncco(input).toJson());
        }
    }

    @Test
    public void testDtmfOnly() {
        var dtmfSettings = DtmfSettings.builder().maxDigits(4).build();

        InputAction input = InputAction.builder().type(Collections.singletonList("dtmf")).dtmf(dtmfSettings).build();
        String expectedJson = "[{\"type\":[\"dtmf\"],\"dtmf\":{\"maxDigits\":4},\"action\":\"input\"}]";
        assertEquals(expectedJson, new Ncco(input).toJson());
    }

    @Test
    public void testDtmfSettingsBoundaries() {
        var blank = DtmfSettings.builder().build();
        testJsonableBaseObject(blank);
        assertNull(blank.isSubmitOnHash());
        assertNull(blank.getMaxDigits());
        assertNull(blank.getTimeOut());

        int timeOutMin = 0, timeOutMax = 10, maxDigitsMin = 0, maxDigitsMax = 20;
        assertEquals(timeOutMin, DtmfSettings.builder().timeOut(timeOutMin).build().getTimeOut());
        assertThrows(IllegalArgumentException.class, () -> DtmfSettings.builder().timeOut(timeOutMin - 1).build());
        assertEquals(timeOutMax, DtmfSettings.builder().timeOut(timeOutMax).build().getTimeOut());
        assertThrows(IllegalArgumentException.class, () -> DtmfSettings.builder().timeOut(timeOutMax + 1).build());

        assertEquals(maxDigitsMin, DtmfSettings.builder().maxDigits(maxDigitsMin).build().getMaxDigits());
        assertThrows(IllegalArgumentException.class, () -> DtmfSettings.builder().maxDigits(maxDigitsMin - 1).build());
        assertEquals(maxDigitsMax, DtmfSettings.builder().maxDigits(maxDigitsMax).build().getMaxDigits());
        assertThrows(IllegalArgumentException.class, () -> DtmfSettings.builder().maxDigits(maxDigitsMax + 1).build());
    }

    @Test
    public void testDtmfAsynchronousMode() {
        var input = InputAction.builder().dtmf().mode(InputMode.ASYNCHRONOUS).build();
        String expectedJson = "[{\"type\":[\"dtmf\"],\"mode\":\"asynchronous\",\"action\":\"input\"}]";
        assertEquals(expectedJson, new Ncco(input).toJson());
        assertThrows(IllegalStateException.class, () -> InputAction.builder()
                    .dtmf(DtmfSettings.builder().build())
                    .mode(InputMode.ASYNCHRONOUS).build()
        );
    }

    @Test
    public void testSpeechAndAynschrounousMode() {
        var input = InputAction.builder()
                .speech(SpeechSettings.builder().build())
                .mode(InputMode.ASYNCHRONOUS).build();
        String expectedJson = "[{\"type\":[\"speech\"],\"speech\":{},\"mode\":\"asynchronous\",\"action\":\"input\"}]";
        assertEquals(expectedJson, new Ncco(input).toJson());
    }

    @Test
    public void testSpeechOnly() {
        String uuid = UUID.randomUUID().toString();
        SpeechSettings speechSettings = SpeechSettings.builder()
                .endOnSilence(2.0).sensitivity(90).startTimeout(10)
                .language(SpeechSettings.Language.ENGLISH_NIGERIA)
                .context("hint1", "Hint 2").uuid(uuid)
                .saveAudio(true).maxDuration(60).build();

        testJsonableBaseObject(speechSettings);
        InputAction input = InputAction.builder().type(Collections.singletonList("speech")).speech(speechSettings).build();
        String expectedJson = "[{\"type\":[\"speech\"],\"speech\":{\"uuid\":[\""+uuid+"\"],\"context\":" +
                "[\"hint1\",\"Hint 2\"],\"endOnSilence\":2.0,\"startTimeout\":10," +
                "\"maxDuration\":60,\"sensitivity\":90,\"language\":\"en-NG\"," +
                "\"saveAudio\":true},\"action\":\"input\"}]";
        assertEquals(expectedJson, new Ncco(input).toJson());
    }

    @Test
    public void testSpeechSettingsBoundaries() {
        testJsonableBaseObject(SpeechSettings.builder().build());

        assertNotNull(SpeechSettings.builder().maxDuration(1).build());
        assertNotNull(SpeechSettings.builder().maxDuration(60).build());
        assertThrows(IllegalArgumentException.class, () ->
                SpeechSettings.builder().maxDuration(0).build()
        );
        assertThrows(IllegalArgumentException.class, () ->
                SpeechSettings.builder().maxDuration(61).build()
        );

        assertNotNull(SpeechSettings.builder().startTimeout(1).build());
        assertNotNull(SpeechSettings.builder().startTimeout(60).build());
        assertThrows(IllegalArgumentException.class, () ->
                SpeechSettings.builder().startTimeout(0).build()
        );
        assertThrows(IllegalArgumentException.class, () ->
                SpeechSettings.builder().startTimeout(61).build()
        );

        assertNotNull(SpeechSettings.builder().endOnSilence(0.4).build());
        assertNotNull(SpeechSettings.builder().endOnSilence(10).build());
        assertThrows(IllegalArgumentException.class, () ->
                SpeechSettings.builder().endOnSilence(0.3).build()
        );
        assertThrows(IllegalArgumentException.class, () ->
                SpeechSettings.builder().endOnSilence(10.1).build()
        );

        assertNotNull(SpeechSettings.builder().sensitivity(10).build());
        assertNotNull(SpeechSettings.builder().sensitivity(100).build());
        assertThrows(IllegalArgumentException.class, () ->
                SpeechSettings.builder().sensitivity(9).build()
        );
        assertThrows(IllegalArgumentException.class, () ->
                SpeechSettings.builder().sensitivity(101).build()
        );
    }

    @Test
    public void testSpeechNullRemovesSettingsAndType() {
        var builder = InputAction.builder().speech(SpeechSettings.builder().build());
        String expectedJson = "[{\"type\":[\"speech\"],\"speech\":{},\"action\":\"input\"}]";
        assertEquals(expectedJson, new Ncco(builder.build()).toJson());
        assertThrows(IllegalStateException.class, () -> builder.speech(null).build());
    }

    @Test
    public void testEventUrlField() {
        InputAction input = InputAction.builder().dtmf()
                .eventUrl("https://nexmo.com", "https://developer.vonage.com")
                .eventUrl("http://example.com").build();
        String expectedJson = "[{\"type\":[\"dtmf\"],\"eventUrl\":[\"http://example.com\"],\"action\":\"input\"}]";
        assertEquals(expectedJson, new Ncco(input).toJson());
    }

    @Test
    public void testEventMethodField() {
        InputAction input = InputAction.builder().dtmf().eventMethod(EventMethod.POST).build();

        String expectedJson = "[{\"type\":[\"dtmf\"],\"eventMethod\":\"POST\",\"action\":\"input\"}]";
        assertEquals(expectedJson, new Ncco(input).toJson());
    }

    @Test
    public void testInputModeDeserialization() {
        assertEquals(InputMode.SYNCHRONOUS, InputMode.fromString("synchronous"));
        assertEquals(InputMode.ASYNCHRONOUS, InputMode.fromString("asynchronous"));
        assertNull(InputMode.fromString("invalid"));
    }
}
