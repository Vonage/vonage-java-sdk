/*
 * Copyright (c) 2020  Vonage
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vonage.client.voice.ncco;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class SpeechSettingsTest {

    @Test
    public void getLanguage() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);
        String jsonStr = mapper.writeValueAsString(SpeechSettings.Language.ENGLISH_AUSTRALIA);
        String jsonStrToObject = "\"Invalid Language\"";
        SpeechSettings.Language language = mapper.readValue(jsonStrToObject, SpeechSettings.Language.class);

        System.out.println(jsonStr);
        System.out.println(language);

        assertEquals(SpeechSettings.Language.UNKNOWN, language);
    }
}