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

import com.vonage.client.TestUtils;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

public class InputActionTest {

    @Test
    public void testBuilderMultipleInstances() {
        InputAction.Builder builder = InputAction.builder();
        assertNotSame(builder.build(), builder.build());
    }

    @Test
    public void testAllFields() {
        SpeechSettings speechSettings = new SpeechSettings();
        speechSettings.setUuid(Collections.singletonList("aaaaaaaa-bbbb-cccc-dddd-0123456789ab"));
        speechSettings.setStartTimeout(3);
        speechSettings.setEndOnSilence(5);
        speechSettings.setLanguage(SpeechSettings.Language.ENGLISH_NIGERIA);
        speechSettings.setMaxDuration(50);
        speechSettings.setContext(Arrays.asList("support", "buy", "credit"));

        DtmfSettings dtmfSettings = new DtmfSettings();
        dtmfSettings.setMaxDigits(4);
        dtmfSettings.setSubmitOnHash(true);
        dtmfSettings.setTimeOut(10);

        InputAction input = InputAction.builder()
                .type(Arrays.asList("speech", "dtmf"))
                .dtmf(dtmfSettings)
                .eventUrl("http://example.com")
                .eventMethod(EventMethod.POST)
                .speech(speechSettings)
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
                "\"action\":\"input\"" +
                "}]";

        assertEquals(expectedJson, new Ncco(input).toJson());
    }

    @Test
    public void testGetAction() {
        InputAction input = InputAction.builder().build();
        assertEquals("input", input.getAction());
    }

    @Test
    public void testDefault() {
        InputAction input = InputAction.builder().build();

        String expectedJson = "[{\"action\":\"input\"}]";
        assertEquals(expectedJson, new Ncco(input).toJson());
    }

    @Test
    public void testDtmfOnly() {
        DtmfSettings dtmfSettings = new DtmfSettings();
        dtmfSettings.setMaxDigits(4);

        InputAction input = InputAction.builder().type(Collections.singletonList("dtmf")).dtmf(dtmfSettings).build();
        String expectedJson = "[{\"type\":[\"dtmf\"],\"dtmf\":{\"maxDigits\":4},\"action\":\"input\"}]";
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

        TestUtils.testJsonableBaseObject(speechSettings);
        InputAction input = InputAction.builder().type(Collections.singletonList("speech")).speech(speechSettings).build();
        String expectedJson = "[{\"type\":[\"speech\"],\"speech\":{\"uuid\":[\""+uuid+"\"],\"context\":" +
                "[\"hint1\",\"Hint 2\"],\"endOnSilence\":2.0,\"startTimeout\":10," +
                "\"maxDuration\":60,\"sensitivity\":90,\"language\":\"en-NG\"," +
                "\"saveAudio\":true},\"action\":\"input\"}]";
        assertEquals(expectedJson, new Ncco(input).toJson());
    }

    @Test
    public void testSpeechSettingsBoundaries() {
        TestUtils.testJsonableBaseObject(SpeechSettings.builder().build());

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
    public void testEventUrlField() {
        InputAction input = InputAction.builder().eventUrl("http://example.com").build();

        String expectedJson = "[{\"eventUrl\":[\"http://example.com\"],\"action\":\"input\"}]";
        assertEquals(expectedJson, new Ncco(input).toJson());
    }

    @Test
    public void testEventMethodField() {
        InputAction input = InputAction.builder().eventMethod(EventMethod.POST).build();

        String expectedJson = "[{\"eventMethod\":\"POST\",\"action\":\"input\"}]";
        assertEquals(expectedJson, new Ncco(input).toJson());
    }
}
